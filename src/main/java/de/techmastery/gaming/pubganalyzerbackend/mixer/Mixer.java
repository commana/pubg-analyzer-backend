package de.techmastery.gaming.pubganalyzerbackend.mixer;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mixer {

    private static final String MIXER_API = "https://mixer.com/api";

    public boolean hasStreamer(String name) {
        WebClient webClient = WebClient
                .builder()
                .baseUrl(MIXER_API)
                .build();

        return webClient
                .method(HttpMethod.HEAD).uri("/v1/channels/" + name)
                .exchange()
                .block()
                .statusCode()
                .is2xxSuccessful();
    }

    public Streamer getStreamer(String name) {
        WebClient webClient = WebClient
                .builder()
                .baseUrl(MIXER_API)
                .build();

        Streamer s = webClient.method(HttpMethod.GET).uri("/v1/channels/" + name)
                .retrieve()
                .bodyToMono(Streamer.class)
                .block();
        VideoOnDemand[] vods = webClient.method(HttpMethod.GET).uri("/v2/vods/channels/" + s.getId())
                .retrieve()
                .bodyToMono(VideoOnDemand[].class)
                .block();
        s.setVods(Arrays.asList(vods));
        return s;
    }
}
