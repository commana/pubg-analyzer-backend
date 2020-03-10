package de.techmastery.gaming.pubganalyzerbackend.clip;

import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Player;

import java.util.Objects;

public class ClipIdentifier {
    private final Player player;

    private final String matchId;

    public ClipIdentifier(Player player, String matchId) {
        this.player = player;
        this.matchId = matchId;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMatchId() {
        return matchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClipIdentifier that = (ClipIdentifier) o;
        return Objects.equals(player, that.player) &&
                Objects.equals(matchId, that.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, matchId);
    }
}
