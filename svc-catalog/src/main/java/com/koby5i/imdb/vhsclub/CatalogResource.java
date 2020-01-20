package com.koby5i.imdb.vhsclub;

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

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.getForObject("localhost:8081/movies/1",Movie.class);

        // get all movieId's and ratings  for userId from /ratings/{userId}
        List<Rating> ratings = Arrays.asList(
                new Rating("123", 3),
                new Rating("124", 4)
        );

        // for each movieId get movie details from /movies/{movieId}

        //put tem all together  and return collection
        // use List<Ratings> list and produce List<CatalogItem>
        return ratings.stream().map(rating -> new CatalogItem("MOvieNAme", "Description", 4))
                .collect(Collectors.toList());

        //return Collections.singletonList(new CatalogItem("TestName", "Test Desc", 4));
    }
}
