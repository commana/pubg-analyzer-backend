package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PendingClipState extends ClipState {

    private final UUID id;

    private final List<ClipDownloaderCallable> tasks = new ArrayList<>();

    public PendingClipState(UUID uuid) {
        this.id = uuid;
    }

    public void add(ClipDownloaderCallable ct) {
        tasks.add(ct);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public UUID getId() {
        return id;
    }
}
