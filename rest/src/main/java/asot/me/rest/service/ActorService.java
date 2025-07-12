package asot.me.rest.service;

import asot.me.rest.dom.Actor;
import asot.me.rest.dom.Movie;
import asot.me.rest.dom.TvShow;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.repository.TvShowRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Actor getActor(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
    }

    @Transactional
    public Actor updateActorMovies(Long actorId, List<Long> movieIds) {
        Actor actor = getActor(actorId);
        // Validate movies exist
        validateMovies(movieIds);

        // Update actor's movie list
        actor.setMovieIds(movieIds);

        // Update actor references in movies
        updateMoviesWithActor(actorId, movieIds);

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor updateActorTvShows(Long actorId, List<Long> tvShowIds) {
        Actor actor = getActor(actorId);
        // Validate TV shows exist
        validateTvShows(tvShowIds);

        // Update actor's TV show list
        actor.setTvShowIds(tvShowIds);

        // Update actor references in TV shows
        updateTvShowsWithActor(actorId, tvShowIds);

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor addActorToMovie(Long actorId, Long movieId) {
        Actor actor = getActor(actorId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

        // Update actor's movie list
        List<Long> movieIds = actor.getMovieIds();
        if (movieIds == null) {
            movieIds = new ArrayList<>();
        }
        if (!movieIds.contains(movieId)) {
            movieIds.add(movieId);
            actor.setMovieIds(movieIds);
        }

        // Update movie's actor list
        List<Long> actorIds = movie.getActorIds();
        if (actorIds == null) {
            actorIds = new ArrayList<>();
        }
        if (!actorIds.contains(actorId)) {
            actorIds.add(actorId);
            movie.setActorIds(actorIds);
            movieRepository.save(movie);
        }

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor addActorToTvShow(Long actorId, Long tvShowId) {
        Actor actor = getActor(actorId);
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new EntityNotFoundException("TV show not found with id: " + tvShowId));

        // Update actor's TV show list
        List<Long> tvShowIds = actor.getTvShowIds();
        if (tvShowIds == null) {
            tvShowIds = new ArrayList<>();
        }
        if (!tvShowIds.contains(tvShowId)) {
            tvShowIds.add(tvShowId);
            actor.setTvShowIds(tvShowIds);
        }

        // Update TV show's actor list
        List<Long> actorIds = tvShow.getActorIds();
        if (actorIds == null) {
            actorIds = new ArrayList<>();
        }
        if (!actorIds.contains(actorId)) {
            actorIds.add(actorId);
            tvShow.setActorIds(actorIds);
            tvShowRepository.save(tvShow);
        }

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor removeActorFromMovie(Long actorId, Long movieId) {
        Actor actor = getActor(actorId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

        // Update actor's movie list
        List<Long> movieIds = actor.getMovieIds();
        if (movieIds != null) {
            movieIds.remove(movieId);
            actor.setMovieIds(movieIds);
        }

        // Update movie's actor list
        List<Long> actorIds = movie.getActorIds();
        if (actorIds != null) {
            actorIds.remove(actorId);
            movie.setActorIds(actorIds);
            movieRepository.save(movie);
        }

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor removeActorFromTvShow(Long actorId, Long tvShowId) {
        Actor actor = getActor(actorId);
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new EntityNotFoundException("TV show not found with id: " + tvShowId));

        // Update actor's TV show list
        List<Long> tvShowIds = actor.getTvShowIds();
        if (tvShowIds != null) {
            tvShowIds.remove(tvShowId);
            actor.setTvShowIds(tvShowIds);
        }

        // Update TV show's actor list
        List<Long> actorIds = tvShow.getActorIds();
        if (actorIds != null) {
            actorIds.remove(actorId);
            tvShow.setActorIds(actorIds);
            tvShowRepository.save(tvShow);
        }

        return actorRepository.save(actor);
    }

    private void validateMovies(List<Long> movieIds) {
        for (Long movieId : movieIds) {
            if (!movieRepository.existsById(movieId)) {
                throw new EntityNotFoundException("Movie not found with id: " + movieId);
            }
        }
    }

    private void validateTvShows(List<Long> tvShowIds) {
        for (Long tvShowId : tvShowIds) {
            if (!tvShowRepository.existsById(tvShowId)) {
                throw new EntityNotFoundException("TV show not found with id: " + tvShowId);
            }
        }
    }

    private void updateMoviesWithActor(Long actorId, List<Long> movieIds) {
        // Remove actor from movies not in the list
        List<Movie> allMovies = movieRepository.findAll();
        for (Movie movie : allMovies) {
            List<Long> actorIds = movie.getActorIds();
            if (actorIds == null) continue;

            boolean shouldBeInMovie = movieIds.contains(movie.getId());
            boolean isInMovie = actorIds.contains(actorId);

            if (shouldBeInMovie && !isInMovie) {
                actorIds.add(actorId);
                movie.setActorIds(actorIds);
                movieRepository.save(movie);
            } else if (!shouldBeInMovie && isInMovie) {
                actorIds.remove(actorId);
                movie.setActorIds(actorIds);
                movieRepository.save(movie);
            }
        }
    }

    private void updateTvShowsWithActor(Long actorId, List<Long> tvShowIds) {
        // Update TV shows with actor
        List<TvShow> allTvShows = tvShowRepository.findAll();
        for (TvShow tvShow : allTvShows) {
            List<Long> actorIds = tvShow.getActorIds();
            if (actorIds == null) {
                actorIds = new ArrayList<>();
            }

            boolean shouldBeInTvShow = tvShowIds.contains(tvShow.getId());
            boolean isInTvShow = actorIds.contains(actorId);

            if (shouldBeInTvShow && !isInTvShow) {
                actorIds.add(actorId);
                tvShow.setActorIds(actorIds);
                tvShowRepository.save(tvShow);
            } else if (!shouldBeInTvShow && isInTvShow) {
                actorIds.remove(actorId);
                tvShow.setActorIds(actorIds);
                tvShowRepository.save(tvShow);
            }
        }
    }
}