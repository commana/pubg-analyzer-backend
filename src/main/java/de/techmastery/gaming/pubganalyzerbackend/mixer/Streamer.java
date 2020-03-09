package de.techmastery.gaming.pubganalyzerbackend.mixer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Streamer {

    private long id;

    private List<Recording> recordings;

    public boolean hasRecording(ZonedDateTime eventTime) {
        for (Recording vod : recordings) {
            if (vod.containsTime(eventTime)) {
                return true;
            }
        }
        return false;
    }

    public Recording getRecording(ZonedDateTime eventTime) {
        for (Recording vod : recordings) {
            if (vod.containsTime(eventTime)) {
                return vod;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> vods) {
        this.recordings = vods;
    }
}
