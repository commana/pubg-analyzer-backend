package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.techmastery.gaming.pubganalyzerbackend.clip.Clip;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipInfo;

import java.time.ZonedDateTime;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME)
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
