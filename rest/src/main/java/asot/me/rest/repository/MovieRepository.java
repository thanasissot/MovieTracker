package asot.me.rest.repository;

import asot.me.rest.dom.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query(value = "SELECT * FROM movie WHERE :genreId = ANY(genre_ids::bigint[])",
            nativeQuery = true)
    Page<Movie> findAllByGenreIdsContaining(@Param("genreId") Long genreId, Pageable pageable);
}
