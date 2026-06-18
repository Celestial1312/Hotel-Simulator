package test;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests voor Grid, Tile en Person.
 *
 * Eén of twee tests per class — gericht op het echte gedrag
 * van die class, niet op triviale getters/setters.
 *
 * Elke test volgt dezelfde 3 stappen:
 *   ARRANGE – objecten klaarzetten
 *   ACT     – de methode aanroepen die we testen
 *   ASSERT  – controleren of het resultaat klopt
 */
public class GridAndPersonTest {

    // ================================================================
    // Grid
    // ================================================================

    /**
     * Grid moet bij het aanmaken automatisch alle tiles aanmaken
     * en met elkaar verbinden (connectTiles).
     *
     * We maken een 3x3 grid en checken dat tile (1,1) buren heeft
     * in alle 4 richtingen, en dat tile (0,0) geen 'up' of 'left' heeft
     * (want hij ligt in de hoek).
     */
    // >>> LOGICA TEST — test de automatische connectTiles() logica
    @Test
    void grid_connectsNeighboringTilesCorrectly() {
        // ARRANGE
        // (geen losse arrange nodig — het aanmaken van de Grid is meteen de actie)

        // ACT
        Grid grid = new Grid(3, 3);
        Tile center = grid.getTile(1, 1);
        Tile corner = grid.getTile(0, 0);

        // ASSERT — middelste tile heeft alle 4 buren
        assertNotNull(center.getUp());
        assertNotNull(center.getDown());
        assertNotNull(center.getLeft());
        assertNotNull(center.getRight());

        // ASSERT — hoektegel (0,0) heeft geen buren naar boven/links
        assertNull(corner.getUp());
        assertNull(corner.getLeft());
    }

    /**
     * Grid moet ook de sub-tiles tussen aangrenzende tiles verbinden,
     * zodat A* van de ene tile naar de andere kan lopen.
     *
     * Tile (0,0) heeft rechterbuur tile (1,0). De rechtste sub-tile van
     * tile (0,0) moet verbonden zijn met de linkse sub-tile van tile (1,0).
     */
    // >>> LOGICA TEST — test de connectSubTilesBetweenTiles() logica (cruciaal voor A*)
    @Test
    void grid_connectsSubTilesAcrossTileBorders() {
        // ARRANGE
        Grid grid = new Grid(2, 1); // 2 tiles naast elkaar
        Tile left  = grid.getTile(0, 0);
        Tile right = grid.getTile(1, 0);

        // ACT
        int lastCol = left.getSubTileColumns() - 1;
        SubTile leftEdge  = left.getSubTile(lastCol, 0);
        SubTile rightEdge = right.getSubTile(0, 0);

        // ASSERT
        assertEquals(rightEdge, leftEdge.getRight(),
                "De rechterrand van tile (0,0) moet verbonden zijn met de linkerrand van tile (1,0)");
        assertEquals(leftEdge, rightEdge.getLeft(),
                "De verbinding moet ook terug werken");
    }

    /**
     * getTile() moet null teruggeven voor coordinaten buiten het grid,
     * in plaats van te crashen met een ArrayIndexOutOfBoundsException.
     */
    // >>> LOGICA TEST — edge case: out-of-bounds mag niet crashen
    @Test
    void grid_getTileReturnsNullForOutOfBoundsCoordinates() {
        // ARRANGE
        Grid grid = new Grid(3, 3);

        // ACT
        Tile negativeX = grid.getTile(-1, 0);
        Tile tooLargeY = grid.getTile(0, 5);

        // ASSERT
        assertNull(negativeX, "Negatieve x moet null teruggeven");
        assertNull(tooLargeY, "Y buiten de grid moet null teruggeven");
    }

    // ================================================================
    // Tile
    // ================================================================

    /**
     * Een nieuwe Tile moet automatisch zijn sub-tiles aanmaken én
     * onderling verbinden (connectSubTiles), zonder dat we dat zelf
     * hoeven te doen.
     *
     * We maken een tile van 2x2 sub-tiles. De sub-tile op (0,0)
     * moet als rechterbuur de sub-tile op (1,0) hebben.
     */
    // >>> LOGICA TEST — test de automatische connectSubTiles() logica
    @Test
    void tile_automaticallyConnectsItsSubTilesOnCreation() {
        // ARRANGE
        // (het aanmaken van de Tile is meteen de actie die we testen)

        // ACT
        Tile tile = new Tile(0, 0, 2, 2);
        SubTile topLeft    = tile.getSubTile(0, 0);
        SubTile topRight   = tile.getSubTile(1, 0);
        SubTile bottomLeft = tile.getSubTile(0, 1);

        // ASSERT
        assertEquals(topRight, topLeft.getRight(),
                "subTile (0,0) moet (1,0) als rechterbuur hebben");
        assertEquals(bottomLeft, topLeft.getDown(),
                "subTile (0,0) moet (0,1) als onderbuur hebben");
    }

    /**
     * getSubTile() moet null teruggeven voor ongeldige lokale coordinaten,
     * in plaats van te crashen.
     */
    // >>> LOGICA TEST — edge case: ongeldige coordinaten mogen niet crashen
    @Test
    void tile_getSubTileReturnsNullForInvalidCoordinates() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);

        // ACT
        SubTile negative = tile.getSubTile(-1, 0);
        SubTile tooLarge = tile.getSubTile(5, 5);

        // ASSERT
        assertNull(negative);
        assertNull(tooLarge);
    }

    // ================================================================
    // Person (via Guest, want Person is abstract)
    // ================================================================

    /**
     * getNextStep() moet de eerste stap uit het pad teruggeven én
     * deze uit het pad verwijderen (FIFO gedrag, pad slinkt na elke stap).
     */
    // >>> LOGICA TEST — test FIFO-gedrag (pad slinkt na elke stap)
    @Test
    void person_getNextStepReturnsAndRemovesFirstStepFromPath() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);

        SubTile start = tile.getSubTile(0, 0);
        SubTile step1 = tile.getSubTile(1, 0);
        SubTile step2 = tile.getSubTile(0, 1);

        Guest guest = new Guest(1, start);
        guest.setPath(java.util.List.of(step1, step2));

        // ACT
        SubTile first  = guest.getNextStep();
        boolean hasPathAfterFirst = guest.hasPath();
        SubTile second = guest.getNextStep();
        boolean hasPathAfterSecond = guest.hasPath();

        // ASSERT
        assertEquals(step1, first, "getNextStep moet de eerste stap teruggeven");
        assertTrue(hasPathAfterFirst, "er moet nog 1 stap overblijven");

        assertEquals(step2, second);
        assertFalse(hasPathAfterSecond, "pad moet nu leeg zijn");
    }

    /**
     * getNextStep() moet null teruggeven wanneer er geen pad is,
     * in plaats van te crashen op een lege lijst.
     */
    // >>> LOGICA TEST — edge case: leeg pad mag niet crashen
    @Test
    void person_getNextStepReturnsNullWhenPathIsEmpty() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);

        Guest guest = new Guest(1, tile.getSubTile(0, 0)); // geen pad gezet

        // ACT
        SubTile result = guest.getNextStep();

        // ASSERT
        assertNull(result);
        assertFalse(guest.hasPath());
    }
}