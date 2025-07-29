package asot.me.rest.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private Long year;

    @Column(columnDefinition = "bigint[]")
    @Type(ListArrayType.class)
    private List<Long> genreIds;

    @JsonIgnore
    @ManyToMany(mappedBy = "movies", targetEntity = Actor.class)
    private Set<Actor> actors = new HashSet<>();

}
