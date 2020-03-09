package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.clip.ClipDownloaderCallable;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipUploaderCallable;
import io.lindstrom.m3u8.model.MediaSegment;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class TestTest {

    @Test
    public void testCompleteableFuture() throws ExecutionException, InterruptedException {
        ClipDownloaderCallable downloader = new ClipDownloaderCallable(
                MediaSegment.builder().uri("0000004.ts").duration(2.0).build(),
                "https://vodcontent-4005.xboxlive.com/channel-47094669-public/d69f48cd-fb42-4226-854d-e391230892a2/");
        CompletableFuture<File> f = CompletableFuture.supplyAsync(downloader::call);

        // implemented as a no-op
        CompletableFuture<URI> g = f.thenApply(x -> new ClipUploaderCallable(x).call());

        g.join();

        assertTrue(g.get().toString().contains("tmp/clip-task"));
    }
}
