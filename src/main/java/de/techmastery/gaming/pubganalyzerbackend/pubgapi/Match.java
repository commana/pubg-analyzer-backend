package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.time.ZonedDateTime;

/**
 * Summarizes a Match in PUBG
 */
public class Match {

    private final String id;

    private final String gameMode;

    private final ZonedDateTime createdAt;

    private final String mapName;

    private final int playerRank;

    private final int playerCount;

    private final String assetLink;

    public Match(String id, String gameMode, ZonedDateTime createdAt, String mapName, int playerRank, int playerCount, String assetLink) {
        this.id = id;
        this.gameMode = gameMode;
        this.createdAt = createdAt;
        this.mapName = mapName;
        this.playerRank = playerRank;
        this.playerCount = playerCount;
        this.assetLink = assetLink;
    }

    public String getId() {
        return id;
    }

    public String getGameMode() {
        return gameMode;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMapName() {
        return mapName;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getAssetLink() {
        return assetLink;
    }
}
