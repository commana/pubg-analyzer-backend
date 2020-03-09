package de.techmastery.gaming.pubganalyzerbackend.clip;

public class CompletedClipState extends ClipState {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
