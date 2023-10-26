package test;

import org.junit.jupiter.api.Test;
import src.Song;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    Song song1 = new Song(
            "aaaa",
            "artist1",
            "9999",
            "1",
            "1",
            "10",
            "1000"
    );

    Song song2 = new Song(
            "zzzz",
            "artist2",
            "8888",
            "2",
            "2",
            "20",
            "2000"
    );

    @Test
    void songIsNotNull() {
        assertNotNull(song1);
    }

    @Test
    void trackNameIsNotNull() {
        assertNotNull(song1.trackName());
    }

    @Test
    void testComparison() {
        assertEquals(0, song1.compareTo(song1));
        assertTrue(song1.compareTo(song2) < 0, "song1 should come before song2");
        assertTrue(song2.compareTo(song1) > 0, "song2 should come after song1");
    }
}