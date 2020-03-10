package de.techmastery.gaming.pubganalyzerbackend.clip;

public class Clip {
    private ClipState state;

    public Clip(ClipState state) {
        this.state = state;
    }

    public String getUrl() {
        return this.state.getUrl();
    }

    public void updateClipState(ClipState next) {
        this.state = next;
    }
}
