package com.koby5i.imdb.vhsclub;

import com.koby5i.imdb.vhsclub.service.MovieInfo;
import com.koby5i.imdb.vhsclub.service.UserRatingInfo;
import com.netflix.appinfo.InstanceInfo;

import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        //
        // show instances found
        //

        List<String> services = discoveryClient.getServices();

        for (String serviceId : services) {
            System.out.println("Eureka registered service: " + serviceId );
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);

            for (ServiceInstance serviceInstance : serviceInstances) {
                String host = serviceInstance.getHost();
                int port = serviceInstance.getPort();
                System.out.println("serviceId: "+serviceId + " host: " + host + " port: " + port);
            }
        }

        UserRating userRating = userRatingInfo.getUserRating(userId);
        // use use List<Ratings> and for each movieId get movie details from /movies/{movieId}
        // put tem all together  and return collection List<CatalogItem>
        return userRating.getRatings().stream()
            .map(rating -> movieInfo.getCatalogItem(rating))
            .collect(Collectors.toList());
    }

}
