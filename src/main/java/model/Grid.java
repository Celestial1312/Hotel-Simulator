package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        connectSubTilesBetweenTiles();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Tile getTile(int x, int y) {
        if (y < 0 || y >= sizeY || x < 0 || x >= sizeX) {
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

                if (y > 0) {
                    current.setUp(tiles[y - 1][x]);
                }

                if (y < sizeY - 1) {
                    current.setDown(tiles[y + 1][x]);
                }

                if (x > 0) {
                    current.setLeft(tiles[y][x - 1]);
                }

                if (x < sizeX - 1) {
                    current.setRight(tiles[y][x + 1]);
                }
            }
        }
    }

    private void connectSubTilesBetweenTiles() {
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Tile currentTile = tiles[y][x];

                connectRightSubTiles(currentTile);
                connectDownSubTiles(currentTile);
            }

        }
    }

    private void connectRightSubTiles(Tile currentTile) {
        Tile rightTile = currentTile.getRight();

        if (rightTile == null) {
            return;
        }

        SubTile[][] currentSubTiles = currentTile.getSubTiles();
        SubTile[][] rightSubTiles = rightTile.getSubTiles();

        int lastX = currentSubTiles[0].length - 1;

        for (int subY = 0; subY < currentSubTiles.length; subY++) {
            currentSubTiles[subY][lastX].setRight(rightSubTiles[subY][0]);
            rightSubTiles[subY][0].setLeft(currentSubTiles[subY][lastX]);
        }
    }

    private void connectDownSubTiles(Tile currentTile) {
        Tile downTile = currentTile.getDown();

        if (downTile == null) {
            return;
        }

        SubTile[][] currentSubTiles = currentTile.getSubTiles();
        SubTile[][] downSubTiles = downTile.getSubTiles();

        int lastY = currentSubTiles[0].length - 1;

        for (int subX = 0; subX < currentSubTiles.length; subX++) {
            currentSubTiles[lastY][subX].setDown(downSubTiles[0][subX]);
            downSubTiles[0][subX].setUp(currentSubTiles[lastY][subX]);
        }
    }

    public void placeAreas(List<Area> areas) {
        for (Area area : areas) {
            int startX = area.getX();
            int startY = area.getY();

            for (int dy = 0; dy < area.getHeight(); dy++) {
                for (int dx = 0; dx < area.getWidth(); dx++) {

                    int currentX = startX + dx;
                    int currentY = startY + dy;

                    Tile tile = getTile(currentX, currentY);

                    tile.setArea(area);
                }
            }
        }
    }

    public SubTile getLobbySpawnArea() {
        Random random = new Random();
        List<SubTile> allLobbySubTiles = new ArrayList<>();

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Tile currentTile = tiles[y][x];

                if (currentTile.getArea() == null) {
                    continue;
                }

                if (!"lobby".equalsIgnoreCase(currentTile.getArea().getAreaType())) {
                    continue;
                }

                SubTile[][] subTiles = currentTile.getSubTiles();

                for (int subY = 0; subY < subTiles.length; subY++) {
                    for (int subX = 0; subX < subTiles[subY].length; subX++) {
                        SubTile subTile = subTiles[subY][subX];

                        if (subTile.getPerson() == null) {
                            allLobbySubTiles.add(subTile);
                        }
                    }
                }
            }
        }
        if (allLobbySubTiles.isEmpty()) {
            return null;
        }

        return allLobbySubTiles.get(random.nextInt(allLobbySubTiles.size()));
    }

    public Tile findElevatorTileOnSameLevel(SubTile subTile) {
        int guestY = subTile.getParentTile().getY();

        for (int x = 0; x < getSizeX(); x++) {
            Tile tile = getTile(x, guestY);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("lift")) {
                return tile;
            }
        }
        return null;
    }

    public Tile findStairTileOnSameLevel(SubTile subTile) {
        int guestY = subTile.getParentTile().getY();

        for (int x = 0; x < getSizeX(); x++) {
            Tile tile = getTile(x, guestY);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("stairs")) {
                return tile;
            }
        }
        return null;
    }

    public SubTile findSubTileByAreaType(String areaType) {
        Random random = new Random();
        List<SubTile> allAreaTypeSubTiles = new ArrayList<>();

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Tile currentTile = tiles[y][x];

                if (currentTile.getArea() == null) {
                    continue;
                }

                if (!areaType.equalsIgnoreCase(currentTile.getArea().getAreaType())) {
                    continue;
                }

                SubTile[][] subTiles = currentTile.getSubTiles();

                for (int subY = 0; subY < subTiles.length; subY++) {
                    for (int subX = 0; subX < subTiles[subY].length; subX++) {
                        SubTile subTile = subTiles[subY][subX];

                        if (subTile.getPerson() == null) {
                            allAreaTypeSubTiles.add(subTile);
                        }
                    }
                }
            }
        }
        if (allAreaTypeSubTiles.isEmpty()) {
            return null;
        }

        return allAreaTypeSubTiles.get(random.nextInt(allAreaTypeSubTiles.size()));
    }

    public SubTile findSubTileInArea(Area area) {
        Random random = new Random();
        List<SubTile> freeSubTiles = new ArrayList<>();

        for (int y = area.getY(); y < area.getY() + area.getHeight(); y++) {
            for (int x = area.getX(); x < area.getX() + area.getWidth(); x++) {
                Tile tile = getTile(x, y);

                if (tile == null) {
                    continue;
                }

                for (SubTile[] row : tile.getSubTiles()) {
                    for (SubTile subTile : row) {
                        if (subTile.getPerson() == null) {
                            freeSubTiles.add(subTile);
                        }
                    }
                }
            }
        }

        if (freeSubTiles.isEmpty()) {
            return null;
        }

        return freeSubTiles.get(random.nextInt(freeSubTiles.size()));
    }
}