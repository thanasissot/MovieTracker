package asot.me.rest.tmdb.response;

import asot.me.rest.dom.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class TmdbMovieDetailsResponse {

    private Long id;
    private List<Genre> genres;
    private String title;
    @JsonProperty("release_date")
    private String releaseDate;
    private Credits credits;
}
