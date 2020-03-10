package de.techmastery.gaming.pubganalyzerbackend.clip;

public class CompletedClipState extends ClipState {

    private final String url;

    public CompletedClipState(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
