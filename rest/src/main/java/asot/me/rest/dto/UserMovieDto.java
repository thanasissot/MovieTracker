package asot.me.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMovieDto {
    private Long appUserId;
    private Long movieId;
    private UserMovieMovieDto movie;
    private boolean watched;
    private boolean favorite;
}
