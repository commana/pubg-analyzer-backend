package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.clip.pipeline.ClipDownloaderCallable;
import de.techmastery.gaming.pubganalyzerbackend.clip.pipeline.ClipPipeline;
import de.techmastery.gaming.pubganalyzerbackend.clip.pipeline.ClipUploaderCallable;
import io.lindstrom.m3u8.model.MediaSegment;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.*;

public class TestTest {

    @Test
    public void testCompleteableFuture() throws ExecutionException, InterruptedException {
        ClipDownloaderCallable downloader = new ClipDownloaderCallable(
                MediaSegment.builder().uri("0000004.ts").duration(2.0).build(),
                "https://vodcontent-7002.xboxlive.com/channel-47094669-public/13bcfd88-1d30-40d0-80a4-25f1ad849120/");
        CompletableFuture<File> f = CompletableFuture.supplyAsync(downloader::call);

        // implemented as a no-op
        CompletableFuture<URI> g = f.thenApply(x -> new ClipUploaderCallable(x).call());

        g.join();

        assertTrue(g.get().toString().contains("tmp/clip-task"));
    }

    @Test
    public void testClipPipeline() throws ExecutionException, InterruptedException {
        ClipDownloaderCallable startTask = new ClipDownloaderCallable(
                MediaSegment.builder().uri("0000004.ts").duration(2.0).build(),
                "https://vodcontent-7002.xboxlive.com/channel-47094669-public/13bcfd88-1d30-40d0-80a4-25f1ad849120/");

        TaskExecutor exec = new SyncTaskExecutor();

        FutureTask<URI> task = new FutureTask<>(new ClipPipeline(startTask));
        exec.execute(task);

        assertTrue(task.get().toString().contains(".mp4"));
    }
}
