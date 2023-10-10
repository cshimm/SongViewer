package main;

import java.util.Objects;

public record Song(
        String trackName,
        String artistName,
        String releasedYear,
        String releasedMonth,
        String releasedDay,
        String totalNumberOfStreamsOnSpotify
) {
    public Song {
        Objects.requireNonNull(trackName, "Track name must not be null");
        Objects.requireNonNull(artistName, "Artist name must not be null");
        Objects.requireNonNull(releasedYear, "Released year must not be null");
        Objects.requireNonNull(releasedMonth, "Released month must not be null");
        Objects.requireNonNull(releasedDay, "Released day must not be null");
        Objects.requireNonNull(totalNumberOfStreamsOnSpotify, "Total number of streams must not be null");
    }

    boolean equals(Song song) {
        return song.equals(this);
    }

}
