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

public class GoToFitnessEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public GoToFitnessEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("GOTO_FITNESS");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        Guest guest = simulation.getGuests().get(event.getGuestId());

        if (guest == null) {
            return;
        }

        SubTile currenSubTile = guest.getCurrentSubTile();

        Tile fitnessTile = simulation.findAreaType("fitness");

        if(fitnessTile == null) {
            return;
        }

        Tile liftTile = simulation.findElevatorTileOnSameLevel(currenSubTile);
        Tile stairTile = simulation.findStairTileOnSameLevel(currenSubTile);

        if (stairTile == null && liftTile == null) {
            return;
        }
        
        AStarPathFinding aStarPathFinding = new AStarPathFinding();

        List<SubTile> pathToLift = aStarPathFinding.findPathToTile(currenSubTile, liftTile);
        List<SubTile> pathToStairs = aStarPathFinding.findPathToTile(currenSubTile, stairTile);

        if (pathToLift.isEmpty() && pathToStairs.isEmpty()) {
            return;
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
            if(!pathToLift.isEmpty()) {
                pathToLift.remove(pathToLift.size() - 1);
            }
            guest.setPath(pathToLift);
            guest.setPersonState(PersonState.WALKING_TO_LIFT);
        }

        guest.setTargetTile(fitnessTile);
        guest.setPersonGoal(PersonGoal.FITNESS);
    }
}
