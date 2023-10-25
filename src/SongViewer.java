package src;

import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SongViewer extends JFrame {
    static final int WIDTH = 400;
    static final int HEIGHT = 250;
    static SongManager sm;
    static int yearIndex;
    static int songIndex;

    public SongViewer(String title) {
        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static class DataContainer extends JPanel {
        JButton[] buttons;
        JLabel detailsLabel;
        JComboBox<String> yearDropdown;

        JButton DrawButton(String label) {
            JButton button = new JButton(label);
            button.setBounds(0, 0, 100, 30);
            return button;
        }

        public DataContainer() {
            GridBagLayout gridLayout = new GridBagLayout();
            setLayout(gridLayout);
            GridBagConstraints gbc = new GridBagConstraints();
            buttons = new JButton[3];
            buttons[0] = DrawButton("Load Data");
            buttons[1] = DrawButton("Prev");
            buttons[2] = DrawButton("Next");
            for (int i = 0; i < buttons.length; i++) {
                gbc.gridx = i;
                gbc.gridy = 0;
                if (i == 0)
                    gbc.weightx = 0.25;
                else
                    gbc.weightx = 0.5;

                gbc.fill = GridBagConstraints.HORIZONTAL;
                add(buttons[i], gbc);
            }
            detailsLabel = new JLabel("");
            detailsLabel.setPreferredSize(new Dimension(200, 30));
            yearDropdown = new JComboBox<>();
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(yearDropdown, gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            add(detailsLabel, gbc);
        }

        public void setData() {
            for (var year : sm.getYears()) {
                yearDropdown.addItem(year);
            }
            yearIndex = 0;
            setDetailsLabel(songIndex, sm.getSongCount(yearIndex));
        }

        public void setDetailsLabel(int currSongIndex, int totalSongs) {
            DecimalFormat df = new DecimalFormat("#.##");
            int currSongNumber = currSongIndex + 1;
            String formatted = df.format((currSongNumber / (double) totalSongs) * 100);
            detailsLabel.setText("    " + formatted + "% | " + currSongNumber + " of " +
                    sm.getSongCount(yearIndex) + " total songs");
        }

        public void handlePrevBtnClicked() {
            if (songIndex <= 0)
                songIndex = sm.getSongCount(yearIndex);
            songIndex--;
            setDetailsLabel(songIndex, sm.getSongCount(yearIndex));
        }

        public void handleNextBtnClicked() {
            if (songIndex >= sm.getSongCount(yearIndex) - 1)
                songIndex = -1;
            songIndex++;
            setDetailsLabel(songIndex, sm.getSongCount(yearIndex));
        }
    }

    public static class SongDetailsContainer extends JPanel {
        SongDetail trackName;
        SongDetail artist;
        SongDetail releaseDate;
        SongDetail totalStreams;

        public SongDetailsContainer() {
            GridLayout gridLayout = new GridLayout(4, 2);
            setLayout(gridLayout);

            add(trackName = new SongDetail("Track Name: "));
            add(artist = new SongDetail("Artist(s): "));
            add(releaseDate = new SongDetail("Release Date: "));
            add(totalStreams = new SongDetail("Total Streams: "));
        }

        public void setData() {
            Song currentSong = sm.getSong(yearIndex, songIndex);
            trackName.setTextField(currentSong.trackName());
            artist.setTextField(currentSong.artistName());
            String formattedReleaseDate = currentSong.releasedMonth() + "/" +
                    currentSong.releasedDay() + "/" + currentSong.releasedYear();
            releaseDate.setTextField(formattedReleaseDate);

            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            try {
                String formattedStreams = nf.format(
                        Double.parseDouble(currentSong.totalNumberOfStreamsOnSpotify()));
                totalStreams.setTextField(formattedStreams);
            } catch (NumberFormatException e) {
                totalStreams.setTextField("Error: Bad data");
            }
        }

        public static class SongDetail extends JPanel {
            JTextField textField;

            public SongDetail(String label) {
                setLayout(new GridBagLayout());
                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                add(new JLabel(label));
                textFieldConstraints.weightx = 1.0;
                textFieldConstraints.anchor = GridBagConstraints.EAST;
                textField = new JTextField();
                textField.setPreferredSize(new Dimension(250, 30));
                add(textField, textFieldConstraints);
            }

            void setTextField(String text) {
                textField.setText(text);
            }
        }
    }

    public static void main(String[] args) {
        // Construct the UI
        SongViewer songViewer = new SongViewer("Song Viewer");
        JPanel container = new JPanel();
        DataContainer dataContainer = new DataContainer();
        SongDetailsContainer songDetailsContainer = new SongDetailsContainer();

        // Listener for when new year is selected
        dataContainer.yearDropdown.addActionListener(e -> {
            yearIndex = dataContainer.yearDropdown.getSelectedIndex();
            songIndex = 0;
            boolean hasMultipleSongsInYear = sm.getSongCount(yearIndex) > 1;
            dataContainer.buttons[1].setEnabled(hasMultipleSongsInYear);
            dataContainer.buttons[2].setEnabled(hasMultipleSongsInYear);

            dataContainer.setDetailsLabel(songIndex, sm.getSongCount(yearIndex));
            songDetailsContainer.setData();
        });
        //Load button
        dataContainer.buttons[0].addActionListener(e -> {
            try {
                sm = new SongManager();
                dataContainer.setData();
                songDetailsContainer.setData();
                dataContainer.buttons[0].setEnabled(false);
            } catch (IOException | CsvValidationException err) {
                throw new RuntimeException(err);
            }
        });
        // Prev btn
        dataContainer.buttons[1].addActionListener(e -> {
            dataContainer.handlePrevBtnClicked();
            songDetailsContainer.setData();
        });
        // next btn
        dataContainer.buttons[2].addActionListener(e -> {
            dataContainer.handleNextBtnClicked();
            songDetailsContainer.setData();
        });

        // Finally, add the UI to the Frame
        container.add(dataContainer);
        container.add(songDetailsContainer);
        songViewer.add(container);

        songViewer.setVisible(true);
    }
}