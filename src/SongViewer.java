package src;

import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
/**
 * @author Cameron Shimmin
 * SongViewer represents a graphical user interface for viewing and interacting with song data.
 * It extends the JFrame class and provides a user interface to interact with a SongManager
 * instance. This class maintains static references to the SongManager, the current year index, and the current
 * song index for managing the displayed data.
 */
public class SongViewer extends JFrame {
    // The song manager the SongViewer with interface with
    private static SongManager sm;
    // Indexes for the year and song
    private static int yearIndex;
    private static int songIndex;

    /**
     * Constructs a new SongViewer with the specified title, width, and height.
     *
     * @param title   the title to set for the SongViewer window.
     * @param width   the width of the SongViewer window.
     * @param height  the height of the SongViewer window.
     */
    public SongViewer(String title, int width, int height) {
        setTitle(title);
        setSize(width, height);
        setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * DataContainer is a JPanel that serves as a container for various UI components.
     * It includes three buttons (Load Data, Prev, Next), a year dropdown (JComboBox),
     * and a details label (JLabel). These components are laid out using a GridBagLayout
     * to create a structured user interface for interacting with data.
     */
    public static class DataContainer extends JPanel {
        private final JButton[] buttons;
        private final JLabel detailsLabel;
        private final JComboBox<String> yearDropdown;

        JButton DrawButton(String label) {
            JButton button = new JButton(label);
            button.setBounds(0, 0, 100, 30);
            return button;
        }

        /**
         * Constructs the Data container with 3 Buttons and sets positions:
         * Load Data, Prev, Next.
         * Constructs the year Dropdown and browse details label and sets positions.
         */
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

        /**
         * Populates the year dropdown with available years from the SongManager and sets the initial yearIndex.
         * Additionally, it sets the details label with song information for the first year.
         */
        public void setData() {
            for (var year : sm.getYears()) {
                yearDropdown.addItem(year);
            }
            yearIndex = 0;
            setDetailsLabel(songIndex);
        }

        /**
         * Updates the details label to display information about the current song index and the total number of songs
         * as well as the proportion of current song to total songs for that year.
         * @param currSongIndex The current song index (zero-based).
         */
        public void setDetailsLabel(int currSongIndex) {
            DecimalFormat df = new DecimalFormat("#.##");
            int currSongNumber = currSongIndex + 1; // add one to handle 0-index
            String formatted = df.format((currSongNumber / (double) sm.getSongCount(yearIndex)) * 100);
            detailsLabel.setText("    " + formatted + "% | " + currSongNumber + " of " +
                    sm.getSongCount(yearIndex) + " total songs");
        }

        /**
         * Callback for when Prev button is clicked.
         */
        public void handlePrevBtnClicked() {
            // If the current song is the first song, then set the index to be the last song.
            if (songIndex <= 0)
                // I set it to the length because the next line will bring it to song count - 1
                songIndex = sm.getSongCount(yearIndex);
            songIndex--;
            setDetailsLabel(songIndex);
        }
        /**
         * Callback for when Next button is clicked.
         * Increment the song Index and set the details label
         */
        public void handleNextBtnClicked() {
            // If the current song is the last song, the set the index to be the first song.
            if (songIndex >= sm.getSongCount(yearIndex) - 1)
                // I set it to -1 because the next line will bring it up to 0
                songIndex = -1;
            songIndex++;
            setDetailsLabel(songIndex);
        }
    }
    /**
     * SongDetailsContainer is a JPanel that serves as a container for song details, including track name,
     * artist, release date, and total streams. It contains instances of the SongDetail class for each
     * of these details.
     */
    public static class SongDetailsContainer extends JPanel {
        private final SongDetail trackName;
        private final SongDetail artist;
        private final SongDetail releaseDate;
        private final SongDetail totalStreams;
        private final SongDetail totalPlaylists;
        /**
         * Constructs a SongDetailsContainer, a JPanel that organizes song details using a GridLayout.
         * It includes SongDetail components for track name, artist(s), release date, and total streams.
         * These details are displayed in pairs within the container.
         */
        public SongDetailsContainer() {
            GridLayout gridLayout = new GridLayout(5, 2);
            setLayout(gridLayout);

            add(trackName = new SongDetail("Track Name: "));
            add(artist = new SongDetail("Artist(s): "));
            add(releaseDate = new SongDetail("Release Date: "));
            add(totalStreams = new SongDetail("Total Streams: "));
            add(totalPlaylists = new SongDetail("Total Playlists: "));
        }

        /**
         * Set the text-fields with the corresponding Song data
         */
        public void setData() {
            Song currentSong = sm.getSong(yearIndex, songIndex);
            trackName.setTextField(currentSong.trackName());
            artist.setTextField(currentSong.artistName());
            String formattedReleaseDate = currentSong.releasedMonth() + "/" +
                    currentSong.releasedDay() + "/" + currentSong.releasedYear();
            releaseDate.setTextField(formattedReleaseDate);

            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            // Handle error if total streams can't be parsed int
            try {
                String formattedStreams = nf.format(
                        Double.parseDouble(currentSong.totalNumberOfStreamsOnSpotify()));
                totalStreams.setTextField(formattedStreams);
            } catch (NumberFormatException e) {
                totalStreams.setTextField("Error: Bad data");
            }
            totalPlaylists.setTextField(currentSong.totalPlaylists());
        }

        /**
         * Inner class: A panel that holds a label and text-field.
         * The text field can be set manually.
         */
        public static class SongDetail extends JPanel {
            private final JTextField textField;
            /**
             * Constructs a SongDetail component that consists of a label and a text field.
             *
             * @param label The label to display for the component.
             */
            public SongDetail(String label) {
                setLayout(new GridBagLayout());
                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                add(new JLabel(label));
                textFieldConstraints.weightx = 1.0;
                textFieldConstraints.anchor = GridBagConstraints.EAST;
                textField = new JTextField();
                textField.setPreferredSize(new Dimension(300, 30));
                add(textField, textFieldConstraints);
            }

            /**
             * Set the text field's text value
             * @param text The text value to set the text field to
             */
            void setTextField(String text) {
                textField.setText(text);
            }
        }
    }

    public static void main(String[] args) {
        // Construct the UI
        SongViewer songViewer = new SongViewer("Song Viewer", 425, 275);
        JPanel container = new JPanel();
        DataContainer dataContainer = new DataContainer();
        SongDetailsContainer songDetailsContainer = new SongDetailsContainer();

        /*
        Listener for year dropdown:
        Get the selected year index and check if that year has multiple songs (more than one). If true set both buttons
        to active, if year only has 1 song, set both inactive.
         */
        dataContainer.yearDropdown.addActionListener(e -> {
            yearIndex = dataContainer.yearDropdown.getSelectedIndex();
            boolean hasMultipleSongsInYear = sm.getSongCount(yearIndex) > 1;
            dataContainer.buttons[1].setEnabled(hasMultipleSongsInYear);
            dataContainer.buttons[2].setEnabled(hasMultipleSongsInYear);

            /*
            Reset song index to the first song in the array for that year.
            Update detail label with appropriate data.
            Update song details container with song data.
             */
            songIndex = 0;
            dataContainer.setDetailsLabel(songIndex);
            songDetailsContainer.setData();
        });
        //Load button
        dataContainer.buttons[0].addActionListener(e -> {
            /*
            When Load button is clicked, create a new SongManager object.
            Then set the data container and song details containers' data.
            Disable the load button to indicate data has been successfully loaded.
             */
            try {
                sm = new SongManager("data/count-by-release-year.csv", "data/spotify-2023.csv");
                dataContainer.setData();
                songDetailsContainer.setData();
                dataContainer.buttons[0].setEnabled(false);
            } catch (IOException | CsvValidationException err) {
                throw new RuntimeException(err);
            }
        });
        /*
        Prev button:
        subscribe to the Data Containers HandlePrevBtnClicked method: This method handles the logic for displaying
        information about the previous song. Then update the song details container.
         */
        dataContainer.buttons[1].addActionListener(e -> {
            dataContainer.handlePrevBtnClicked();
            songDetailsContainer.setData();
        });
        /*
        Next button:
        subscribe to the Data Containers HandleNextBtnClicked method: This method handles the logic for displaying
        information about the next song. Then update the song details container.
         */
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