package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

public class Player {

    private final String platform;

    private final String name;

    public Player(String platform, String name) {
        this.platform = platform.toUpperCase();
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getName() {
        return name;
    }
}
