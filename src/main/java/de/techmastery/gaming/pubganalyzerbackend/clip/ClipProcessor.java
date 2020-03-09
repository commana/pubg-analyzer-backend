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

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ClipProcessor {

    private static final Map<UUID, List<ClipDownloaderCallable>> storage = new HashMap<>();

    private final TaskExecutor taskExecutor;

    public ClipProcessor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public ClipState process(Recording recording, List<MatchEvent> events) {
        try {
            ArrayList<ClipDownloaderCallable> clipList = new ArrayList<>();
            UUID uuid = UUID.randomUUID();

            storage.put(uuid, clipList);

            PendingClipState pendingClipState = new PendingClipState(uuid);
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
                        ClipDownloaderCallable task = new ClipDownloaderCallable(segment, recording.getBaseUrl());
                        CompletableFuture<File> f = CompletableFuture.supplyAsync(task::call, this.taskExecutor);
                        //f.thenApplyAsync()
                        //this.taskExecutor.execute(task);
                        pendingClipState.add(task);
                        event.setClip(new ClipInfo(uuid));
                        clipList.add(task);
                        break;
                    }
                }

            }
            return pendingClipState;
        } catch (PlaylistParserException e) {
            return new NotFoundClipState();
        }
    }

    public List<ClipDownloaderCallable> getTasksById(UUID uuid) {
        return storage.get(uuid);
    }

}
