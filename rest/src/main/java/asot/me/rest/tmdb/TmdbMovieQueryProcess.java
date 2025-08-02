package asot.me.rest.tmdb;

import asot.me.rest.dom.Actor;
import asot.me.rest.dom.Genre;
import asot.me.rest.dom.GlobalSettings;
import asot.me.rest.dom.Movie;
import asot.me.rest.repository.ActorRepository;
import asot.me.rest.repository.GlobalSettingsRepository;
import asot.me.rest.repository.MovieRepository;
import asot.me.rest.service.RequestTrackingService;
import asot.me.rest.tmdb.response.TmdbCastMember;
import asot.me.rest.tmdb.response.TmdbMovieDetailsResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TmdbMovieQueryProcess {
    @Value("${TMDB:asd}")
    private String apiToken;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final RequestTrackingService requestTrackingService;
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    private final String BASE_URL = "https://api.themoviedb.org/3/movie/%s";

    private final OkHttpClient client = new OkHttpClient();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<TmdbMovieDetailsResponse> tmdbMovieDetailsResponseJsonAdapter = moshi.adapter(TmdbMovieDetailsResponse.class);

    public void processNextMovieId() {
        Optional<GlobalSettings> globalSettings = globalSettingsRepository.findById(1L);
        if (globalSettings.isPresent()) {
            Long movieId = globalSettings.get().getNextMovieIdToQuery();
            log.info("movieId to query = {}", movieId);
            String fullUrl = String.format(BASE_URL, movieId);

            Request request = new Request.Builder()
                .url(fullUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    globalSettings.get().setNextMovieIdToQuery(globalSettings.get().getNextMovieIdToQuery() + 1);
                    globalSettingsRepository.save(globalSettings.get());
                    throw new IOException("Unexpected code " + response);
                }
                requestTrackingService.trackRequest(BASE_URL, "{movieId}=".concat(String.valueOf(movieId)), true);
                TmdbMovieDetailsResponse tmdbMovieDetailsResponse = tmdbMovieDetailsResponseJsonAdapter.fromJson(response.body().source());

                Movie movie = Movie.builder()
                    .genreIds(tmdbMovieDetailsResponse.getGenres().stream().map(Genre::getId).toList())
                    .id(tmdbMovieDetailsResponse.getId())
                    .year((long) LocalDate.parse(tmdbMovieDetailsResponse.getReleaseDate()).getYear())
                    .title(tmdbMovieDetailsResponse.getTitle())
                    .build();

                movie = movieRepository.save(movie);

                List<Actor> actorList = new ArrayList<>();
                int counter = 12;
                for (TmdbCastMember castMember : tmdbMovieDetailsResponse.getCredits().getCast()) {
                    String[] nameParts = castMember.getName().split(" ", 2);
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";

                    Actor actor = actorRepository.findById(castMember.getId())
                            .orElse(
                                    Actor.builder()
                                            .firstname(firstName)
                                            .lastname(lastName)
                                            .id(castMember.getId())
                                            .movies(new ArrayList<>())
                                            .build()
                            );

                    actor.getMovies().add(movie);
                    actorList.add(actor);

                    if (counter-- < 1)
                        break;
                }

                actorRepository.saveAll(actorList);
                globalSettings.get().setNextMovieIdToQuery(globalSettings.get().getNextMovieIdToQuery() + 1);
                globalSettingsRepository.save(globalSettings.get());

            } catch (Exception e) {
                log.error("Exception processNextMovieId e:{}", e.getLocalizedMessage());
            }

        }
    }

}
