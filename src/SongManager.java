package src;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author cameron shimmin
 *
 */
public final class SongManager implements SongManagerInterface {
    private final Song[][] songsByYears;

    public SongManager() throws IOException, CsvValidationException {
        String pathToCountByReleaseYear = "C:\\Users\\camer\\Documents\\NSC AD\\FALL23\\SongViewer\\data\\count-by-release-year.csv";
        File countByReleaseYear = new File(pathToCountByReleaseYear);
        CSVReader csvReader = new CSVReader(new FileReader(countByReleaseYear));

        int yearCount = Integer.parseInt(csvReader.readNext()[0]);
        songsByYears = new Song[yearCount][];
        csvReader.skip(1);

        for (int i = 0; i < yearCount; i++) {
            var line = csvReader.readNext();
            if (line.length < 2) continue;
            songsByYears[i] = new Song[Integer.parseInt(line[1])];
        }

        String pathToSpotifyStreamData = "C:\\Users\\camer\\Documents\\NSC AD\\FALL23\\SongViewer\\data\\spotify-2023.csv";
        File spotifyStreamData = new File(pathToSpotifyStreamData);
        csvReader = new CSVReader(new FileReader(spotifyStreamData));
        csvReader.skip(1);

        String[] line;
        Song song;
        String currYear = "";
        int yearIndex = -1;
        int songIndex = 0;
        while ((line = csvReader.readNext()) != null) {
            // Create a Song object with the extracted data
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
            }
            songsByYears[yearIndex][songIndex] = song;
            songIndex++;
        }
        for (var songArr : songsByYears) {
            Arrays.sort(songArr);
        }
    }

    @Override
    public int getYearCount() {
        return songsByYears.length;
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
        Song song = songsByYears[yearIndex][0];
        return song.releasedYear();
    }

    @Override
    public int getSongCount(String year) {
        for (Song[] y : songsByYears) {
            Song song = y[0];
            if (song.releasedYear().equals(year)) {
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
        System.arraycopy(songsByYears[yearIndex], 0, songs, 0, songs.length);
        return songs;
    }

    @Override
    public int findSongYear(String trackName) {
        for (int i = 0; i < songsByYears.length; i++) {
            for (int j = 0; j < songsByYears[i].length; j++) {
                Song song = songsByYears[i][j];
                if (song.trackName().equals(trackName)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
