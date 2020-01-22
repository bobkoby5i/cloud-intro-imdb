package com.koby5i.imdb.vhsclub.service;

import com.koby5i.imdb.vhsclub.Rating;
import com.koby5i.imdb.vhsclub.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    public UserRating getUserRating(String userId) {
        // get all movieId's and ratings  for userId from /ratings/{userId}
        System.out.println("Fetch movies and ratings for user = " + userId + " from http://ratings-svc/ratings/user/" + userId + " ...");
        //UserRating userRating = restTemplate.getForObject("http://localhost:8082/ratings/user/" + userId, UserRating.class);
        UserRating userRating = restTemplate.getForObject("http://ratings-svc/ratings/user/" + userId, UserRating.class);
        return userRating;
    }

    public UserRating getFallbackUserRating(String userId) {
        System.out.println("Hystrix getFallbackUserRating()");
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(Arrays.asList(new Rating("0",0)));
        return userRating;
    }
}
