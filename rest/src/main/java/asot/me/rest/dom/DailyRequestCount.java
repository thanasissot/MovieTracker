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
public class DailyRequestCount {

    @Id
    private String date; // Format: dd-MM-yyyy

    private Long totalRequests;


}
