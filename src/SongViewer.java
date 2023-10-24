package src;

import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SongViewer extends JFrame {
    static final int WIDTH = 350;
    static final int HEIGHT = 400;

    public SongViewer() {
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(2, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static class DataContainer extends JPanel {
        JButton[] buttons;
        JLabel detailsLabel;

        JButton DrawButton(String label) {
            JButton button = new JButton(label);
            button.setBounds(0, 0, 100, 30);
            return button;
        }

        public DataContainer(SongManager sm) {
            GridLayout gridLayout = new GridLayout(2, 3);
            setLayout(gridLayout);
            buttons = new JButton[3];
            buttons[0] = DrawButton("Load Data");
            buttons[1] = DrawButton("Prev");
            buttons[2] = DrawButton("Next");
            for (var button : buttons) {
                add(button);
            }
            JComboBox<String> yearDropdown = new JComboBox<>(sm.getYears());
            add(yearDropdown);
            detailsLabel = new JLabel("TESTSETESTESTSE");

            add(detailsLabel);
        }

        void setDetailsLabel(int currSongIndex, int totalSongs) {
            detailsLabel.setText(String.valueOf(currSongIndex / totalSongs));
        }
    }

    public static class SongDetailsContainer extends JPanel {
        public SongDetailsContainer() {
            GridLayout gridLayout = new GridLayout(4, 2);
            setLayout(gridLayout);
            SongDetail trackName = new SongDetail("Track Name: ");
            SongDetail artist = new SongDetail("Artist(s): ");
            SongDetail releaseDate = new SongDetail("Release Date: ");
            SongDetail totalStreams = new SongDetail("Total Streams: ");
            add(trackName);
            add(artist);
            add(releaseDate);
            add(totalStreams);
        }

        public static class SongDetail extends JPanel {
            JTextField textField;

            public SongDetail(String label) {
                GridBagConstraints textFieldConstraints = new GridBagConstraints();
                textFieldConstraints.weightx = 1.0;
                textFieldConstraints.anchor = GridBagConstraints.EAST;
                setLayout(new GridBagLayout());
                JLabel jLabel = new JLabel(label);
                textField = new JTextField();
                textField.setPreferredSize(new Dimension(200, 30));
                add(jLabel);
                add(textField, textFieldConstraints);
            }

            void setTextField(String text) {
                textField.setText(text);
            }
        }
    }

    public static void main(String[] args) throws IOException, CsvValidationException {
        SongManager sm = new SongManager();
        SongViewer songViewer = new SongViewer();
        DataContainer dataContainer = new DataContainer(sm);

        dataContainer.buttons[0].addActionListener(actionEvent -> {

        });
        dataContainer.buttons[1].addActionListener((actionEvent) -> System.out.println("Prev button"));
        dataContainer.buttons[2].addActionListener((actionEvent) -> System.out.println("next button"));


        SongDetailsContainer songDetailsContainer = new SongDetailsContainer();

        songViewer.add(dataContainer);
        songViewer.add(songDetailsContainer);

        songViewer.setVisible(true);
    }
}
