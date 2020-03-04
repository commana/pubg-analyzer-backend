package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import com.github.gplnature.pubgapi.api.PubgClient;
import com.github.gplnature.pubgapi.exception.PubgClientException;
import com.github.gplnature.pubgapi.model.Platform;
import com.github.gplnature.pubgapi.model.asset.Asset;
import com.github.gplnature.pubgapi.model.generic.Entity;
import com.github.gplnature.pubgapi.model.match.MatchResponse;
import com.github.gplnature.pubgapi.model.participant.Participant;
import com.github.gplnature.pubgapi.model.telemetry.Telemetry;
import com.github.gplnature.pubgapi.model.telemetry.event.LogPlayerKill;
import com.github.gplnature.pubgapi.model.telemetry.event.TelemetryEvent;

import java.net.SocketTimeoutException;
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
                String assetId = m.getRelationships().getAssets().get(0).getId();
                Asset asset = (Asset) response.getIncluded().stream().filter(i -> i.getId().equals(assetId)).collect(Collectors.toList()).get(0);

                Stream<Entity> participantStubs = response.getIncluded().stream().filter(p -> p.getType().equals("participant"));
                Stream<Participant> participantStream = participantStubs.map((Entity ent) -> (Participant)ent);
                List<Participant> participants = participantStream.collect(Collectors.toList());

                int playerCount = participants.size();
                int playerRank = findPlayer(player, participants).getAttributes().getStats().getWinPlace();

                matches.add(new Match(
                        m.getId(),
                        m.getAttributes().getGameMode().toString(),
                        m.getAttributes().getCreatedAt(),
                        m.getAttributes().getMapName().toString(),
                        playerRank,
                        playerCount,
                        asset.getAttributes().getUrl()
                ));
            }
            return matches;
        } catch (PubgClientException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                SocketTimeoutException socketTimeoutException = (SocketTimeoutException) e.getCause();
                // TODO do something useful with this exception...
                throw new RuntimeException(socketTimeoutException);
            }
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
    public MatchDetails getMatchDetailsForPlayer(Match match, Player player) {
        try {
            Telemetry telemetry = this.client.getTelemetry(match.getAssetLink());
            Stream<TelemetryEvent> killsOnly = telemetry.getTelemetryEvents().stream().filter(e -> e.getType().toUpperCase().equals("LOGPLAYERKILL"));
            Stream<LogPlayerKill> playerKills = killsOnly
                    .map(e -> (LogPlayerKill)e)
                    .filter(e -> e.getKiller() != null && e.getKiller().getName().equals(player.getName()));
            TelemetryEvent gameStart = telemetry.getTelemetryEvents().stream().filter(e -> e.getType().toUpperCase().equals("LOGMATCHSTART")).collect(Collectors.toList()).get(0);

            List<MatchEvent> matchEvents = new ArrayList<>();
            StartMatchEvent gs = new StartMatchEvent(gameStart.getTimestamp(), gameStart.getCommon().getIsGame().toString());
            matchEvents.add(gs);

            for (LogPlayerKill k : playerKills.collect(Collectors.toList())) {
                matchEvents.add(new PlayerKillMatchEvent(k.getTimestamp(), k.getCommon().getIsGame().toString(), k.getVictim().getName()));
            }

            LogPlayerKill playerDies = telemetry.getTelemetryEvents().stream()
                    .filter(e -> e.getType().toUpperCase().equals("LOGPLAYERKILL"))
                    .map(e -> (LogPlayerKill)e)
                    .filter(e -> e.getVictim() != null && e.getVictim().getName().equals(player.getName()))
                    .collect(Collectors.toList())
                    .get(0);
            matchEvents.add(new PlayerDieMatchEvent(
                    playerDies.getTimestamp(),
                    playerDies.getCommon().getIsGame().toString(),
                    playerDies.getKiller().getName()
            ));

            return new MatchDetails(matchEvents);
        } catch (PubgClientException e) {
            throw new RuntimeException(e);
        }
    }
}
