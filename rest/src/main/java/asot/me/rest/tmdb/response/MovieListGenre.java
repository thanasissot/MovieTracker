package asot.me.rest.tmdb.response;

import asot.me.rest.dom.Genre;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieListGenre {
    List<Genre> genres;
}
