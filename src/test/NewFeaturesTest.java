package test;

import model.*;
import model.Person.PersonGoal;
import model.Person.PersonState;
import model.Area.AreaState;
import handler.ElevatorHandler;
import simulation.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests voor de NIEUWE code: Stair, Elevator, PersonState/PersonGoal (in Person)
 * en de delen van ElevatorHandler die geen volledig Grid nodig hebben.
 *
 * Elke test volgt dezelfde 3 stappen:
 *   ARRANGE – objecten klaarzetten
 *   ACT     – de methode aanroepen die we testen
 *   ASSERT  – controleren of het resultaat klopt
 *
 * Net als bij de vorige testbestanden staat boven elke test of het een
 * SIMPELE TEST (alleen getter/setter, geen berekening) of een
 * LOGICA TEST (echte berekening/beslissing) is.
 */
public class NewFeaturesTest {

    // ================================================================
    // Stair
    // ================================================================

    /**
     * Een nieuwe Stair moet leeg beginnen: niemand op de trap.
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde
    @Test
    void stair_hasNoPeopleByDefault() {
        // ARRANGE
        Stair stair = new Stair();

        // ACT
        boolean hasPeople = stair.hasPeople();

        // ASSERT
        assertFalse(hasPeople, "Een nieuwe trap mag nog niemand op zich hebben");
    }

    /**
     * addPerson() moet de persoon toevoegen aan de lijst, zodat
     * hasPeople() daarna true wordt en de persoon ook echt in de lijst staat.
     */
    // >>> LOGICA TEST — test of addPerson() de lijst en hasPeople() correct bijwerkt
    @Test
    void stair_addPersonAddsPersonAndUpdatesHasPeople() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        Stair stair = new Stair();

        // ACT
        stair.addPerson(guest);

