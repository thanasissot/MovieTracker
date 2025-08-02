package asot.me.rest.tmdb;

import asot.me.rest.dom.GlobalSettings;
import asot.me.rest.repository.GenreRepository;
import asot.me.rest.repository.GlobalSettingsRepository;
import asot.me.rest.service.RequestTrackingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Log4j2
public class TmdbRequestService {
    @Value("${TMDB:asd}")
    private String apiToken;
    private final GenreRepository genreRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final RequestTrackingService requestTrackingService;

    private final String BASE_URL = " https://api.themoviedb.org/3/";

    private final OkHttpClient client = new OkHttpClient();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<MovieListGenre> movieListGenreJsonAdapter = moshi.adapter(MovieListGenre.class);

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
            log.error("Exception e:{}", e.getLocalizedMessage());
        }
    }

    private GlobalSettings getGlobalSettings() {
        return globalSettingsRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Global settings not found."));
    }
}
