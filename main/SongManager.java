package main;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class SongManager implements SongManagerInterface {
    private final Object[][] songArray;

    public SongManager() throws IOException, CsvValidationException {
        String pathToCountByReleaseYear = "C:\\Users\\camer\\Documents\\NSC AD\\FALL23\\SongViewer\\data\\count-by-release-year.csv";
        File countByReleaseYear = new File(pathToCountByReleaseYear);
        CSVReader csvReader = new CSVReader(new FileReader(countByReleaseYear));

        int yearCount = Integer.parseInt(csvReader.readNext()[0]);
        songArray = new Song[yearCount][];
        csvReader.skip(1);

        for (int i = 0; i < yearCount; i++) {
            var line = csvReader.readNext();
            if (line.length < 2) continue;
            songArray[i] = new Song[Integer.parseInt(line[1])];
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
            songArray[yearIndex][songIndex] = song;
            songIndex++;
        }
    }

    @Override
    public int getYearCount() {
        return songArray.length;
    }

    @Override
    public int getSongCount(int yearIndex) {
        return songArray[yearIndex].length;
    }

    @Override
    public int getSongCount() {
        int total = 0;
        for (var year : songArray) {
            total += year.length;
        }
        return total;
    }

    @Override
    public String getYearName(int yearIndex) {
        Song song = (Song) songArray[yearIndex][0];
        return song.releasedYear();
    }

    @Override
    public int getSongCount(String year) {
        for (Object[] y : songArray) {
            Song song = (Song) y[0];
            if (song.releasedYear().equals(year)) {
                return y.length;
            }
        }
        return -1;
    }

    @Override
    public Song getSong(int yearIndex, int songIndex) {
        return (Song) songArray[yearIndex][songIndex];
    }

    @Override
    public Song[] getSongs(int yearIndex) {
        Song[] songs = new Song[songArray[yearIndex].length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = (Song) songArray[yearIndex][i];
        }
        return songs;
    }

    @Override
    public int findSongYear(String trackName) {
        for (int i = 0; i < songArray.length; i++) {
            for (int j = 0; j < songArray[i].length; j++) {
                Song song = (Song) songArray[i][j];
                if (song.trackName().equals(trackName)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
