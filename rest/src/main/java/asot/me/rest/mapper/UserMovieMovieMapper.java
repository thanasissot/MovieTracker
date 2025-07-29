package asot.me.rest.mapper;

import asot.me.rest.dom.Movie;
import asot.me.rest.dto.UserMovieMovieDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMovieMovieMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    UserMovieMovieDto toDTO(Movie movie);

    Movie toEntity(UserMovieMovieDto movieDto);

    List<UserMovieMovieDto> toDtoList(List<Movie> movies);
    List<Movie> toEntityList(List<UserMovieMovieDto> movieDtos);
    // Add any additional mapping methods as required
}
