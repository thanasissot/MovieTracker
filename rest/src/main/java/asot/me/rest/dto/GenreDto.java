package asot.me.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    private String id;
    private String genreName;
}
