package asot.me.rest.tmdb.response;

import lombok.Data;

import java.util.List;

@Data
public class MovieSearchResult {
    private boolean adult;
    private String backdrop_path;
    private List<Long> genre_ids;
    private Long id;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private boolean video;
    private double vote_average;
    private long vote_count;
}
