package com.koby5i.imdb.vhsclub.service;

import com.koby5i.imdb.vhsclub.CatalogItem;
import com.koby5i.imdb.vhsclub.Movie;
import com.koby5i.imdb.vhsclub.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"), // wait for this long if no response than time out
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "6"),      // if 6 timeouts then turn circuit breaker on
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),   // check if 50% of last 6 request failed then turn circuit breaker on
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "100000") // try again in 1000 milliseconds
            })
    public CatalogItem getCatalogItem(Rating rating){
        // each response is single movie json
        //Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
        System.out.println("Fetch movie info from http://movie-info-svc/movies/" + rating.getMovieId() +" ...");
        Movie movie = restTemplate.getForObject("http://movie-info-svc/movies/" + rating.getMovieId(), Movie.class);
        System.out.println("adding to list movie:" + movie.getName()+" "+ movie.getDescription()+" "+ rating.getRating());
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating){
        System.out.println("Hystrix getFallbackCatalogItem()");
        return new CatalogItem("Movie name not available. try again", "Unknown", rating.getRating());

    }

}
