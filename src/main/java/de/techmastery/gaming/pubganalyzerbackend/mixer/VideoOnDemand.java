package de.techmastery.gaming.pubganalyzerbackend.mixer;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

public class VideoOnDemand {
    private String contentId;
    private List<ContentLocator> contentLocators;
    private String contentState;
    private long durationInSeconds;
    private ZonedDateTime uploadDate;

    public String getPlaylistUrl() {
        return contentLocators.stream()
                .filter(c -> c.locatorType.equals("SmoothStreaming"))
                .collect(Collectors.toList())
                .get(0)
                .getUri();
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public List<ContentLocator> getContentLocators() {
        return contentLocators;
    }

    public void setContentLocators(List<ContentLocator> contentLocators) {
        this.contentLocators = contentLocators;
    }

    public String getContentState() {
        return contentState;
    }

    public void setContentState(String contentState) {
        this.contentState = contentState;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean containsTime(ZonedDateTime time) {
        ZonedDateTime streamStartTime = uploadDate.minusSeconds(durationInSeconds);

        boolean streamStartedBeforeTargetTime = streamStartTime.compareTo(time) < 0;
        boolean streamEndedAfterTargetTime = uploadDate.compareTo(time) > 0;

        return  streamStartedBeforeTargetTime && streamEndedAfterTargetTime;
    }

    public long getRelativeTime(ZonedDateTime timestamp) {
        ZonedDateTime streamStartTime = uploadDate.minusSeconds(durationInSeconds);

        return SECONDS.between(streamStartTime, timestamp);
    }

    private static class ContentLocator {
        private String locatorType;
        private String uri;

        public String getLocatorType() {
            return locatorType;
        }

        public void setLocatorType(String locatorType) {
            this.locatorType = locatorType;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
