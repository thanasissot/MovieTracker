package asot.me.rest.tmdb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TmdbCastMember {
    private Boolean adult;
    private Integer gender;
    private Long id;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private Double popularity;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("cast_id")
    private Long castId;

    private String character;

    @JsonProperty("credit_id")
    private String creditId;

}
