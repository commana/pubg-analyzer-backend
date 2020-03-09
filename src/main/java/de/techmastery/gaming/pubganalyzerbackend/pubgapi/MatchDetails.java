package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import de.techmastery.gaming.pubganalyzerbackend.ClipStatus;
import de.techmastery.gaming.pubganalyzerbackend.NotFoundClipStatus;

import java.time.ZonedDateTime;
import java.util.List;

public class MatchDetails {

    private final List<MatchEvent> events;

    private final ZonedDateTime startTime;

    private ClipStatus clipStatus = new NotFoundClipStatus();

    public MatchDetails(List<MatchEvent> events, ZonedDateTime startTime) {
        this.events = events;
        this.startTime = startTime;
    }

    public List<MatchEvent> getEvents() {
        return events;
    }

    public ClipStatus getClipStatus() {
        return clipStatus;
    }

    public void setClipStatus(ClipStatus clipStatus) {
        this.clipStatus = clipStatus;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }
}
