package model;

import java.util.List;

public class Grid {
    private final int size;
    private final Tile[][] tiles;

    public Grid(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
    }

    public int getSize() {
        return size;
    }

    public Tile getTile(int y, int x) {
        if(y < 0 || y >= size || x < 0 || x >= size) {
            return null;
        }
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void initialize() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                tiles[y][x] = new Tile(x, y);
            }
        }

        connectTiles();
    }

    private void connectTiles() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Tile current = tiles[y][x];

                if(y > 0) {
                    current.setUp(tiles[y - 1][x]);
                }

                if(y < size - 1) {
                    current.setDown(tiles[y + 1][x]);
                }

                if(x > 0) {
                    current.setLeft(tiles[y][x - 1]);
                }

                if(x < size - 1) {
                    current.setRight(tiles[y][x + 1]);
                }
            }
        }
    }

    public void placeAreas(List<Area> areas) {
        for (Area area : areas) {
            int startX = area.getX() - 1;
            int startY = area.getY() - 1;

            for(int dy = 0; dy < area.getHeight(); dy++) {
                for(int dx = 0; dx < area.getWidth(); dx++) {

                    int currentX = startX + dx;
                    int currentY = startY + dy;

                    Tile tile = getTile(currentY, currentX);

                    if (tile == null) {
                        throw new IllegalArgumentException(
                                "Area out of bounds: " + area.getAreaType() +
                                        " at x=" + area.getX() +
                                        ", y=" + area.getY() +
                                        ", width=" + area.getWidth() +
                                        ", height=" + area.getHeight()
                        );
                    }

                    tile.setArea(area);
                }
            }
        }
    }
}