        // ASSERT
        assertTrue(stair.hasPeople(), "Na addPerson moet hasPeople true zijn");
        assertTrue(stair.getPeople().contains(guest), "De gast moet in de lijst van de trap staan");
    }

    /**
     * removePerson() moet de persoon weer uit de lijst halen,
     * zodat hasPeople() daarna weer false is.
     */
    // >>> LOGICA TEST — test of removePerson() de persoon écht verwijdert
    @Test
    void stair_removePersonRemovesPersonFromList() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        Stair stair = new Stair();
        stair.addPerson(guest);

        // ACT
        stair.removePerson(guest);

        // ASSERT
        assertFalse(stair.hasPeople(), "Na removePerson mag er niemand meer op de trap staan");
        assertFalse(stair.getPeople().contains(guest));
    }


    // ================================================================
    // Elevator
    // ================================================================

    /**
     * Een nieuwe Elevator moet beginnen op het level waarmee hij
     * aangemaakt is, en nog niet bewegen.
     */
    // >>> SIMPELE TEST — checkt alleen de startwaarden uit de constructor
    @Test
    void elevator_startsOnGivenLevelAndIsNotMoving() {
        // ARRANGE + ACT
        Elevator elevator = new Elevator(2);

        // ASSERT
        assertEquals(2, elevator.getCurrentLevel());
        assertFalse(elevator.isMoving(), "Een nieuwe lift staat nog stil");
    }

    /**
     * callToLevel() moet de lift op "moving" zetten ALS het doel-level
     * verschilt van het huidige level. Als het doel gelijk is aan het
     * huidige level, mag de lift niet gaan bewegen.
     *
     * We testen hier dus de echte if-voorwaarde in callToLevel():
     *   if (targetLevel != currentLevel) { moving = true; }
     */
    // >>> LOGICA TEST — test de if-voorwaarde in callToLevel() (verschillend level vs hetzelfde level)
    @Test
    void elevator_callToLevelOnlyStartsMovingWhenLevelDiffers() {
        // ARRANGE
        Elevator elevator = new Elevator(0);

        // ACT
        elevator.callToLevel(0); // zelfde level als nu
        boolean movingAfterSameLevel = elevator.isMoving();

        elevator.callToLevel(3); // ander level
        boolean movingAfterDifferentLevel = elevator.isMoving();

        // ASSERT
        assertFalse(movingAfterSameLevel,
                "Lift mag niet gaan bewegen als het doel-level gelijk is aan het huidige level");
        assertTrue(movingAfterDifferentLevel,
                "Lift moet gaan bewegen als het doel-level verschilt van het huidige level");
        assertEquals(3, elevator.getTargetLevel());
    }

    /**
     * addPassenger() en removePassenger() moeten de lijst van passagiers
     * correct bijwerken, zodat hasPassengers() het juiste antwoord geeft.
     */
    // >>> LOGICA TEST — test of de passagierslijst en hasPassengers() synchroon blijven
    @Test
    void elevator_addAndRemovePassengerUpdatesHasPassengers() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        Elevator elevator = new Elevator(0);

        // ACT
        elevator.addPassenger(guest);
        boolean hasPassengersAfterAdd = elevator.hasPassengers();

        elevator.removePassenger(guest);
        boolean hasPassengersAfterRemove = elevator.hasPassengers();

        // ASSERT
        assertTrue(hasPassengersAfterAdd, "Na addPassenger moet hasPassengers true zijn");
        assertFalse(hasPassengersAfterRemove, "Na removePassenger moet hasPassengers weer false zijn");
    }


    // ================================================================
    // Person — PersonState en PersonGoal
    // ================================================================

    /**
     * Een nieuw aangemaakte Guest heeft nog geen PersonState of PersonGoal
     * (deze zijn pas later gezet, dus standaard null).
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde (null)
    @Test
    void person_hasNoStateOrGoalByDefault() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);

        // ACT
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        // ASSERT
        assertNull(guest.getPersonState(), "Een nieuwe gast heeft nog geen state");
        assertNull(guest.getPersonGoal(), "Een nieuwe gast heeft nog geen goal");
    }

    /**
     * setPersonState() en setPersonGoal() moeten de waarde opslaan
     * en teruggeven via de bijbehorende getter.
     */
    // >>> SIMPELE TEST — getter/setter, "wat je erin stopt komt eruit"
    @Test
    void person_canSetAndGetStateAndGoal() {
        // ARRANGE
        Tile tile = new Tile(0, 0, 2, 2);
        Area area = new Area();
        tile.setArea(area);
        Guest guest = new Guest(1, tile.getSubTile(0, 0));

        // ACT
        guest.setPersonState(PersonState.WAITING);
        guest.setPersonGoal(PersonGoal.CHECKIN);

        // ASSERT
        assertEquals(PersonState.WAITING, guest.getPersonState());
        assertEquals(PersonGoal.CHECKIN, guest.getPersonGoal());
    }


    // ================================================================
    // Area — AreaState
    // ================================================================

    /**
     * Een nieuwe Area moet standaard AVAILABLE zijn (vrije kamer).
     */
    // >>> SIMPELE TEST — checkt alleen een default waarde
    @Test
    void area_isAvailableByDefault() {
        // ARRANGE
        Area area = new Area();

        // ACT
        AreaState state = area.getState();

        // ASSERT
        assertEquals(AreaState.AVAILABLE, state);
    }

    /**
     * setState() moet de status van de Area kunnen veranderen,
     * bijvoorbeeld naar NEEDS_CLEANING zodra een gast is uitgecheckt.
     */
    // >>> SIMPELE TEST — getter/setter, "wat je erin stopt komt eruit"
    @Test
    void area_canChangeStateToNeedsCleaning() {
        // ARRANGE
        Area area = new Area();

        // ACT
        area.setState(AreaState.NEEDS_CLEANING);

        // ASSERT
        assertEquals(AreaState.NEEDS_CLEANING, area.getState());
    }


    // ================================================================
    // ElevatorHandler — logica die alleen de Elevator nodig heeft
    // (geen Grid nodig voor deze methodes)
    // ================================================================

    /**
     * Kleine subclass van Simulation, alleen nodig omdat ElevatorHandler
     * een Simulation object in de constructor verwacht. De methodes die
     * we hier testen (boardWaitingPeopleOnCurrentLevel, sendElevatorToNextTarget)
     * gebruiken de Simulation zelf niet, dus we hoeven niks te overschrijven.
     */
    static class FakeSimulation extends Simulation {
    }

    private FakeSimulation fakeSimulation;

    @BeforeEach
    void setUp() {
        fakeSimulation = new FakeSimulation();
    }

    /**
     * boardWaitingPeopleOnCurrentLevel() moet alleen de wachtende personen
     * laten instappen die op HETZELFDE level staan als de lift.
     * Iemand op een ander level moet blijven wachten.
     */
    // >>> LOGICA TEST — test de level-vergelijking in boardWaitingPeopleOnCurrentLevel()
    @Test
    void elevatorHandler_boardsOnlyPeopleOnSameLevel() {
        // ARRANGE
        Tile tileOnLevel0 = new Tile(0, 0, 2, 2); // level 0 (y=0)
        Area area0 = new Area();
        tileOnLevel0.setArea(area0);

        Tile tileOnLevel2 = new Tile(0, 2, 2, 2); // level 2 (y=2)
        Area area2 = new Area();
        tileOnLevel2.setArea(area2);

        Guest guestOnLevel0 = new Guest(1, tileOnLevel0.getSubTile(0, 0));
        Guest guestOnLevel2 = new Guest(2, tileOnLevel2.getSubTile(0, 0));

        Elevator elevator = new Elevator(0); // lift staat op level 0
        elevator.addWaitingGuest(guestOnLevel0);
        elevator.addWaitingGuest(guestOnLevel2);

        ElevatorHandler handler = new ElevatorHandler(fakeSimulation, elevator);

        // ACT
        handler.boardWaitingPeopleOnCurrentLevel();

        // ASSERT
        assertTrue(elevator.getPassengers().contains(guestOnLevel0),
                "Gast op level 0 moet zijn ingestapt, want de lift staat op level 0");
        assertFalse(elevator.getPassengers().contains(guestOnLevel2),
                "Gast op level 2 mag niet instappen, want de lift staat op level 0");
        assertTrue(elevator.getWaitingPeople().contains(guestOnLevel2),
                "Gast op level 2 moet nog steeds in de wachtlijst staan");
    }

    /**
     * update() moet de lift één level dichter naar het doel bewegen,
     * en stoppen met bewegen zodra het doel bereikt is.
     */
    // >>> LOGICA TEST — test de beweeglogica (1 level per tick, stoppen bij doel)
    @Test
    void elevatorHandler_updateMovesElevatorOneLevelTowardsTarget() {
        // ARRANGE
        Elevator elevator = new Elevator(0);
        elevator.callToLevel(2); // doel is level 2, dus moving = true

        ElevatorHandler handler = new ElevatorHandler(fakeSimulation, elevator);

        // ACT — eerste tick
        handler.update();
        int levelAfterFirstTick = elevator.getCurrentLevel();
        boolean movingAfterFirstTick = elevator.isMoving();

        // ACT — tweede tick (lift moet nu op het doel aankomen)
        handler.update();
        int levelAfterSecondTick = elevator.getCurrentLevel();
        boolean movingAfterSecondTick = elevator.isMoving();

        // ASSERT
        assertEquals(1, levelAfterFirstTick, "Na 1 tick moet de lift 1 level omhoog zijn (0 -> 1)");
        assertTrue(movingAfterFirstTick, "Lift is er nog niet, moet nog blijven bewegen");

        assertEquals(2, levelAfterSecondTick, "Na 2 ticks moet de lift op het doel-level staan");
        assertFalse(movingAfterSecondTick, "Lift moet stoppen met bewegen zodra het doel bereikt is");
    }

    /**
     * update() mag niks doen als de lift niet aan het bewegen is
     * (moving == false) — het level mag dan niet veranderen.
     */
    // >>> LOGICA TEST — edge case: update() mag niks doen als de lift stilstaat
    @Test
    void elevatorHandler_updateDoesNothingWhenElevatorIsNotMoving() {
        // ARRANGE
        Elevator elevator = new Elevator(1); // staat stil op level 1, niet aangeroepen met callToLevel
        ElevatorHandler handler = new ElevatorHandler(fakeSimulation, elevator);

        // ACT
        handler.update();

        // ASSERT
        assertEquals(1, elevator.getCurrentLevel(), "Level mag niet veranderen als de lift stilstaat");
    }
}