package com.interview.sports.weather.netty.response;

import com.interview.sports.weather.controller.response.Forecast;
import com.interview.sports.weather.controller.response.TeamGameEvent;

public class TeamGameEventResponse implements WeatherResponse{
    public String message;

    private TeamGameEventResponse(Forecast forecast, TeamGameEvent teamGameEvent) {
        //The following below I would never do, no validation and direct access.
        //I would have liked to extend to support a list of events and do more proper mapper to ensure the correct
        //event was getting mapped to the correct weather and also ensure the time.

        TeamGameEvent.GameEvent gameEvent = teamGameEvent.dates().get(0).games().get(0);
        TeamGameEvent.TeamData.Team homeTeam = gameEvent.teams().home().team();
        TeamGameEvent.TeamData.Team awayTeam = gameEvent.teams().away().team();
        Forecast.ForecastPeriod forecastPeriod = forecast.properties().periods().get(0);

        String gameDetails = homeTeam.name() + " @ " + awayTeam.name();
        this.message =  gameDetails + "\\n" +
                "Forecast for " + forecastPeriod.startTime() + ":" + "\\n" +
                forecastPeriod.detailedForecast();
    }

    public static TeamGameEventResponse getInstance(TeamGameEvent event, Forecast forecast) {
        return new TeamGameEventResponse(forecast, event);
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
