package asot.me.rest.repository;

import asot.me.rest.dom.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByMoviesId(Long movieId, Pageable pageable);

    Page<Actor> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String firstname, String lastname, Pageable pageable);

    Optional<Actor> findByFirstnameIsIgnoreCaseAndLastnameIsIgnoreCase(String firstname, String lastname);
}