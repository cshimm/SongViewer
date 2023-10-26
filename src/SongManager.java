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
    // The 2D array that will hold Arrays of Song arrays.
    private final Song[][] songsByYears;
    // An array of every year.
    private final String[] years;

    public SongManager() throws IOException, CsvValidationException {
        /*
        Create File with count-by-release-year.csv
        Then construct CSVReader with that File
        */
        String pathToCountByReleaseYear = "data/count-by-release-year.csv";
        File countByReleaseYear = new File(pathToCountByReleaseYear);
        CSVReader csvReader = new CSVReader(new FileReader(countByReleaseYear));

        /*
        Define years array size with the year count which is read from the first line
        of the CSV file.
        Define first level of songsByYears array size with the year count
         */
        int yearCount = Integer.parseInt(csvReader.readNext()[0]);
        years = new String[yearCount];
        songsByYears = new Song[yearCount][];
        csvReader.skip(1); //Skip line 2

        /*
        Loop through lines in CSV file. if the array returned from the line has a length of 2 then it is valid
        and the 2nd element in the array can be used to create a new Song array and initialize its size.
        The subsequent song array is assigned to the songsByYears array at a different index position.
        */
        for (int i = 0; i < yearCount; i++) {
            var line = csvReader.readNext();
            if (line.length == 2)
                songsByYears[i] = new Song[Integer.parseInt(line[1])];
        }

        /*
        Create File with spotify-2023.csv
        Reinitialize csvReader with new File
         */
        String pathToSpotifyStreamData = "data/spotify-2023.csv";
        File spotifyStreamData = new File(pathToSpotifyStreamData);
        csvReader = new CSVReader(new FileReader(spotifyStreamData));
        csvReader.skip(1); // Skip line 2

        /*
        Prepare to loop through spotify-2023.csv File
         */
        String[] line;
        Song song;
        String currYear = "";
        int yearIndex = -1;
        int songIndex = 0;
        /*
        Populate "line" String[] with the next line of the file if it is not null.
        Create a new Song record with elements from line.
         */
        while ((line = csvReader.readNext()) != null) {
            song = new Song(
                    line[0], // Track name
                    line[1], // Artist name
                    line[3], // release Year
                    line[4], // release Month
                    line[5], // release day
                    line[7], // total playlists
                    line[8] // total streams
            );
            /*
            Check if the current year is equal to the new Song's release year.
            If it is not then we have started reading songs from a new year.
            We need to set the current year to the new year, increase the year index by 1 and reset
            the song index to 0.
            Finally, set the years array at the current year index to the current year.
             */
            if (!currYear.equals(song.releasedYear())) {
                currYear = song.releasedYear();
                yearIndex++;
                songIndex = 0;
                years[yearIndex] = currYear;
            }
            /*
            Set the songs by years array at the appropriate year index and song index to the new song.
            Increment song index by 1.
             */
            songsByYears[yearIndex][songIndex] = song;
            songIndex++;
        }
        /*
        At this point every space in the songsByYears arras has been populated with all songs and can now be sorted.
        The Song record has a Comparable interface and sorts by Title.
         */
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
