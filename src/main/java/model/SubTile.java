package model;

public class SubTile {
    private final Tile parentTile;
    private int x;
    private int y;

    private Guest guest;

    private SubTile up;
    private SubTile down;
    private SubTile left;
    private SubTile right;

    public SubTile(Tile parentTile, int x, int y) {
        this.parentTile = parentTile;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Guest getGuest() {
        return guest;
    }

    public SubTile getUp() {
        return up;
    }

    public SubTile getDown() {
        return down;
    }

    public SubTile getLeft() {
        return left;
    }

    public SubTile getRight() {
        return right;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setUp(SubTile up) {
        this.up = up;
    }

    public void setDown(SubTile down) {
        this.down = down;
    }

    public void setLeft(SubTile left) {
        this.left = left;
    }

    public void setRight(SubTile right) {
        this.right = right;
    }
}
