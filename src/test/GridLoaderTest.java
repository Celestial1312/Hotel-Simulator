package test;

import loader.GridLoader;
import model.Area;
import model.Grid;
import model.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests voor GridLoader.
 *
 * GridLoader leest een JSON-bestand en zet dat om naar een Grid met Areas.
 * We gebruiken @TempDir om een echt (tijdelijk) JSON-bestand te schrijven,
 * zodat we de hele laadketen testen zoals hij in het echt werkt —
 * zonder dat we een vast bestand in de repo nodig hebben.
 *
 * Elke test volgt dezelfde 3 stappen:
 *   ARRANGE – JSON-bestand klaarzetten
 *   ACT     – de GridLoader methode aanroepen die we testen
 *   ASSERT  – controleren of het resultaat klopt
 */
public class GridLoaderTest {

    /**
     * loadAreasFromFile moet een JSON-bestand correct omzetten naar
     * een lijst van Area objecten met de juiste velden.
     *
     * We schrijven zelf een klein JSON-bestand met 1 area (een Lobby)
     * en controleren of alle velden goed uitgelezen worden.
     */
    // >>> LOGICA TEST — test Jackson parsing/splitsing logica ("1 star" -> 1, "0,0" -> x=0,y=0)
    @Test
    void loadAreasFromFile_readsAreaFieldsCorrectly(@TempDir Path tempDir) throws IOException {
        // ARRANGE — schrijf een tijdelijk JSON bestand met 1 area
        String json = """
            [
              {
                "AreaType": "Lobby",
                "Classification": "1 star",
                "Position": "0,0",
                "Dimension": "2,3"
              }
            ]
            """;

        File jsonFile = tempDir.resolve("hotel.json").toFile();
        Files.writeString(jsonFile.toPath(), json);

        GridLoader loader = new GridLoader();

        // ACT
        List<Area> areas = loader.loadAreasFromFile(jsonFile);
        Area area = areas.get(0);

        // ASSERT
        assertEquals(1, areas.size());
        assertEquals("Lobby", area.getAreaType());
        assertEquals(1, area.getClassification());   // "1 star" -> 1
        assertEquals(0, area.getX());                 // "0,0" -> x=0
        assertEquals(0, area.getY());                 // "0,0" -> y=0
        assertEquals(2, area.getWidth());             // "2,3" -> width=2
        assertEquals(3, area.getHeight());            // "2,3" -> height=3
    }

    /**
     * loadGridFromAreas moet een Grid maken dat groot genoeg is om
     * alle areas te bevatten, en de areas op de juiste tiles plaatsen.
     *
     * We maken zelf (zonder JSON) een lijst met 1 Area van 2x2 op
     * positie (1,1) en checken dat het grid minstens tot (2,2) reikt
     * en dat tile (1,1) die area heeft.
     */
    // >>> LOGICA TEST — test calculateGridSize() berekening + placeAreas() plaatsingslogica
    @Test
    void loadGridFromAreas_buildsGridLargeEnoughAndPlacesArea(@TempDir Path tempDir) throws IOException {
        // ARRANGE — JSON met 1 area op positie (1,1), grootte 2x2
        String json = """
            [
              {
                "AreaType": "Room",
                "Classification": "2 star",
                "Position": "1,1",
                "Dimension": "2,2"
              }
            ]
            """;

        File jsonFile = tempDir.resolve("hotel.json").toFile();
        Files.writeString(jsonFile.toPath(), json);

        GridLoader loader = new GridLoader();
        List<Area> areas = loader.loadAreasFromFile(jsonFile);

        // ACT
        Grid grid = loader.loadGridFromAreas(areas);
        Tile startTile = grid.getTile(1, 1); // start van de area
        Tile endTile   = grid.getTile(2, 2); // einde van de area

        // ASSERT — area loopt van (1,1) tot (2,2), grid moet dus minstens 3x3 zijn
        assertEquals(3, grid.getSizeX());
        assertEquals(3, grid.getSizeY());

        // De area moet op tile (1,1) geplaatst zijn
        assertNotNull(startTile.getArea(), "Tile (1,1) moet een Area hebben");
        assertEquals("Room", startTile.getArea().getAreaType());

        // En ook op het einde van de area: tile (2,2)
        assertNotNull(endTile.getArea(), "Tile (2,2) moet ook tot de area horen");
    }

