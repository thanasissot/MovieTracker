package asot.me.rest.controller;

import asot.me.rest.dom.Actor;
import asot.me.rest.dto.ActorDto;
import asot.me.rest.service.ActorService;
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
    public ResponseEntity<Actor> getActor(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.getActor(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ActorDto> createActor(@RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.createActor(actorDto));
    }

    @PutMapping("/{id}/movies")
    public ResponseEntity<Actor> updateActorMovies(
            @PathVariable Long id,
            @RequestBody List<Long> movieIds) {
        return ResponseEntity.ok(actorService.updateActorMovies(id, movieIds));
    }

    @PutMapping("/{id}/tvshows")
    public ResponseEntity<Actor> updateActorTvShows(
            @PathVariable Long id,
            @RequestBody List<Long> tvShowIds) {
        return ResponseEntity.ok(actorService.updateActorTvShows(id, tvShowIds));
    }

    @PostMapping("/{actorId}/movies/{movieId}")
    public ResponseEntity<Actor> addActorToMovie(
            @PathVariable Long actorId,
            @PathVariable Long movieId) {
        return ResponseEntity.ok(actorService.addActorToMovie(actorId, movieId));
    }

    @PostMapping("/{actorId}/tvshows/{tvShowId}")
    public ResponseEntity<Actor> addActorToTvShow(
            @PathVariable Long actorId,
            @PathVariable Long tvShowId) {
        return ResponseEntity.ok(actorService.addActorToTvShow(actorId, tvShowId));
    }

    @DeleteMapping("/{actorId}/movies/{movieId}")
    public ResponseEntity<Actor> removeActorFromMovie(
            @PathVariable Long actorId,
            @PathVariable Long movieId) {
        return ResponseEntity.ok(actorService.removeActorFromMovie(actorId, movieId));
    }

    @DeleteMapping("/{actorId}/tvshows/{tvShowId}")
    public ResponseEntity<Actor> removeActorFromTvShow(
            @PathVariable Long actorId,
            @PathVariable Long tvShowId) {
        return ResponseEntity.ok(actorService.removeActorFromTvShow(actorId, tvShowId));
    }
}