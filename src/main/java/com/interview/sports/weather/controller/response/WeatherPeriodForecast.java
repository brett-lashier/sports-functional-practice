package com.interview.sports.weather.controller.response;

public record WeatherPeriodForecast(VenueWeatherProperties properties) {
    public record VenueWeatherProperties(String forecast) { }
}
