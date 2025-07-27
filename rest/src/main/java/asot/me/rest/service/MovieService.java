package asot.me.rest.service;

import asot.me.rest.dom.Actor;
import asot.me.rest.dom.Genre;
import asot.me.rest.dom.Movie;
import asot.me.rest.dto.MovieDto;
import asot.me.rest.mapper.MovieMapper;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final MovieMapper movieMapper;

    public Page<MovieDto> getAllMovies(
        Pageable pageable
    ) {
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        return moviesPage.map(movieMapper::toDTO);
    }

    public MovieDto getMovie(Long id) {
        return movieMapper.toDTO(movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id)));
    }

    public MovieDto createMovie(MovieDto movieDto) {
        if (movieDto.getGenreIds() == null) {
            movieDto.setGenreIds(new ArrayList<>());
        }
        return movieMapper.toDTO(movieRepository.save(movieMapper.toEntity(movieDto)));
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setYear(movieDto.getYear() != null ? movieDto.getYear() : null);

        return movieMapper.toDTO(movieRepository.save(existingMovie));
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));

        // Remove references from the join table by removing this movie from all actors' collections
        for (Actor actor : movie.getActors()) {
            actor.getMovies().remove(movie);
        }

        // Clear the movie's actors collection
        movie.setActors(new HashSet<>());

        // Save to update the join table
        movieRepository.save(movie);

        // Now delete the movie
        movieRepository.delete(movie);
    }

    private List<Long> getGenreIdsByNames(List<String> genreNames) {

        List<Genre> genres = genreRepository.findByNameIn(genreNames);

        if (genres.size() != genreNames.size()) {
            throw new IllegalArgumentException("Some genres could not be found");
        }

        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovieDto updateMovieGenres(Long movieId, List<String> genreNames) {
        MovieDto movie = getMovie(movieId);
        List<Long> genreIds = getGenreIdsByNames(genreNames);
        movie.setGenreIds(genreIds);
        return movieMapper.toDTO(movieRepository.save(movieMapper.toEntity(movie)));
    }

    @Transactional
    public MovieDto addMovieGenres(Long movieId, List<String> genreNames) {
        MovieDto movie = getMovie(movieId);
        List<Long> genreIdsToAdd = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = movie.getGenreIds();
        if (currentGenreIds == null) {
            currentGenreIds = new ArrayList<>();
        }

        Set<Long> updatedGenreIds = new HashSet<>(currentGenreIds);
        updatedGenreIds.addAll(genreIdsToAdd);

        movie.setGenreIds(new ArrayList<>(updatedGenreIds));
        return movieMapper.toDTO(movieRepository.save(movieMapper.toEntity(movie)));
    }

    @Transactional
    public MovieDto removeMovieGenres(Long movieId, List<String> genreNames) {
        MovieDto movie = getMovie(movieId);
        List<Long> genreIdsToRemove = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = movie.getGenreIds();
        if (currentGenreIds != null && !currentGenreIds.isEmpty()) {
            currentGenreIds.removeAll(genreIdsToRemove);
            movie.setGenreIds(currentGenreIds);
            return movieMapper.toDTO(movieRepository.save(movieMapper.toEntity(movie)));
        }

        return movie;
    }
}
