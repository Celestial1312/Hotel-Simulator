package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    private int id;
    private SubTile currentSubTile;
    private Tile targetTile;
    private SubTile nextStep;
    private List<SubTile> path = new ArrayList<>();

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

    public boolean hasPath() {
        return !path.isEmpty();
    }
}
