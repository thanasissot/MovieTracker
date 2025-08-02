package asot.me.rest.controller;

import asot.me.rest.tmdb.TmdbRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tmdb")
@RequiredArgsConstructor
public class TmdbController {
    private final TmdbRequestService tmdbRequestService;


    @GetMapping("/genres")
    public ResponseEntity<Void> createGenres() {
        tmdbRequestService.fetchGenresFromApi();
        return ResponseEntity.ok().build();
    }

}