package handler;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class NeedFoodEventHandler implements SimulationEventHandler {
    private final Simulation simulation;

    public NeedFoodEventHandler(Simulation simulation){
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("NEED_FOOD");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.needFood(event.getGuestId());
    }
    
}
