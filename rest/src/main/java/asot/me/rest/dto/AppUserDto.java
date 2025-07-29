package asot.me.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AppUserDto {
    private Long id;
    private String username;
    private Set<UserMovieDto> userMovies;
}
