package handler;

import java.util.List;

import hotelevents.HotelEvent;
import model.Guest;
import model.SubTile;
import model.Tile;
import model.Person.PersonGoal;
import model.Person.PersonState;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class EvacuateEmergencyEventHandler implements SimulationEventHandler {

    private final Simulation simulation;
    private boolean evacuating;

    public EvacuateEmergencyEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("EVACUATE");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        evacuating = true;
        for (Guest guest : simulation.getGuests().values()) {
            if (guest == null) {
                continue;
            }

            SubTile lobbyTile = simulation.getGrid().findSubTileByAreaType("lobby");

            if (lobbyTile == null) {
                continue;
            }

            SubTile currentSubtile = guest.getCurrentSubTile();

            Tile liftTile = simulation.getGrid().findElevatorTileOnSameLevel(currentSubtile);
            Tile stairTile = simulation.getGrid().findStairTileOnSameLevel(currentSubtile);

            if (liftTile == null && stairTile == null) {
                continue;
            }

            AStarPathFinding aStarPathFinding = new AStarPathFinding();

            List<SubTile> pathToLift = aStarPathFinding.findPathToTile(currentSubtile, liftTile);
            List<SubTile> pathToStairs = aStarPathFinding.findPathToTile(currentSubtile, stairTile);

            if (pathToLift.isEmpty() && pathToStairs.isEmpty()) {
                continue;
            }

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

            guest.setCheckingOut(true);
            guest.setTargetSubTile(lobbyTile);
            guest.setPersonGoal(PersonGoal.EVACUATE);
        }
    }
}