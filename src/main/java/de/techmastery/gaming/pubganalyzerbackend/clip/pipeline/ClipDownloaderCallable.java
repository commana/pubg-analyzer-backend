package de.techmastery.gaming.pubganalyzerbackend.clip.pipeline;

import io.lindstrom.m3u8.model.MediaSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ClipDownloaderCallable implements Callable<File> {
    private static final Logger logger = LoggerFactory.getLogger(ClipDownloaderCallable.class);

    private final MediaSegment segment;
    private final String baseUrl;

    public ClipDownloaderCallable(MediaSegment segment, String baseUrl) {
        this.segment = segment;
        this.baseUrl = baseUrl;
    }

    @Override
    public File call() {
        try {
            Flux<DataBuffer> videoContent =
                    WebClient.create(baseUrl + segment.uri())
                            .get().accept(MediaType.APPLICATION_OCTET_STREAM)
                            .retrieve()
                            .bodyToFlux(DataBuffer.class);
            File tempFile = File.createTempFile("clip-task-", ".ts");
            logger.info("Writing to tempfile {}", tempFile);
            DataBufferUtils.write(videoContent, new FileOutputStream(tempFile)).map(DataBufferUtils::release).blockLast();
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
