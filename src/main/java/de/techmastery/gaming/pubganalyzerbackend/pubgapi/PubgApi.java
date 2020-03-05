package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.util.List;

public interface PubgApi {

    List<Match> getMatchesForPlayer(Player p);

    MatchDetails getMatchDetailsForPlayer(Asset a, Player p);

    MatchDetails getMatchDetailsForPlayer(String matchId, Player p);

}
