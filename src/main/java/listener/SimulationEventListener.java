package listener;

import java.util.List;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;

// Deze class luistert naar alle events van de HotelEventManager
public class SimulationEventListener implements HotelEventListener {

    // Lijst met alle beschikbare handlers
    private final List<SimulationEventHandler> handlers;

    // Constructor
    public SimulationEventListener(List<SimulationEventHandler> handlers) {

        // Handlers opslaan
        this.handlers = handlers;
    }

    // Wordt uitgevoerd wanneer een event ontvangen wordt
    @Override
    public void notify(HotelEvent hotelEvent) { // context class

        // Debug informatie tonen in console
        System.out.println(
                "event ontvangen "
                        + "time=" + hotelEvent.getTime() + " "
                        + "type=" + hotelEvent.getEventType() + " "
                        + "data=" + hotelEvent.getData() + " "
                        + "guestId=" + hotelEvent.getGuestId()
        );

        // Door alle handlers lopen
        for (SimulationEventHandler handler : handlers) {

            // Controleert welke handler geschikt is
            if (handler.canHandle(hotelEvent)) {

                try {

                    // Event uitvoeren
                    handler.handleEvent(hotelEvent);

                } catch (Exception e) {

                    // Eventuele fouten tonen
                    e.printStackTrace();
                }

                // Stoppen nadat juiste handler gevonden is
                return;
            }
        }
    }
}