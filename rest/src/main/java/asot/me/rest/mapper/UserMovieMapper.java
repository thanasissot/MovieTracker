package asot.me.rest.mapper;

import asot.me.rest.dom.UserMovie;
import asot.me.rest.dto.UserMovieDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMovieMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    UserMovieDto toDTO(UserMovie userMovie);

    UserMovie toEntity(UserMovieDto userMovieDto);

    List<UserMovieDto> toDtoList(List<UserMovie> userMovies);
    List<UserMovie> toEntityList(List<UserMovieDto> userMovieDtos);
    // Add any additional mapping methods as required
}
