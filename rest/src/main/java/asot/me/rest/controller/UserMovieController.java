//package asot.me.rest.controller;
//
//import asot.me.rest.dto.MovieWithStatusDTO;
//import asot.me.rest.service.UserMovieService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserMovieController {
//    private final UserMovieService userMovieService;
//
//    @GetMapping("/{username}/favorites")
//    public ResponseEntity<List<MovieWithStatusDTO>> getUserFavorites(@PathVariable String username) {
//        return ResponseEntity.ok(userMovieService.getUserFavoriteMovies(username));
//    }
//}