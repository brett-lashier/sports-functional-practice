package com.interview.sports.weather.netty.router;

import com.interview.sports.weather.netty.handler.GameWeatherEventHandler;
import com.interview.sports.weather.netty.handler.WeatherHandler;
import com.interview.sports.weather.netty.router.path.directory.SportsWeatherDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class WeatherRouter {
    @Bean
    public RouterFunction<ServerResponse> getVenueWeatherEvent(WeatherHandler handler) {
        return RouterFunctions.route(RequestPredicates
                .POST(SportsWeatherDirectory.SPORTS_VENUE_WEATHER_ENDPOINT)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::handle);
    }

    @Bean
    public RouterFunction<ServerResponse> getGameWeatherEvent(GameWeatherEventHandler handler) {
        return RouterFunctions.route(RequestPredicates
                .POST(SportsWeatherDirectory.SPORTS_TEAM_WEATHER_ENDPOINT)
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::handle);
    }
}
