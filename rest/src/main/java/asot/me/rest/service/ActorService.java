package asot.me.rest.service;

import asot.me.rest.dom.Actor;
import asot.me.rest.dom.Movie;
import asot.me.rest.dom.TvShow;
import asot.me.rest.dto.ActorDto;
import asot.me.rest.mapper.ActorMapper;
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
    private final ActorMapper actorMapper;

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Actor getActor(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
    }

    public ActorDto createActor(ActorDto actorDto) {
        Actor actor = actorMapper.toEntity(actorDto);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    @Transactional
    public Actor updateActorMovies(Long actorId, List<Long> movieIds) {
        Actor actor = getActor(actorId);
        // Validate movies exist
        validateMovies(movieIds);

        // Update actor's movie list

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

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor addActorToTvShow(Long actorId, Long tvShowId) {
        Actor actor = getActor(actorId);
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new EntityNotFoundException("TV show not found with id: " + tvShowId));

        // Update actor's TV show list
        return actorRepository.save(actor);
    }

    @Transactional
    public Actor removeActorFromMovie(Long actorId, Long movieId) {
        Actor actor = getActor(actorId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

        // Update actor's movie list

        return actorRepository.save(actor);
    }

    @Transactional
    public Actor removeActorFromTvShow(Long actorId, Long tvShowId) {
        Actor actor = getActor(actorId);
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new EntityNotFoundException("TV show not found with id: " + tvShowId));

        // Update actor's TV show list

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
        }
    }

    private void updateTvShowsWithActor(Long actorId, List<Long> tvShowIds) {
        // Update TV shows with actor
        List<TvShow> allTvShows = tvShowRepository.findAll();
        for (TvShow tvShow : allTvShows) {
        }
    }
}