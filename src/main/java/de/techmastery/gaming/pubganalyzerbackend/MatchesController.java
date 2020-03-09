package de.techmastery.gaming.pubganalyzerbackend;

import com.github.gplnature.pubgapi.api.PubgClient;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipProcessor;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Mixer;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.AnalyzerPubgApi;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.20.4.221:3000"})
@RestController
public class MatchesController {

    private final TaskExecutor taskExecutor;

    public MatchesController(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @GetMapping("/matches/{platform}/{player}")
    public List<Match> getMatchesForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor)).findRecentMatchesForPlayer(platform, player);
    }

    @GetMapping("/matches/{platform}/{player}/{matchId}")
    public MatchDetails getMatchDetailsForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player, @PathVariable("matchId") String matchId) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor)).getMatchDetailsForPlayer(platform, player, matchId);
    }

    @GetMapping("/clips/{uuid}")
    public void getClips(@PathVariable("uuid") UUID clipTaskIdentifier) {
        new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor)).getClips(clipTaskIdentifier);
    }
}
