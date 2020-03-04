package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import com.github.gplnature.pubgapi.api.PubgClient;
import com.github.gplnature.pubgapi.exception.PubgClientException;
import com.github.gplnature.pubgapi.model.Map;
import com.github.gplnature.pubgapi.model.Platform;
import com.github.gplnature.pubgapi.model.generic.Entity;
import com.github.gplnature.pubgapi.model.match.MatchResponse;
import com.github.gplnature.pubgapi.model.participant.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyzerPubgApi implements PubgApi {

    private final PubgClient client;

    public AnalyzerPubgApi(PubgClient client) {
        this.client = client;
    }

    @Override
    public List<Match> getMatchesForPlayer(Player player) {
        try {
            Platform platform = Platform.valueOf(player.getPlatform());
            List<com.github.gplnature.pubgapi.model.player.Player> playerList = client.getPlayersByNames(platform, player.getName());
            // contains just the match IDs
            List<Entity> stubMatches = playerList.get(0).getRelationships().getMatches();

            List<Match> matches = new ArrayList<>();
            for (Entity e : stubMatches) {
                // getMatch is not rate-limited
                MatchResponse response = client.getMatch(platform, e.getId());
                com.github.gplnature.pubgapi.model.match.Match m = response.getData();

                Stream<Entity> participantStubs = response.getIncluded().stream().filter(p -> p.getType().equals("participant"));
                Stream<Participant> participantStream = participantStubs.map((Entity ent) -> (Participant)ent);
                List<Participant> participants = participantStream.collect(Collectors.toList());

                int playerCount = participants.size();
                int playerRank = findPlayer(player, participants).getAttributes().getStats().getWinPlace();

                Map map = m.getAttributes().getMapName();

                matches.add(new Match(
                        m.getId(),
                        m.getAttributes().getGameMode().toString(),
                        m.getAttributes().getCreatedAt(),
                        // TODO file bug or fork library
                        map == null ? "Summerland_Main" : map.toString(),
                        playerRank,
                        playerCount
                ));
            }
            return matches;
        } catch (PubgClientException e) {
            throw new RuntimeException(e);
        }
    }

    private Participant findPlayer(Player player, List<Participant> participants) {
        Predicate<Participant> isPlayer = p -> p.getAttributes().getStats().getName().equals(player.getName());

        return participants.stream()
                .filter(isPlayer)
                .collect(Collectors.toList())
                .get(0);
    }

    @Override
    public MatchDetails getMatchDetails(Match m) {
        return null;
    }
}
