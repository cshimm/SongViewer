package main;

import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.Arrays;

public class SongViewer {

    public static void main(String[] args) throws IOException, CsvValidationException {
        SongManager sm = new SongManager();
        System.out.println(sm.getSongCount());
        System.out.println(sm.getSongCount(48));
        System.out.println(sm.getYearCount());
        System.out.println(sm.getYearName(49));
        System.out.println(sm.getSongCount("2023"));
        System.out.println(sm.getSong(40, 0));
        System.out.println(Arrays.toString(sm.getSongs(47)));
        System.out.println(sm.findSongYear("White Christmas"));
    }
}
