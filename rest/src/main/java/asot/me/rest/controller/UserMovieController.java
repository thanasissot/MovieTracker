package asot.me.rest.controller;

import asot.me.rest.dto.UserMovieDto;
import asot.me.rest.service.UserMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-movie")
@RequiredArgsConstructor
public class UserMovieController {
    private final UserMovieService userMovieService;

    @PatchMapping
    public ResponseEntity<List<UserMovieDto>> getUserFavorites(
            @RequestBody UserMovieDto userMovieDto
    ) {
        return ResponseEntity.ok(userMovieService.updateUserMovie(userMovieDto));
    }
}