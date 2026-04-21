package simulation;

import config.SimulatorSettings;
import controller.SimulatorController;
import loader.GridLoader;
import model.Grid;
import ui.SimulationFrame;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Simulation {

    private Grid grid;
    private SimulatorSettings settings;
    private SimulatorController controller;
    private GridLoader gridLoader;
    private boolean running;

    private final Random random;
    private SimulationFrame frame;

    public Simulation() {
        this.settings = new SimulatorSettings();
        this.controller = new SimulatorController();
        this.gridLoader = new GridLoader();
        this.running = false;
        this.random = new Random();
    }

    public Grid getGrid() {
        return grid;
    }

    public SimulatorSettings getSettings() {
        return settings;
    }

    public SimulatorController getController() {
        return controller;
    }

    public boolean isRunning() {
        return running;
    }

    public SimulationFrame getFrame() {
        return frame;
    }

    public void start () {
        int size = settings.getBoardSize();

        grid = new Grid(size);
        grid.initialize();

        frame = new SimulationFrame(this);
        running = true;
    }

    public void loadGridFromJsonFile(File file) {
        try {
            this.grid = gridLoader.loadGridFromFile(file);

            if(frame != null) {
                frame.refreshGrid();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
