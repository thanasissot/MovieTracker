package asot.me.rest.repository;

import asot.me.rest.dom.UserMovieWatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieWatchStatusRepository extends JpaRepository<UserMovieWatchStatus, Long> {
    List<UserMovieWatchStatus> findByAppUserId(Long appUserId);
}