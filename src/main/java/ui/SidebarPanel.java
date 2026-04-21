package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption; 
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.SimulatorController;
import loader.GridLoader;
import model.Area;

public class SidebarPanel extends JPanel {
    private final SimulatorController controller;

    public SidebarPanel(SimulatorController controller) {
        this.controller = controller;

       setPreferredSize(new Dimension(600, 1080));
       setBackground(Color.GRAY);
       setLayout(new GridBagLayout());

       JButton uploadButton = new JButton("Upload JSON");
       JButton chooseLayoutButton = new JButton("Choose Layout");
       JButton startSimulationButton = new JButton("Start Simulation");

        Dimension size = new Dimension(180, 40);

        uploadButton.setPreferredSize(size);
        chooseLayoutButton.setPreferredSize(size);
        startSimulationButton.setPreferredSize(size);

       add(uploadButton);
       add(chooseLayoutButton);
       add(startSimulationButton);

       uploadButton.addActionListener(e -> uploadJson());
       chooseLayoutButton.addActionListener(e -> chooseLayout());
       startSimulationButton.addActionListener(e -> {controller.startScenario(0);});
    }

    private void uploadJson() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                File folder = new File("layouts");
                if(!folder.exists()){
                    folder.mkdir();
                }

                File destination = new File(folder, selectedFile.getName());

                Files.copy(selectedFile.toPath(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
                GridLoader loader = new GridLoader();
                boolean SLLAvailable;
                try {
                    List<Area> areas = loader.ReadableJsonFile(destination);
                    SLLAvailable = loader.CheckForSLL(areas);

                    if(!SLLAvailable){
                        destination.delete();
                        JOptionPane.showMessageDialog(this, "File does not contain all areas!", "Upload Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "File upload failed!", "Upload Failed", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(this, "File uploaded successfully!", "Upload Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "File upload failed!", "Upload Failed", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        }
    }

    private void chooseLayout() {
        File layoutsFolder = new File("layouts");

        File[] jsonFiles = layoutsFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json")
        );

        String[] fileNames = new String[jsonFiles.length];
        for(int i = 0; i < jsonFiles.length; i++){
            fileNames[i] = jsonFiles[i].getName();
        }

        String selectedFileName = (String) JOptionPane.showInputDialog(this, "Choose a layout", "Layouts",
                JOptionPane.QUESTION_MESSAGE, null, fileNames, fileNames[0]
        );

        if (selectedFileName != null) {
            File selectedFile = new File(layoutsFolder, selectedFileName);

            try {
                controller.loadLayout(selectedFile);
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
}
