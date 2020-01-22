package com.koby5i.imdb.vhsclub;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = getUserRating(userId);
        // for each movieId get movie details from /movies/{movieId}
        // put tem all together  and return collection
        // use List<Ratings> list and produce List<CatalogItem>
        return userRating.getRatings().stream()
            .map(rating -> getCatalogItem(rating))
            .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    private UserRating getUserRating(@PathVariable("userId") String userId) {
        // get all movieId's and ratings  for userId from /ratings/{userId}
        System.out.println("Fetch movies and ratings for user = " + userId + " from http://ratings-svc/ratings/user/" + userId + " ...");
        //UserRating userRating = restTemplate.getForObject("http://localhost:8082/ratings/user/" + userId, UserRating.class);
        UserRating userRating = restTemplate.getForObject("http://ratings-svc/ratings/user/" + userId, UserRating.class);
        return userRating;
    }

    private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
        System.out.println("Hystrix getFallbackUserRating()");
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("0",0)));
        return userRating;
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating){
        // each response is single movie json
        //Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
        System.out.println("Fetch movie info from http://movie-info-svc/movies/" + rating.getMovieId() +" ...");
        Movie movie = restTemplate.getForObject("http://movie-info-svc/movies/" + rating.getMovieId(), Movie.class);
        System.out.println("adding to list movie:" + movie.getName()+" "+ movie.getDescription()+" "+ rating.getRating());
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating){
        System.out.println("Hystrix getFallbackCatalogItem()");
        return new CatalogItem("Movie name not available. try again", "Unknown", rating.getRating());

    }

}
