package asot.me.rest.service;

import asot.me.rest.dom.*;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.repository.TvShowRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BootStrapService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final ActorRepository actorRepository;

    public void deleteAll() {
        log.info("Deleting all data from the database...");
        actorRepository.deleteAll();
        tvShowRepository.deleteAll();
        movieRepository.deleteAll();
        genreRepository.deleteAll();
        log.info("Successfully deleted all data from the database.");
    }

    public void deleteByParam(String param) {
        switch (param) {
            case "all":
                deleteAll();
                break;
            case "actors":
                actorRepository.deleteAll();
                log.info("Successfully deleted all actors from the database.");
                break;
            case "movies":
                movieRepository.deleteAll();
                log.info("Successfully deleted all movies from the database.");
                break;
            case "tvshows":
                tvShowRepository.deleteAll();
                log.info("Successfully deleted all TV shows from the database.");
                break;
            case "genres":
                genreRepository.deleteAll();
                log.info("Successfully deleted all genres from the database.");
                break;
            default:
                log.warn("Unknown parameter: {}", param);
        }
    }

    public void deleteByMultiParams(String ...params) {
        for (String param : params) {
            switch (param) {
                case "actors":
                    actorRepository.deleteAll();
                    log.info("Successfully deleted all actors from the database.");
                    break;
                case "movies":
                    movieRepository.deleteAll();
                    log.info("Successfully deleted all movies from the database.");
                    break;
                case "tvshows":
                    tvShowRepository.deleteAll();
                    log.info("Successfully deleted all TV shows from the database.");
                    break;
                case "genres":
                    genreRepository.deleteAll();
                    log.info("Successfully deleted all genres from the database.");
                    break;
                case "all":
                    deleteAll();
                    return;
                default:
                    log.warn("Unknown parameter: {}", param);
            }
        }
    }

    @Transactional
    public void createAll() {
        log.info("Creating all data in the database...");
        run();
        log.info("Successfully created all data in the database.");
    }

    private void run() {
        List<Movie> movies = new ArrayList<>();
        List<TvShow> tvShows = new ArrayList<>();
        initializeGenres();

        HashMap<String, Long> genreMap = new HashMap<>();
        for (GenreEnums genreEnums : GenreEnums.values()) {
            Genre genre = genreRepository.findByName(genreEnums.toString())
                    .orElseThrow(() -> new RuntimeException("Genre not found: " + genreEnums));
            genreMap.put(genre.getName(), genre.getId());
        }

        movies = initializeMovies(genreMap);
        tvShows = initializeTvShows(genreMap);
        initializeActors(movies, tvShows);
    }

    protected void initializeGenres() {
        List<Genre> genres = new ArrayList<>();
        for (GenreEnums genreEnums : GenreEnums.values()) {
            Genre genre = Genre.builder()
                    .name(genreEnums.toString().substring(0, 1).toUpperCase() + genreEnums.toString().substring(1))
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

        // Create new ArrayList instances instead of using subList views directly
        actors.get(0).setMovies(new ArrayList<>(movies.subList(0, 2)));
        actors.get(0).setTvshows(new ArrayList<>(tvShows.subList(0, 2)));
        actors.get(1).setMovies(new ArrayList<>(movies.subList(2, 4)));
        actors.get(1).setTvshows(new ArrayList<>(tvShows.subList(2, 4)));
        actors.get(2).setMovies(new ArrayList<>(movies.subList(1, 4)));
        actors.get(2).setTvshows(new ArrayList<>(tvShows.subList(1, 4)));

        actorRepository.saveAll(actors);

    }

    @PostConstruct
    public void init() {
        log.info("BootStrapService initialized");
        try {
            run();
        } catch (Exception e) {
            log.error("Error during BootStrapService initialization: {}", e.getMessage());
        }
    }


}
