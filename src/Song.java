package src;

import java.util.Objects;

/**
 * @author cameron shimmin
 * @param trackName name of the song
 * @param artistName the song's artist
 * @param releasedYear the year the song was released
 * @param releasedMonth the month the song was released
 * @param releasedDay the day the song was released
 * @param totalNumberOfStreamsOnSpotify total number of streams on spotify
 */
public record Song(
        String trackName,
        String artistName,
        String releasedYear,
        String releasedMonth,
        String releasedDay,
        String totalPlaylists,
        String totalNumberOfStreamsOnSpotify
) implements Comparable<Song> {
    public Song  {
        Objects.requireNonNull(trackName, "Track name must not be null");
        Objects.requireNonNull(artistName, "Artist name must not be null");
        Objects.requireNonNull(releasedYear, "Released year must not be null");
        Objects.requireNonNull(releasedMonth, "Released month must not be null");
        Objects.requireNonNull(releasedDay, "Released day must not be null");
        Objects.requireNonNull(totalPlaylists, "Total playlists must not be null");
        Objects.requireNonNull(totalNumberOfStreamsOnSpotify, "Total number of streams must not be null");
    }

    @Override
    public int compareTo(Song other) {
        return trackName.compareTo(other.trackName);
    }
}