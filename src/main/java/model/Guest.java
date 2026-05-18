package model;

public class Guest extends Person {
    private Area room;

    private boolean isCheckingOut;

    public Guest(int id, SubTile startTile) {
        super(id, startTile);
    }

    public void setCheckingOut(boolean isCheckingOut) {
        this.isCheckingOut = isCheckingOut;
    }

    public boolean isCheckingOut() {
        return isCheckingOut;
    }
}