package asot.me.rest.service;

import asot.me.rest.dom.*;
import asot.me.rest.repository.*;
import asot.me.rest.tmdb.TmdbRequestService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class BootStrapService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final TvShowRepository tvShowRepository;
    private final ActorRepository actorRepository;
    private final AppUserRepository appUserRepository;
    private final UserMovieRepository userMovieRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final TmdbRequestService tmdbRequestService;

    @PostConstruct
    public void init() {
        log.info("BootStrapService initialized");
        try {
            if (globalSettingsRepository.findAll().isEmpty()) {
                GlobalSettings globalSettings =
                        GlobalSettings.builder()
                            .id(1L)
                            .build();

                globalSettingsRepository.save(globalSettings);
                log.info("Global settings created (id=1)");
            }

            tmdbRequestService.fetchGenresFromApi();

        } catch (Exception e) {
            log.error("Error during BootStrapService initialization: {}", e.getMessage());
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

        List<AppUser> users = initializeUsers();
        initializeUserMovieWatchStatus(users, movies);
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



    protected List<AppUser> initializeUsers() {
        List<AppUser> users = Arrays.asList(
                AppUser.builder()
                        .username("johndoe")
                        .build(),
                AppUser.builder()
                        .username("janedoe")
                        .build(),
                AppUser.builder()
                        .username("bobsmith")
                        .build()
        );

        List<AppUser> savedUsers = appUserRepository.saveAll(users);
        log.info("Successfully initialized {} user records", savedUsers.size());
        return savedUsers;
    }

    protected void initializeUserMovieWatchStatus(List<AppUser> users, List<Movie> movies) {
        List<UserMovie> userMovies = new ArrayList<>();

        // User 1 (John) has watched movies 0, 1, 2 and added movies 0, 1 to favorites
        AppUser john = users.get(0);

        userMovies.add(UserMovie.builder()
                .appUserId(john.getId())
                .movieId(movies.get(0).getId())
//                .appUser(john)
//                .movie(movies.get(0))
                .watched(true)
                .favorite(true)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(john.getId())
                .movieId(movies.get(1).getId())
//                .appUser(john)
//                .movie(movies.get(1))
                .watched(true)
                .favorite(true)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(john.getId())
                .movieId(movies.get(2).getId())
//                .appUser(john)
//                .movie(movies.get(2))
                .watched(true)
                .favorite(false)
                .build());

        // User 2 (Jane) has watched movies 2, 3, 4 and added movies 2, 3 to favorites
        AppUser jane = users.get(1);

        userMovies.add(UserMovie.builder()
                .appUserId(jane.getId())
                .movieId(movies.get(2).getId())
                .appUser(jane)
                .movie(movies.get(2))
                .watched(true)
                .favorite(true)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(jane.getId())
                .movieId(movies.get(3).getId())
                .appUser(jane)
                .movie(movies.get(3))
                .watched(true)
                .favorite(true)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(jane.getId())
                .movieId(movies.get(4).getId())
                .appUser(jane)
                .movie(movies.get(4))
                .watched(true)
                .favorite(false)
                .build());

        // User 3 (Bob) has watched movie 1 and added it to favorites
        // Also has movie 2 and 4 in watchlist (not watched yet)
        AppUser bob = users.get(2);

        userMovies.add(UserMovie.builder()
                .appUserId(bob.getId())
                .movieId(movies.get(1).getId())
                .appUser(bob)
                .movie(movies.get(1))
                .watched(true)
                .favorite(true)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(bob.getId())
                .movieId(movies.get(2).getId())
                .appUser(bob)
                .movie(movies.get(2))
                .watched(false)
                .favorite(false)
                .build());

        userMovies.add(UserMovie.builder()
                .appUserId(bob.getId())
                .movieId(movies.get(4).getId())
                .appUser(bob)
                .movie(movies.get(4))
                .watched(false)
                .favorite(false)
                .build());

        userMovieRepository.saveAll(userMovies);
        log.info("Successfully initialized {} user movie records", userMovies.size());
    }

}
