package test;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;
import src.SongManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class SongManagerTest {

    public static void main(String[] args) throws CsvValidationException, IOException {
        var files = new String[][]{
                {"data/count-by-release-year_no_2023_2022.csv", "data/spotify-2023_no_2023_2022.csv"},
                {"data/count-by-release-year_no_2023.csv", "data/spotify-2023_no_2023.csv"},
                {"data/count-by-release-year.csv", "data/spotify-2023.csv"},
        };
        int numIterations = 10;
        for (int i = 0; i < numIterations; i++) {
            for (var set : files) {
                long start = System.nanoTime();
                var sm = new SongManager(set[0], set[1]);
                if (i == numIterations - 1)
                    System.out.println(set[1] + ": " + (System.nanoTime() - start));
            }
        }
    }
}
