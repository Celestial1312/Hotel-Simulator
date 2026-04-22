package simulation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import config.SimulatorSettings;
import controller.SimulatorController;
import hotelevents.HotelEventManager;
import listener.SimulationEventListener;
import loader.GridLoader;
import model.Grid;
import model.Guest;
import model.SubTile;
import ui.SimulationFrame;

public class Simulation {

    private Grid grid;
    private SimulatorSettings settings;
    private SimulatorController controller;
    private GridLoader gridLoader;
    private SimulationFrame frame;

    private boolean running;

    private HotelEventManager manager;
    private SimulationEventListener listener;

    private HashMap<Integer, Guest> guests = new HashMap<>();

    public Simulation() {
        this.settings = new SimulatorSettings();
        this.controller = new SimulatorController(this);
        this.gridLoader = new GridLoader();
        this.manager = new HotelEventManager();
        this.listener = new SimulationEventListener(this);
        this.running = false;
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

    public void startApplication () {
        manager.register(listener);
        frame = new SimulationFrame(this);
    }

    public void startScenario(int scenarioId) {
        manager.setHte(settings.getHte());
        manager.start(scenarioId);
        running = true;
    }

    public void pauseScenario() {
        manager.pauze();
        running = false;
    }

    public void handleCheckIn(int guestId) {
        SubTile spawnSubTile = grid.getLobbySpawnTile();

        Guest guest = new Guest(guestId, spawnSubTile);
        spawnSubTile.setGuest(guest);
        guests.put(guestId, guest);

        frame.refreshGrid();

        System.out.println("CHECK_IN: " + guestId);
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