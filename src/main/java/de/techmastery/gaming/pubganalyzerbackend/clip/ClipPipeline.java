package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ClipPipeline implements Callable<URI> {

    private final ClipDownloaderCallable startTask;

    private final List<Clip> subscribers = new ArrayList<>();

    public ClipPipeline(ClipDownloaderCallable startTask) {
        this.startTask = startTask;
    }

    @Override
    public URI call() {
        File downloadedFile = startTask.call();
        File transcodedFile = new ClipTranscodeCallable(downloadedFile).call();
        URI result = new ClipUploaderCallable(transcodedFile).call();
        subscribers.forEach(x -> x.updateClipState(new CompletedClipState(result.toString())));
        return result;
    }

    public void onComplete(Clip c) {
        subscribers.add(c);
    }
}
