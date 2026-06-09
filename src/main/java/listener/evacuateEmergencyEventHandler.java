package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;
public class evacuateEmergencyEventHandler implements SimulationEventHandler {

    private final Simulation simulation;

    public evacuateEmergencyEventHandler(Simulation simulation) {

        this.simulation = simulation;
    }



    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType()
                .toString()
                .equalsIgnoreCase("EVACUATE");

    }


    @Override
    public void handleEvent(HotelEvent event) {

        simulation.evacuateEmergency(event.getGuestId());
    }
}

