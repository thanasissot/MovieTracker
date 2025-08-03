package asot.me.rest.controller;

import asot.me.rest.dto.MovieDto;
import asot.me.rest.service.MovieService;
import asot.me.rest.tmdb.TmdbSearchService;
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
    private final TmdbSearchService tmdbSearchService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies(
        @RequestParam(value = "_start", defaultValue = "0") int start,
        @RequestParam(value = "_end", defaultValue = "20") int end,
        @RequestParam(value = "_sort", defaultValue = "title") String sort,
        @RequestParam(value = "_order", defaultValue = "asc") String order,
        @RequestParam(value = "title_like", required = false) String titleLike,
        @RequestParam(value = "genreId", required = false) Long genreId,
        @RequestParam(value = "actorId", required = false) Long actorId
    ) {
        int size = end - start;
        int page = start / size;
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<MovieDto> pageResult = movieService.getAllMovies(pageable, titleLike, genreId, actorId);
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
    ) throws Exception {
        MovieDto createdMovie = tmdbSearchService.searchAndCreateMovie(movieDto);
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

    @PostMapping("/{id}/genres")
    public ResponseEntity<MovieDto> addGenreToMovie(
        @PathVariable Long id,
        @RequestBody List<Long> genreIds
    ) {
        MovieDto movieDto = movieService.addGenreToMovie(id, genreIds);
        return ResponseEntity.ok(movieDto);
    }

    @DeleteMapping("/{id}/genres")
    public ResponseEntity<MovieDto> deleteGenreFromMovie(
        @PathVariable Long id,
        @RequestBody List<Long> genreIds
    ) {
        MovieDto movieDto = movieService.deleteGenreFromMovie(id, genreIds);
        return ResponseEntity.ok(movieDto);
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
