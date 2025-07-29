package asot.me.rest.dto;

import asot.me.rest.dom.UserMovieId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMovieDto {
    private Long appUserId;
    private Long movieId;
    private boolean watched;
    private boolean favorite;
}
