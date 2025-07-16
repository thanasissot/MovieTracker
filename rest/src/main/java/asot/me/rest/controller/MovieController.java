package asot.me.rest.controller;

import asot.me.rest.dto.MovieDto;
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
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @PostMapping("/create")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping("/edit")
    public ResponseEntity<MovieDto> editMovie(@RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/genres")
    public ResponseEntity<MovieDto> updateMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        MovieDto updatedMovie = movieService.updateMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<MovieDto> addMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        MovieDto updatedMovie = movieService.addMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<MovieDto> removeMovieGenres(
            @PathVariable Long id,
            @RequestBody MovieGenresUpdateRequest request) {
        MovieDto updatedMovie = movieService.removeMovieGenres(id, request.getGenreNames());
        return ResponseEntity.ok(updatedMovie);
    }
}
