package asot.me.rest.service;

import asot.me.rest.dom.AppUser;
import asot.me.rest.dto.AppUserDto;
import asot.me.rest.mapper.AppUserMapper;
import asot.me.rest.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserDto getAppUser(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found with id: " + id));
        return appUserMapper.toDTO(appUser);
    }

    public AppUserDto getAppUserByUsername(String username) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("AppUser not found with username: " + username));
        return appUserMapper.toDTO(appUser);
    }

    public Page<AppUserDto> getAllAppUsers(String username, Pageable pageable) {
        Page<AppUser> appUserPage = null;
//        if (name == null) {
//            actorsPage = actorRepository.findAll(pageable);
//        } else {
//            actorsPage = actorRepository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(name, name, pageable);
//        }

        return appUserRepository.findAll(pageable).map(appUserMapper::toDTO);
    }

//    public Page<ActorDto> getAllActorsByMovie(Long movieId, Pageable pageable) {
//        Page<Actor> actors = actorRepository.findByMoviesId(movieId, pageable);
//        return actors.map(actorMapper::toDTO);
//    }
//

//
//    public ActorDto createActor(ActorDto actorDto) {
//        Actor actor = actorMapper.toEntity(actorDto);
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
//
//    /**
//     * Updates an existing actor's details.
//     * only the firstname and lastaname are updated.
//     * @param actorDto the actor data transfer object containing updated information
//     * @return the updated actor data transfer object
//     */
//    public ActorDto updateActor(Long id, ActorDto actorDto) {
//        Actor actor =  actorRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
//
//        actor.setFirstname(actorDto.getFirstname());
//        actor.setLastname(actorDto.getLastname());
//
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
//
//    public void deleteActor(Long id) {
//        Actor actor =  actorRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + id));
//        // do not remove movies or tvshows just delete the actor
//        actor.setMovies(null);
//        actor.setTvshows(null);
//        actorRepository.delete(actor);
//    }
//
//    public ActorDto addActorToMovies(Long actorId, List<Long> movieIds) {
//        Actor actor =  actorRepository.findById(actorId)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
//        List<Movie> movies = movieRepository.findAllById(movieIds);
//        actor.getMovies().addAll(movies);
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
//
//    public ActorDto addActorToTvshows(Long actorId, List<Long> tvshowIds) {
//        Actor actor =  actorRepository.findById(actorId)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
//        List<TvShow> tvshows = tvShowRepository.findAllById(tvshowIds);
//
//        actor.getTvshows().addAll(tvshows);
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
//
//    public ActorDto removeActorFromMovies(Long actorId, List<Long> movieIds) {
//        Actor actor =  actorRepository.findById(actorId)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
//        List<Movie> movies = new ArrayList<>();
//        Set<Long> movieIdSet = new HashSet<>(movieIds);
//        for (Movie movie : actor.getMovies()) {
//            if (!movieIdSet.contains(movie.getId())) {
//                movies.add(movie);
//            }
//        }
//
//        actor.setMovies(movies);
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
//
//    public ActorDto removeActorFromTvshows(Long actorId, List<Long> tvshowIds) {
//        Actor actor =  actorRepository.findById(actorId)
//                .orElseThrow(() -> new EntityNotFoundException("Actor not found with id: " + actorId));
//        List<TvShow> tvshows = new ArrayList<>();
//        Set<Long> tvshowsIdSet = new HashSet<>(tvshowIds);
//        for (TvShow tvshow : actor.getTvshows()) {
//            if (!tvshowsIdSet.contains(tvshow.getId())) {
//                tvshows.add(tvshow);
//            }
//        }
//
//        actor.setTvshows(tvshows);
//        return actorMapper.toDTO(actorRepository.save(actor));
//    }
}