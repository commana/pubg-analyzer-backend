package de.techmastery.gaming.pubganalyzerbackend;

import com.github.gplnature.pubgapi.api.PubgClient;
import de.techmastery.gaming.pubganalyzerbackend.clip.Clip;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipProcessor;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipStorage;
import de.techmastery.gaming.pubganalyzerbackend.clip.ClipUrl;
import de.techmastery.gaming.pubganalyzerbackend.mixer.Mixer;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.AnalyzerPubgApi;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.Match;
import de.techmastery.gaming.pubganalyzerbackend.pubgapi.MatchDetails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CrossOrigin(origins = {"http://localhost:3000", "http://10.20.4.221:3000"})
@RestController
public class MatchesController {

    private final TaskExecutor taskExecutor;

    private final ClipStorage clipStorage;

    public MatchesController(TaskExecutor taskExecutor, ClipStorage clipStorage) {
        this.taskExecutor = taskExecutor;
        this.clipStorage = clipStorage;
    }

    @GetMapping("/matches/{platform}/{player}")
    public List<Match> getMatchesForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor, clipStorage)).findRecentMatchesForPlayer(platform, player);
    }

    @GetMapping("/matches/{platform}/{player}/{matchId}")
    public MatchDetails getMatchDetailsForPlayer(@PathVariable("platform") String platform, @PathVariable("player") String player, @PathVariable("matchId") String matchId) {
        return new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor, clipStorage)).getMatchDetailsForPlayer(platform, player, matchId);
    }

    @GetMapping("/clips/{platform}/{player}/{matchId}")
    public List<ClipUrl> getClips(@PathVariable("platform") String platform, @PathVariable("player") String player, @PathVariable("matchId") String matchId) {
        List<Clip> clips = new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor, clipStorage)).getClips(platform, player, matchId);
        return IntStream.range(0, clips.size()).mapToObj(i ->
            new ClipUrl(clips.get(i), i)
        ).collect(Collectors.toList());
    }

    @GetMapping("/clips/{platform}/{player}/{matchId}/{index}")
    public ResponseEntity<Resource> getClip(@PathVariable("platform") String platform, @PathVariable("player") String player, @PathVariable("matchId") String matchId, @PathVariable("index") int index, HttpServletRequest request) throws MalformedURLException {
        Clip c = new MatchesService(new AnalyzerPubgApi(new PubgClient()), new Mixer(), new ClipProcessor(taskExecutor, clipStorage)).getClip(platform, player, matchId, index);
        Resource resource = new UrlResource(c.getLocalFile().toURI());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
