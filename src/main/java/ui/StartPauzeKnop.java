/* package ui;

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

    public SidebarPanel(Simulation simulation) {
        this.simulation = simulation;


        setPreferredSize(new Dimension(600, 1080));
        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());

       /* JButton uploadButton = new JButton("Upload JSON");
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

        // Layout setup
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        add(uploadButton, gbc);

        gbc.gridy = 1;
        add(chooseLayoutButton, gbc);

        gbc.gridy = 2;
        add(startButton, gbc);

        gbc.gridy = 3;
        add(pauseButton, gbc);

        gbc.gridy = 4; // 👈 ADDED
        add(instellingen, gbc);

        uploadButton.addActionListener(e -> uploadJson());
        chooseLayoutButton.addActionListener(e -> chooseLayout());

        startButton.addActionListener(e -> {
            simulation.start();
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
        });

        pauseButton.addActionListener(e -> {
            simulation.pause();
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        });

        instellingen.addActionListener(e -> openSettings());

        // Initial state
        pauseButton.setEnabled(false);
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
*/