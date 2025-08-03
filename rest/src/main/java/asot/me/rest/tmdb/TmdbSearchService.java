package asot.me.rest.tmdb;

import asot.me.rest.dom.GlobalSettings;
import asot.me.rest.dom.Movie;
import asot.me.rest.dto.MovieDto;
import asot.me.rest.mapper.MovieMapper;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.GlobalSettingsRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.service.RequestTrackingService;
import asot.me.rest.tmdb.response.ActorSearchResponse;
import asot.me.rest.tmdb.response.MovieListGenre;
import asot.me.rest.tmdb.response.MovieSearchResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TmdbSearchService {
    @Value("${TMDB:asd}")
    private String apiToken;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final RequestTrackingService requestTrackingService;
    private final MovieMapper movieMapper;

    private final String BASE_URL = " https://api.themoviedb.org/3/";

    private final OkHttpClient client = new OkHttpClient();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<MovieListGenre> movieListGenreJsonAdapter = moshi.adapter(MovieListGenre.class);
    private final JsonAdapter<ActorSearchResponse> actorSearchResponseJsonAdapter = moshi.adapter(ActorSearchResponse.class);
    private final JsonAdapter<MovieSearchResponse> movieSearchResponseJsonAdapter = moshi.adapter(MovieSearchResponse.class);


    // GENDERS
    public void fetchGenresFromApi() {
        GlobalSettings globalSettings = getGlobalSettings();

        if (globalSettings.isGenresCreated()) {
            log.warn("Genres already created.");
            return;
        }

        String url = "genre/movie/list";
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            requestTrackingService.trackRequest(url, null, response.isSuccessful());
            MovieListGenre movieListGenre = movieListGenreJsonAdapter.fromJson(response.body().source());
            if (movieListGenre != null) {
                genreRepository.saveAll(movieListGenre.getGenres());
                log.info("Successfully stored Genre data from TMDB.");

                globalSettings.setGenresCreated(true);
                globalSettingsRepository.save(globalSettings);
            }

        } catch (Exception e) {
            log.error("Exception fetchGenresFromApi e:{}", e.getLocalizedMessage());
        }
    }

    public void searchActorsByName(String fullname) {
        String[] names = fullname.split(" ", 2);
        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";

        Optional<asot.me.rest.dom.Actor> exists = actorRepository.findByFirstnameIsIgnoreCaseAndLastnameIsIgnoreCase(firstName, lastName);
        if (exists.isPresent()) {
            log.warn("Actor already exists");
            return;
        }

        String url = "search/person";
        String queryParams = "query=" + fullname;
        String fullUrl = BASE_URL + url + "?" + queryParams;
        Request request = new Request.Builder()
                .url(fullUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            requestTrackingService.trackRequest(url, queryParams, response.isSuccessful());
            ActorSearchResponse actorSearchResponse = actorSearchResponseJsonAdapter.fromJson(response.body().source());

            if (actorSearchResponse != null && actorSearchResponse.getResults() != null) {
                // Find exact name match (case insensitive)
                var matchingActor = actorSearchResponse.getResults().stream()
                        .filter(actor -> actor.getName().equalsIgnoreCase(fullname) &&
                                actor.getKnown_for_department().equalsIgnoreCase("Acting"))
                        .findFirst()
                        .orElse(null);

                if (matchingActor != null) {
                    // Split name into first and last name
                    String[] nameParts = matchingActor.getName().split(" ", 2);
                    firstName = nameParts[0];
                    lastName = nameParts.length > 1 ? nameParts[1] : "";

                    // Create actor entity
                    asot.me.rest.dom.Actor actorEntity = asot.me.rest.dom.Actor.builder()
                            .firstname(firstName)
                            .lastname(lastName)
                            .build();

                    // Create movie entities from known_for list (only movies)
                    List<Movie> movieEntities = matchingActor.getKnown_for().stream()
                            .filter(media -> "movie".equals(media.getMedia_type()))
                            .map(movie -> asot.me.rest.dom.Movie.builder()
                                    .title(movie.getTitle())
                                    .year((long) LocalDate.parse(movie.getRelease_date()).getYear())
                                    .id(movie.getId())
                                    .genreIds(movie.getGenre_ids())
                                    .build())
                            .collect(Collectors.toList());

                    // Set relationships
                    actorEntity.setMovies(movieEntities);

                    // Save to database and return
                    actorRepository.save(actorEntity);

                    log.info("Successfully stored Genre data from TMDB.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MovieDto searchAndCreateMovie(MovieDto movieDto) throws Exception {
        String movieTitle = movieDto.getTitle();

        Movie movie = movieRepository.findByTitleIs(movieTitle).orElse(
                Movie.builder().title(movieTitle).year(movieDto.getYear()).build()
        );

        if (movie.getId() != null) {
            throw new Exception(String.format("Movie with title:%s, already exists.", movieTitle));
        }

        // search tmdb api
        String url = "search/movie?";
        String queryParams = String.format("query=%s&primary_release_year=%s", movieTitle, movieDto.getYear());
        String fullUrl = BASE_URL.concat(url).concat(queryParams);
        Request request = new Request.Builder()
                .url(fullUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                requestTrackingService.trackRequest(url, queryParams, false);
                throw new IOException("Unexpected code " + response);
            }

            MovieSearchResponse movieSearchResponse = movieSearchResponseJsonAdapter.fromJson(response.body().source());

            if (movieSearchResponse == null) {
                throw new Exception("movieSearchResponse was null");
            }
            var matchingMovie = movieSearchResponse.getResults().stream()
                    .filter(movieSearchResult ->
                            movieSearchResult.getTitle().equalsIgnoreCase(movieTitle) ||
                                    movieSearchResult.getOriginal_title().equalsIgnoreCase(movieTitle))
                    .findFirst()
                    .orElse(null);

            if (matchingMovie != null) {
                log.info("Movie found with id={}, creating new entry...", matchingMovie.getId());
                movie.setId(matchingMovie.getId());
                movie.setGenreIds(matchingMovie.getGenre_ids());
                movie.setQueried(false);
                movie = movieRepository.save(movie);
                log.info("Movie ''{}'' was created", movie.getTitle());
                return movieMapper.toDTO(movie);
            }
        }

        throw new Exception("Code should never reach this point: searchAndCreateMovie");
    }

    public void searchMovieDetailsAndAddActors(Long id) {

    }

    private GlobalSettings getGlobalSettings() {
        return globalSettingsRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Global settings not found."));
    }
}
