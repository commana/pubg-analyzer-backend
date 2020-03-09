package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.clip.ClipProcessor;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipState;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipDownloaderCallable;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Mixer;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Recording;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Streamer;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Player;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.PubgApi;

import java.util.List;
import java.util.UUID;

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
                ClipState cs = this.clipProcessor.process(rec, details.getEvents());
            }
        }

        return details;
    }

    public void getClips(UUID clipTaskIdentifier) {
        List<ClipDownloaderCallable> tasks = this.clipProcessor.getTasksById(clipTaskIdentifier);
    }
}
