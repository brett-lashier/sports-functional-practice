package com.interview.sports.weather.netty.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class DefaultWeatherResponse implements WeatherResponse{
    @JsonSerialize
    private String message;
    private int status;

    public DefaultWeatherResponse(String message) {
        this.message = message;
        this.status = status;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
