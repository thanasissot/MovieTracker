package asot.me.rest.tmdb.response;

import lombok.Data;
import java.util.List;

@Data
public class Credits {
    private List<TmdbCastMember> cast;

}
