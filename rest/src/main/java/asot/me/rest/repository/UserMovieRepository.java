package asot.me.rest.repository;

import asot.me.rest.dom.UserMovie;
import asot.me.rest.dom.UserMovieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, UserMovieId> {
    List<UserMovie> findByAppUserId(Long appUserId);
}