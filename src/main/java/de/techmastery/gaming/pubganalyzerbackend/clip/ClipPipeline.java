package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Callable;

public class ClipPipeline implements Callable<URI> {

    private final ClipDownloaderCallable startTask;

    public ClipPipeline(ClipDownloaderCallable startTask) {
        this.startTask = startTask;
    }

    @Override
    public URI call() {
        File downloadedFile = startTask.call();
        File transcodedFile = new ClipTranscodeCallable(downloadedFile).call();
        return new ClipUploaderCallable(transcodedFile).call();
    }
}
