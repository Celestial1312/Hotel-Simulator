package simulation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import config.SimulatorSettings;
import controller.SimulatorController;
import hotelevents.HotelEventManager;
import listener.SimulationEventListener;
import loader.GridLoader;
import model.Grid;
import model.Guest;
import model.SubTile;
import ui.SimulationFrame;
import java.util.Random;
import java.util.ArrayList;

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

    private final Random random = new Random();

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
    public HashMap<Integer, Guest> getGuests() {
        return guests;
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
        moveGuestRandomly(guest);

        System.out.println("CHECK_IN: " + guestId);
    }
    public void handleCheckOut(int guestId) {
        Guest guest = guests.get(guestId);

        SubTile currentSubtile = guest.getCurrentSubTile();

        currentSubtile.setGuest(null);

        guests.remove(guestId, guest);
        frame.refreshGrid();
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

    public void moveGuestRandomly(Guest guest) {

        SubTile current = guest.getCurrentSubTile();

        ArrayList<SubTile> possibleMoves = new ArrayList<>();

        if(current.getUp() != null)
            possibleMoves.add(current.getUp());

        if(current.getDown() != null)
            possibleMoves.add(current.getDown());

        if(current.getLeft() != null)
            possibleMoves.add(current.getLeft());

        if(current.getRight() != null)
            possibleMoves.add(current.getRight());

        if(possibleMoves.isEmpty()) {
            return;
        }

        SubTile next =
                possibleMoves.get(
                        random.nextInt(possibleMoves.size())
                );

        if(next == null) {
            return;
        }

        if(next.getGuest() != null) {
            return;
        }

        current.setGuest(null);

        next.setGuest(guest);

        guest.setCurrentSubTile(next);

        frame.refreshGrid();
    }
}