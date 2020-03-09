package de.techmastery.gaming.pubganalyzerbackend.mixer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Streamer {

    private long id;

    private List<VideoOnDemand> vods;

    public boolean hasVOD(ZonedDateTime eventTime) {
        for (VideoOnDemand vod : vods) {
            if (vod.containsTime(eventTime)) {
                return true;
            }
        }
        return false;
    }

    public VideoOnDemand getVOD(ZonedDateTime eventTime) {
        for (VideoOnDemand vod : vods) {
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

    public List<VideoOnDemand> getVods() {
        return vods;
    }

    public void setVods(List<VideoOnDemand> vods) {
        this.vods = vods;
    }
}
