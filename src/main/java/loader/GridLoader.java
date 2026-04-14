package loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Grid;

import model.Area;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GridLoader {
    private final ObjectMapper mapper;

    public GridLoader() {
        this.mapper = new ObjectMapper();
    }

    public Grid loadGridFromFile(File file) throws IOException {
        List<Area> areas = mapper.readValue(
                file, new TypeReference<List<Area>>() {
                }
        );

        int size = calculateGridSize(areas);

        Grid grid = new Grid(size);
        grid.initialize();
        grid.placeAreas(areas);

        return grid;
    }

    private int calculateGridSize(List<Area> areas) {
        int maxX = 0;
        int maxY = 0;

        for (Area area : areas) {
            int endX = area.getX() + area.getWidth() - 1;
            int endY = area.getY() + area.getHeight() - 1;

            if (endX > maxX) maxX = endX;
            if (endY > maxY) maxY = endY;
        }

        return Math.max(maxX, maxY);
    }

    public List<Area> ReadableJsonFile(File file) throws IOException {
        List<Area> areas = mapper.readValue(
                file, new TypeReference<List<Area>>() {

                }
        );

        return areas;
    }

    public boolean CheckForLobby(Area area) {
            return "Lobby".equalsIgnoreCase(area.getAreaType());
    }

    public boolean CheckForStairs(Area area) {
        return "Stairs".equalsIgnoreCase(area.getAreaType());

    }

    public boolean CheckForLift(Area area) {
        return "Lift".equalsIgnoreCase(area.getAreaType());
    }

    public boolean CheckForSLL(List<Area> areas) {
        boolean LobbyAvailable = false;
        boolean StairsAvailable = false;
        boolean LiftAvailable = false;

        for (Area area : areas) {
            if(CheckForLobby(area)) {
                 LobbyAvailable = true;
            }
            if(CheckForStairs(area)) {
                StairsAvailable = true;
            }
            if(CheckForLift(area)) {
                LiftAvailable = true;
            }
        }

        return LobbyAvailable && StairsAvailable &&  LiftAvailable;
    }
}
