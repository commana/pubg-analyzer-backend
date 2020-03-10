package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.io.File;

public class CompletedClipState extends ClipState {

    private final File localFile;

    public CompletedClipState(File localFile) {
        this.localFile = localFile;
    }

    @Override
    public File getLocalFile() {
        return localFile;
    }
}
