package model;

public class Tile {

    private int x;
    private int y;
    private Area area;
    private final SubTile[][] subTiles;

    private Tile up;
    private Tile down;
    private Tile left;
    private Tile right;

    public Tile(int x, int y, int subTilesPerTileX, int subTilesPerTileY) {
        this.x = x;
        this.y = y;
        this.subTiles = new SubTile[subTilesPerTileX][subTilesPerTileY];

        initializeSubTiles();
        connectSubTiles();
    }

    public void initializeSubTiles() {
        for(int subY = 0; subY < subTiles.length; subY++) {
            for(int subX = 0; subX < subTiles[subY].length; subX++) {
                subTiles[subY][subX] = new SubTile(this, subX, subY);
            }
        }
    }

    public void connectSubTiles() {
        for (int y = 0; y < subTiles.length; y++) {
            for (int x = 0; x < subTiles[y].length; x++) {
                SubTile current = subTiles[y][x];

                if(y > 0) {
                    current.setUp(subTiles[y-1][x]);
                }

                if(y > subTiles.length - 1) {
                    current.setDown(subTiles[y+1][x]);
                }

                if(x > 0) {
                    current.setLeft(subTiles[y][x-1]);
                }

                if(x > subTiles[y].length - 1) {
                    current.setRight(subTiles[y][x+1]);
                }
            }
        }
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

    public Tile getUp() {
        return up;
    }

    public void setDown(Tile down) {
        this.down = down;
    }

    public Tile getDown() {
        return down;
    }

    public void setLeft(Tile left) {
        this.left = left;
    }

    public Tile getLeft() {
        return left;
    }

    public void setRight(Tile right) {
        this.right = right;
    }

    public Tile getRight() {
        return right;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public SubTile[][] getSubTiles() {
        return subTiles;
    }
}
