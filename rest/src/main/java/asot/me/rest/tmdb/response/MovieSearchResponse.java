package asot.me.rest.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieSearchResponse {
    private int page;
    @JsonProperty("totalPages")
    private int total_pages;
    @JsonProperty("totalResults")
    private int total_results;
    List<MovieSearchResult> results;

}
