package asot.me.rest.tmdb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private boolean adult;
    private String backdrop_path;
    private long id;
    private String title;
    private String original_title;
    private String overview;
    private String poster_path;
    private String media_type;
    private String original_language;
    private List<Long> genre_ids;
    private double popularity;
    private String release_date;
    private boolean video;
    private double vote_average;
    private long vote_count;
}
