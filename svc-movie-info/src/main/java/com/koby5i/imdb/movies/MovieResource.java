package com.koby5i.imdb.movies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/v1/{movieId}")
    public Movie getMovieInfoV1(@PathVariable("movieId") String movieId) {
        return new Movie(movieId, "Frantic", "This movie description");
    }


    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        try {
            int i = 20;
            System.out.println("sleep " + i);
            Thread.sleep(i);

        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
        MovieSummary movieSummary = restTemplate.getForObject("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" +  apiKey, MovieSummary.class);
        return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());

    }


}
