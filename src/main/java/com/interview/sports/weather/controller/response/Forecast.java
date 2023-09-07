package com.interview.sports.weather.controller.response;

import java.util.List;

public record Forecast(ForecastProperties properties) {
    public record ForecastProperties(List<ForecastPeriod> periods){ }
    public record ForecastPeriod(int number, String name, String startTime, String endTime, String detailedForecast){}
}
