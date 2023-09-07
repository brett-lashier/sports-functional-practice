package com.interview.sports.weather.controller.response;

import java.util.List;

public record TeamGameEvent(List<GameDate>dates) {
    public record GameDate(List<GameEvent> games){}
    public record GameEvent(TeamData teams, VenueData venue){}

    public record TeamData(AwayTeam away, HomeTeam home){
        public record AwayTeam(Team team){}
        public record HomeTeam(Team team){}
        public record Team(String id, String name){}
    }
    public record VenueData(String id, String name, String link){}
}
