package asot.me.rest.bs;

import asot.me.rest.dom.*;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class BootStrap implements CommandLineRunner {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final ActorRepository actorRepository;

    @Override
    public void run(String... args) {
        if (genreRepository.count() == 0) {
            initializeGenres();
        } else {
            log.info("Genres already exist in the database. Skipping initialization.");
        }

        if (movieRepository.count() == 0) {
            initializeMovies();
        } else {
            log.info("Movies already exist in the database. Skipping initialization.");
        }

        if (tvShowRepository.count() == 0) {
            initializeTvShows();
        } else {
            log.info("TV Shows already exist in the database. Skipping initialization.");
        }

        if (actorRepository.count() == 0) {
            initializeActors();
        } else {
            log.info("Actors already exist in the database. Skipping initialization.");
        }
    }

    private void initializeGenres() {
        List<Genre> genres = new ArrayList<>();
        for (GenreEnums genreEnums : GenreEnums.values()) {
            Genre genre = Genre.builder()
//                    .id((long) genreEnums.getId())
                    .genreName(genreEnums.toString())
                    .build();
            genres.add(genre);
        }
        genreRepository.saveAll(genres);
        log.info("Successfully initialized {} genre records", genres.size());
    }

    private void initializeMovies() {
        List<Movie> movies = Arrays.asList(
                Movie.builder()
                        .id(1L)
                        .title("The Shawshank Redemption")
                        .year(1994L)
                        .genreIds(Arrays.asList(5L, 7L)) // CRIME, DRAMA
                        .build(),
                Movie.builder()
                        .id(2L)
                        .title("The Dark Knight")
                        .year(2008L)
                        .genreIds(Arrays.asList(1L, 5L, 17L)) // ACTION, CRIME, THRILLER
                        .build(),
                Movie.builder()
                        .id(3L)
                        .title("Inception")
                        .year(2010L)
                        .genreIds(Arrays.asList(1L, 9L, 15L)) // ACTION, FANTASY, SCI-FI
                        .build(),
                Movie.builder()
                        .id(4L)
                        .title("Pulp Fiction")
                        .year(1994L)
                        .genreIds(Arrays.asList(5L, 7L)) // CRIME, DRAMA
                        .build(),
                Movie.builder()
                        .id(5L)
                        .title("The Lord of the Rings: The Fellowship of the Ring")
                        .year(2001L)
                        .genreIds(Arrays.asList(2L, 9L)) // ADVENTURE, FANTASY
                        .build()
        );

        movieRepository.saveAll(movies);
        log.info("Successfully initialized {} TV movie records", movies.size());
    }

    private void initializeTvShows() {
        List<TvShow> tvShows = Arrays.asList(
                TvShow.builder()
                        .id(1L)
                        .title("Breaking Bad")
                        .year(2008L)
                        .genreIds(Arrays.asList(5L, 7L, 17L)) // CRIME, DRAMA, THRILLER
                        .build(),
                TvShow.builder()
                        .id(2L)
                        .title("Game of Thrones")
                        .year(2011L)
                        .genreIds(Arrays.asList(1L, 2L, 7L, 9L)) // ACTION, ADVENTURE, DRAMA, FANTASY
                        .build(),
                TvShow.builder()
                        .id(3L)
                        .title("Stranger Things")
                        .year(2016L)
                        .genreIds(Arrays.asList(7L, 9L, 13L, 15L)) // DRAMA, FANTASY, MYSTERY, SCI-FI
                        .build(),
                TvShow.builder()
                        .id(4L)
                        .title("The Office")
                        .year(2005L)
                        .genreIds(Arrays.asList(4L)) // COMEDY
                        .build(),
                TvShow.builder()
                        .id(5L)
                        .title("Planet Earth")
                        .year(2006L)
                        .genreIds(Arrays.asList(6L)) // DOCUMENTARY
                        .build()
        );

        tvShowRepository.saveAll(tvShows);
        log.info("Successfully initialized {} TV show records", tvShows.size());
    }

    private void initializeActors() {
        List<Actor> actors = Arrays.asList(
                Actor.builder()
                        .id(1L)
                        .firstname("Tom")
                        .lastname("Hanks")
                        .movieIds(Arrays.asList(1L, 3L))
                        .tvShowIds(Arrays.asList(2L))
                        .build(),
                Actor.builder()
                        .id(2L)
                        .firstname("Meryl")
                        .lastname("Streep")
                        .movieIds(Arrays.asList(2L, 4L))
                        .tvShowIds(Arrays.asList(1L, 3L))
                        .build(),
                Actor.builder()
                        .id(3L)
                        .firstname("Leonardo")
                        .lastname("DiCaprio")
                        .movieIds(Arrays.asList(3L, 5L))
                        .tvShowIds(Arrays.asList(4L))
                        .build()
        );

        actorRepository.saveAll(actors);
        log.info("Successfully initialized {} actors records", actors.size());

        // Update bidirectional references
        updateMovieActorReferences();
        updateTvShowActorReferences();
    }

    private void updateMovieActorReferences() {
        List<Actor> actors = actorRepository.findAll();
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            List<Long> actorIds = new ArrayList<>();
            for (Actor actor : actors) {
                if (actor.getMovieIds() != null && actor.getMovieIds().contains(movie.getId())) {
                    actorIds.add(actor.getId());
                }
            }
            movie.setActorIds(actorIds);
            movieRepository.save(movie);
        }
        log.info("Updated actor references in movies");
    }

    private void updateTvShowActorReferences() {
        List<Actor> actors = actorRepository.findAll();
        List<TvShow> tvShows = tvShowRepository.findAll();

        for (TvShow tvShow : tvShows) {
            List<Long> actorIds = new ArrayList<>();
            for (Actor actor : actors) {
                if (actor.getTvShowIds() != null && actor.getTvShowIds().contains(tvShow.getId())) {
                    actorIds.add(actor.getId());
                }
            }
            tvShow.setActorIds(actorIds);
            tvShowRepository.save(tvShow);
        }
        log.info("Updated actor references in TV shows");
    }

}
