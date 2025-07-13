package asot.me.rest.dto;

import asot.me.rest.dom.Movie;
import asot.me.rest.dom.TvShow;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ActorDto {
    private Long id;
    private String firstname;
    private String lastname;
    private List<Movie> movies;
    private List<TvShow> tvshows;
}
