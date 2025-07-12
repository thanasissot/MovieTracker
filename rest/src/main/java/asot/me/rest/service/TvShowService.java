package asot.me.rest.service;

import asot.me.rest.dom.Genre;
import asot.me.rest.dom.GenreEnums;
import asot.me.rest.dom.TvShow;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.TvShowRepository;
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
public class TvShowService {

    private final TvShowRepository tvShowRepository;
    private final GenreRepository genreRepository;

    public List<TvShow> getAllTvShows() {
        return tvShowRepository.findAll();
    }

    public TvShow getTvShow(Long id) {
        return tvShowRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TvShow not found with id: " + id));
    }

    private List<Long> getGenreIdsByNames(List<String> genreNames) {
        List<Genre> genres = genreRepository.findByGenreNameIn(genreNames);

        if (genres.size() != genreNames.size()) {
            throw new IllegalArgumentException("Some genres could not be found");
        }

        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public TvShow updateTvShowGenres(Long tvShowId, List<String> genreNames) {
        TvShow tvShow = getTvShow(tvShowId);
        List<Long> genreIds = getGenreIdsByNames(genreNames);
        tvShow.setGenreIds(genreIds);
        return tvShowRepository.save(tvShow);
    }

    @Transactional
    public TvShow addTvShowGenres(Long tvShowId, List<String> genreNames) {
        TvShow tvShow = getTvShow(tvShowId);
        List<Long> genreIdsToAdd = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = tvShow.getGenreIds();
        if (currentGenreIds == null) {
            currentGenreIds = new ArrayList<>();
        }

        Set<Long> updatedGenreIds = new HashSet<>(currentGenreIds);
        updatedGenreIds.addAll(genreIdsToAdd);

        tvShow.setGenreIds(new ArrayList<>(updatedGenreIds));
        return tvShowRepository.save(tvShow);
    }

    @Transactional
    public TvShow removeTvShowGenres(Long tvShowId, List<String> genreNames) {
        TvShow tvShow = getTvShow(tvShowId);
        List<Long> genreIdsToRemove = getGenreIdsByNames(genreNames);

        List<Long> currentGenreIds = tvShow.getGenreIds();
        if (currentGenreIds != null && !currentGenreIds.isEmpty()) {
            currentGenreIds.removeAll(genreIdsToRemove);
            tvShow.setGenreIds(currentGenreIds);
            return tvShowRepository.save(tvShow);
        }

        return tvShow;
    }
}