package de.techmastery.gaming.pubganalyzerbackend.mixer;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

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
        Recording[] recordings = webClient.method(HttpMethod.GET).uri("/v1/channels/" + s.getId() + "/recordings")
                .retrieve()
                .bodyToMono(Recording[].class)
                .block();
        s.setRecordings(Arrays.asList(recordings));
        return s;
    }
}
