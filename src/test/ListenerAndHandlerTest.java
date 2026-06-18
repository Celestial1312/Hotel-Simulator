package test;

import handler.*;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;
import listener.SimulationEventListener;
import model.Guest;
import model.Person.PersonGoal;
import model.Person.PersonState;
import simulation.Simulation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests voor de SimulationEventListener en de event handlers.
 *
 * BELANGRIJK -- waarom dit bestand anders is dan de vorige versie:
 * In een oudere versie van de code had Simulation methodes zoals
 * checkIn()/checkOut()/evacuateEmergency() die we konden overschrijven
 * met een FakeSimulation om te zien of ze aangeroepen werden.
 *
 * Die methodes bestaan niet meer. Nu zit alle logica rechtstreeks IN
 * de handlers (CheckInEventHandler.handleEvent() doet al het werk zelf:
 * kamer zoeken, pad berekenen, guest object aanmaken, etc.) en gebruiken
 * ze Simulation alleen om bij het grid, de guests-lijst en de rooms te komen.
 *
 * Daarom testen we nu het ECHTE resultaat: we bouwen een echte (kleine)
 * Simulation met een echt JSON-laagje (Lobby + Stairs + 1 kamer), sturen
 * er een event doorheen, en controleren wat er daadwerkelijk veranderd is
 * (zit de guest in de lijst? heeft hij een pad? staat zijn state goed?).
 *
 * Dit is dus eigenlijk een nog BETERE test dan voorheen: we testen niet
 * alleen "werd de methode aangeroepen", maar "deed de methode het juiste".
 *
 * Elke test volgt dezelfde 3 stappen:
 *   ARRANGE - Simulation + JSON layout + event klaarzetten
 *   ACT     - het event door de handler/listener laten verwerken
 *   ASSERT  - controleren of de simulatie-status nu klopt
 */
public class ListenerAndHandlerTest {

    /**
     * HELPER -- bouwt een echte, kleine, werkende Simulation.
     *
     * Layout (1 verdieping is genoeg voor CheckIn/CheckOut/Evacuate):
     *   (0,0) Lobby   (1,0) Stairs   (2,0) Room (classificatie 1)
     *
     * We schrijven dit als een echt JSON-bestand naar een tijdelijke map
     * en laten Simulation het op de normale manier inladen
     * (loadGridFromJsonFile), precies zoals in de echte applicatie.
     */
    private Simulation buildSimulationWithSimpleLayout(Path tempDir) throws IOException {
        String json = "[\n" +
                "  { \"AreaType\": \"Lobby\",  \"Classification\": \"0\", \"Position\": \"0,0\", \"Dimension\": \"1,1\" },\n" +
                "  { \"AreaType\": \"Stairs\", \"Classification\": \"0\", \"Position\": \"1,0\", \"Dimension\": \"1,1\" },\n" +
                "  { \"AreaType\": \"Room\",   \"Classification\": \"1\", \"Position\": \"2,0\", \"Dimension\": \"1,1\" }\n" +
                "]";

        File jsonFile = tempDir.resolve("layout.json").toFile();
        Files.writeString(jsonFile.toPath(), json);

        Simulation simulation = new Simulation();
        simulation.loadGridFromJsonFile(jsonFile);
        return simulation;
    }

    // ================================================================
    // CheckInEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor CHECK_IN en false voor andere types.
     */
    // >>> LOGICA TEST -- test de canHandle() beslissingslogica
    @Test
    void checkInHandler_recognizesOnlyCheckInEvents(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckInEventHandler handler = new CheckInEventHandler(simulation);

        HotelEvent checkInEvent  = new HotelEvent(0, HotelEventType.CHECK_IN, 1, 1);
        HotelEvent checkOutEvent = new HotelEvent(0, HotelEventType.CHECK_OUT, 1, 1);

        // ACT
        boolean canHandleCheckIn  = handler.canHandle(checkInEvent);
        boolean canHandleCheckOut = handler.canHandle(checkOutEvent);

        // ASSERT
        assertTrue(canHandleCheckIn, "moet CHECK_IN herkennen");
        assertFalse(canHandleCheckOut, "mag CHECK_OUT niet herkennen");
    }

