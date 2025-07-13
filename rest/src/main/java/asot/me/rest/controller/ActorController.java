package asot.me.rest.controller;

import asot.me.rest.dom.Actor;
import asot.me.rest.dto.ActorDto;
import asot.me.rest.service.ActorService;
import jakarta.persistence.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping("/all")
    public ResponseEntity<List<Actor>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDto> getActor(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.getActor(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ActorDto> createActor(@RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.createActor(actorDto));
    }

    // Add movies to actor
    @PostMapping("/{id}/movies")
    public ResponseEntity<ActorDto> addMoviesToActor(
            @PathVariable Long id,
            @RequestBody List<Long> movieIds) {
        return ResponseEntity.ok(actorService.addActorToMovies(id, movieIds));
    }

    // Add TV shows to actor
    @PostMapping("/{id}/tvshows")
    public ResponseEntity<ActorDto> addTvShowsToActor(
            @PathVariable Long id,
            @RequestBody List<Long> tvShowIds) {
        return ResponseEntity.ok(actorService.addActorToTvshows(id, tvShowIds));
    }

    // Remove movies from actor
    @DeleteMapping("/{id}/movies")
    public ResponseEntity<ActorDto> removeMoviesFromActor(
            @PathVariable Long id,
            @RequestBody List<Long> movieIds) {
        return ResponseEntity.ok(actorService.removeActorFromMovies(id, movieIds));
    }

    // Remove TV shows from actor
    @DeleteMapping("/{id}/tvshows")
    public ResponseEntity<ActorDto> removeTvShowsFromActor(
            @PathVariable Long id,
            @RequestBody List<Long> tvShowIds) {
        return ResponseEntity.ok(actorService.removeActorFromTvshows(id, tvShowIds));
    }

}