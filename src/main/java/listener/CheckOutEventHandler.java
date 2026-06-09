package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Deze handler verwerkt CHECK_OUT events
public class CheckOutEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public CheckOutEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    // Controleert of het event een CHECK_OUT is
    @Override
    public boolean canHandle(HotelEvent event) {

        // IgnoreCase maakt hoofdletters niet belangrijk
        return event.getEventType()
                .toString()
                .equalsIgnoreCase("CHECK_OUT");
    }

    // Verwerkt het CHECK_OUT event
    @Override
    public void handleEvent(HotelEvent event) {

        // Gast uitchecken
        simulation.checkOut(event.getGuestId());
    }
}