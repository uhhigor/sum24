package org.example.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); // Adjust the mapping to match your controller's request mapping
//                .allowedOrigins("http://localhost:3000") // Update with your frontend URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*");
    }
}