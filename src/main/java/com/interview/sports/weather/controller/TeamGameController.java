package com.interview.sports.weather.controller;

import com.interview.sports.weather.controller.response.TeamGameEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TeamGameController {
    private static final WebClient webClient = WebClient.create();
    private static final String WEATHER_ENDPOINT = "[WEATHER_URL_HERE]/teamId=%s&startDate=%s&endDate=%s";
    public static Mono<TeamGameEvent> getTeamGameEvent(String teamId, String startDate, String endDate) {
        String SPORT_EVENT_URL = String.format(WEATHER_ENDPOINT,teamId, startDate, endDate);
        Mono<TeamGameEvent> events =  webClient.get()
                .uri(SPORT_EVENT_URL)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, clientResponse ->
                        clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(TeamGameEvent.class);

        return events;
    }
}
