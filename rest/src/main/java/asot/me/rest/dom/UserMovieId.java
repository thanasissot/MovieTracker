package asot.me.rest.dom;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieId implements Serializable {
    private Long appUserId;
    private Long movieId;
}
