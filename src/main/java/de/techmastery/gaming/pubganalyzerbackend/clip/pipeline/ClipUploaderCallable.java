package de.techmastery.gaming.pubganalyzerbackend.clip.pipeline;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;

public class ClipUploaderCallable implements Callable<URI> {

    private final File tempFile;
    private final Path rootLocation;

    public ClipUploaderCallable(File tempFile) {
        this.tempFile = tempFile;
        // TODO: this is a no-op
        this.rootLocation = Paths.get("/tmp");
    }

    @Override
    public URI call() {
        try (InputStream is = new FileInputStream(tempFile)) {
            Path destination = this.rootLocation.resolve(tempFile.getName());
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toUri();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
