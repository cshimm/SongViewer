package test;

import org.junit.jupiter.api.Test;
import src.Song;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    Song song1 = new Song(
            "aaaa",
            "artist1",
            "1999",
            "1",
            "1",
            "10",
            "1000"
    );

    Song song2 = new Song(
            "zzzz",
            "artist2",
            "1980",
            "2",
            "2",
            "20",
            "2000"
    );

    @Test
    void test_songIsNotNull() {
        assertNotNull(song1);
    }

    @Test
    void test_trackNameIsNotNull() {
        assertNotNull(song1.trackName());
    }
    @Test
    void test_artistNameIsNotNull() {
        assertNotNull(song1.artistName());
    }
    @Test
    void test_releasedYearNotNull() {
        assertNotNull(song1.releasedYear());
    }
    @Test
    void test_releasedMonthIsNotNull() {
        assertNotNull(song1.releasedMonth());
    }
    @Test
    void test_releasedDayIsNotNull() {
        assertNotNull(song1.releasedDay());
    }
    @Test
    void test_totalStreamsIsNotNull() {
        assertNotNull(song1.totalNumberOfStreamsOnSpotify());
    }
    @Test
    void test_totalPlaylistsIsNotNull() {
        assertNotNull(song1.totalPlaylists());
    }

    @Test
    void test_Comparison() {
        assertEquals(0, song1.compareTo(song1));
        assertTrue(song1.compareTo(song2) < 0, "song1 should come before song2");
        assertTrue(song2.compareTo(song1) > 0, "song2 should come after song1");
    }
}