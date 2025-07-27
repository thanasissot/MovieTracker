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

    Page<Movie> findAllByActors_Id(Long actorId, Pageable pageable);

    @Query(value = "SELECT m.* FROM movie m " +
            "LEFT JOIN movie_actor am ON m.id = am.movie_id " +
            "WHERE (:genreId IS NULL OR :genreId = ANY(m.genre_ids)) " +
            "AND (:actorId IS NULL OR am.actor_id = :actorId)",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM movie m " +
                    "LEFT JOIN movie_actor am ON m.id = am.movie_id " +
                    "WHERE (:genreId IS NULL OR :genreId = ANY(m.genre_ids)) " +
                    "AND (:actorId IS NULL OR am.actor_id = :actorId)",
            nativeQuery = true)
    Page<Movie> findByGenreIdAndActorId(
            @Param("genreId") Long genreId,
            @Param("actorId") Long actorId,
            Pageable pageable);
}
