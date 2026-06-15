package handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Person;
import model.Stair;
import model.SubTile;
import model.Tile;
import model.Person.PersonState;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class StairHandler {
    private final Simulation simulation;
    private final Stair stair;

    private Map<Person, Integer> tickCounters = new HashMap<>();

    public StairHandler(Simulation simulation, Stair stair) {
        this.simulation = simulation;
        this.stair = stair;
    }

    private Tile findStairTileOnLevel(int level) {
        for(int x = 0; x < simulation.getGrid().getSizeX(); x++) {
            Tile tile = simulation.getGrid().getTile(x, level);

            if(tile == null || tile.getArea() == null) {
                continue;
            }

            if(tile.getArea().getAreaType().equalsIgnoreCase("stairs")) {
                return tile;
            }
        }

        return null;
    }

    private SubTile findFreeSubTileNextToStair(Tile stairTile) {
        Tile exitTile = stairTile.getRight();

        if(exitTile == null) {
            exitTile = stairTile.getLeft();
        }

        if(exitTile == null) {
            return null;
        }

        for(SubTile[] row : exitTile.getSubTiles()) {
            for(SubTile subTile : row) {
                if(subTile.isWalkable()) {
                    return subTile;
                }
            }
        }

        return null;
    }

    public void enterStairs(Person person) {
        person.getCurrentSubTile().setPerson(null);
        person.setPersonState(PersonState.TAKING_STAIRS);

        stair.addPerson(person);
        tickCounters.put(person, 0);
    }

    private void exitStairs(Person person) {
        int targetLevel = person.getTargetTile().getY();

        Tile stairTile = findStairTileOnLevel(targetLevel);

        if(stairTile == null) {
            return;
        }

        SubTile exitSubTile = findFreeSubTileNextToStair(stairTile);

        if(exitSubTile == null) {
            return;
        }

        List<SubTile> path = new AStarPathFinding().findPathToTile(exitSubTile, person.getTargetTile());
        
        if(path.isEmpty()) {
            return;
        }

        path.remove(0);

        person.setCurrentSubTile(exitSubTile);
        exitSubTile.setPerson(person);
        person.setPath(path);
        person.setPersonState(PersonState.WALKING_TO_DESTINATION);

        stair.removePerson(person);
        tickCounters.remove(person);
    }

    public void update() {
        for(Person person: new ArrayList<>(stair.getPeople())) {
            int ticks = tickCounters.getOrDefault(person, 0) + 1;
            tickCounters.put(person, ticks);

            int startLevel = person.getCurrentSubTile().getParentTile().getY();

            int targetLevel = person.getTargetTile().getY();

            int requiredTicks = Math.abs(targetLevel - startLevel) * stair.getTicksPerLevel();

            if(ticks >= requiredTicks) {
                exitStairs(person);
            }
        }
    }
}
