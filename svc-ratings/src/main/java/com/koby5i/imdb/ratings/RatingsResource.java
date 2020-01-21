package com.koby5i.imdb.ratings;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    // return one movie rating
    // {
    //    "movieId": "1",
    //    "rating": 4
    // }
    @RequestMapping("/movie/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }


    // return all movies and ratings for userId
    // {
    //    "userId": "1",
    //    "ratings": [
    //        {
    //            "movieId": "1234",
    //            "rating": 3
    //        },
    //        {
    //            "movieId": "5678",
    //            "rating": 4
    //        }
    //    ]
    //}
    @RequestMapping("/user/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;
    }
}
