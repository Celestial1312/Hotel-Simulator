package model;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private int currentLevel;
    private int targetLevel;
    private boolean moving;
    private List<Person> passengers = new ArrayList();
    private List<Person> waitingPeople = new ArrayList<>();

    public Elevator(int currentLevel) {
        this.currentLevel = currentLevel;
        this.targetLevel = currentLevel;
        this.moving = false;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public List<Person> getPassengers() {
        return passengers;
    }

    public List<Person> getWaitingPeople() {
        return waitingPeople;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void callToLevel(int level) {
        targetLevel = level;

        if (targetLevel != currentLevel) {
            moving = true;
        }
    }

    public void addPassenger(Person person) {
        passengers.add(person);
    }

    public void removePassenger(Person person) {
        passengers.remove(person);
    }

    public boolean hasPassengers() {
        return !passengers.isEmpty();
    }

    public void addWaitingGuest(Person person) {
        waitingPeople.add(person);
    }

    public void removeWaitingGuest(Person person) {
        waitingPeople.remove(person);
    }

    public boolean hasWaitingGuests() {
        return !waitingPeople.isEmpty();
    }
}
