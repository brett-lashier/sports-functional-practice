package com.interview.sports.weather.controller;

import com.interview.sports.weather.controller.response.StadiumEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public final class StadiumController {
    private static final WebClient webClient = WebClient.create();
    private static final String WEATHER_ENDPOINT = "[WEATHER_URL_HERE]/%s";
    // Caching could be implemented to prevent constant calling to the weather endpoint
    // We would need to set TTL to expire after 24 hours
    public static Mono<StadiumEvent> getStadiumEventInfo(String venueId) {
        String SPORT_EVENT_URL = String.format(WEATHER_ENDPOINT,venueId);
        Mono<StadiumEvent> events =  webClient.get()
                .uri(SPORT_EVENT_URL)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse ->
                        clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(StadiumEvent.class);

        return events;
    }
}
