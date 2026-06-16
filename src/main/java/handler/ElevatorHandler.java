package handler;

import java.util.Iterator;
import java.util.List;

import model.Area;
import model.Elevator;
import model.Person;
import model.Person.PersonGoal;
import model.Person.PersonState;
import model.SubTile;
import model.Tile;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class ElevatorHandler {
    private final Simulation simulation;
    private final Elevator elevator;

    public ElevatorHandler(Simulation simulation, Elevator elevator) {
        this.simulation = simulation;
        this.elevator = elevator;
    }

    private Tile findElevatorTileOnLevel(int level) {
        for (int x = 0; x < simulation.getGrid().getSizeX(); x++) {
            Tile tile = simulation.getGrid().getTile(x, level);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("lift")) {
                return tile;
            }
        }

        return null;
    }

    private SubTile findFreeSubTileNextToLift(Tile liftTile) {
        Tile exitTile = liftTile.getRight();

        if (exitTile == null) {
            exitTile = liftTile.getLeft();
        }

        if (exitTile == null) {
            return null;
        }

        for (SubTile[] row : exitTile.getSubTiles()) {
            for (SubTile subTile : row) {
                if (subTile.isWalkable()) {
                    return subTile;
                }
            }
        }

        return null;
    }

    public void boardWaitingPeopleOnCurrentLevel() {
        int currentLevel = elevator.getCurrentLevel();

        for (Iterator<Person> iterator = elevator.getWaitingPeople().iterator(); iterator.hasNext();) {
            Person person = iterator.next();

            int guestLevel = person.getCurrentSubTile().getParentTile().getY();

            if (guestLevel == currentLevel) {
                person.getCurrentSubTile().setPerson(null);

                if (person.getPersonState() == PersonState.WAITING) {
                    person.setPersonState(PersonState.IN_LIFT);
                }

                elevator.addPassenger(person);
                iterator.remove();
            }
        }
    }

    public void sendElevatorToNextTarget() {
        if (!elevator.getPassengers().isEmpty()) {
            Person person = elevator.getPassengers().get(0);
            elevator.callToLevel(person.getTargetSubTile().getParentTile().getY());
            return;
        }

        if (!elevator.getWaitingPeople().isEmpty()) {
            Person person = elevator.getWaitingPeople().get(0);

            int guestLevel = person.getCurrentSubTile()
                    .getParentTile()
                    .getY();

            elevator.callToLevel(guestLevel);
        }
    }

    private boolean exitElevator(Person person) {
        Tile liftTile = findElevatorTileOnLevel(elevator.getCurrentLevel());

        if (liftTile == null) {
            return false;
        }

        SubTile exitSubTile = findFreeSubTileNextToLift(liftTile);

        if (exitSubTile == null) {
            return false;
        }

        if (person.getPersonGoal() == PersonGoal.CHECKIN) {
            Area room = person.getTargetSubTile().getParentTile().getArea();

            if (room == null || room.getGuest() != person) {
                return false;
            }
        }

        List<SubTile> path = new AStarPathFinding().findPath(exitSubTile, person.getTargetSubTile());

        if (path.isEmpty()) {
            return false;
        }

        path.remove(0);

        person.setCurrentSubTile(exitSubTile);
        exitSubTile.setPerson(person);
        person.setPath(path);
        person.setPersonState(PersonState.WALKING_TO_DESTINATION);

        return true;
    }

    public void exitPassengersForCurrentLevel() {
        int currentLevel = elevator.getCurrentLevel();

        for (Iterator<Person> iterator = elevator.getPassengers().iterator(); iterator.hasNext();) {
            Person person = iterator.next();

            if (person.getTargetSubTile().getParentTile().getY() == currentLevel) {
                if (exitElevator(person)) {
                    iterator.remove();
                }
            }
        }
    }

    public void update() {
        if (!elevator.isMoving()) {
            return;
        }

        int currentLevel = elevator.getCurrentLevel();
        int targetLevel = elevator.getTargetLevel();

        if (currentLevel < targetLevel) {
            currentLevel++;
        } else if (currentLevel > targetLevel) {
            currentLevel--;
        }

        elevator.setCurrentLevel(currentLevel);

        if (currentLevel == targetLevel) {
            elevator.setMoving(false);
        }
    }
}
