package asot.me.rest.controller;

import asot.me.rest.dom.Movie;
import asot.me.rest.dto.MovieGenresUpdateRequest;
import asot.me.rest.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @PutMapping("/{id}/genres")
    public ResponseEntity<Movie> updateMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        Movie updatedMovie = movieService.updateMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<Movie> addMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        Movie updatedMovie = movieService.addMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<Movie> removeMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        Movie updatedMovie = movieService.removeMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }
}
