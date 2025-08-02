package asot.me.rest.dom;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GlobalSettings {

    @Id
    private Long id;

    private boolean genresCreated;

    private boolean genresUpdatedLastMonth;
}
