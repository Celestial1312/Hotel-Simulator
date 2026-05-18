package simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;

import config.SimulatorSettings;
import controller.SimulatorController;
import hotelevents.HotelEventManager;
import listener.CheckInEventHandler;
import listener.CheckOutEventHandler;
import listener.CleaningEmergencyEventHandler;
import listener.GoToCinemaEventHandler;
import listener.GoToFitnessEventHandler;
import listener.NeedFoodEventHandler;
import listener.SimulationEventListener;
import loader.GridLoader;
import model.Area;
import model.Cleaner;
import model.Grid;
import model.Guest;
import model.Person;
import model.SubTile;
import model.Tile;
import pathfinding.AStarPathFinding;
import ui.SimulationFrame;

public class Simulation {

    private Grid grid;
    private SimulatorSettings settings;
    private SimulatorController controller;
    private GridLoader gridLoader;
    private SimulationFrame frame;

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
        this.manager = new HotelEventManager();
        this.listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(this),
                new CheckOutEventHandler(this),
                new CleaningEmergencyEventHandler(this),
                new NeedFoodEventHandler(this),
                new GoToCinemaEventHandler(this),
                new GoToFitnessEventHandler(this)
            ));
                
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

    public void startApplication() {
        manager.register(listener);
        frame = new SimulationFrame(this);
    }

    public void startScenario(int scenarioId) {
        manager.setHte(settings.getHte());
        manager.start(scenarioId);

        simulationTimer = new Timer(settings.getHte(), e -> {
            movePeopleOneStep();
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

    public void checkIn(int guestId, int preferredClassification) {
        SubTile spawnSubTile = grid.getLobbySpawnArea();

        Guest guest = new Guest(guestId, spawnSubTile);

        spawnSubTile.setPerson(guest);
        guests.put(guestId, guest);

        Area room = findAvailableRoom(preferredClassification);

        if (room == null) {
            return;
        }
        Tile roomTile = grid.getTile(room.getX(), room.getY());

        List<SubTile> path = new AStarPathFinding().findPathToTile(spawnSubTile, roomTile);

        if (path.isEmpty()) {
            return;
        }

        path.remove(0);

        room.setGuest(guest);
        guest.setTargetTile(roomTile);
        guest.setPath(path);
    }

    public void checkOut(int guestId) {
        Guest guest = guests.get(guestId);

        if (guest == null) {
            return;
        }

        SubTile currentSubtile = guest.getCurrentSubTile();

        Tile lobbyTile = findAreaType("lobby");

        List<SubTile> path = new AStarPathFinding().findPathToTile(currentSubtile, lobbyTile);

        if (path.isEmpty()) {
            return;
        }

        path.remove(0);

        guest.setCheckingOut(true);
        guest.setTargetTile(lobbyTile);
        guest.setPath(path);
    }

    public void cleaningEmergency(int guestId) {
        Area guestRoom = findGuestRoom(guestId);

        if (guestRoom == null) {
            return;
        }

        SubTile spawnSubTile = grid.getLobbySpawnArea();

        Tile guestRoomTile = grid.getTile(guestRoom.getX(), guestRoom.getY());

        List<SubTile> path = new AStarPathFinding().findPathToTile(spawnSubTile, guestRoomTile);

        if (path.isEmpty()) {
            return;
        }

        Cleaner cleaner = new Cleaner(guestId, spawnSubTile);

        cleaners.put(guestId, cleaner);
        spawnSubTile.setPerson(cleaner);

        path.remove(0);
        cleaner.setTargetTile(guestRoomTile);
        cleaner.setPath(path);
    }

    public void goToCinema(int guestId) {
        Guest guest = guests.get(guestId);

        if (guest == null) {
            return;
        }

        SubTile startSubTile = guest.getCurrentSubTile();

        Tile cinemaTile = findAreaType("cinema");

        List<SubTile> path = new AStarPathFinding().findPathToTile(startSubTile, cinemaTile);

        if (path.isEmpty()) {
            return;
        }

        path.remove(0);

        guest.setTargetTile(cinemaTile);
        guest.setPath(path);
    }

    public void needFood(int guestId) {
        Guest guest = guests.get(guestId);

        if(guest == null){ 
            return;
        }

        SubTile currentSubTile = guest.getCurrentSubTile();

        Tile restaurantTile = findAreaType("restaurant");

        List<SubTile> path = new AStarPathFinding().findPathToTile(currentSubTile, restaurantTile);

        if(path.isEmpty()) {
            return;
        }

        path.remove(0);
        guest.setTargetTile(restaurantTile);
        guest.setPath(path);
    }

    public void goToFitness(int guestId) {
        Guest guest = guests.get(guestId);

        if(guest == null) {
            return;
        }

        SubTile currenSubTile = guest.getCurrentSubTile();

        Tile fitnessTile = findAreaType("fitness");

        List<SubTile> path = new AStarPathFinding().findPathToTile(currenSubTile, fitnessTile);

        if(path.isEmpty()) {
            return;
        }

        path.remove(0);
        guest.setTargetTile(fitnessTile);
        guest.setPath(path);
    }

    // public void evacuate(){

    // }

    // public void godzilla(){

    // }

    // public void startCinema(){

    // }

    public void loadGridFromJsonFile(File file) {
        try {
            List<Area> areas = gridLoader.loadAreasFromFile(file);

            this.grid = gridLoader.loadGridFromAreas(areas);
            this.rooms = gridLoader.getListOfRooms(areas);

            if (frame != null) {
                frame.refreshGrid();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Tile findAreaType(String areaType) {
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

    private Area findGuestRoom(int guestId) {
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

    private void movePeopleOneStep() {
        for (Iterator<Guest> iterator = guests.values().iterator(); iterator.hasNext();) {
            Guest guest = iterator.next();

            movePersonOneStep(guest);

            if (guest.isCheckingOut() && isInLobby(guest)) {
                Area guestRoom = findGuestRoom(guest.getId());

                if (guestRoom != null) {
                    guestRoom.setGuest(null);
                    guestRoom.setIsClaimed(false);
                }

                guest.getCurrentSubTile().setPerson(null);
                iterator.remove();
            }
        }

        for (Iterator<Cleaner> iterator = cleaners.values().iterator(); iterator.hasNext();) {
            Cleaner cleaner = iterator.next();

            if (cleaner.isCleaning()) {
                cleaner.decreaseCleaningTicks();

                if (!cleaner.isCleaning()) {
                    cleaner.getCurrentSubTile().setPerson(null);
                    iterator.remove();
                }

                continue;
            }

            movePersonOneStep(cleaner);

            if (!cleaner.hasPath()) {
                cleaner.startCleaning(5);
            }
        }

        frame.refreshGrid();
    }

    private void movePersonOneStep(Person person) {
        SubTile current = person.getCurrentSubTile();
        SubTile next = person.getNextStep();

        if (next == null) {
            return;
        }

        current.setPerson(null);
        next.setPerson(person);
        person.setCurrentSubTile(next);
    }

    private boolean isInLobby(Person person) {
        Area area = person.getCurrentSubTile()
                .getParentTile()
                .getArea();

        return area != null && area.getAreaType().equalsIgnoreCase("lobby");
    }

    private Area findAvailableRoom(int preferredClassification) {
        for (Area room : rooms) {
            if (room.getIsClaimed()) {
                continue;
            }

            if (preferredClassification != room.getClassification()) {
                continue;
            }

            room.setIsClaimed(true);
            return room;
        }

        return null;
    }
}