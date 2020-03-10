package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.io.File;

public class Clip {
    private final ClipIdentifier identifier;

    private ClipState state;

    public Clip(ClipIdentifier identifier, ClipState state) {
        this.identifier = identifier;
        this.state = state;
    }

    public File getLocalFile() {
        return this.state.getLocalFile();
    }

    public void updateClipState(ClipState next) {
        this.state = next;
    }

    public ClipIdentifier getIdentifier() {
        return identifier;
    }

    public boolean isCompleted() {
        return state instanceof CompletedClipState;
    }

    public boolean isPending() {
        return state instanceof PendingClipState;
    }

    public boolean isNotFound() {
        return state instanceof NotFoundClipState;
    }
}
