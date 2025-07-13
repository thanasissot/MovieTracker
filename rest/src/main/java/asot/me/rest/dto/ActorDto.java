package asot.me.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ActorDto {
    private Long id;
    private String firstname;
    private String lastname;
    private List<Long> movieIds; // JSON string representation of List<Long>
    private List<Long> tvShowIds; // JSON string representation of List<Long>
}
