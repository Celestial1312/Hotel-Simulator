package test;

// --- Imports: these bring in the classes we want to test ---
import model.*;                          // imports Guest, Cleaner, Area, Tile, SubTile, Person
import pathfinding.AStarPathFinding;     // the pathfinding algorithm we want to test
import org.junit.jupiter.api.Test;       // @Test annotation: marks a method as a test

import java.util.List;                   // used for the path returned by A*

// assertFalse, assertTrue, assertEquals are the "check" methods we use in every test
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Hotel Simulator.
 *
 * Each method marked with @Test is one test.
 * JUnit runs every @Test method automatically and reports pass or fail.
 *
 * Every test follows the same three steps:
 *   ARRANGE – create the objects you need
 *   ACT     – call the method you want to test
 *   ASSERT  – check that the result is what you expected
 */
public class HotelSimulatorTest {

    // -----------------------------------------------------------------------
    // HELPER METHOD (not a test itself, just reused setup code)
    // -----------------------------------------------------------------------

    /**
     * Creates a Tile that has an Area attached to it.
     *
     * Why do we need this?
     * SubTile.isWalkable() returns true only when its parent Tile has a non-null Area.
     * Almost every test needs at least one walkable tile, so instead of writing
     * the same three lines over and over we put them in this helper.
     *
     * @param x  the tile's column position in the grid
     * @param y  the tile's row position in the grid
     * @return   a fully set-up walkable Tile
     */
    private Tile makeWalkableTile(int x, int y) {
        Tile tile = new Tile(x, y, 2, 2); // 2x2 sub-tiles per tile
        Area area = new Area();           // a real Area object (no JSON needed)
        tile.setArea(area);                // attach the area so sub-tiles become walkable
        return tile;
    }


    // ========================================================================
    // SECTION 1 – Guest tests
    // ========================================================================

    /**
     * Test: does the Guest store the ID we gave it?
     *
     * This also indirectly tests the super() call: Guest passes the id up
     * to Person, which is the class that actually stores it.
     */
    // >>> LOGICA TEST — test de super() constructor-keten (Guest -> Person)
    @Test
    void guest_hasCorrectId() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        SubTile startSubTile = tile.getSubTile(0, 0); // the sub-tile the guest starts on

        // ACT
        Guest guest = new Guest(42, startSubTile); // create guest with id=42

