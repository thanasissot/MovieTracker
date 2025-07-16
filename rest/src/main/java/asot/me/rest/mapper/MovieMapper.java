package asot.me.rest.mapper;

import asot.me.rest.dom.Movie;
import asot.me.rest.dto.MovieDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    MovieDto toDTO(Movie movie);

    Movie toEntity(MovieDto movieDto);

    List<MovieDto> toDtoList(List<Movie> movies);
    List<Movie> toEntityList(List<MovieDto> movieDtos);
    // Add any additional mapping methods as required
}
