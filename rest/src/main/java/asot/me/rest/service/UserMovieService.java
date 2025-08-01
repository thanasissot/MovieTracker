package asot.me.rest.service;

import asot.me.rest.dom.UserMovie;
import asot.me.rest.dom.UserMovieId;
import asot.me.rest.dto.UserMovieDto;
import asot.me.rest.mapper.UserMovieMapper;
import asot.me.rest.repository.AppUserRepository;
import asot.me.rest.repository.UserMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMovieService {
    private final AppUserRepository appUserRepository;
    private final UserMovieRepository userMovieRepository;
    private final UserMovieMapper userMovieMapper;

    public List<UserMovieDto> updateUserMovie(UserMovieDto userMovieDto) {
        UserMovieId userMovieId = UserMovieId
                .builder()
                .appUserId(userMovieDto.getAppUserId())
                .movieId(userMovieDto.getMovieId())
                .build();

        UserMovie userMovie = userMovieRepository.findById(userMovieId).orElse(null);

        if (userMovie == null){
            userMovie = UserMovie
                    .builder()
                    .movieId(userMovieDto.getMovieId())
                    .appUserId(userMovieDto.getAppUserId())
                    .watched(userMovieDto.isWatched())
                    .favorite(userMovieDto.isFavorite())
                    .build();
        } else {
            userMovie.setFavorite(userMovieDto.isFavorite());
            userMovie.setWatched(userMovieDto.isWatched());
        }

        if (!userMovieDto.isFavorite() && !userMovieDto.isWatched()) {
            userMovieRepository.deleteById(userMovieId);
        } else {
            userMovieRepository.save(userMovie);
        }

        List<UserMovie> userMovies = userMovieRepository.findByAppUserId(userMovieDto.getAppUserId());

        return userMovieMapper.toDtoList(userMovies);
    }

}