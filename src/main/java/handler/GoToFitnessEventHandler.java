package handler;

import hotelevents.HotelEvent;
import simulation.Simulation;

public class GoToFitnessEventHandler implements SimulationEventHandler{
    private final Simulation simulation;

    public GoToFitnessEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("GOTO_FITNESS");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        simulation.goToFitness(event.getGuestId());
    }
    
}