        // ASSERT
        // assertEquals(expected, actual) – fails if the two values are not equal
        assertEquals(42, guest.getId());
    }

    /**
     * Test: a brand-new Guest should NOT be checking out yet.
     *
     * When a guest is first created (check-in), isCheckingOut should be false.
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde, geen berekening
    @Test
    void guest_isNotCheckingOutByDefault() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);

        // ACT
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        // ASSERT
        // assertFalse(x) fails if x is true
        assertFalse(guest.isCheckingOut(),
                "A newly created guest should not be checking out");
    }

    /**
     * Test: we should be able to mark a guest as checking out.
     */
    // >>> SIMPELE TEST — getter/setter, "wat je erin stopt komt eruit"
    @Test
    void guest_canBeMarkedAsCheckingOut() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        // ACT
        guest.setCheckingOut(true);

        // ASSERT
        // assertTrue(x) fails if x is false
        assertTrue(guest.isCheckingOut());
    }


    // ========================================================================
    // SECTION 2 – Cleaner tests
    // ========================================================================

    /**
     * Test: a new Cleaner should not be cleaning yet.
     *
     * cleaningTicks starts at 0, so isCleaning() should return false.
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde
    @Test
    void cleaner_isNotCleaningByDefault() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);

        // ACT
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        // ASSERT
        assertFalse(cleaner.isCleaning(),
                "A new cleaner should not be cleaning yet");
    }

    /**
     * Test: after startCleaning() the cleaner should be busy.
     *
     * startCleaning(3) sets cleaningTicks to 3, so isCleaning() should be true.
     */
    // >>> SIMPELE TEST — checkt of een boolean omslaat na 1 methode-aanroep
    @Test
    void cleaner_isCleaningAfterStartCleaning() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        // ACT
        cleaner.startCleaning(3); // give the cleaner 3 ticks of work

        // ASSERT
        assertTrue(cleaner.isCleaning());
    }

    /**
     * Test: the cleaner should stop cleaning once all ticks have been used up.
     *
     * We give the cleaner 2 ticks, then call decreaseCleaningTicks() twice.
     * After that cleaningTicks reaches 0, so isCleaning() should return false.
     */
    // >>> LOGICA TEST — test de afteltelling (cleaningTicks 2 -> 1 -> 0)
    @Test
    void cleaner_stopsCleaningAfterTicksRunOut() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        // ACT
        cleaner.startCleaning(2);        // cleaningTicks = 2
        cleaner.decreaseCleaningTicks(); // cleaningTicks = 1
        cleaner.decreaseCleaningTicks(); // cleaningTicks = 0

        // ASSERT
        assertFalse(cleaner.isCleaning(),
                "Cleaner should stop cleaning once all ticks are used up");
    }


    // ========================================================================
    // SECTION 3 – SubTile walkability tests
    // ========================================================================

    /**
     * Test: a SubTile whose Tile has NO Area should NOT be walkable.
     *
     * isWalkable() checks: parentTile.getArea() != null && person == null
     * If there is no area, the first condition is false → not walkable.
     */
    // >>> LOGICA TEST — test de voorwaarde area != null in isWalkable()
    @Test
    void subTile_isNotWalkableWhenTileHasNoArea() {
        // ARRANGE – note: we do NOT use makeWalkableTile here on purpose
        Tile tile = new Tile(0, 0, 2, 2); // no area attached
        SubTile subTile = tile.getSubTile(0, 0);

        // ACT
        boolean walkable = subTile.isWalkable();

        // ASSERT
        assertFalse(walkable,
                "A SubTile whose Tile has no Area should not be walkable");
    }

    /**
     * Test: a SubTile whose Tile HAS an Area should be walkable (when empty).
     */
    // >>> LOGICA TEST — test isWalkable() in het positieve geval
    @Test
    void subTile_isWalkableWhenTileHasArea() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0); // area is attached
        SubTile subTile = tile.getSubTile(0, 0);

        // ACT
        boolean walkable = subTile.isWalkable();

        // ASSERT
        assertTrue(walkable);
    }

    /**
     * Test: a SubTile that already has a Person standing on it should NOT be walkable.
     *
     * isWalkable() also checks person == null.
     * If someone is already there, a second person cannot walk onto it.
     */
    // >>> LOGICA TEST — test de voorwaarde person == null in isWalkable()
    @Test
    void subTile_isNotWalkableWhenOccupiedByPerson() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        SubTile subTile = tile.getSubTile(0, 0);
        Guest guest = new Guest(1, subTile);

        // ACT
        subTile.setPerson(guest); // place the guest on the sub-tile
        boolean walkable = subTile.isWalkable();

        // ASSERT
        assertFalse(walkable,
                "A SubTile with a Person on it should not be walkable");
    }

    /**
     * Test: getGlobalX() and getGlobalY() calculate the correct world coordinates.
     *
     * Formula from SubTile.java:
     *   globalX = tileX * subTileColumns + localX
     *   globalY = tileY * subTileRows    + localY
     *
     * Example: tile is at grid position (2, 3), each tile has 2 columns and 2 rows.
     * Sub-tile at local position (1, 0):
     *   globalX = 2 * 2 + 1 = 5
     *   globalY = 3 * 2 + 0 = 6
     */
    // >>> LOGICA TEST — test de coordinaten-berekening (formule)
    @Test
    void subTile_globalCoordinatesAreCorrect() {
        // ARRANGE
        Tile tile = new Tile(2, 3, 2, 2);        // tile at column 2, row 3
        SubTile subTile = tile.getSubTile(1, 0); // local x=1, y=0

        // ACT
        int globalX = subTile.getGlobalX();
        int globalY = subTile.getGlobalY();

        // ASSERT
        assertEquals(5, globalX); // 2*2 + 1 = 5
        assertEquals(6, globalY); // 3*2 + 0 = 6
    }


    // ========================================================================
    // SECTION 4 – A* Pathfinding tests
    // ========================================================================

    /**
     * HELPER for pathfinding tests.
     *
     * Builds a small corridor of 3 walkable tiles placed side by side:
     *
     *   [Tile A (0,0)] — [Tile B (1,0)] — [Tile C (2,0)]
     *
     * Each tile has 2x2 sub-tiles. We also connect the border sub-tiles
     * between neighbouring tiles so A* can actually travel across them.
     *
     * We need to do this manually because in a real run the Grid class
     * does this setup by reading the JSON file — but in tests we don't
     * want to depend on any file.
     *
     * @return array [tileA, tileB, tileC]
     */
    private Tile[] buildLinearCorridor() {
        // three walkable tiles in a row
        Tile a = makeWalkableTile(0, 0);
        Tile b = makeWalkableTile(1, 0);
        Tile c = makeWalkableTile(2, 0);

        // Connect tiles to each other (so the grid knows they are neighbours)
        a.setRight(b);
        b.setLeft(a);
        b.setRight(c);
        c.setLeft(b);

        // Connect the border sub-tiles between tile A and tile B
        SubTile aRight = a.getSubTile(1, 0); // rightmost sub-tile of A
        SubTile bLeft  = b.getSubTile(0, 0); // leftmost  sub-tile of B
        aRight.setRight(bLeft);              // A can step right into B
        bLeft.setLeft(aRight);               // B can step left back into A

        // Connect the border sub-tiles between tile B and tile C
        SubTile bRight = b.getSubTile(1, 0); // rightmost sub-tile of B
        SubTile cLeft  = c.getSubTile(0, 0); // leftmost  sub-tile of C
        bRight.setRight(cLeft);
        cLeft.setLeft(bRight);

        return new Tile[]{a, b, c};
    }

    /**
     * Test: A* should find a path between two adjacent sub-tiles.
     */
    // >>> LOGICA TEST — test of A* zelf een pad berekent
    @Test
    void aStar_findsPathBetweenAdjacentSubTiles() {
        // ARRANGE
        Tile[] corridor = buildLinearCorridor();
        SubTile start  = corridor[0].getSubTile(0, 0); // far left
        SubTile target = corridor[0].getSubTile(1, 0); // one step to the right
        AStarPathFinding aStar = new AStarPathFinding();

        // ACT
        List<SubTile> path = aStar.findPath(start, target);

        // ASSERT
        assertFalse(path.isEmpty(), "A path should exist between adjacent sub-tiles");
    }

    /**
     * Test: the path A* returns must begin at 'start' and end at 'target'.
     *
     * We search across the full corridor (tile A to tile C).
     * path.get(0)              → first element (should be start)
     * path.get(path.size()-1)  → last  element (should be target)
     */
    // >>> LOGICA TEST — test of A* het juiste pad berekent over meerdere tiles
    @Test
    void aStar_pathStartsAtStartAndEndsAtTarget() {
        // ARRANGE
        Tile[] corridor = buildLinearCorridor();
        SubTile start  = corridor[0].getSubTile(0, 0); // tile A, top-left sub-tile
        SubTile target = corridor[2].getSubTile(1, 0); // tile C, top-right sub-tile
        AStarPathFinding aStar = new AStarPathFinding();

        // ACT
        List<SubTile> path = aStar.findPath(start, target);

        // ASSERT
        assertFalse(path.isEmpty());
        assertEquals(start,  path.get(0),               "Path must start at the start tile");
        assertEquals(target, path.get(path.size() - 1), "Path must end at the target tile");
    }

    /**
     * Test: passing null as the target should return an empty list, NOT crash.
     *
     * This is called an "edge case" test – we check that the code handles
     * bad input gracefully instead of throwing a NullPointerException.
     */
    // >>> LOGICA TEST — edge case: A* mag niet crashen bij null
    @Test
    void aStar_returnsEmptyPathWhenTargetIsNull() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        SubTile start = tile.getSubTile(0, 0);
        AStarPathFinding aStar = new AStarPathFinding();

        // ACT
        List<SubTile> path = aStar.findPath(start, null); // null target!

        // ASSERT
        assertTrue(path.isEmpty(),
                "findPath should return an empty list when the target is null");
    }

    /**
     * Test: passing null as the start should also return an empty list, NOT crash.
     */
    // >>> LOGICA TEST — edge case: A* mag niet crashen bij null
    @Test
    void aStar_returnsEmptyPathWhenStartIsNull() {
        // ARRANGE
        Tile tile = makeWalkableTile(0, 0);
        SubTile target = tile.getSubTile(0, 0);
        AStarPathFinding aStar = new AStarPathFinding();

        // ACT
        List<SubTile> path = aStar.findPath(null, target); // null start!

        // ASSERT
        assertTrue(path.isEmpty(),
                "findPath should return an empty list when the start is null");
    }


    // ========================================================================
    // SECTION 5 – Area tests
    // ========================================================================

    /**
     * Test: a newly created Area should be AVAILABLE by default.
     *
     * When the simulation starts, all rooms are free.
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde
    @Test
    void area_isAvailableByDefault() {
        // ARRANGE
        Area area = new Area();

        // ACT
        Area.AreaState state = area.getState();

        // ASSERT
        assertEquals(Area.AreaState.AVAILABLE, state, "A new Area should be AVAILABLE");
    }

    /**
     * Test: we should be able to change an Area's state.
     *
     * setState(OCCUPIED) marks the room as occupied by a guest.
     */
    // >>> SIMPELE TEST — getter/setter, "wat je erin stopt komt eruit"
    @Test
    void area_canBeMarkedOccupied() {
        // ARRANGE
        Area area = new Area();

        // ACT
        area.setState(Area.AreaState.OCCUPIED);

        // ASSERT
        assertEquals(Area.AreaState.OCCUPIED, area.getState());
    }

    /**
     * Test: we should be able to assign a Guest to an Area and get it back.
     *
     * assertEquals(expected, actual) checks that the guest we put in
     * is the exact same object we get back out.
     */
    // >>> SIMPELE TEST — getter/setter, "wat je erin stopt komt eruit"
    @Test
    void area_canAssignAndRetrieveGuest() {
        // ARRANGE
        Area area = new Area();
        Tile tile = makeWalkableTile(0, 0);
        Guest guest = new Guest(7, tile.getSubTile(0, 0));

        // ACT
        area.setGuest(guest);

        // ASSERT
        assertEquals(guest, area.getGuest());
    }
}