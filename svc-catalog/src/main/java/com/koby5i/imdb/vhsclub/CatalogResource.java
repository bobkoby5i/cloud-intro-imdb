package com.koby5i.imdb.vhsclub;

import com.netflix.discovery.DiscoveryClient;
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

        // get all movieId's and ratings  for userId from /ratings/{userId}
        System.out.println("Fetch movies and ratings for user = "+userId+" from http://ratings-svc/ratings/user/" + userId + " ...");
        //UserRating userRating = restTemplate.getForObject("http://localhost:8082/ratings/user/" + userId, UserRating.class);
        UserRating userRating = restTemplate.getForObject("http://ratings-svc/ratings/user/" + userId, UserRating.class);



        // for each movieId get movie details from /movies/{movieId}
        //put tem all together  and return collection
        // use List<Ratings> list and produce List<CatalogItem>
        return userRating.getRatings().stream()
            .map(rating -> {
            System.out.println("Fetch movie info from http://movie-info-svc/movies/" + rating.getMovieId() +" ...");

        // each response is single movie json
        //Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
        Movie movie = restTemplate.getForObject("http://movie-info-svc/movies/" + rating.getMovieId(), Movie.class);
        System.out.println("adding to list movie:" + movie.getName()+" "+ movie.getDescription()+" "+ rating.getRating());
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
        }).collect(Collectors.toList());


        //return Collections.singletonList(new CatalogItem("TestName", "Test Desc", 4));
        }
        }
