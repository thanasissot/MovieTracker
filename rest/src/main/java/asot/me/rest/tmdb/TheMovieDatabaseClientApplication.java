package asot.me.rest.tmdb;

import asot.me.rest.service.RequestTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@RequiredArgsConstructor
public class TheMovieDatabaseClientApplication implements CommandLineRunner {

    private final RequestTrackingService requestTrackingService;
    private final TmdbRequestService tmdbRequestService;
//
//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(TheMovieDatabaseClientApplication.class);
//        application.setWebApplicationType(WebApplicationType.NONE); // Prevents web server startup
//        application.run(args);
//    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Processing data without web server");

        // Example API call
        String exampleUrl = "https://api.themoviedb.org/3/movie/popular";
        String exampleQueryParams = "api_key=your_key&language=en-US&page=1";

        // Track this request
        requestTrackingService.trackRequest(exampleUrl, exampleQueryParams);

        System.out.println("Processing completed");
    }

}
