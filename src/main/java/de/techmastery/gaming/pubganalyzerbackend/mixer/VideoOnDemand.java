package de.techmastery.gaming.pubganalyzerbackend.mixer;

import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;

public class VideoOnDemand {
    private VideoData data;
    private String id;
    private String baseUrl;
    private String format;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private long recordingId;

    public VideoData getData() {
        return data;
    }

    public void setData(VideoData data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(long recordingId) {
        this.recordingId = recordingId;
    }

    private static class VideoData {
        @SerializedName("Width")
        private int width;
        @SerializedName("Height")
        private int height;
        @SerializedName("Has720pPreview")
        private boolean has720pPreview;
        @SerializedName("Fps")
        private double fps;
        @SerializedName("Bitrate")
        private long bitrate;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public boolean isHas720pPreview() {
            return has720pPreview;
        }

        public void setHas720pPreview(boolean has720pPreview) {
            this.has720pPreview = has720pPreview;
        }

        public double getFps() {
            return fps;
        }

        public void setFps(double fps) {
            this.fps = fps;
        }

        public long getBitrate() {
            return bitrate;
        }

        public void setBitrate(long bitrate) {
            this.bitrate = bitrate;
        }
    }
}
