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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final ActorMapper actorMapper;

    public List<ActorDto> getAllActors() {
        return actorMapper.toDtoList(actorRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    public ActorDto getActor(Long id) {
        Actor actor =  actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
        return actorMapper.toDTO(actor);
    }

    public ActorDto createActor(ActorDto actorDto) {
        Actor actor = actorMapper.toEntity(actorDto);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    /**
     * Updates an existing actor's details.
     * only the firstname and lastaname are updated.
     * @param actorDto the actor data transfer object containing updated information
     * @return the updated actor data transfer object
     */
    public ActorDto updateActor(ActorDto actorDto) {
        Actor actor =  actorRepository.findById(actorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorDto.getId()));

        actor.setFirstname(actorDto.getFirstname());
        actor.setLastname(actorDto.getLastname());

        return actorMapper.toDTO(actorRepository.save(actor));
    }

    public void deleteActor(Long id) {
        Actor actor =  actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
        // do not remove movies or tvshows just delete the actor
        actor.setMovies(null);
        actor.setTvshows(null);
        actorRepository.delete(actor);
    }

    public ActorDto addActorToMovies(Long actorId, List<Long> movieIds) {
        Actor actor =  actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
        List<Movie> movies = movieRepository.findAllById(movieIds);
        actor.getMovies().addAll(movies);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    public ActorDto addActorToTvshows(Long actorId, List<Long> tvshowIds) {
        Actor actor =  actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
        List<TvShow> tvshows = tvShowRepository.findAllById(tvshowIds);

        actor.getTvshows().addAll(tvshows);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    public ActorDto removeActorFromMovies(Long actorId, List<Long> movieIds) {
        Actor actor =  actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
        List<Movie> movies = new ArrayList<>();
        Set<Long> movieIdSet = new HashSet<>(movieIds);
        for (Movie movie : actor.getMovies()) {
            if (!movieIdSet.contains(movie.getId())) {
                movies.add(movie);
            }
        }

        actor.setMovies(movies);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    public ActorDto removeActorFromTvshows(Long actorId, List<Long> tvshowIds) {
        Actor actor =  actorRepository.findById(actorId)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
        List<TvShow> tvshows = new ArrayList<>();
        Set<Long> tvshowsIdSet = new HashSet<>(tvshowIds);
        for (TvShow tvshow : actor.getTvshows()) {
            if (!tvshowsIdSet.contains(tvshow.getId())) {
                tvshows.add(tvshow);
            }
        }

        actor.setTvshows(tvshows);
        return actorMapper.toDTO(actorRepository.save(actor));
    }
}