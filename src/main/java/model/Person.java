package model;

public abstract class Person {
    private int id;
    private SubTile currentSubTile;
    private Tile targetTile;

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

    public void setCurrentSubTile(SubTile currentSubTile) {
        this.currentSubTile = currentSubTile;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    @Override
    public String toString() {
        return "Guest " + id;
    }
}
