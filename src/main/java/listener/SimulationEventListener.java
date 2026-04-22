package listener;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import simulation.Simulation;

public class SimulationEventListener implements HotelEventListener {
    private final Simulation simulation;

    public SimulationEventListener(Simulation simulation) {
        this.simulation = simulation;
    }
    @Override
    public void notify(HotelEvent hotelEvent) {
        System.out.println("event ontvangen " +  hotelEvent.getEventType());

        switch(hotelEvent.getEventType()) {
            case CHECK_IN -> simulation.handleCheckIn(hotelEvent.getGuestId());
            default -> {
            }
        }
    }
}
