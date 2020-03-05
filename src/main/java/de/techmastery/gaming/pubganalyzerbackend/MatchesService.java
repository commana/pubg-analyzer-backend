package de.techmastery.gaming.pubganalyzerbackend;

import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Player;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.PubgApi;

import java.util.List;

public class MatchesService {

    private final PubgApi api;

    public MatchesService(PubgApi api) {
        this.api = api;
    }

    public List<Match> findRecentMatchesForPlayer(String platform, String name) {
        Player p = new Player(platform, name);
        return this.api.getMatchesForPlayer(p);
    }

    public MatchDetails getMatchDetailsForPlayer(String platform, String name, String matchId) {
        Player p = new Player(platform, name);
        return this.api.getMatchDetailsForPlayer(matchId, p);
    }
}
