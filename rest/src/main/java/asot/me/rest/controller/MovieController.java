package asot.me.rest.controller;

import asot.me.rest.dto.GenreDto;
import asot.me.rest.dto.MovieDto;
import asot.me.rest.dto.MovieGenresUpdateRequest;
import asot.me.rest.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(
        @RequestParam(value = "_start", defaultValue = "0") int start,
        @RequestParam(value = "_end", defaultValue = "20") int end,
        @RequestParam(value = "_sort", defaultValue = "title") String sort,
        @RequestParam(value = "_order", defaultValue = "asc") String order,
        @RequestParam(value = "title_like", required = false) String titleLike
    ) {
        int size = end - start;
        int page = start / size;
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<MovieDto> pageResult = movieService.getAllMovies(pageable, titleLike);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pageResult.getTotalElements()))
                .body(pageResult.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(
        @RequestBody MovieDto movieDto
    ) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> editMovie(
        @PathVariable Long id,
        @RequestBody MovieDto movieDto
    ) {
        MovieDto updatedMovie = movieService.updateMovie(id, movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDto> editMovie2(
            @PathVariable Long id,
            @RequestBody MovieDto movieDto
    ) {
        MovieDto updatedMovie = movieService.updateMovie(id, movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<MovieDto> updateMovieGenres(
//        @PathVariable Long id,
//        @RequestBody MovieDto movieDto)
//    {
////        MovieDto updatedMovie = movieService.updateMovieGenres(id, request.getGenreNames());
//        return ResponseEntity.ok(movieDto);
//    }

}
