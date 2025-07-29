package asot.me.rest.dom;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(UserMovieId.class)
@EqualsAndHashCode(of = {"appUserId", "movieId"})  // Only use ID fields
public class UserMovie {
    @Id
    private Long appUserId;
    @Id
    private Long movieId;

    private boolean watched;
    private boolean favorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appUserId", insertable = false, updatable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;
}
