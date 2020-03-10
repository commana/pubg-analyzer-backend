package de.techmastery.gaming.pubganalyzerbackend.pubgapi;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(platform, player.platform) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platform, name);
    }
}
