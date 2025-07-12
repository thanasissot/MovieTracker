package asot.me.rest.repository;

import asot.me.rest.dom.Genre;
import asot.me.rest.dom.GenreName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByTypeIn(List<GenreName> types);
}
