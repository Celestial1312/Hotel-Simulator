package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Handler voor gasten die naar de bioscoop gaan
public class GoToCinemaEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public GoToCinemaEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    // Controleert of dit een GOTO_CINEMA event is
    @Override
    public boolean canHandle(HotelEvent event) {

        return event.getEventType()
                .toString()
                .equalsIgnoreCase("GOTO_CINEMA");
    }

    // Verwerkt het bioscoop event
    @Override
    public void handleEvent(HotelEvent event) {

        // Gast naar bioscoop sturen
        simulation.goToCinema(event.getGuestId());
    }
}