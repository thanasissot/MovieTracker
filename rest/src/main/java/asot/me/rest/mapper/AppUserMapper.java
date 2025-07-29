package asot.me.rest.mapper;

import asot.me.rest.dom.AppUser;
import asot.me.rest.dto.AppUserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    // Define methods to map between Genre and GenreDto if needed
    // For example:
    AppUserDto toDTO(AppUser appUser);

    AppUser toEntity(AppUserDto appUserDto);

    List<AppUserDto> toDtoList(List<AppUser> appUsers);
    List<AppUser> toEntityList(List<AppUserDto> appUserDtos);
    // Add any additional mapping methods as required
}
