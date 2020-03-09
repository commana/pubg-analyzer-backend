package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.mixer.VideoOnDemand;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchEvent;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class ClipProcessor {
    public ClipStatus process(VideoOnDemand vod, List<MatchEvent> events) {
        try {
            WebClient webClient = WebClient.builder().baseUrl(vod.getPlaylistUrl()).build();
            String playListSrc = webClient.method(HttpMethod.GET).exchange().block().bodyToMono(String.class).block();
            MediaPlaylistParser parser = new MediaPlaylistParser();
            MediaPlaylist playlist = parser.readPlaylist(playListSrc.replaceAll("#EXT-X-ALLOW-CACHE:(YES|NO)", ""));
            for (MatchEvent event : events) {
                long seconds = vod.getRelativeTime(event.getTimestamp());
                double playlistSegmentTime = 0;
                for (MediaSegment segment : playlist.mediaSegments()) {
                    playlistSegmentTime += segment.duration();
                    if (playlistSegmentTime >= seconds) {
                        String theSegment = segment.uri();
                        System.out.println(theSegment);
                        break;
                    }
                }

            }
            return new PendingClipStatus();
        } catch (PlaylistParserException e) {
            return new NotFoundClipStatus();
        }
    }
}