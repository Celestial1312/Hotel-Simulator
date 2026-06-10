package test;

import model.*;
import pathfinding.AStarPathFinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for the Hotel Simulator project.
 */
public class HotelSimulatorTest {

    // -------------------------------------------------------
    // Helper: creates a single Tile with an Area assigned
    // (needed because SubTile.isWalkable() requires a non-null Area)
    // -------------------------------------------------------
    private Tile makeWalkableTile(int x, int y) {
        Tile tile = new Tile(x, y, 2, 2);
        Area area = new Area();
        tile.setArea(area);
        return tile;
    }

    // ================================================================
    // 1.  Person / Guest / Cleaner  (model tests)
    // ================================================================

    @Test
    void guest_hasCorrectId() {
        Tile tile = makeWalkableTile(0, 0);
        SubTile startSubTile = tile.getSubTile(0, 0);

        Guest guest = new Guest(42, startSubTile);

        assertEquals(42, guest.getId());
    }

    @Test
    void guest_isNotCheckingOutByDefault() {
        Tile tile = makeWalkableTile(0, 0);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        assertFalse(guest.isCheckingOut(),
                "A newly created guest should not be checking out");
    }

    @Test
    void guest_canBeMarkedAsCheckingOut() {
        Tile tile = makeWalkableTile(0, 0);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        guest.setCheckingOut(true);

        assertTrue(guest.isCheckingOut());
    }

    @Test
    void cleaner_isNotCleaningByDefault() {
        Tile tile = makeWalkableTile(0, 0);
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        assertFalse(cleaner.isCleaning(),
                "A new cleaner should not be cleaning yet");
    }

    @Test
    void cleaner_isCleaningAfterStartCleaning() {
        Tile tile = makeWalkableTile(0, 0);
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        cleaner.startCleaning(3);

        assertTrue(cleaner.isCleaning());
    }

    @Test
    void cleaner_stopsCleaningAfterTicksRunOut() {
        Tile tile = makeWalkableTile(0, 0);
        Cleaner cleaner = new Cleaner(5, tile.getSubTile(0, 0));

        cleaner.startCleaning(2);
        cleaner.decreaseCleaningTicks(); // tick 1 -> 1 remaining
        cleaner.decreaseCleaningTicks(); // tick 2 -> 0 remaining

        assertFalse(cleaner.isCleaning(),
                "Cleaner should stop cleaning once all ticks are used up");
    }

    // ================================================================
    // 2.  SubTile (walkability)
    // ================================================================

    @Test
    void subTile_isNotWalkableWhenTileHasNoArea() {
        Tile tile = new Tile(0, 0, 2, 2); // no area assigned
        SubTile subTile = tile.getSubTile(0, 0);

        assertFalse(subTile.isWalkable(),
                "A SubTile whose Tile has no Area should not be walkable");
    }

    @Test
    void subTile_isWalkableWhenTileHasArea() {
        Tile tile = makeWalkableTile(0, 0);
        SubTile subTile = tile.getSubTile(0, 0);

        assertTrue(subTile.isWalkable());
    }

    @Test
    void subTile_isNotWalkableWhenOccupiedByPerson() {
        Tile tile = makeWalkableTile(0, 0);
        SubTile subTile = tile.getSubTile(0, 0);
        Guest guest = new Guest(1, subTile);

        subTile.setPerson(guest); // someone is already standing here

        assertFalse(subTile.isWalkable(),
                "A SubTile with a Person on it should not be walkable");
    }

    @Test
    void subTile_globalCoordinatesAreCorrect() {
        // Tile at grid position (2, 3), each tile has 2x2 sub-tiles
        Tile tile = new Tile(2, 3, 2, 2);
        SubTile subTile = tile.getSubTile(1, 0); // local x=1, y=0

        // globalX = tileX * columns + localX = 2 * 2 + 1 = 5
        // globalY = tileY * rows    + localY = 3 * 2 + 0 = 6
        assertEquals(5, subTile.getGlobalX());
        assertEquals(6, subTile.getGlobalY());
    }

