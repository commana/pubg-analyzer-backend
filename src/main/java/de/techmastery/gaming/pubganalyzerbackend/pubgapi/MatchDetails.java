package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;
import java.util.List;

public class MatchDetails {

    private final List<MatchEvent> events;

    private final ZonedDateTime startTime;

    public MatchDetails(List<MatchEvent> events, ZonedDateTime startTime) {
        this.events = events;
        this.startTime = startTime;
    }

    public List<MatchEvent> getEvents() {
        return events;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }
}
