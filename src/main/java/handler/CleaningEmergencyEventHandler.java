package handler;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class CleaningEmergencyEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public CleaningEmergencyEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("cleaning_emergency");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.cleaningEmergency(event.getGuestId());
    }

}