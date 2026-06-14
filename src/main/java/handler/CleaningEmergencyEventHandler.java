package handler;

import java.util.List;

import hotelevents.HotelEvent;
import model.Area;
import model.Cleaner;
import model.SubTile;
import model.Tile;
import model.Area.AreaState;
import model.Person.PersonGoal;
import model.Person.PersonState;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class CleaningEmergencyEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public CleaningEmergencyEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("cleaning_emergency");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        Area guestRoom = simulation.findGuestRoom(event.getGuestId());

        if (guestRoom == null) {
            return;
        }

        SubTile spawnSubTile = simulation.getGrid().getLobbySpawnArea();

        if (spawnSubTile == null) {
            return;
        }

        Tile guestRoomTile = simulation.getGrid().getTile(guestRoom.getX(), guestRoom.getY());

        Tile liftTile = simulation.findElevatorTileOnSameLevel(spawnSubTile);
        Tile stairTile = simulation.findStairTileOnSameLevel(spawnSubTile);

        if (liftTile == null && stairTile == null) {
            return;
        }

        AStarPathFinding aStarPathFinding = new AStarPathFinding();

        List<SubTile> pathToLift = aStarPathFinding.findPathToTile(spawnSubTile, liftTile);
        List<SubTile> pathToStairs = aStarPathFinding.findPathToTile(spawnSubTile, stairTile);

        if (pathToLift.isEmpty() && pathToStairs.isEmpty()) {
            return;
        }

        Cleaner cleaner = new Cleaner(event.getGuestId(), spawnSubTile);

        if (pathToLift.isEmpty() || (!pathToStairs.isEmpty() && pathToLift.size() > pathToStairs.size())) {
            pathToStairs.remove(0);
            if (!pathToStairs.isEmpty()) {
                pathToStairs.remove(pathToStairs.size() - 1);
            }
            cleaner.setPath(pathToStairs);
            cleaner.setPersonState(PersonState.WALKING_TO_STAIRS);
        } else {
            pathToLift.remove(0);
            if (!pathToLift.isEmpty()) {
                pathToLift.remove(pathToLift.size() - 1);
            }
            cleaner.setPath(pathToLift);
            cleaner.setPersonState(PersonState.WALKING_TO_LIFT);

        }

        guestRoom.setState(AreaState.BEING_CLEANED);
        spawnSubTile.setPerson(cleaner);
        simulation.getCleaners().put(event.getGuestId(), cleaner);
        cleaner.setTargetTile(guestRoomTile);
        cleaner.setPersonGoal(PersonGoal.CLEANING);
    }
}