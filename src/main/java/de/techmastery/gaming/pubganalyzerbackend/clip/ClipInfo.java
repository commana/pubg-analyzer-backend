package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.util.UUID;

public class ClipInfo {

    private final UUID id;

    public ClipInfo(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
