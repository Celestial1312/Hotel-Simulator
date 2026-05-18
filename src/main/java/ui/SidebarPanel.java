package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.SimulatorController;
import loader.GridLoader;
import model.Area;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import model.Guest;

public class SidebarPanel extends JPanel {
    private final SimulatorController controller;
    private boolean layoutChosen = false;

    public SidebarPanel(SimulatorController controller) {
        this.controller = controller;

        setPreferredSize(new Dimension(600, 1080));
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());

        SimulationClock simulationClock = new SimulationClock(controller.getHte());
        RealTimeClock realTimeClock = new RealTimeClock();

        JButton uploadButton = new JButton("Upload JSON");
        JButton chooseLayoutButton = new JButton("Choose Layout");
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton settings = new JButton("Settings");

        Dimension size = new Dimension(180, 40);

        uploadButton.setPreferredSize(size);
        chooseLayoutButton.setPreferredSize(size);
        startButton.setPreferredSize(size);
        pauseButton.setPreferredSize(size);
        settings.setPreferredSize(size);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        add(realTimeClock, gbc);

        gbc.gridy = 1;
        add(simulationClock, gbc);

        gbc.gridy = 2;
        add(uploadButton, gbc);

        gbc.gridy = 3;
        add(chooseLayoutButton, gbc);

        gbc.gridy = 4;
        add(startButton, gbc);

        gbc.gridy = 5;
        add(pauseButton, gbc);

        gbc.gridy = 6;
        add(settings, gbc);

        uploadButton.addActionListener(e -> uploadJson());
        chooseLayoutButton.addActionListener(e -> chooseLayout());

        startButton.addActionListener(e -> {
            if (!layoutChosen) {
                JOptionPane.showMessageDialog(this, "Please choose a layout first!", "No Layout Chosen",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.startScenario(2);
            simulationClock.start();

            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            pauseButton.setText("Pause");
        });

        pauseButton.addActionListener(e -> {
            if (!layoutChosen) {
                JOptionPane.showMessageDialog(this, "Please choose a layout first!", "No Layout Chosen",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.togglePauseScenario();
            if (controller.isPaused()) {
                simulationClock.stop();
                pauseButton.setText("Resume");
            } else {
                simulationClock.start();
                pauseButton.setText("Pause");
            }
        });

        settings.addActionListener(e -> openSettings());
    }

    private void uploadJson() {
        JFileChooser chooser = new JFileChooser();
        GridLoader loader = new GridLoader();

        boolean SLLAvailable;

        chooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                File folder = new File("layouts");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                File destination = new File(folder, selectedFile.getName());

                Files.copy(
                        selectedFile.toPath(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                try {
                    List<Area> areas = loader.loadAreasFromFile(destination);
                    SLLAvailable = loader.CheckForSLL(areas);

                    if (!SLLAvailable) {
                        destination.delete();
                        JOptionPane.showMessageDialog(this, "File does not contain all areas!", "Upload Failed",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "File upload failed!", "Upload Failed",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(this, "File uploaded successfully!", "Upload Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "File upload failed!",
                        "Upload Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chooseLayout() {
        File layoutsFolder = new File("layouts");

        File[] jsonFiles = layoutsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        String[] fileNames = new String[jsonFiles.length];
        for (int i = 0; i < jsonFiles.length; i++) {
            fileNames[i] = jsonFiles[i].getName();
        }

        String selectedFileName = (String) JOptionPane.showInputDialog(
                this,
                "Choose a layout",
                "Layouts",
                JOptionPane.QUESTION_MESSAGE,
                null,
                fileNames,
                fileNames[0]);

        if (selectedFileName != null) {
            File selectedFile = new File(layoutsFolder, selectedFileName);

            try {
                controller.loadLayout(selectedFile);
                layoutChosen = true;
                JOptionPane.showMessageDialog(
                        this,
                        "Loaded layout: " + selectedFileName);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openSettings() {

        JFrame settingsFrame = new JFrame("Instellingen");

        settingsFrame.setSize(300, 200);

        settingsFrame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Simulation speed:");

        JSlider speedSlider = new JSlider(1, 10, 5);

        speedSlider.setMajorTickSpacing(1);

        speedSlider.setPaintTicks(true);

        speedSlider.setPaintLabels(true);

        JLabel guestLabel = new JLabel("Choose Guest:");

        JComboBox<Guest> guestBox = new JComboBox<>();

        for (Guest guest : controller.getGuests().values()) {
            guestBox.addItem(guest);
        }
        JLabel eventLabel = new JLabel("Choose Event:");

        String[] events = {
                "Cinema",
                "Fitness",
                "Food",
                "Evacuate"
        };

        JComboBox<String> eventBox = new JComboBox<>(events);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {

            int value = speedSlider.getValue();

            controller.setHte(value * 100);

            JOptionPane.showMessageDialog(
                    settingsFrame,
                    "Saved: " + value);
            Guest selectedGuest = (Guest) guestBox.getSelectedItem();

            String selectedEvent = (String) eventBox.getSelectedItem();

            System.out.println(selectedGuest);

            System.out.println(selectedEvent);

            System.out.println(
                    selectedGuest + " goes to " + selectedEvent);

            settingsFrame.dispose();
        });

        settingsFrame.add(label);

        settingsFrame.add(speedSlider);

        settingsFrame.add(guestLabel);
        settingsFrame.add(guestBox);

        settingsFrame.add(eventLabel);
        settingsFrame.add(eventBox);

        settingsFrame.add(saveButton);

        settingsFrame.setLocationRelativeTo(this);

        settingsFrame.setVisible(true);
    }
}