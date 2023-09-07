package com.interview.sports.weather.service;

import com.interview.sports.weather.controller.WeatherController;
import com.interview.sports.weather.controller.response.TeamGameEvent;
import com.interview.sports.weather.netty.response.TeamGameEventResponse;
import com.interview.sports.weather.netty.response.VenueWeather;
import com.interview.sports.weather.controller.StadiumController;
import com.interview.sports.weather.controller.TeamGameController;
import com.interview.sports.weather.netty.request.GameWeatherRequest;
import com.interview.sports.weather.netty.request.VenueWeatherRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class WeatherRetrievalService {
    public Mono<ServerResponse> retrieveTeamWeatherEvent(GameWeatherRequest request) {
        Mono<TeamGameEvent> test = TeamGameController.getTeamGameEvent(request.team(), request.date(), request.date());
        return test.filter(teamEvent -> teamEvent != null && teamEvent.dates() != null && !teamEvent.dates().isEmpty())
                .flatMap(teamGameEvent -> retrieveVenueWeatherEvent(
                new VenueWeatherRequest(teamGameEvent.dates()
                        .get(0)
                        .games()
                        .get(0)
                        .venue().id()), teamGameEvent));
    }

    public Mono<ServerResponse> retrieveVenueWeatherEvent(VenueWeatherRequest request, TeamGameEvent event) {
        Flux<TeamGameEventResponse> test = StadiumController.getStadiumEventInfo(request.venue())
                .filter(venues -> venues != null && venues.venues() != null && !venues.venues().isEmpty())
                .flatMapMany(venueInfo -> Flux.concat(venueInfo
                        .venues()
                        .stream()
                        .map(venue -> WeatherController.getWeatherForGameEvent(event, venue))
                        .collect(Collectors.toList())));
        return ServerResponse.ok().body(test, TeamGameEventResponse.class);
    }

    public Mono<ServerResponse> retrieveVenueWeatherEvent(VenueWeatherRequest request) {
        Flux<VenueWeather> test = StadiumController.getStadiumEventInfo(request.venue())
                .filter(venueInfo -> venueInfo != null && venueInfo.venues() != null & !venueInfo.venues().isEmpty())
                .flatMapMany(venueInfo -> Flux.concat(venueInfo
                        .venues()
                        .stream()
                        .filter(venueData -> venueData != null &&
                                venueData.location() != null &&
                                venueData.location().defaultCoordinates() != null)
                        .map(venue -> WeatherController.getWeatherForVenue(venue))
                        .collect(Collectors.toList())));
        return ServerResponse.ok().body(test, VenueWeather.class);
    }
}
