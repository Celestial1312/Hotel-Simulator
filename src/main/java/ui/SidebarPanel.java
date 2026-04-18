package ui;

import simulation.Simulation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SidebarPanel extends JPanel {

    private final Simulation simulation;

    // 🎮 Simulation clock variables
    private int simSeconds = 0;
    private Timer simTimer;
    private JLabel simClockLabel;

    public SidebarPanel(Simulation simulation) {
        this.simulation = simulation;

        setPreferredSize(new Dimension(600, 1080));
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());

        // Buttons
        JButton uploadButton = new JButton("Upload JSON");
        JButton chooseLayoutButton = new JButton("Choose Layout");
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton instellingen = new JButton("Instellingen");

        Dimension size = new Dimension(180, 40);

        uploadButton.setPreferredSize(size);
        chooseLayoutButton.setPreferredSize(size);
        startButton.setPreferredSize(size);
        pauseButton.setPreferredSize(size);
        instellingen.setPreferredSize(size);

        // Layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 🕒 Real clock
        RealTimeKlok(gbc);

        // 🎮 Simulation clock
        SimulationKlok(gbc);

        // Buttons
        gbc.gridy = 2;
        add(uploadButton, gbc);

        gbc.gridy = 3;
        add(chooseLayoutButton, gbc);

        gbc.gridy = 4;
        add(startButton, gbc);

        gbc.gridy = 5;
        add(pauseButton, gbc);

        gbc.gridy = 6;
        add(instellingen, gbc);

        // Actions
        uploadButton.addActionListener(e -> uploadJson());
        chooseLayoutButton.addActionListener(e -> chooseLayout());

        startButton.addActionListener(e -> {
            simulation.start();
            simTimer.start(); // ▶ start simulation clock

            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });

        pauseButton.addActionListener(e -> {
            simulation.pause();
            simTimer.stop(); // ⏸ pause simulation clock

            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        instellingen.addActionListener(e -> openSettings());

        pauseButton.setEnabled(false);
    }

    // 🕒 REAL CLOCK
    private void RealTimeKlok(GridBagConstraints gbc) {
        JLabel clockLabel = new JLabel("00:00:00");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clockLabel.setForeground(Color.BLACK);

        Timer timer = new Timer(1000, e -> {
            java.time.LocalTime now = java.time.LocalTime.now();
            String time = String.format("%02d:%02d:%02d",
                    now.getHour(),
                    now.getMinute(),
                    now.getSecond());
            clockLabel.setText(time);
        });
        timer.start();

        gbc.gridy = 0;
        add(clockLabel, gbc);
    }

    // 🎮 SIMULATION CLOCK
    private void SimulationKlok(GridBagConstraints gbc) {
        simClockLabel = new JLabel("Sim: 00:00:00");
        simClockLabel.setFont(new Font("Arial", Font.BOLD, 20));
        simClockLabel.setForeground(Color.BLUE);

        simTimer = new Timer(1000, e -> {
            simSeconds++;

            int hours = simSeconds / 3600;
            int minutes = (simSeconds % 3600) / 60;
            int seconds = simSeconds % 60;

            String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            simClockLabel.setText("Sim: " + time);
        });

        gbc.gridy = 1;
        add(simClockLabel, gbc);
    }

    private void uploadJson() {
        JFileChooser chooser = new JFileChooser();
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
                        StandardCopyOption.REPLACE_EXISTING
                );

                JOptionPane.showMessageDialog(
                        this,
                        "File uploaded successfully!",
                        "Upload Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "File upload failed!",
                        "Upload Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void chooseLayout() {
        File layoutsFolder = new File("layouts");

        if (!layoutsFolder.exists() || !layoutsFolder.isDirectory()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Layout folder does not exist!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        File[] jsonFiles = layoutsFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json")
        );

        if (jsonFiles == null || jsonFiles.length == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No JSON layout files found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

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
                fileNames[0]
        );

        if (selectedFileName != null) {
            File selectedFile = new File(layoutsFolder, selectedFileName);

            try {
                simulation.loadGridFromJsonFile(selectedFile);

                JOptionPane.showMessageDialog(
                        this,
                        "Loaded layout: " + selectedFileName
                );

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void openSettings() {
        JFrame settingsFrame = new JFrame("Instellingen");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Simulation speed:");
        JTextField speedField = new JTextField(10);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            String value = speedField.getText();
            JOptionPane.showMessageDialog(settingsFrame, "Saved: " + value);
            settingsFrame.dispose();
        });

        settingsFrame.add(label);
        settingsFrame.add(speedField);
        settingsFrame.add(saveButton);

        settingsFrame.setLocationRelativeTo(this);
        settingsFrame.setVisible(true);
    }
}