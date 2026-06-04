package handler;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class GoToCinemaEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public GoToCinemaEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("GOTO_CINEMA");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.goToCinema(event.getGuestId());
    }
    
}
