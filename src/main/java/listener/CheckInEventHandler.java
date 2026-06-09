package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Deze handler verwerkt CHECK_IN events
public class CheckInEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public CheckInEventHandler(Simulation simulation) {

        // Simulatie opslaan
        this.simulation = simulation;
    }

    // Controleert of dit een CHECK_IN event is
    @Override
    public boolean canHandle(HotelEvent event) {

        // Alleen CHECK_IN verwerken
        return event.getEventType().toString().equals("CHECK_IN");
    }

    // Verwerkt het CHECK_IN event
    @Override
    public void handleEvent(HotelEvent event) {

        // Gast laten inchecken
        simulation.checkIn(
                event.getGuestId(),
                event.getData()
        );
    }
}