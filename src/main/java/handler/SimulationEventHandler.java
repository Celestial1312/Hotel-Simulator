package handler;

import hotelevents.HotelEvent;

// Interface voor alle event handlers
public interface SimulationEventHandler {

    // Controleert of de handler het event kan verwerken
    boolean canHandle(HotelEvent event);

    // Verwerkt het event
    void handleEvent(HotelEvent event);
}