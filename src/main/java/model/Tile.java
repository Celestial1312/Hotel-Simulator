package model;

public class Tile {
    private Area area;
    private int x;
    private int y;

    private Tile up;
    private Tile down;
    private Tile left;
    private Tile right;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setUp(Tile up) {
        this.up = up;
    }

    public Tile getUp(Tile up) {
        return up;
    }

    public void setDown(Tile down) {
        this.down = down;
    }

    public Tile getDown(Tile down) {
        return down;
    }

    public void setLeft(Tile left) {
        this.left = left;
    }

    public Tile getLeft(Tile left) {
        return left;
    }

    public void setRight(Tile right) {
        this.right = right;
    }

    public Tile getRight(Tile right) {
        return right;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

}
