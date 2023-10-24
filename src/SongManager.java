package src;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author cameron shimmin
 * <br>
 * @description The SongManager reads data from 2 files count-by-release-year.csv and
 * spotify-2023.csv and organizes the songs by year in sub-arrays. These
 * arrays are placed in a parent array 'songsByYears' and are organized
 * in chronological order.
 * <br>
 * The SongManager implements the SongManagerInterface which allows for
 * querying of the songsByYears array.
 */
public final class SongManager implements SongManagerInterface {
    private final Song[][] songsByYears;
    private final String[] years;

    public SongManager() throws IOException, CsvValidationException {
        String pathToCountByReleaseYear = "data/count-by-release-year.csv";
        File countByReleaseYear = new File(pathToCountByReleaseYear);
        CSVReader csvReader = new CSVReader(new FileReader(countByReleaseYear));

        int yearCount = Integer.parseInt(csvReader.readNext()[0]);
        years = new String[yearCount];
        songsByYears = new Song[yearCount][];
        csvReader.skip(1);

        for (int i = 0; i < yearCount; i++) {
            var line = csvReader.readNext();
            if (line.length == 2)
                songsByYears[i] = new Song[Integer.parseInt(line[1])];
        }

        String pathToSpotifyStreamData = "data/spotify-2023.csv";
        File spotifyStreamData = new File(pathToSpotifyStreamData);
        csvReader = new CSVReader(new FileReader(spotifyStreamData));
        csvReader.skip(1);

        String[] line;
        Song song;
        String currYear = "";
        int yearIndex = -1;
        int songIndex = 0;
        while ((line = csvReader.readNext()) != null) {
            song = new Song(
                    line[0], // Track name
                    line[1], // Artist name
                    line[3], // release Year
                    line[4], // release Month
                    line[5], // release day
                    line[8] // total streams
            );
            if (!currYear.equals(song.releasedYear())) {
                currYear = song.releasedYear();
                yearIndex++;
                songIndex = 0;
                years[yearIndex] = currYear;
            }
            songsByYears[yearIndex][songIndex] = song;
            songIndex++;
        }
        for (var songArr : songsByYears) {
            Arrays.sort(songArr);
        }
    }

    public String[] getYears() {
        return years;
    }

    @Override
    public int getYearCount() {
        return years.length;
    }

    @Override
    public int getSongCount(int yearIndex) {
        return songsByYears[yearIndex].length;
    }

    @Override
    public int getSongCount() {
        int total = 0;
        for (var year : songsByYears) {
            total += year.length;
        }
        return total;
    }

    @Override
    public String getYearName(int yearIndex) {
        return years[yearIndex];
    }

    @Override
    public int getSongCount(String year) {
        for (Song[] y : songsByYears) {
            String releaseYear = y[0].releasedYear();
            if (releaseYear.equals(year)) {
                return y.length;
            }
        }
        return -1;
    }

    @Override
    public Song getSong(int yearIndex, int songIndex) {
        return songsByYears[yearIndex][songIndex];
    }

    @Override
    public Song[] getSongs(int yearIndex) {
        Song[] songs = new Song[songsByYears[yearIndex].length];
        System.arraycopy(
                songsByYears[yearIndex],
                0,
                songs,
                0,
                songs.length
        );
        return songs;
    }

    @Override
    public int findSongYear(String trackName) {
        for (int i = 0; i < songsByYears.length; i++) {
            for (int j = 0; j < songsByYears[i].length; j++) {
                String track = songsByYears[i][j].trackName();
                if (track.equals(trackName)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
