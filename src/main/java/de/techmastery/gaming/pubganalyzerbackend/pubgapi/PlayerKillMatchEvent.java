package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;

public class PlayerKillMatchEvent extends MatchEvent {

    private final String victim;

    public PlayerKillMatchEvent(ZonedDateTime timestamp, String gamePhase, String victim) {
        super(timestamp, gamePhase);
        this.victim = victim;
    }

    public String getVictim() {
        return victim;
    }
}
