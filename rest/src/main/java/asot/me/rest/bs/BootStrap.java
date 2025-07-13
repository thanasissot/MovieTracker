package asot.me.rest.bs;

import asot.me.rest.dom.*;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.repository.TvShowRepository;
import jakarta.transaction.Transactional;
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
        List<Movie> movies = new ArrayList<>();
        List<TvShow> tvShows = new ArrayList<>();

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
            movies = initializeMovies(genreMap);
        } else {
            log.info("Movies already exist in the database. Skipping initialization.");
        }

        if (tvShowRepository.count() == 0) {
            tvShows = initializeTvShows(genreMap);
        } else {
            log.info("TV Shows already exist in the database. Skipping initialization.");
        }

        if (actorRepository.count() == 0) {
            initializeActors(movies, tvShows);
        } else {
            log.info("Actors already exist in the database. Skipping initialization.");
        }
    }

    protected void initializeGenres() {
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

    protected List<Movie> initializeMovies(HashMap<String, Long> genreMap) {
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
        log.info("Successfully initialized {} TV movie records", movies.size());
        return movieRepository.saveAll(movies);
    }

    protected List<TvShow> initializeTvShows(HashMap<String, Long> genreMap) {
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
        log.info("Successfully initialized {} TV show records", tvShows.size());
        return tvShowRepository.saveAll(tvShows);
    }

    protected void initializeActors(List<Movie> movies, List<TvShow> tvShows) {
        List<Actor> actors = Arrays.asList(
                Actor.builder()
                        .firstname("Tom")
                        .lastname("Hanks")
                        .build(),
                Actor.builder()
                        .firstname("Meryl")
                        .lastname("Streep")
                        .build(),
                Actor.builder()
                        .firstname("Leonardo")
                        .lastname("DiCaprio")
                        .build()
        );

        actors = actorRepository.saveAll(actors);
        log.info("Successfully initialized {} actors records", actors.size());

        actors.get(0).setMovies(movies.subList(0, 2));
        actors.get(0).setTvshows(tvShows.subList(0, 2));
        actors.get(1).setMovies(movies.subList(2, 4));
        actors.get(1).setTvshows(tvShows.subList(2, 4));
        actors.get(2).setMovies(movies.subList(1, 4));
        actors.get(2).setTvshows(tvShows.subList(1, 4));

        actors = actorRepository.saveAll(actors);

    }


}
