package com.interview.sports.weather.netty.handler;

import com.interview.sports.weather.netty.response.WeatherResponse;
import com.interview.sports.weather.service.WeatherRetrievalService;
import com.interview.sports.weather.validation.WeatherValidator;
import com.interview.sports.weather.netty.request.VenueWeatherRequest;
import com.interview.sports.weather.netty.response.DefaultWeatherResponse;
import com.interview.sports.weather.netty.router.path.directory.SportsWeatherDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WeatherHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private WeatherRetrievalService weatherRetrievalService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(VenueWeatherRequest.class).flatMap(req -> {
            switch (request.path()) {
                case SportsWeatherDirectory.SPORTS_VENUE_WEATHER_ENDPOINT -> {
                    if(!WeatherValidator.isValidVenueWeatherRequest(req)) {
                        return ServerResponse.ok().body(Flux.just(new DefaultWeatherResponse("Invalid body")), WeatherResponse.class);
                    }
                    return weatherRetrievalService.retrieveVenueWeatherEvent(req);
                }
            }
            return ServerResponse.ok().body(Mono.just(new DefaultWeatherResponse("Invalid Path")), WeatherResponse.class);
        }).switchIfEmpty(ServerResponse.ok().body(Mono.just(new DefaultWeatherResponse("Check Venue, Invalid venueId")), WeatherResponse.class));
    }
}
