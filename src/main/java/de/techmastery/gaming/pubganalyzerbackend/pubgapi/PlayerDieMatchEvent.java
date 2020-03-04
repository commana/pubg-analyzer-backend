package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;

public class PlayerDieMatchEvent extends MatchEvent {

    private final String killer;

    public PlayerDieMatchEvent(ZonedDateTime timestamp, String gamePhase, String killer) {
        super(timestamp, gamePhase);
        this.killer = killer;
    }

    public String getKiller() {
        return killer;
    }
}
