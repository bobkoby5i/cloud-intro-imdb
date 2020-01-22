# VHS Catalog


Eureka discovery resolves names to registered services. Client (catalog-svc) does load balancing.  

* discovery
* catalog-svc http://localhost:8083/catalog/11 (request from postman no direct instance)
* movie-info-svc http://movie-info-svc/movies/123 -> fetch movie info from http://www.themoviedb.org 
* ratings-svc http://ratings-svc/ratings/user/12
 


## Application flow:
Get catalog info for UserId. 
Catalog service sends requests to rating-svc to get list of (movieId, rating) for userId 
For each movieId request is sent to movie-info-svc to get (name, description)
Then movie-info-svc sends request to external API https://api.themoviedb.org
Combined information returned to client (Postman)
Discovery server resolves names 
Data is mocked up. (no DB)  

Postman:
* http://localhost:8181/movies/123 GET MovieInfo for movieId
* http://localhost:8182/ratings/user/12 GET ratings for userId
* http://localhost:8182/ratings/movie/2345 GET rating for movieId 
* http://localhost:8083/catalog/11 GET catalog (movie_name, movie_description, rating) for userId
 


## start many instances:
* start many instances:
    * java -jar -Dserver.port=8083 .\svc-catalog\build\libs\svc-catalog-0.0.1-SNAPSHOT.jar
    * java -jar -Dserver.port=8181 .\svc-movie-info\build\libs\svc-movie-info-0.0.1-SNAPSHOT.jar
    * java -jar -Dserver.port=8281 .\svc-movie-info\build\libs\svc-movie-info-0.0.1-SNAPSHOT.jar
    * java -jar -Dserver.port=8182 .\svc-ratings\build\libs\svc-ratings-0.0.1-SNAPSHOT.jar
    * java -jar -Dserver.port=8282 .\svc-ratings\build\libs\svc-ratings-0.0.1-SNAPSHOT.jar

### Change Log

* 2020-01-22 add fetch movies from https://www.themoviedb.org
* 2020-01-21 add Discovery Seervice & Load balancer
* 2020-01-20 add Communication REST template, mocked up responses
* 2020-01-20 add Services: Catalog, MovieInfo, Ratings;  RestControlers

