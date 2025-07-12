package asot.me.rest.service;

import asot.me.rest.dom.Genre;
import asot.me.rest.dom.GenreName;
import asot.me.rest.dom.Movie;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));
    }

    private List<Long> getGenreIdsByNames(List<String> genreNames) {
        List<GenreName> genreTypes = genreNames.stream()
                .map(name -> {
                    try {
                        return GenreName.valueOf(name.toUpperCase().replace(" ", "_"));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Unknown genre: " + name);
                    }
                })
                .collect(Collectors.toList());

        List<Genre> genres = genreRepository.findByTypeIn(genreTypes);

        if (genres.size() != genreTypes.size()) {
            throw new IllegalArgumentException("Some genres could not be found");
        }

        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public Movie updateMovieGenres(Long movieId, List<String> genreNames) {
        Movie movie = getMovie(movieId);
        List<Long> genreIds = getGenreIdsByNames(genreNames);
        movie.setGenreIds(genreIds);
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie addMovieGenres(Long movieId, List<String> genreNames) {
        Movie movie = getMovie(movieId);
        List<Long> genreIdsToAdd = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = movie.getGenreIds();
        if (currentGenreIds == null) {
            currentGenreIds = new ArrayList<>();
        }

        Set<Long> updatedGenreIds = new HashSet<>(currentGenreIds);
        updatedGenreIds.addAll(genreIdsToAdd);

        movie.setGenreIds(new ArrayList<>(updatedGenreIds));
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie removeMovieGenres(Long movieId, List<String> genreNames) {
        Movie movie = getMovie(movieId);
        List<Long> genreIdsToRemove = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = movie.getGenreIds();
        if (currentGenreIds != null && !currentGenreIds.isEmpty()) {
            currentGenreIds.removeAll(genreIdsToRemove);
            movie.setGenreIds(currentGenreIds);
            return movieRepository.save(movie);
        }

        return movie;
    }
}
