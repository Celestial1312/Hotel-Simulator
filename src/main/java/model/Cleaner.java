package model;

public class Cleaner extends Person {
    private int cleaningTicks;

    public Cleaner(int id, SubTile startTile) {
        super(id, startTile);
    }

    public void startCleaning(int ticks) {
        cleaningTicks = ticks;
    }

    public void decreaseCleaningTicks() {
        if (cleaningTicks > 0) {
            cleaningTicks--;
        }
    }

    public boolean isCleaning() {
        return cleaningTicks > 0;
    }
}
