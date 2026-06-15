package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {

    public enum PersonState {
        WALKING_TO_LIFT,
        WALKING_TO_STAIRS,
        WAITING,
        IN_LIFT,
        TAKING_STAIRS,
        WALKING_TO_DESTINATION,
        CLEANING,
        IDLE
    }

    public enum PersonGoal {
        CHECKIN,
        CHECKOUT,
        FOOD,
        FITNESS,
        CINEMA,
        CLEANING
    }

    private int id;
    private SubTile currentSubTile;
    private Tile targetTile;
    private SubTile nextStep;
    private List<SubTile> path = new ArrayList<>();
    private PersonState personState;
    private PersonGoal personGoal;

    public Person(int id, SubTile startTile) {
        this.id = id;
        this.currentSubTile = startTile;
    }

    public int getId() {
        return id;
    }

    public SubTile getCurrentSubTile() {
        return currentSubTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public SubTile getNextStep() {
        if (path.isEmpty()) {
            return null;
        }

        return path.remove(0);
    }

    public PersonState getPersonState() {
        return personState;
    }

    public PersonGoal getPersonGoal() {
        return personGoal;
    }

    public void setCurrentSubTile(SubTile currentSubTile) {
        this.currentSubTile = currentSubTile;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public void setPath(List<SubTile> path) {
        this.path = new ArrayList<>(path);
    }

    public void setPersonState(PersonState personState) {
        this.personState = personState;
    }

    public void setPersonGoal(PersonGoal personGoal) {
        this.personGoal = personGoal;
    }

    public boolean hasPath() {
        return !path.isEmpty();
    }
}