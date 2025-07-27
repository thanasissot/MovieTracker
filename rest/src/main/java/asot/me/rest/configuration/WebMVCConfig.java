package asot.me.rest.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*", "http://localhost:5173")  // Add this line to allow all origins
                .allowedMethods("GET", "PATCH", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*", "content-type")  // Allow all headers
                .allowCredentials(false);  // For '*' wildcard, credentials must be false


    }
}
