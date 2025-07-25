package asot.me.rest.repository;

import asot.me.rest.dom.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByGenreNameIn(List<String> genreNames);
    Optional<Genre> findByGenreName(String genreName);
}
