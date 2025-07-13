package asot.me.rest.mapper;

import asot.me.rest.dom.Actor;
import asot.me.rest.dom.Genre;
import asot.me.rest.dto.ActorDto;
import asot.me.rest.dto.GenreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    ActorDto toDTO(Actor actor);

    Actor toEntity(ActorDto actorDto);

    List<ActorDto> toDtoList(List<Actor> actors);
    List<Actor> toEntityList(List<ActorDto> actorDtos);
    // Add any additional mapping methods as required
}
