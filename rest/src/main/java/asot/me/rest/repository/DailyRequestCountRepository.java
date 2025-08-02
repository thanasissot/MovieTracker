package asot.me.rest.repository;

import asot.me.rest.dom.DailyRequestCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRequestCountRepository extends JpaRepository<DailyRequestCount, String> {
}
