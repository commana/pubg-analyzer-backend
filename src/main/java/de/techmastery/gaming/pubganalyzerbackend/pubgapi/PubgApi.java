package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.util.List;

public interface PubgApi {

    List<Match> getMatchesForPlayer(Player p);

    MatchDetails getMatchDetails(Match m);

}
