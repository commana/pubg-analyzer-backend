package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class ClipTranscodeCallable implements Callable<File> {

    private final File downloadedFile;

    public ClipTranscodeCallable(File downloadedFile) {
        this.downloadedFile = downloadedFile;
    }

    @Override
    public File call() {
        try {
            Path outputFile = Paths.get("/tmp").resolve(downloadedFile.getName().replaceAll("\\.ts", ".mp4"));
            Process process = new ProcessBuilder("ffmpeg", "-i", downloadedFile.getAbsolutePath(), "-codec", "copy", outputFile.toString()).start();
            int waitFlag = process.waitFor();
            if (waitFlag == 0) {
                return outputFile.toFile();
            }
            throw new RuntimeException("Something went wrong: ffmpeg exit value was " + process.exitValue());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
