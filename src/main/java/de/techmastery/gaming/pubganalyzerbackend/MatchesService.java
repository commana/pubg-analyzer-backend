package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.clip.Clip;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipIdentifier;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipProcessor;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipUrl;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Mixer;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Recording;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Streamer;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Player;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.PubgApi;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatchesService {

    private final PubgApi api;

    private final Mixer mixer;

    private final ClipProcessor clipProcessor;

    public MatchesService(PubgApi api, Mixer mixer, ClipProcessor clipProcessor) {
        this.api = api;
        this.mixer = mixer;
        this.clipProcessor = clipProcessor;
    }

    public List<Match> findRecentMatchesForPlayer(String platform, String name) {
        Player p = new Player(platform, name);
        return this.api.getMatchesForPlayer(p);
    }

    public MatchDetails getMatchDetailsForPlayer(String platform, String name, String matchId) {
        Player p = new Player(platform, name);
        MatchDetails details = this.api.getMatchDetailsForPlayer(matchId, p);

        if (this.mixer.hasStreamer(p.getName())) {
            Streamer s = this.mixer.getStreamer(p.getName());
            if (s.hasRecording(details.getStartTime())) {
                Recording rec = s.getRecording(details.getStartTime());
                List<Clip> clips = this.clipProcessor.process(rec, new ClipIdentifier(p, matchId), details.getEvents());
                details.setClips(IntStream.range(0, clips.size()).mapToObj(i -> new ClipUrl(clips.get(i), i)).collect(Collectors.toList()));
            }
        }

        return details;
    }

    public List<Clip> getClips(String platform, String name, String matchId) {
        return this.clipProcessor.getClipStorage().get(new ClipIdentifier(new Player(platform, name), matchId));
    }

    public Clip getClip(String platform, String name, String matchId, int index) {
        return this.clipProcessor.getClipStorage().get(new ClipIdentifier(new Player(platform, name), matchId)).get(index);
    }
}
