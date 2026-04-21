package simulation;

import config.SimulatorSettings;
import controller.SimulatorController;
import hotelevents.HotelEventManager;
import listener.SimulationEventListener;
import loader.GridLoader;
import model.Grid;
import model.Guest;
import model.SubTile;
import ui.SimulationFrame;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

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
        this.running = false;
        this.manager = new HotelEventManager();
        this.listener = new SimulationEventListener(this);
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
        manager.setHte(1);
        manager.start(scenarioId);
        running = true;
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

    public void pause() {

    }

    public void step() {
    }
}




/* package simulation;

import config.SimulatorSettings;
import loader.GridLoader;
import model.Grid;

import java.io.File;
import java.io.IOException;

public class Simulation {

    private Grid grid;
    private final SimulatorSettings settings;
    private final GridLoader gridLoader;

    public Simulation() {
        this.settings = new SimulatorSettings();
        this.gridLoader = new GridLoader();
    }

    // called ONCE when simulation starts
    public void init() {
        int size = settings.getBoardSize();
        grid = new Grid(size);
        grid.initialize();
    }

    // called every tick
    public void step() {
        // TODO: your simulation logic here
    }

    public void loadGridFromJsonFile(File file) {
        try {
            this.grid = gridLoader.loadGridFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Grid getGrid() {
        return grid;
    }
} */