package model;

public class SubTile {
    private final Tile parentTile;
    private int x;
    private int y;

    private Person person;

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

    public Person getPerson() {
        return person;
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

    public Tile getParentTile() {
        return parentTile;
    }

    public int getGlobalX() {
        return parentTile.getX() * parentTile.getSubTileColumns() + x;
    }

    public int getGlobalY() {
        return parentTile.getY() * parentTile.getSubTileRows() + y;
    }

    public boolean isWalkable() {
        return parentTile.getArea() != null && person == null;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPerson(Person person) {
        this.person = person;
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
