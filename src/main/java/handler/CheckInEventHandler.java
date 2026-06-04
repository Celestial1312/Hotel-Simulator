package handler;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class CheckInEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public CheckInEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equals("CHECK_IN");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.checkIn(event.getGuestId(), event.getData());
    }
    
}