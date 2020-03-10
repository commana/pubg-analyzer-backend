package de.techmastery.gaming.pubganalyzerbackend.clip;

import de.techmastery.gaming.pubganalyzerbackend.mixer.Recording;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchEvent;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.*;
import java.util.concurrent.FutureTask;

public class ClipProcessor {

    private final TaskExecutor taskExecutor;

    private final ClipStorage clipStorage;

    public ClipProcessor(TaskExecutor taskExecutor, ClipStorage clipStorage) {
        this.taskExecutor = taskExecutor;
        this.clipStorage = clipStorage;
    }

    public ClipState process(Recording recording, ClipIdentifier identifier, List<MatchEvent> events) {
        try {
            WebClient webClient = WebClient.builder().baseUrl(recording.getPlaylistUrl()).build();
            String playListSrc = webClient.method(HttpMethod.GET).exchange().block().bodyToMono(String.class).block();
            MediaPlaylistParser parser = new MediaPlaylistParser();
            MediaPlaylist playlist = parser.readPlaylist(playListSrc.replaceAll("#EXT-X-ALLOW-CACHE:(YES|NO)", ""));
            for (MatchEvent event : events) {
                long seconds = recording.getRelativeTimeInSeconds(event.getTimestamp());
                double playlistSegmentTime = 0;
                for (MediaSegment segment : playlist.mediaSegments()) {
                    playlistSegmentTime += segment.duration();
                    if (playlistSegmentTime >= seconds) {
                        Clip c = new Clip(new PendingClipState());
                        ClipDownloaderCallable startTask = new ClipDownloaderCallable(segment, recording.getBaseUrl());
                        ClipPipeline pipe = new ClipPipeline(startTask);
                        pipe.onComplete(c);
                        clipStorage.put(identifier, c);
                        FutureTask<URI> task = new FutureTask<>(pipe);
                        taskExecutor.execute(task);
                        break;
                    }
                }

            }
            return null;
        } catch (PlaylistParserException e) {
            return new NotFoundClipState();
        }
    }

    public ClipStorage getClipStorage() {
        return clipStorage;
    }
}
