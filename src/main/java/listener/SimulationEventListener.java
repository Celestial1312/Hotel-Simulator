package listener;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import simulation.Simulation;

import static hotelevents.HotelEventType.CHECK_OUT;

public class SimulationEventListener implements HotelEventListener {
    private final Simulation simulation;

    public SimulationEventListener(Simulation simulation) {
        this.simulation = simulation;
    }
    @Override
    public void notify(HotelEvent hotelEvent) {
        System.out.println("event ontvangen " +  hotelEvent.getEventType() + " guestId="+ hotelEvent.getGuestId());

        switch(hotelEvent.getEventType()) {
            case CHECK_IN -> simulation.handleCheckIn(hotelEvent.getGuestId());
            case CHECK_OUT -> simulation.handleCheckOut(hotelEvent.getGuestId());
            default -> {
            }
        }
    }
}
