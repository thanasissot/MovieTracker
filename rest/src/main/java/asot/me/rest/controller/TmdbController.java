package asot.me.rest.controller;

import asot.me.rest.tmdb.TmdbMovieQueryProcess;
import asot.me.rest.tmdb.TmdbRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    private final TmdbRequestService tmdbRequestService;
    private final TmdbMovieQueryProcess movieQueryProcess;

    @GetMapping("/genres")
    public ResponseEntity<Void> createGenres() {
        tmdbRequestService.fetchGenresFromApi();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/movies")
    public ResponseEntity<Void> queryMovie() {
        movieQueryProcess.processNextMovieId();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/actors/{fullname}")
    public ResponseEntity<Void> searchAndCreateActor(
            @PathVariable("fullname") String fullname
    ) {
        tmdbRequestService.searchActorsByName(fullname);
        return ResponseEntity.ok().build();
    }

}