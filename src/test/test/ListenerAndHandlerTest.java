package test;

import listener.*;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;
import simulation.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests voor de SimulationEventListener en de event handlers.
 *
 * We gebruiken de ECHTE HotelEvent en HotelEventType klassen uit de
 * meegeleverde jar (HotelEventsObs.jar) — geen nep-objecten nodig,
 * want HotelEvent is een simpele data-klasse met een normale constructor:
 *
 *   new HotelEvent(time, eventType, guestId, data)
 *
 * Voor Simulation gebruiken we een kleine subclass (FakeSimulation)
 * die de zware methodes overschrijft, zodat we niet het hele
 * hotel-grid hoeven te laden om de handlers te testen.
 *
 * Elke test volgt dezelfde 3 stappen:
 *   ARRANGE – objecten klaarzetten
 *   ACT     – de methode aanroepen die we testen
 *   ASSERT  – controleren of het resultaat klopt
 */
public class ListenerAndHandlerTest {

    /**
     * Kleine subclass van Simulation.
     * We overschrijven alleen de methodes die de handlers aanroepen,
     * zodat we kunnen controleren of ze aangeroepen worden.
     */
    static class FakeSimulation extends Simulation {
        String lastCalledMethod = "";
        int lastGuestId = -1;

        @Override
        public void checkIn(int guestId, int classification) {
            lastCalledMethod = "checkIn";
            lastGuestId = guestId;
        }

        @Override
        public void checkOut(int guestId) {
            lastCalledMethod = "checkOut";
            lastGuestId = guestId;
        }

        @Override
        public void evacuateEmergency(int guestId) {
            lastCalledMethod = "evacuateEmergency";
            lastGuestId = guestId;
        }
    }

    private FakeSimulation fakeSimulation;

    @BeforeEach
    void setUp() {
        // Verse FakeSimulation voor elke test, zodat lastCalledMethod altijd leeg begint
        fakeSimulation = new FakeSimulation();
    }

    // ================================================================
    // CheckInEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor CHECK_IN en false voor andere types.
     */
    // >>> LOGICA TEST — test de canHandle() beslissingslogica (accepteert eigen type, weigert ander type)
    @Test
    void checkInHandler_recognizesOnlyCheckInEvents() {
        // ARRANGE
        CheckInEventHandler handler = new CheckInEventHandler(fakeSimulation);
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
     * handleEvent moet simulation.checkIn() aanroepen met het juiste guestId.
     */
    // >>> LOGICA TEST — test of handleEvent() de juiste methode + juiste guestId doorgeeft
    @Test
    void checkInHandler_callsCheckInWithCorrectGuestId() {
        // ARRANGE
        CheckInEventHandler handler = new CheckInEventHandler(fakeSimulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_IN, 42, 1);

        // ACT
        handler.handleEvent(event);

        // ASSERT
        assertEquals("checkIn", fakeSimulation.lastCalledMethod);
        assertEquals(42, fakeSimulation.lastGuestId);
    }

    // ================================================================
    // CheckOutEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor CHECK_OUT.
     */
    // >>> SIMPELE TEST — checkt alleen 1 kant van canHandle() (geen "weigert ander type")
    @Test
    void checkOutHandler_recognizesCheckOutEvent() {
        // ARRANGE
        CheckOutEventHandler handler = new CheckOutEventHandler(fakeSimulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_OUT, 1, 0);

        // ACT
        boolean canHandle = handler.canHandle(event);

        // ASSERT
        assertTrue(canHandle);
    }

    /**
     * handleEvent moet simulation.checkOut() aanroepen met het juiste guestId.
     */
    // >>> LOGICA TEST — test of handleEvent() de juiste methode + juiste guestId doorgeeft
    @Test
    void checkOutHandler_callsCheckOutWithCorrectGuestId() {
        // ARRANGE
        CheckOutEventHandler handler = new CheckOutEventHandler(fakeSimulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_OUT, 7, 0);

        // ACT
        handler.handleEvent(event);

        // ASSERT
        assertEquals("checkOut", fakeSimulation.lastCalledMethod);
        assertEquals(7, fakeSimulation.lastGuestId);
    }

    // ================================================================
    // evacuateEmergencyEventHandler
    // ================================================================

    /**
     * canHandle moet true zijn voor EVACUATE.
     */
    // >>> SIMPELE TEST — checkt alleen 1 kant van canHandle() (geen "weigert ander type")
    @Test
    void evacuateHandler_recognizesEvacuateEvent() {
        // ARRANGE
        evacuateEmergencyEventHandler handler = new evacuateEmergencyEventHandler(fakeSimulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.EVACUATE, 1, 0);

        // ACT
        boolean canHandle = handler.canHandle(event);

        // ASSERT
        assertTrue(canHandle);
    }

    /**
     * handleEvent moet simulation.evacuateEmergency() aanroepen met het juiste guestId.
     */
    // >>> LOGICA TEST — test of handleEvent() de juiste methode + juiste guestId doorgeeft
    @Test
    void evacuateHandler_callsEvacuateWithCorrectGuestId() {
        // ARRANGE
        evacuateEmergencyEventHandler handler = new evacuateEmergencyEventHandler(fakeSimulation);
        HotelEvent event = new HotelEvent(0, HotelEventType.EVACUATE, 99, 0);

        // ACT
        handler.handleEvent(event);

        // ASSERT
        assertEquals("evacuateEmergency", fakeSimulation.lastCalledMethod);
        assertEquals(99, fakeSimulation.lastGuestId);
    }

    // ================================================================
    // SimulationEventListener
    // ================================================================

    /**
     * De listener moet een CHECK_IN event doorsturen naar CheckInEventHandler,
     * en niet naar CheckOutEventHandler.
     */
    // >>> LOGICA TEST — test de Chain of Responsibility routing
    @Test
    void listener_routesEventToCorrectHandler() {
        // ARRANGE
        SimulationEventListener listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(fakeSimulation),
                new CheckOutEventHandler(fakeSimulation)
        ));
        HotelEvent event = new HotelEvent(0, HotelEventType.CHECK_IN, 10, 1);

        // ACT
        listener.notify(event);

        // ASSERT
        assertEquals("checkIn", fakeSimulation.lastCalledMethod,
                "Listener moet CHECK_IN naar CheckInEventHandler sturen");
    }

    /**
     * Een eventType waarvoor geen handler bestaat (bv. NONE) mag
     * geen enkele methode aanroepen — de listener mag niet crashen.
     */
    // >>> LOGICA TEST — edge case: geen handler past, mag niet crashen
    @Test
    void listener_doesNothingWhenNoHandlerMatches() {
        // ARRANGE
        SimulationEventListener listener = new SimulationEventListener(List.of(
                new CheckInEventHandler(fakeSimulation),
                new CheckOutEventHandler(fakeSimulation)
        ));
        HotelEvent event = new HotelEvent(0, HotelEventType.NONE, 1, 0);

        // ACT
        listener.notify(event);

        // ASSERT
        assertEquals("", fakeSimulation.lastCalledMethod,
                "Geen handler past op NONE, dus mag niks aangeroepen worden");
    }
}