package de.techmastery.gaming.pubganalyzerbackend;

import com.github.gplnature.pubgapi.api.PubgClient;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.AnalyzerPubgApi;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.20.4.221:3000"})
@RestController
public class MatchesController {

    @GetMapping("/matches/{platform}/{player}")
    public List<Match> getMatchesForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient())).findRecentMatchesForPlayer(platform, player);
    }

    @GetMapping("/matches/{platform}/{player}/{matchId}")
    public MatchDetails getMatchDetailsForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player, @PathVariable("matchId") String matchId) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient())).getMatchDetailsForPlayer(platform, player, matchId);
    }
}
