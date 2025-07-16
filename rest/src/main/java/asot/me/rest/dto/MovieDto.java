package asot.me.rest.dto;

import asot.me.rest.dom.Actor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private Long year;
    private List<Actor> actors;
    private List<Long> genreIds;
}
