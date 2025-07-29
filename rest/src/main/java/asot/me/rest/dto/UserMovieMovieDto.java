package asot.me.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMovieMovieDto {
    private Long id;
    private String title;
    private Long year;
}
