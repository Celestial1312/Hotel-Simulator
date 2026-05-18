package listener;

import java.util.List;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;

public class SimulationEventListener implements HotelEventListener {
    private final List<SimulationEventHandler> handlers;

    public SimulationEventListener(List<SimulationEventHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        System.out.println("event ontvangen "
                + "time=" + hotelEvent.getTime() + " "
                + "type=" + hotelEvent.getEventType() + " "
                + "data=" + hotelEvent.getData() + " "
                + "guestId=" + hotelEvent.getGuestId());

        for (SimulationEventHandler handler : handlers) {
            if (handler.canHandle(hotelEvent)) {
                try {
                    handler.handleEvent(hotelEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}