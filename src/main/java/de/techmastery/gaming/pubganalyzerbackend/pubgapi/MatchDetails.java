package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.util.List;

public class MatchDetails {

    private final List<MatchEvent> events;

    public MatchDetails(List<MatchEvent> events) {
        this.events = events;
    }

    public List<MatchEvent> getEvents() {
        return events;
    }
}