    // ================================================================
    // 3.  A* Pathfinding
    // ================================================================

    /**
     * Builds a small 3×1 corridor of walkable tiles and connects them
     * so A* can move between them:
     *
     *   [Tile A (0,0)] — [Tile B (1,0)] — [Tile C (2,0)]
     */
    private Tile[] buildLinearCorridor() {
        Tile a = makeWalkableTile(0, 0);
        Tile b = makeWalkableTile(1, 0);
        Tile c = makeWalkableTile(2, 0);

        // Connect tiles horizontally (right/left neighbours)
        a.setRight(b);
        b.setLeft(a);
        b.setRight(c);
        c.setLeft(b);

        // Connect the sub-tiles that live on the border between tiles
        // a's rightmost sub-tile (column 1, row 0) -> b's leftmost (column 0, row 0)
        SubTile aRight = a.getSubTile(1, 0);
        SubTile bLeft  = b.getSubTile(0, 0);
        SubTile bRight = b.getSubTile(1, 0);
        SubTile cLeft  = c.getSubTile(0, 0);

        aRight.setRight(bLeft);
        bLeft.setLeft(aRight);
        bRight.setRight(cLeft);
        cLeft.setLeft(bRight);

        return new Tile[]{a, b, c};
    }

    @Test
    void aStar_findsPathBetweenAdjacentSubTiles() {
        Tile[] corridor = buildLinearCorridor();
        SubTile start  = corridor[0].getSubTile(0, 0);
        SubTile target = corridor[0].getSubTile(1, 0);

        AStarPathFinding aStar = new AStarPathFinding();
        List<SubTile> path = aStar.findPath(start, target);

        assertFalse(path.isEmpty(), "A path should exist between adjacent sub-tiles");
    }

    @Test
    void aStar_pathStartsAtStartAndEndsAtTarget() {
        Tile[] corridor = buildLinearCorridor();
        SubTile start  = corridor[0].getSubTile(0, 0);
        SubTile target = corridor[2].getSubTile(1, 0);

        AStarPathFinding aStar = new AStarPathFinding();
        List<SubTile> path = aStar.findPath(start, target);

        assertFalse(path.isEmpty());
        assertEquals(start,  path.get(0),               "Path must start at the start tile");
        assertEquals(target, path.get(path.size() - 1), "Path must end at the target tile");
    }

    @Test
    void aStar_returnsEmptyPathWhenTargetIsNull() {
        Tile tile = makeWalkableTile(0, 0);
        SubTile start = tile.getSubTile(0, 0);

        AStarPathFinding aStar = new AStarPathFinding();
        List<SubTile> path = aStar.findPath(start, null);

        assertTrue(path.isEmpty(),
                "findPath should return an empty list when the target is null");
    }

    @Test
    void aStar_returnsEmptyPathWhenStartIsNull() {
        Tile tile = makeWalkableTile(0, 0);
        SubTile target = tile.getSubTile(0, 0);

        AStarPathFinding aStar = new AStarPathFinding();
        List<SubTile> path = aStar.findPath(null, target);

        assertTrue(path.isEmpty(),
                "findPath should return an empty list when the start is null");
    }

    // ================================================================
    // 4.  Area (model)
    // ================================================================

    @Test
    void area_isNotClaimedByDefault() {
        Area area = new Area();

        assertFalse(area.getIsClaimed(),
                "A new Area should not be claimed");
    }

    @Test
    void area_canBeClaimed() {
        Area area = new Area();
        area.setIsClaimed(true);

        assertTrue(area.getIsClaimed());
    }

    @Test
    void area_canAssignAndRetrieveGuest() {
        Area area = new Area();
        Tile tile = makeWalkableTile(0, 0);
        Guest guest = new Guest(7, tile.getSubTile(0, 0));

        area.setGuest(guest);

        assertEquals(guest, area.getGuest());
    }
}
