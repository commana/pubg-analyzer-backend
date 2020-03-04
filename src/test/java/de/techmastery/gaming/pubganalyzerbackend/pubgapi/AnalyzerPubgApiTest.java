package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import com.github.gplnature.pubgapi.api.PubgClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class AnalyzerPubgApiTest {

    @Test
    public void shouldReturnListOfMatchesForPlayer() {
        PubgClient client = new PubgClient();
        AnalyzerPubgApi api = new AnalyzerPubgApi(client);

        List<Match> matches = api.getMatchesForPlayer(new Player("xbox", "Brentarus"));

        // We assume that we find at least some games
        assertTrue(matches.size() > 0);
    }

    @Test
    public void shouldFillPlayerRankInformation() {
        PubgClient client = new PubgClient();
        AnalyzerPubgApi api = new AnalyzerPubgApi(client);

        List<Match> matches = api.getMatchesForPlayer(new Player("xbox", "Brentarus"));
        Match actual = matches.get(0);

        // TODO this will fail once I play more games...
        assertEquals(2, actual.getPlayerRank());
        assertEquals(63, actual.getPlayerCount());
    }

    @Test
    public void shouldGetAllKilsForPlayer() {
        PubgClient client = new PubgClient();
        AnalyzerPubgApi api = new AnalyzerPubgApi(client);

        Player player = new Player("xbox", "Brentarus");
        List<Match> matches = api.getMatchesForPlayer(new Player("xbox", "Brentarus"));
        Match m = matches.get(0);

        MatchDetails actual = api.getMatchDetailsForPlayer(m, player);

        // should contain 5 kills, 1 match start, 1 death event
        assertEquals(5+1+1, actual.getEvents().size());
    }
}
