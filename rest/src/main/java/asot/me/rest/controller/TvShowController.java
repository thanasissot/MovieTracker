package asot.me.rest.controller;

import asot.me.rest.dom.TvShow;
import asot.me.rest.dto.MovieGenresUpdateRequest;
import asot.me.rest.service.TvShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tvshows")
@RequiredArgsConstructor
public class TvShowController {
    private final TvShowService tvShowService;

    @GetMapping("/all")
    public ResponseEntity<List<TvShow>> getAllTvShows() {
        return ResponseEntity.ok(tvShowService.getAllTvShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TvShow> getTvShow(@PathVariable Long id) {
        return ResponseEntity.ok(tvShowService.getTvShow(id));
    }

    @PutMapping("/{id}/genres")
    public ResponseEntity<TvShow> updateTvShowGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        TvShow updatedTvShow = tvShowService.updateTvShowGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedTvShow);
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<TvShow> addTvShowGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        TvShow updatedTvShow = tvShowService.addTvShowGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedTvShow);
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<TvShow> removeTvShowGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        TvShow updatedTvShow = tvShowService.removeTvShowGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedTvShow);
    }
}
