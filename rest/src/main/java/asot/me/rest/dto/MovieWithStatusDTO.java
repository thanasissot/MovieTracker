package asot.me.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieWithStatusDTO {
    private Long id;
    private String title;
    private boolean favorite;
    private boolean watched;
}