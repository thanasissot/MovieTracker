package asot.me.rest;

import asot.me.rest.tmdb.TmdbRequestService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class RestApplication {
    @Value("${TMDB:asd}")
    private String apiToken;
    private final TmdbRequestService tmdbRequestService;

//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(RestApplication.class);
//        application.setWebApplicationType(WebApplicationType.NONE);
//        application.run(args);
//    }
//
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @PostConstruct
    public void printEnv() {
        try {
            tmdbRequestService.getMovieList("genre/movie/list");
        } catch (Exception e) {
            // nothing
        }
    }

}
