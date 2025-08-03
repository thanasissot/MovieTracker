package asot.me.rest.controller;

import asot.me.rest.dto.ActorDto;
import asot.me.rest.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
@Log4j2
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorDto>> getAllActors(
        @RequestParam(value = "_start", defaultValue = "0") int start,
        @RequestParam(value = "_end", defaultValue = "20") int end,
        @RequestParam(value = "_sort", defaultValue = "id") String sort,
        @RequestParam(value = "_order", defaultValue = "asc") String order,
        @RequestParam(value = "movieId", required = false) Long movieId,
        @RequestParam(value = "name_like", required = false) String name
    ) {
        int size = end - start;
        int page = start / size;
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<ActorDto> pageResult;
        if (movieId != null) {
            log.info("Fetching actors for movie with ID: {}", movieId);
            pageResult = actorService.getAllActorsByMovie(movieId, pageable);
        } else {
            log.info("Fetching actors WITHOUT movie id filter");
            pageResult = actorService.getAllActors(name, pageable);
        }
        return ResponseEntity.ok()
                .header("access-control-expose-headers", "X-Total-Count")
                .header("x-total-count", String.valueOf(pageResult.getTotalElements()))
                .body(pageResult.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDto> getActor(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.getActor(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<ActorDto> createActor(
        @RequestBody ActorDto actorDto
    ) {
        return ResponseEntity.ok(actorService.createActor(actorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDto> editActor(
        @PathVariable Long id,
        @RequestBody ActorDto actorDto)
    {
        ActorDto updatedActor = actorService.updateActor(id, actorDto);
        return ResponseEntity.ok(updatedActor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDto> editActor2(
            @PathVariable Long id,
            @RequestBody ActorDto actorDto)
    {
        ActorDto updatedActor = actorService.updateActor(id, actorDto);
        return ResponseEntity.ok(updatedActor);
    }

    // Add movies to actor
    @PostMapping("/{id}/movies")
    public ResponseEntity<ActorDto> addMoviesToActor(
            @PathVariable Long id,
            @RequestBody List<Long> movieIds) {
        return ResponseEntity.ok(actorService.addActorToMovies(id, movieIds));
    }

    // Remove movies from actor
    @DeleteMapping("/{id}/movies")
    public ResponseEntity<ActorDto> removeMoviesFromActor(
            @PathVariable Long id,
            @RequestBody List<Long> movieIds) {
        return ResponseEntity.ok(actorService.removeActorFromMovies(id, movieIds));
    }


//
//    // Add TV shows to actor
//    @PostMapping("/{id}/tvshows")
//    public ResponseEntity<ActorDto> addTvShowsToActor(
//            @PathVariable Long id,
//            @RequestBody List<Long> tvShowIds) {
//        return ResponseEntity.ok(actorService.addActorToTvshows(id, tvShowIds));
//    }
//

//
//    // Remove TV shows from actor
//    @DeleteMapping("/{id}/tvshows")
//    public ResponseEntity<ActorDto> removeTvShowsFromActor(
//            @PathVariable Long id,
//            @RequestBody List<Long> tvShowIds) {
//        return ResponseEntity.ok(actorService.removeActorFromTvshows(id, tvShowIds));
//    }

}