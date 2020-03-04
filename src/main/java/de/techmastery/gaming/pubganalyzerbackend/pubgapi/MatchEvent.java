package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;

public abstract class MatchEvent {

    private final ZonedDateTime timestamp;

    private final String gamePhase;

    public MatchEvent(ZonedDateTime timestamp, String gamePhase) {
        this.timestamp = timestamp;
        this.gamePhase = gamePhase;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getGamePhase() {
        return gamePhase;
    }
}
