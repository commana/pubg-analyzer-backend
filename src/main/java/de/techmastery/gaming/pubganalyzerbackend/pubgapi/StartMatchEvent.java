package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;

public class StartMatchEvent extends MatchEvent {
    public StartMatchEvent(ZonedDateTime timestamp, String gamePhase) {
        super(timestamp, gamePhase);
    }
}
