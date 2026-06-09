package loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Grid;
import model.Area;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Deze class laadt een hotel layout vanuit een JSON bestand
public class GridLoader {

    // ObjectMapper van Jackson library
    // Wordt gebruikt om JSON bestanden om te zetten naar Java objecten
    private final ObjectMapper mapper;

    // Constructor
    public GridLoader() {

        // Nieuwe ObjectMapper aanmaken
        this.mapper = new ObjectMapper();
    }

    // Laadt alle areas uit een JSON bestand
    public List<Area> loadAreasFromFile(File file) throws IOException {

        // JSON bestand lezen en omzetten naar List<Area>
        return mapper.readValue(
                file,
                new TypeReference<List<Area>>() {}
        );
    }

    // Zet een lijst met areas om naar een Grid object
    public Grid loadGridFromAreas(List<Area> areas) {

        // Grootte van grid berekenen
        Grid grid = calculateGridSize(areas);

        // Areas plaatsen in het grid
        grid.placeAreas(areas);

        // Volledige grid teruggeven
        return grid;
    }

    // Laadt direct een grid vanuit een bestand
    public Grid loadGridFromFile(File file) throws IOException {

        // Eerst areas ophalen
        List<Area> areas = loadAreasFromFile(file);

        // Daarna grid maken
        return loadGridFromAreas(areas);
    }

    // Berekent hoe groot het grid moet zijn
    private Grid calculateGridSize(List<Area> areas) {

        // Grootste X en Y waarden opslaan
        int maxX = 0;
        int maxY = 0;

        // Door alle areas lopen
        for (Area area : areas) {

            // Eindpositie van area berekenen
            int endX = area.getX() + area.getWidth() - 1;
            int endY = area.getY() + area.getHeight() - 1;

            // Grootste X opslaan
            if (endX > maxX)
                maxX = endX;

            // Grootste Y opslaan
            if (endY > maxY)
                maxY = endY;
        }

        // Nieuw grid maken
        // +1 omdat arrays beginnen bij index 0
        return new Grid(maxX + 1, maxY + 1);
    }

    // Controleert of een area een Lobby is
    public boolean CheckForLobby(Area area) {

        return "Lobby".equalsIgnoreCase(area.getAreaType());
    }

    // Controleert of een area trappen bevat
    public boolean CheckForStairs(Area area) {

        return "Stairs".equalsIgnoreCase(area.getAreaType());
    }

    // Controleert of een area een lift bevat
    public boolean CheckForLift(Area area) {

        return "Lift".equalsIgnoreCase(area.getAreaType());
    }

    // Controleert of het hotel een:
    // S = Stairs
    // L = Lobby
    // L = Lift
    // bevat
    public boolean CheckForSLL(List<Area> areas) {

        // Boolean waarden voor controle
        boolean LobbyAvailable = false;
        boolean StairsAvailable = false;
        boolean LiftAvailable = false;

        // Door alle areas lopen
        for (Area area : areas) {

            // Lobby gevonden
            if (CheckForLobby(area)) {
                LobbyAvailable = true;
            }

            // Trap gevonden
            if (CheckForStairs(area)) {
                StairsAvailable = true;
            }

            // Lift gevonden
            if (CheckForLift(area)) {
                LiftAvailable = true;
            }
        }

        // Alleen true als alles aanwezig is
        return LobbyAvailable
                && StairsAvailable
                && LiftAvailable;
    }

    // Geeft een lijst terug van alle kamers
    public List<Area> getListOfRooms(List<Area> areas) {

        // Nieuwe lijst voor kamers
        List<Area> rooms = new ArrayList<>();

        // Door alle areas lopen
        for (Area area : areas) {

            // Alleen rooms toevoegen
            if (area.getAreaType().equalsIgnoreCase("room")) {

                rooms.add(area);
            }
        }

        // Lijst met kamers teruggeven
        return rooms;
    }
}