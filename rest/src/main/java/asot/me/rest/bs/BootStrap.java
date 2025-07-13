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
import java.util.HashMap;
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

        HashMap<String, Long> genreMap = new HashMap<>();
        for (GenreEnums genreEnums : GenreEnums.values()) {
            Genre genre = genreRepository.findByGenreName(genreEnums.toString())
                    .orElseThrow(() -> new RuntimeException("Genre not found: " + genreEnums));
            genreMap.put(genre.getGenreName(), genre.getId());
        }

        if (movieRepository.count() == 0) {
            initializeMovies(genreMap);
        } else {
            log.info("Movies already exist in the database. Skipping initialization.");
        }

        if (tvShowRepository.count() == 0) {
            initializeTvShows(genreMap);
        } else {
            log.info("TV Shows already exist in the database. Skipping initialization.");
        }
//
//        if (actorRepository.count() == 0) {
//            initializeActors();
//        } else {
//            log.info("Actors already exist in the database. Skipping initialization.");
//        }
    }

    private void initializeGenres() {
        List<Genre> genres = new ArrayList<>();
        for (GenreEnums genreEnums : GenreEnums.values()) {
            Genre genre = Genre.builder()
                    .genreName(genreEnums.toString())
                    .build();
            genres.add(genre);
        }
        genreRepository.saveAll(genres);
        log.info("Successfully initialized {} genre records", genres.size());
    }

    private void initializeMovies(HashMap<String, Long> genreMap) {
        List<Movie> movies = Arrays.asList(
                Movie.builder()
                        .title("The Shawshank Redemption")
                        .year(1994L)
                        .genreIds(Arrays.asList(genreMap.get("CRIME"), genreMap.get("DRAMA"))) // CRIME, DRAMA
                        .build(),
                Movie.builder()
                        .title("The Dark Knight")
                        .year(2008L)
                        .genreIds(Arrays.asList(genreMap.get("ACTION"), genreMap.get("CRIME"), genreMap.get("THRILLER"))) // ACTION, CRIME, THRILLER
                        .build(),
                Movie.builder()
                        .title("Inception")
                        .year(2010L)
                        .genreIds(Arrays.asList(genreMap.get("ACTION"), genreMap.get("FANTASY"), genreMap.get("SCIENCE_FICTION"))) // CRIME, DRAMA
                        .build(),
                Movie.builder()
                        .title("Pulp Fiction")
                        .year(1994L)
                        .genreIds(Arrays.asList(genreMap.get("CRIME"), genreMap.get("DRAMA"))) // CRIME, DRAMA
                        .build(),
                Movie.builder()
                        .title("The Lord of the Rings: The Fellowship of the Ring")
                        .year(2001L)
                        .genreIds(Arrays.asList(genreMap.get("ADVENTURE"), genreMap.get("FANTASY"))) // CRIME, DRAMA
                        .build()
        );

        movieRepository.saveAll(movies);
        log.info("Successfully initialized {} TV movie records", movies.size());
    }

    private void initializeTvShows(HashMap<String, Long> genreMap) {
        List<TvShow> tvShows = Arrays.asList(
                TvShow.builder()
                        .title("Breaking Bad")
                        .year(2008L)
                        .genreIds(Arrays.asList(genreMap.get("DRAMA"), genreMap.get("CRIME"), genreMap.get("THRILLER"))) // ACTION, CRIME, THRILLER
                        .build(),
                TvShow.builder()
                        .title("Game of Thrones")
                        .year(2011L)
                        .genreIds(Arrays.asList(genreMap.get("ADVENTURE"), genreMap.get("FANTASY"), genreMap.get("DRAMA"), genreMap.get("ACTION"))) // CRIME, DRAMA
                        .build(),
                TvShow.builder()
                        .title("Stranger Things")
                        .year(2016L)
                        .genreIds(Arrays.asList(genreMap.get("MYSTERY"), genreMap.get("FANTASY"), genreMap.get("DRAMA"), genreMap.get("SCIENCE_FICTION"))) // CRIME, DRAMA
                        .build(),
                TvShow.builder()
                        .title("The Office")
                        .year(2005L)
                        .genreIds(Arrays.asList(genreMap.get("COMEDY"))) // COMEDY
                        .build(),
                TvShow.builder()
                        .title("Planet Earth")
                        .year(2006L)
                        .genreIds(Arrays.asList(genreMap.get("DOCUMENTARY"))) // DOCUMENTARY
                        .build()
        );

        tvShowRepository.saveAll(tvShows);
        log.info("Successfully initialized {} TV show records", tvShows.size());
    }

    private void initializeActors() {
        List<Actor> actors = Arrays.asList(
                Actor.builder()
//                        .id(1L)
                        .firstname("Tom")
                        .lastname("Hanks")
                        .build(),
                Actor.builder()
//                        .id(2L)
                        .firstname("Meryl")
                        .lastname("Streep")
                        .build(),
                Actor.builder()
//                        .id(3L)
                        .firstname("Leonardo")
                        .lastname("DiCaprio")
                        .build()
        );

        actorRepository.saveAll(actors);
        log.info("Successfully initialized {} actors records", actors.size());

        // Update bidirectional references
//        updateMovieActorReferences();
//        updateTvShowActorReferences();
    }

//    private void updateMovieActorReferences() {
//        List<Actor> actors = actorRepository.findAll();
//        List<Movie> movies = movieRepository.findAll();
//
//        for (Movie movie : movies) {
//            List<Long> actorIds = new ArrayList<>();
//            for (Actor actor : actors) {
//                if (actor.getMovieIds() != null && actor.getMovieIds().contains(movie.getId())) {
//                    actorIds.add(actor.getId());
//                }
//            }
//            movie.setActorIds(actorIds);
//            movieRepository.save(movie);
//        }
//        log.info("Updated actor references in movies");
//    }
//
//    private void updateTvShowActorReferences() {
//        List<Actor> actors = actorRepository.findAll();
//        List<TvShow> tvShows = tvShowRepository.findAll();
//
//        for (TvShow tvShow : tvShows) {
//            List<Long> actorIds = new ArrayList<>();
//            for (Actor actor : actors) {
//                if (actor.getTvShowIds() != null && actor.getTvShowIds().contains(tvShow.getId())) {
//                    actorIds.add(actor.getId());
//                }
//            }
//            tvShow.setActorIds(actorIds);
//            tvShowRepository.save(tvShow);
//        }
//        log.info("Updated actor references in TV shows");
//    }

}
