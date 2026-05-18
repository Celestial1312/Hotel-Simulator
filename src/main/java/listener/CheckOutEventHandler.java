package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class CheckOutEventHandler implements SimulationEventHandler{
    private final Simulation simulation;

    public CheckOutEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("CHECK_OUT");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.checkOut(event.getGuestId());
    }
    
}
