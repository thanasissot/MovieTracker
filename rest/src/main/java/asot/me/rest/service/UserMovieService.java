package asot.me.rest.service;

import asot.me.rest.dom.AppUser;
import asot.me.rest.dom.Movie;
import asot.me.rest.dom.UserMovieWatchStatus;
import asot.me.rest.dto.MovieWithStatusDTO;
import asot.me.rest.repository.AppUserRepository;
import asot.me.rest.repository.UserMovieWatchStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMovieService {
    private final AppUserRepository appUserRepository;
    private final UserMovieWatchStatusRepository watchStatusRepository;

    public List<MovieWithStatusDTO> getUserFavoriteMovies(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        // Get user's watch status map (movieId -> watched)
        Map<Long, Boolean> watchStatusMap = watchStatusRepository.findByAppUserId(user.getId())
                .stream()
                .collect(Collectors.toMap(
                        status -> status.getMovie().getId(),
                        UserMovieWatchStatus::isWatched
                ));

        // Convert favorite movies to DTOs with watch status
        return user.getFavoriteMovies().stream()
                .map(movie -> MovieWithStatusDTO.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .favorite(true)
                        .watched(watchStatusMap.getOrDefault(movie.getId(), false))
                        .build())
                .collect(Collectors.toList());
    }
}