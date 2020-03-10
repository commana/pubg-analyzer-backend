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

    public ClipProcessor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public ClipState process(Recording recording, List<MatchEvent> events) {
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
                        ClipDownloaderCallable startTask = new ClipDownloaderCallable(segment, recording.getBaseUrl());
                        FutureTask<URI> task = new FutureTask<>(new ClipPipeline(startTask));
                        break;
                    }
                }

            }
            return null;
        } catch (PlaylistParserException e) {
            return new NotFoundClipState();
        }
    }

}
