package asot.me.rest.controller;

import asot.me.rest.dto.GenreDto;
import asot.me.rest.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/all")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
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

    @PostMapping("/create")
    public ResponseEntity<GenreDto>createGenre(@RequestBody GenreDto genreDto) {
        GenreDto createdGenre = genreService.createGenre(genreDto);
        return ResponseEntity.ok(createdGenre);
    }

    @PutMapping("/edit")
    public ResponseEntity<GenreDto> updateGenreName(
            @RequestBody GenreDto genreDto) {
        GenreDto updatedGenre = genreService.updateGenreName(genreDto.getId(), genreDto.getGenreName());
        return ResponseEntity.ok(updatedGenre);
    }
}