    /**
     * handleEvent moet een nieuwe Guest aanmaken, hem toevoegen aan
     * simulation.getGuests(), en hem op weg sturen naar zijn kamer
     * (via de trap, want er is geen lift in deze layout).
     *
     * Classificatie 1 in het event moet matchen met de kamer
     * die classificatie 1 heeft in de JSON layout.
     */
    // >>> LOGICA TEST -- test het ECHTE resultaat van handleEvent() op een echte Simulation
    @Test
    void checkInHandler_createsGuestAndSendsToRoom(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckInEventHandler handler = new CheckInEventHandler(simulation);

        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_IN, 42, 1); // guestId=42, classificatie=1

        // ACT
        handler.handleEvent(event);
        Guest guest = simulation.getGuests().get(42);

        // ASSERT
        assertNotNull(guest, "Er moet een Guest met id 42 zijn toegevoegd aan de simulatie");
        assertEquals(PersonGoal.CHECKIN, guest.getPersonGoal(), "Het doel van de gast moet CHECKIN zijn");
        assertTrue(guest.hasPath(), "De gast moet een pad hebben gekregen naar zijn kamer");
    }

    /**
     * handleEvent moet NIKS doen als het guestId al bestaat
     * (de gast is al ingecheckt) -- er mag geen tweede Guest worden aangemaakt.
     */
    // >>> LOGICA TEST -- edge case: dubbele check-in mag niet opnieuw aanmaken
    @Test
    void checkInHandler_doesNothingIfGuestAlreadyExists(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckInEventHandler handler = new CheckInEventHandler(simulation);

        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_IN, 42, 1);
        handler.handleEvent(event); // eerste keer inchecken
        Guest firstGuestObject = simulation.getGuests().get(42);

        // ACT -- probeer hetzelfde guestId nog een keer in te checken
        handler.handleEvent(event);
        Guest guestAfterSecondCall = simulation.getGuests().get(42);

        // ASSERT -- het moet nog steeds hetzelfde object zijn, niet vervangen
        assertSame(firstGuestObject, guestAfterSecondCall,
                "Een tweede CHECK_IN met hetzelfde guestId mag geen nieuwe Guest aanmaken");
        assertEquals(1, simulation.getGuests().size(), "Er mag maar 1 gast in de simulatie staan");
    }

    // ================================================================
    // CheckOutEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor CHECK_OUT.
     */
    // >>> SIMPELE TEST -- checkt alleen 1 kant van canHandle()
    @Test
    void checkOutHandler_recognizesCheckOutEvent(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckOutEventHandler handler = new CheckOutEventHandler(simulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_OUT, 1, 0);

        // ACT
        boolean canHandle = handler.canHandle(event);

        // ASSERT
        assertTrue(canHandle);
    }

    /**
     * handleEvent moet een bestaande gast op pad zetten naar de lobby
     * en zijn status op "checking out" zetten.
     *
     * We checken eerst in (zodat er een echte gast met een geldige
     * currentSubTile bestaat), en sturen daarna pas het CHECK_OUT event.
     */
    // >>> LOGICA TEST -- test het ECHTE resultaat van handleEvent() op een echte Simulation
    @Test
    void checkOutHandler_sendsExistingGuestToLobby(@TempDir Path tempDir) throws IOException {
        // ARRANGE -- eerst inchecken zodat er een gast bestaat om uit te checken
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        new CheckInEventHandler(simulation).handleEvent(
                new HotelEvent(0, HotelEventType.CHECK_IN, 7, 1));

        CheckOutEventHandler handler = new CheckOutEventHandler(simulation);
        HotelEvent checkOutEvent = new HotelEvent(0, HotelEventType.CHECK_OUT, 7, 0);

        // ACT
        handler.handleEvent(checkOutEvent);
        Guest guest = simulation.getGuests().get(7);

        // ASSERT
        assertTrue(guest.isCheckingOut(), "Gast moet nu checkingOut=true hebben");
        assertEquals(PersonGoal.CHECKOUT, guest.getPersonGoal());
        assertTrue(guest.hasPath(), "Gast moet een pad naar de lobby hebben gekregen");
    }

    /**
     * handleEvent mag niks doen als het guestId niet bestaat
     * (er is niemand om uit te checken) -- geen crash.
     */
    // >>> LOGICA TEST -- edge case: onbekend guestId mag niet crashen
    @Test
    void checkOutHandler_doesNothingForUnknownGuest(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckOutEventHandler handler = new CheckOutEventHandler(simulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_OUT, 999, 0); // bestaat niet

        // ACT + ASSERT -- mag geen exception geven
        assertDoesNotThrow(() -> handler.handleEvent(event));
    }

    // ================================================================
    // EvacuateEmergencyEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor EVACUATE.
     */
    // >>> SIMPELE TEST -- checkt alleen 1 kant van canHandle()
    @Test
    void evacuateHandler_recognizesEvacuateEvent(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        EvacuateEmergencyEventHandler handler = new EvacuateEmergencyEventHandler(simulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.EVACUATE, 1, 0);

        // ACT
        boolean canHandle = handler.canHandle(event);

        // ASSERT
        assertTrue(canHandle);
    }

    /**
     * handleEvent moet ALLE ingecheckte gasten op pad zetten naar de lobby,
     * niet alleen een specifieke gast (in tegenstelling tot checkOut).
     */
    // >>> LOGICA TEST -- test of evacuatie echt alle gasten beinvloedt
    @Test
    void evacuateHandler_sendsAllGuestsToLobby(@TempDir Path tempDir) throws IOException {
        // ARRANGE -- check 2 gasten in
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);
        CheckInEventHandler checkInHandler = new CheckInEventHandler(simulation);
        checkInHandler.handleEvent(new HotelEvent(0, HotelEventType.CHECK_IN, 1, 1));
        checkInHandler.handleEvent(new HotelEvent(0, HotelEventType.CHECK_IN, 2, 1));

        EvacuateEmergencyEventHandler handler = new EvacuateEmergencyEventHandler(simulation);
        HotelEvent evacuateEvent = new HotelEvent(0, HotelEventType.EVACUATE, 0, 0);

        // ACT
        handler.handleEvent(evacuateEvent);
        Guest guest1 = simulation.getGuests().get(1);
        Guest guest2 = simulation.getGuests().get(2);

        // ASSERT -- beide gasten moeten nu op weg zijn naar de lobby
        assertTrue(guest1.isCheckingOut(), "Gast 1 moet geevacueerd zijn");
        assertTrue(guest2.isCheckingOut(), "Gast 2 moet geevacueerd zijn");
    }

    // ================================================================
    // SimulationEventListener
    // ================================================================

    /**
     * De listener moet een CHECK_IN event doorsturen naar CheckInEventHandler,
     * en dus moet er na notify() een nieuwe Guest in de simulatie staan.
     */
    // >>> LOGICA TEST -- test de Chain of Responsibility routing op een echte Simulation
    @Test
    void listener_routesEventToCorrectHandler(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);

        SimulationEventListener listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(simulation),
                new CheckOutEventHandler(simulation)
        ));

        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_IN, 10, 1);

        // ACT
        listener.notify(event);
        Guest guest = simulation.getGuests().get(10);

        // ASSERT
        assertNotNull(guest, "Listener moet CHECK_IN naar CheckInEventHandler gestuurd hebben");
    }

    /**
     * Een eventType waarvoor geen handler bestaat (bv. NONE) mag
     * geen enkele guest aanmaken -- de listener mag niet crashen.
     */
    // >>> LOGICA TEST -- edge case: geen handler past, mag niet crashen
    @Test
    void listener_doesNothingWhenNoHandlerMatches(@TempDir Path tempDir) throws IOException {
        // ARRANGE
        Simulation simulation = buildSimulationWithSimpleLayout(tempDir);

        SimulationEventListener listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(simulation),
                new CheckOutEventHandler(simulation)
        ));

        HotelEvent event = new HotelEvent(0, HotelEventType.NONE, 1, 0);

        // ACT
        listener.notify(event);

        // ASSERT
        assertTrue(simulation.getGuests().isEmpty(),
                "Geen handler past op NONE, dus mag er geen guest zijn aangemaakt");
    }
}