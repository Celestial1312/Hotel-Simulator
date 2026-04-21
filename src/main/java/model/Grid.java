package model;

import java.util.List;

public class Grid {
    private int sizeX;
    private int sizeY;
    private final Tile[][] tiles;

    public Grid(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.tiles = new Tile[sizeY][sizeX];

        initializeTiles();
        connectTiles();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Tile getTile(int x, int y) {
        if(y < 0 || y >= sizeY || x < 0 || x >= sizeX) {
            return null;
        }
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void initializeTiles() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                tiles[y][x] = new Tile(x, y, 4, 4);
            }
        }

    }

    private void connectTiles() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Tile current = tiles[y][x];

                if(y > 0) {
                    current.setUp(tiles[y - 1][x]);
                }

                if(y < sizeY - 1) {
                    current.setDown(tiles[y + 1][x]);
                }

                if(x > 0) {
                    current.setLeft(tiles[y][x - 1]);
                }

                if(x < sizeX - 1) {
                    current.setRight(tiles[y][x + 1]);
                }
            }
        }
    }

    public void placeAreas(List<Area> areas) {
        for (Area area : areas) {
            int startX = area.getX();
            int startY = area.getY();

            for(int dy = 0; dy < area.getHeight(); dy++) {
                for(int dx = 0; dx < area.getWidth(); dx++) {

                    int currentX = startX + dx;
                    int currentY = startY + dy;

                    Tile tile = getTile(currentX, currentY);

                    tile.setArea(area);
                }
            }
        }
    }

    public SubTile getLobbySpawnTile() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Tile tile = tiles[y][x];

                if(tile.getArea() == null) {
                    continue;
                }

                if(!"lobby".equalsIgnoreCase(tile.getArea().getAreaType())) {
                    continue;
                }

                SubTile[][] subTiles = tile.getSubTiles();

                for (int subY = 0; subY < subTiles.length; subY++) {
                    for (int subX = 0; subX < subTiles[subY].length; subX++) {
                        SubTile subTile = subTiles[subY][subX];

                        if(subTile.getGuest() == null) {
                            return subTile;
                        }
                    }
                }
            }
        }
        return null;
    }
}
