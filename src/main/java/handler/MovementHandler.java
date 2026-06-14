package handler;

import java.util.Iterator;

import model.Area;
import model.Cleaner;
import model.Guest;
import model.Person.PersonGoal;
import model.Person.PersonState;
import simulation.Simulation;
import model.Person;
import model.SubTile;
import model.Area.AreaState;

public class MovementHandler {
    private final Simulation simulation;
    private final ElevatorHandler elevatorHandler;
    private final StairHandler stairHandler;

    public MovementHandler(Simulation simulation, ElevatorHandler elevatorHandler, StairHandler stairHandler) {
        this.simulation = simulation;
        this.elevatorHandler = elevatorHandler;
        this.stairHandler = stairHandler;
    }

    public void movePeopleOneStep() {
        elevatorHandler.update();
        stairHandler.update();

        for (Iterator<Guest> iterator = simulation.getGuests().values().iterator(); iterator.hasNext();) {
            Guest guest = iterator.next();

            if (guest.getPersonState() == PersonState.WALKING_TO_LIFT
                    || guest.getPersonState() == PersonState.WALKING_TO_STAIRS
                    || guest.getPersonState() == PersonState.WALKING_TO_DESTINATION) {
                movePersonOneStep(guest);
            }

            if (guest.getPersonState() == PersonState.WALKING_TO_LIFT && !guest.hasPath()) {
                guest.setPersonState(PersonState.WAITING);
                simulation.getElevator().addWaitingGuest(guest);
            }

            if (guest.getPersonState() == PersonState.WALKING_TO_STAIRS
                    && !guest.hasPath()) {
                stairHandler.enterStairs(guest);
            }

            if (guest.getPersonState() == PersonState.WALKING_TO_DESTINATION
                    && !guest.hasPath()) {

                if (guest.getPersonGoal() == PersonGoal.CHECKOUT) {
                    checkOutGuest(guest, iterator);
                    continue;
                }

                guest.setPersonState(PersonState.IDLE);
            }
        }

        if (!simulation.getElevator().isMoving()) {
            elevatorHandler.exitPassengersForCurrentLevel();
            elevatorHandler.boardWaitingPeopleOnCurrentLevel();
            elevatorHandler.sendElevatorToNextTarget();
        }

        for (Iterator<Cleaner> iterator = simulation.getCleaners().values().iterator(); iterator.hasNext();) {
            Cleaner cleaner = iterator.next();

            if (cleaner.getPersonState() == PersonState.WALKING_TO_LIFT
                    || cleaner.getPersonState() == PersonState.WALKING_TO_STAIRS
                    || cleaner.getPersonState() == PersonState.WALKING_TO_DESTINATION) {
                movePersonOneStep(cleaner);
            }

            if (cleaner.getPersonState() == PersonState.WALKING_TO_STAIRS && !cleaner.hasPath()) {
                stairHandler.enterStairs(cleaner);
            }

            if (cleaner.getPersonState() == PersonState.WALKING_TO_LIFT
                    && !cleaner.hasPath()) {
                cleaner.setPersonState(PersonState.WAITING);

                if (!simulation.getElevator().getWaitingPeople().contains(cleaner)) {
                    simulation.getElevator().addWaitingGuest(cleaner);
                }
            }

            if (!cleaner.hasPath()
                    && cleaner.getPersonGoal() == PersonGoal.CLEANING
                    && (cleaner.getPersonState() == PersonState.WALKING_TO_DESTINATION
                            || cleaner.getPersonState() == PersonState.CLEANING)) {
                cleanerCleaning(cleaner, iterator);
            }
        }
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

    private void checkOutGuest(Guest guest, Iterator<Guest> iterator) {
        Area guestRoom = simulation.findGuestRoom(guest.getId());

        if (guestRoom != null) {
            guestRoom.setGuest(null);
            guestRoom.setState(AreaState.AVAILABLE);
        }

        SubTile currentSubTile = guest.getCurrentSubTile();

        if (currentSubTile.getParentTile().getArea().getAreaType().equalsIgnoreCase("lobby")) {

            guest.getCurrentSubTile().setPerson(null);
            iterator.remove();
        }
    }

    private void cleanerCleaning(Cleaner cleaner, Iterator<Cleaner> iterator) {
        if (cleaner.getPersonState() == PersonState.WALKING_TO_DESTINATION) {
            cleaner.setPersonState(PersonState.CLEANING);
            cleaner.startCleaning(5);
            return;
        }

        if (cleaner.getPersonState() == PersonState.CLEANING) {
            cleaner.decreaseCleaningTicks();

            if (!cleaner.isCleaning()) {
                cleaner.getCurrentSubTile().setPerson(null);
                iterator.remove();
            }
        }
    }
}