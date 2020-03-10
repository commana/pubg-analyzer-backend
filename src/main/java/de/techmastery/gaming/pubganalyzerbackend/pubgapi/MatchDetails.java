package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import de.techmastery.gaming.pubganalyzerbackend.clip.ClipUrl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchDetails {

    private final List<MatchEvent> events;

    private final ZonedDateTime startTime;

    private List<ClipUrl> clips = new ArrayList<>();

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

    public List<ClipUrl> getClips() {
        return clips;
    }

    public void setClips(List<ClipUrl> clips) {
        this.clips = clips;
    }
}
