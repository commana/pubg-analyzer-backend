package de.techmastery.gaming.pubganalyzerbackend.mixer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Recording {

    private long id;

    private String name;

    private String state;

    private float duration;

    private ZonedDateTime createdAt;

    private List<VideoOnDemand> vods;

    public boolean containsTime(ZonedDateTime time) {
        boolean streamStartedBeforeTargetTime = getStreamStartedAt().compareTo(time) < 0;
        boolean streamEndedAfterTargetTime = createdAt.compareTo(time) > 0;

        return streamStartedBeforeTargetTime && streamEndedAfterTargetTime;
    }

    private ZonedDateTime getStreamStartedAt() {
        return createdAt.minusSeconds((long)duration);
    }

    public long getRelativeTimeInSeconds(ZonedDateTime timestamp) {
        return SECONDS.between(getStreamStartedAt(), timestamp);
    }

    public String getPlaylistUrl() {
        VideoOnDemand vod = vods.stream().filter(v -> v.getFormat().equals("hls")).collect(Collectors.toList()).get(0);
        return vod.getBaseUrl() + "manifest.m3u8";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<VideoOnDemand> getVods() {
        return vods;
    }

    public void setVods(List<VideoOnDemand> vods) {
        this.vods = vods;
    }
}