    /**
     * CheckForSLL moet true teruggeven als de lijst minstens
     * een Lobby, Stairs en Lift area bevat.
     *
     * We testen zowel het "alles aanwezig" geval als
     * het "iets ontbreekt" geval.
     */
    // >>> LOGICA TEST — test AND-logica (alles aanwezig vs iets ontbreekt)
    @Test
    void checkForSLL_detectsRequiredAreaTypes(@TempDir Path tempDir) throws IOException {
        // ARRANGE — JSON met Lobby, Stairs en Lift
        String completeJson = """
            [
              { "AreaType": "Lobby",  "Classification": "0", "Position": "0,0", "Dimension": "1,1" },
              { "AreaType": "Stairs", "Classification": "0", "Position": "1,0", "Dimension": "1,1" },
              { "AreaType": "Lift",   "Classification": "0", "Position": "2,0", "Dimension": "1,1" }
            ]
            """;

        // JSON zonder Lift
        String incompleteJson = """
            [
              { "AreaType": "Lobby",  "Classification": "0", "Position": "0,0", "Dimension": "1,1" },
              { "AreaType": "Stairs", "Classification": "0", "Position": "1,0", "Dimension": "1,1" }
            ]
            """;

        File completeFile = tempDir.resolve("complete.json").toFile();
        Files.writeString(completeFile.toPath(), completeJson);

        File incompleteFile = tempDir.resolve("incomplete.json").toFile();
        Files.writeString(incompleteFile.toPath(), incompleteJson);

        GridLoader loader = new GridLoader();
        List<Area> completeAreas   = loader.loadAreasFromFile(completeFile);
        List<Area> incompleteAreas = loader.loadAreasFromFile(incompleteFile);

        // ACT
        boolean completeResult   = loader.CheckForSLL(completeAreas);
        boolean incompleteResult = loader.CheckForSLL(incompleteAreas);

        // ASSERT
        assertTrue(completeResult,
                "Met Lobby + Stairs + Lift moet CheckForSLL true zijn");
        assertFalse(incompleteResult,
                "Zonder Lift moet CheckForSLL false zijn");
    }

    /**
     * getListOfRooms moet alleen areas met AreaType "room" teruggeven,
     * andere types (zoals Lobby) moeten genegeerd worden.
     */
    // >>> LOGICA TEST — test filter-logica (alleen "room" types overhouden)
    @Test
    void getListOfRooms_returnsOnlyRoomAreas(@TempDir Path tempDir) throws IOException {
        // ARRANGE — JSON met 2 rooms en 1 lobby
        String json = """
            [
              { "AreaType": "Room",  "Classification": "1", "Position": "0,0", "Dimension": "1,1" },
              { "AreaType": "Room",  "Classification": "2", "Position": "1,0", "Dimension": "1,1" },
              { "AreaType": "Lobby", "Classification": "0", "Position": "2,0", "Dimension": "1,1" }
            ]
            """;

        File jsonFile = tempDir.resolve("hotel.json").toFile();
        Files.writeString(jsonFile.toPath(), json);

        GridLoader loader = new GridLoader();
        List<Area> areas = loader.loadAreasFromFile(jsonFile);

        // ACT
        List<Area> rooms = loader.getListOfRooms(areas);

        // ASSERT
        assertEquals(2, rooms.size(), "Er zijn 2 rooms in de JSON, lobby telt niet mee");
        for (Area room : rooms) {
            assertEquals("Room", room.getAreaType());
        }
    }
}