package de.techmastery.gaming.pubganalyzerbackend.mixer;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MixerTest {

    @Test
    public void testHasStreamer() {
        Mixer m = new Mixer();

        assertTrue(m.hasStreamer("Brentarus"));
    }

    @Test
    public void testGetStreamer() {
        Mixer m = new Mixer();

        assertEquals(47094669, m.getStreamer("Brentarus").getId());
    }

    @Test
    public void testHasVod() {
        Mixer m = new Mixer();
        Streamer s = m.getStreamer("Brentarus");

        // TODO these will fail at some point
        assertTrue(s.hasRecording(ZonedDateTime.of(2020, 03, 05, 21, 00, 00, 00, ZoneId.of("UTC"))));
        assertFalse(s.hasRecording(ZonedDateTime.now()));
    }
}
