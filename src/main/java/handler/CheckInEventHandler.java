package handler;

import java.util.List;

import hotelevents.HotelEvent;
import model.Area;
import model.Guest;
import model.Person.PersonGoal;
import model.Person.PersonState;
import model.SubTile;
import model.Tile;
import model.Area.AreaState;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class CheckInEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public CheckInEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equals("CHECK_IN");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        if (simulation.getGuests().containsKey(event.getGuestId())) {
            return;
        }

        SubTile spawnSubTile = simulation.getGrid().getLobbySpawnArea();

        if (spawnSubTile == null) {
            return;
        }

        Area room = findAvailableRoom(event.getData());

        if (room == null) {
            return;
        }

        Tile roomTile = simulation.getGrid().getTile(room.getX(), room.getY());

        Tile liftTile = simulation.findElevatorTileOnSameLevel(spawnSubTile);
        Tile stairTile = simulation.findStairTileOnSameLevel(spawnSubTile);

        if (liftTile == null && stairTile == null) {
            room.setState(AreaState.AVAILABLE);
            return;
        }

        AStarPathFinding aStarPathFinding = new AStarPathFinding();

        List<SubTile> pathToLift = aStarPathFinding.findPathToTile(spawnSubTile, liftTile);
        List<SubTile> pathToStairs = aStarPathFinding.findPathToTile(spawnSubTile, stairTile);

        if (pathToLift.isEmpty() && pathToStairs.isEmpty()) {
            room.setState(AreaState.AVAILABLE);
            return;
        }

        Guest guest = new Guest(event.getGuestId(), spawnSubTile);

        if (pathToLift.isEmpty() || (!pathToStairs.isEmpty() && pathToLift.size() > pathToStairs.size())) {
            pathToStairs.remove(0);
            if (!pathToStairs.isEmpty()) {
                pathToStairs.remove(pathToStairs.size() - 1);
            }
            guest.setPath(pathToStairs);
            guest.setPersonState(PersonState.WALKING_TO_STAIRS);
        } else {
            pathToLift.remove(0);
            if (!pathToLift.isEmpty()) {
                pathToLift.remove(pathToLift.size() - 1);
            }
            guest.setPath(pathToLift);
            guest.setPersonState(PersonState.WALKING_TO_LIFT);
        }

        room.setGuest(guest);
        spawnSubTile.setPerson(guest);
        simulation.getGuests().put(event.getGuestId(), guest);

        guest.setTargetTile(roomTile);
        guest.setPersonGoal(PersonGoal.CHECKIN);
    }

    private Area findAvailableRoom(int preferredClassification) {
        for (Area room : simulation.getRooms()) {
            if (room.getState() != AreaState.AVAILABLE) {
                continue;
            }

            if (room.getGuest() != null) {
                continue;
            }

            if (preferredClassification != room.getClassification()) {
                continue;
            }

            room.setState(AreaState.OCCUPIED);
            return room;
        }

        return null;
    }
}