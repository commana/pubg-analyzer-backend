package de.techmastery.gaming.pubganalyzerbackend.clip;

public class ClipUrl {
    private final Clip clip;
    private final int index;

    public ClipUrl(Clip clip, int index) {
        this.clip = clip;
        this.index = index;
    }

    public String getUrl() {
        return String.format(
                "/clips/%s/%s/%s/%d",
                clip.getIdentifier().getPlayer().getPlatform(),
                clip.getIdentifier().getPlayer().getName(),
                clip.getIdentifier().getMatchId(),
                index);
    }

    public String getState() {
        if (clip.isPending()) {
            return "PENDING";
        } else if (clip.isNotFound()) {
            return "NOT_FOUND";
        } else {
            return "AVAILABLE";
        }
    }
}
