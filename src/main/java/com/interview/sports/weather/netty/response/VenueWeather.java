package com.interview.sports.weather.netty.response;

import com.interview.sports.weather.controller.response.Forecast;
import com.interview.sports.weather.controller.response.Stadium;

public class VenueWeather implements WeatherResponse{

    public String message;

    private VenueWeather(Stadium stadium, Forecast forecast) {
        Forecast.ForecastPeriod forecastPeriod = forecast.properties().periods().get(0);
        this.message = stadium.name() + "," + stadium.location().state() + "\\n" +
                "Current Weather" + "\\n" +
                forecastPeriod.detailedForecast();
    }

    public static VenueWeather getInstance(Stadium stadium, Forecast forecast) {
        return new VenueWeather(stadium, forecast);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
