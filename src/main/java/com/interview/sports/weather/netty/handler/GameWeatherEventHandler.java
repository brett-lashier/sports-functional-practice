package com.interview.sports.weather.netty.handler;

import com.interview.sports.weather.netty.request.GameWeatherRequest;
import com.interview.sports.weather.netty.response.DefaultWeatherResponse;
import com.interview.sports.weather.netty.response.WeatherResponse;
import com.interview.sports.weather.netty.router.path.directory.SportsWeatherDirectory;
import com.interview.sports.weather.service.WeatherRetrievalService;
import com.interview.sports.weather.validation.WeatherValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GameWeatherEventHandler implements HandlerFunction<ServerResponse> {

    private WeatherRetrievalService weatherRetrievalService;

    @Autowired
    public void setWeatherRetrievalService(WeatherRetrievalService weatherRetrievalService) {
        this.weatherRetrievalService = weatherRetrievalService;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(GameWeatherRequest.class).flatMap(req -> {
            switch (request.path()) {
                case SportsWeatherDirectory.SPORTS_TEAM_WEATHER_ENDPOINT -> {
                    if(!WeatherValidator.isValidGameWeatherRequest(req)) {
                        return ServerResponse.ok().body(Flux.just(new DefaultWeatherResponse("Invalid body")), WeatherResponse.class);
                    } else if(!WeatherValidator.isDateWithinAWeek(req)) {
                        return ServerResponse.ok().body(Flux.just(new DefaultWeatherResponse("Invalid date format, date must be a past event or a future date no longer than a week from current date")), WeatherResponse.class);
                    }
                    return weatherRetrievalService.retrieveTeamWeatherEvent(req);
                }
            }
            return ServerResponse.ok().body(Mono.just(new DefaultWeatherResponse("Invalid Path")), WeatherResponse.class);
        }).switchIfEmpty(ServerResponse.ok().body(Mono.just(new DefaultWeatherResponse("Check Date and Venue, Invalid team date combination")), WeatherResponse.class));
    }
}