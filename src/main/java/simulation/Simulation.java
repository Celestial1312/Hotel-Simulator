package simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;

import config.SimulatorSettings;
import controller.SimulatorController;
import handler.CheckInEventHandler;
import handler.CheckOutEventHandler;
import handler.CleaningEmergencyEventHandler;
import handler.ElevatorHandler;
import handler.GoToCinemaEventHandler;
import handler.GoToFitnessEventHandler;
import handler.MovementHandler;
import handler.NeedFoodEventHandler;
import handler.StairHandler;
import hotelevents.HotelEventManager;
import listener.SimulationEventListener;
import loader.GridLoader;
import model.Area;
import model.Cleaner;
import model.Elevator;
import model.Grid;
import model.Guest;
import model.Stair;
import model.SubTile;
import model.Tile;
import ui.SimulationFrame;

public class Simulation {

    private Grid grid;
    private SimulatorSettings settings;
    private SimulatorController controller;
    private GridLoader gridLoader;
    private SimulationFrame frame;
    private Elevator elevator;
    private Stair stair;
    private File currentLayoutFile;

    private ElevatorHandler elevatorHandler;
    private StairHandler stairHandler;
    private MovementHandler movementHandler;

    private boolean running;
    private boolean paused;

    private HotelEventManager manager;
    private SimulationEventListener listener;

    private HashMap<Integer, Guest> guests = new HashMap<>();
    private HashMap<Integer, Cleaner> cleaners = new HashMap<>();
    private List<Area> rooms = new ArrayList<>();

    private Timer simulationTimer;

    public Simulation() {
        this.settings = new SimulatorSettings();
        this.controller = new SimulatorController(this);
        this.gridLoader = new GridLoader();
        this.elevator = new Elevator(0);
        this.stair = new Stair();
        this.elevatorHandler = new ElevatorHandler(this, elevator);
        this.stairHandler = new StairHandler(this, stair);
        this.movementHandler = new MovementHandler(this, elevatorHandler, stairHandler);
        this.manager = new HotelEventManager();
        this.listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(this),
                new CheckOutEventHandler(this),
                new CleaningEmergencyEventHandler(this),
                new NeedFoodEventHandler(this),
                new GoToCinemaEventHandler(this),
                new GoToFitnessEventHandler(this)));

        this.simulationTimer = new Timer(0, null);
        this.running = false;
        this.paused = false;
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

    public boolean isPaused() {
        return paused;
    }

    public SimulationFrame getFrame() {
        return frame;
    }

    public HashMap<Integer, Guest> getGuests() {
        return guests;
    }

    public HashMap<Integer, Cleaner> getCleaners() {
        return cleaners;
    }

    public List<Area> getRooms() {
        return rooms;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public Stair getStair() {
        return stair;
    }

    public void startApplication() {
        manager.register(listener);
        frame = new SimulationFrame(this);
    }

    public void startScenario(int scenarioId) {
        if (running) {
            return;
        }

        manager.setHte(settings.getHte());
        manager.start(scenarioId);

        simulationTimer = new Timer(settings.getHte(), e -> {
            movementHandler.movePeopleOneStep();

            if (frame != null) {
                frame.refreshGrid();
            }
        });

        simulationTimer.start();

        running = true;
        paused = false;
    }

    public void togglePauseScenario() {
        manager.pauze();

        if (paused) {
            simulationTimer.start();
            running = true;
            paused = false;
        } else {
            simulationTimer.stop();
            running = false;
            paused = true;
        }
    }

    public void stopScenario() {
        if (!running && !paused) {
            return;
        }

        simulationTimer.stop();

        manager.stop();

        guests.clear();
        cleaners.clear();
        elevator.getPassengers().clear();
        elevator.getWaitingPeople().clear();
        stair.getPeople().clear();

        running = false;
        paused = false;

        if (currentLayoutFile != null) {
            loadGridFromJsonFile(currentLayoutFile);
        }
    }

    public void loadGridFromJsonFile(File file) {
        if (file == null) {
            return;
        }

        currentLayoutFile = file;

        try {
            List<Area> areas = gridLoader.loadAreasFromFile(file);

            this.grid = gridLoader.loadGridFromAreas(areas);
            this.rooms = gridLoader.getListOfRooms(areas);
            this.elevator.setCurrentLevel(grid.getSizeY() - 1);

            if (frame != null) {
                frame.refreshGrid();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tile findAreaType(String areaType) {
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile.getArea() == null) {
                    continue;
                }

                if (areaType.equalsIgnoreCase(tile.getArea().getAreaType())) {
                    return tile;
                }
            }
        }

        return null;
    }

    public Area findGuestRoom(int guestId) {
        for (Area room : rooms) {
            Guest guest = room.getGuest();

            if (guest == null) {
                continue;
            }

            if (guest.getId() == guestId) {
                return room;
            }
        }

        return null;
    }

    public Tile findElevatorTileOnSameLevel(SubTile subTile) {
        int guestY = subTile.getParentTile().getY();

        for (int x = 0; x < grid.getSizeX(); x++) {
            Tile tile = grid.getTile(x, guestY);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("lift")) {
                return tile;
            }
        }
        return null;
    }

    public Tile findStairTileOnSameLevel(SubTile subTile) {
        int guestY = subTile.getParentTile().getY();

        for (int x = 0; x < grid.getSizeX(); x++) {
            Tile tile = grid.getTile(x, guestY);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("stairs")) {
                return tile;
            }
        }
        return null;
    }

    public void refreshHte() {
        int hte = settings.getHte();

        manager.setHte(hte);

        simulationTimer.setDelay(hte);
        simulationTimer.setInitialDelay(hte);
    }
}