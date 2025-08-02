package asot.me.rest.repository;

import asot.me.rest.dom.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestHistoryRepository extends JpaRepository<RequestHistory, String> {
}
