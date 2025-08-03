package asot.me.rest.controller;

import asot.me.rest.tmdb.TmdbMovieQueryProcess;
import asot.me.rest.tmdb.TmdbSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    private final TmdbSearchService tmdbSearchService;
    private final TmdbMovieQueryProcess movieQueryProcess;

    @GetMapping("/genres")
    public ResponseEntity<Void> createGenres() {
        tmdbSearchService.fetchGenresFromApi();
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
        tmdbSearchService.searchActorsByName(fullname);
        return ResponseEntity.ok().build();
    }

}