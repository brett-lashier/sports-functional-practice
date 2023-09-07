package com.interview.sports.weather.controller;

import com.interview.sports.weather.controller.response.*;
import com.interview.sports.weather.netty.response.TeamGameEventResponse;
import com.interview.sports.weather.netty.response.VenueWeather;
import com.interview.sports.weather.conversion.FloatConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//I would also like to implement caching for this service so we do not have to keep making calls out to the weather service
public class WeatherController {
    private static final WebClient webClient = WebClient.create();
    private static final String WEATHER_ENDPOINT = "[WEATHER_URL_HERE]?lat=%s&lgt=%s";
    public static Flux<TeamGameEventResponse> getWeatherForGameEvent(TeamGameEvent event, Stadium stadium) {
        DefaultCoordinates coordinates = stadium.location().defaultCoordinates();
        String lat = FloatConverter.roundStringFloatNPlaces(4, coordinates.latitude());
        String lgt = FloatConverter.roundStringFloatNPlaces(4, coordinates.longitude());

        String WEATHER_URL = String.format(WEATHER_ENDPOINT,lat, lgt);
        return webClient.get()
                .uri(WEATHER_URL)
                .retrieve()
                .bodyToFlux(WeatherPeriodForecast.class)
                .filter(data -> data != null && data.properties() != null && data.properties().forecast() != null)
                .flatMap(venueWeatherData -> getWeatherPeriodsForEvent(venueWeatherData, event));

    }

    public static Flux<VenueWeather> getWeatherForVenue(Stadium stadium) {
        DefaultCoordinates coordinates = stadium.location().defaultCoordinates();
        String lat = FloatConverter.roundStringFloatNPlaces(4, coordinates.latitude());
        String lgt = FloatConverter.roundStringFloatNPlaces(4, coordinates.longitude());

        String WEATHER_URL = String.format(WEATHER_ENDPOINT,lat, lgt);
        return webClient.get()
                .uri(WEATHER_URL)
                .retrieve()
                .bodyToFlux(WeatherPeriodForecast.class)
                .filter(data -> data != null && data.properties() != null && data.properties().forecast() != null)
                .flatMap(venueWeatherData -> getWeatherPeriodsVenue(venueWeatherData, stadium));
    }

    public static Flux<TeamGameEventResponse> getWeatherPeriodsForEvent(WeatherPeriodForecast weatherPeriodForecast, TeamGameEvent event){
        return webClient.get()
                .uri(weatherPeriodForecast.properties().forecast())
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, clientResponse ->
                        clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToFlux(Forecast.class)
                .filter(forecast -> forecast != null &&
                        forecast.properties() != null &&
                        forecast.properties().periods() != null &&
                        !forecast.properties().periods().isEmpty())
                .flatMap(forecast -> mapForecastToGameEventResponse(forecast, event));
    }

    public static Flux<VenueWeather> getWeatherPeriodsVenue(WeatherPeriodForecast url, Stadium stadium){
        return webClient.get()
                .uri(url.properties().forecast())
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, clientResponse ->
                        clientResponse.bodyToMono(String.class).map(Exception::new))
                .bodyToFlux(Forecast.class)
                .filter(forecast -> forecast != null &&
                        forecast.properties() != null &&
                        forecast.properties().periods() != null &&
                        !forecast.properties().periods().isEmpty())
                .flatMap(forecast -> mapForecastToResponse(forecast, stadium));
    }

    private static Mono<VenueWeather> mapForecastToResponse(Forecast forecastMono, Stadium stadium) {
        return Mono.just(VenueWeather.getInstance(stadium, forecastMono));
    }

    private static Mono<TeamGameEventResponse> mapForecastToGameEventResponse(Forecast forecastMono, TeamGameEvent event) {
        return Mono.just(TeamGameEventResponse.getInstance(event, forecastMono));
    }
}
