package asot.me.rest.service;

import asot.me.rest.repository.AppUserRepository;
import asot.me.rest.repository.UserMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMovieService {
    private final AppUserRepository appUserRepository;
    private final UserMovieRepository userMovieRepository;

//    public List<MovieWithStatusDTO> getUserFavoriteMovies(String username) {
//        AppUser user = appUserRepository.findByUsername(username)
//                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
//
//        // Get user's watch status map (movieId -> watched)
//        Map<Long, Boolean> watchStatusMap = userMovieRepository.findByAppUserId(user.getId())
//                .stream()
//                .collect(Collectors.toMap(
//                        status -> status.getMovie().getId(),
//                        UserMovie::isWatched
//                ));
//
//        // Convert favorite movies to DTOs with watch status
//        return userMovies.stream()
//                .filter(UserMovie::isFavorite)
//                .map(userMovie -> {
//                    Movie movie = userMovie.getMovie();
//                    return MovieWithStatusDTO.builder()
//                            .id(movie.getId())
//                            .title(movie.getTitle())
//                            .favorite(true)
//                            .watched(userMovie.isWatched())
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }
}