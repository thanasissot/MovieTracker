package asot.me.rest.tmdb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorSearchResponse {
    private int page;
    private List<Actor> results;
    private int total_pages;
    private int total_results;
}
