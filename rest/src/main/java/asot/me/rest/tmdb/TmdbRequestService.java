package asot.me.rest.tmdb;

import asot.me.rest.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TmdbRequestService {
    @Value("${TMDB:asd}")
    private String apiToken;
    final String BASE_URL = " https://api.themoviedb.org/3/";
    private final GenreRepository genreRepository;
    final OkHttpClient client = new OkHttpClient();

    // GENDERS
    public void getMovieList(String url) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
//            genreRepository.deleteAll();
            System.out.println(response.body().string());

        }

    }
}
