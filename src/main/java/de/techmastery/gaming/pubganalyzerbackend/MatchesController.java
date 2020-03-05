package de.techmastery.gaming.pubganalyzerbackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchesController {

    @CrossOrigin(origins = {"http://localhost:3000", "http://10.20.4.221:3000"})
    @GetMapping("/matches/{platform}/{player}")
    public Matches getMatchesForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player) {
        return new Matches(String.format("Hello, %2$s @ %1$s", platform, player));
    }
}
