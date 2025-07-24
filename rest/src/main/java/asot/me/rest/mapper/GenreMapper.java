package asot.me.rest.mapper;

import asot.me.rest.dom.Genre;
import asot.me.rest.dto.GenreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    @Mapping(target = "id", source = "genre.id")
    @Mapping(target = "genre", source = "genre.genreName")
    GenreDto toDTO(Genre genre);

    @Mapping(target = "id", source = "genreDto.id")
    @Mapping(target = "genreName", source = "genreDto.genre")
    Genre toEntity(GenreDto genreDto);

    List<GenreDto> toDtoList(List<Genre> genres);
    List<Genre> toEntityList(List<GenreDto> GenreDto);
    // Add any additional mapping methods as required
}
