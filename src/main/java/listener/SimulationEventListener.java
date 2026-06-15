package listener;

import java.util.List;

import handler.SimulationEventHandler;
import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;

// Deze class luistert naar alle events van de HotelEventManager
public class SimulationEventListener implements HotelEventListener {

    private final List<SimulationEventHandler> handlers;

    public SimulationEventListener(List<SimulationEventHandler> handlers) {
        this.handlers = handlers;
    }

    // Wordt uitgevoerd wanneer een event ontvangen wordt
    @Override
    public void notify(HotelEvent hotelEvent) {

        // Debug informatie tonen in console
        System.out.println(
                "event ontvangen "
                        + "time=" + hotelEvent.getTime() + " "
                        + "type=" + hotelEvent.getEventType() + " "
                        + "data=" + hotelEvent.getData() + " "
                        + "guestId=" + hotelEvent.getGuestId()
        );

        // Door alle handlers lopen en controleert welke handler geschikt is 
        for (SimulationEventHandler handler : handlers) {
            if (handler.canHandle(hotelEvent)) {
                handler.handleEvent(hotelEvent);
            }
        }
    }
}