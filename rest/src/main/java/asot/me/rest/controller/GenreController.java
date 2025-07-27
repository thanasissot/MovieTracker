package asot.me.rest.controller;

import asot.me.rest.dto.GenreDto;
import asot.me.rest.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres(
        @RequestParam(value = "_start", defaultValue = "0") int start,
        @RequestParam(value = "_end", defaultValue = "20") int end,
        @RequestParam(value = "_sort", defaultValue = "id") String sort,
        @RequestParam(value = "_order", defaultValue = "asc") String order
    ) {
        int size = end - start;
        int page = start / size;
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<GenreDto> pageResult = genreService.getAllGenres(pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pageResult.getTotalElements()))
                .body(pageResult.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(
            @RequestBody GenreDto genreDto
    ) {
        GenreDto createdGenre = genreService.createGenre(genreDto);
        return ResponseEntity.ok(createdGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenreName(
            @PathVariable Long id,
            @RequestBody GenreDto genreDto) {
        GenreDto updatedGenre = genreService.updateGenreName(genreDto.getId(), genreDto.getName());
        return ResponseEntity.ok(updatedGenre);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenreDto> patchGenreName(
            @PathVariable Long id,
            @RequestBody GenreDto genreDto) {
        GenreDto updatedGenre = genreService.updateGenreName(id, genreDto.getName());
        return ResponseEntity.ok(updatedGenre);
    }
}
