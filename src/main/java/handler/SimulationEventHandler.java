package handler;

import hotelevents.HotelEvent;

public interface SimulationEventHandler {
    boolean canHandle(HotelEvent event);
    
    void handleEvent(HotelEvent event);
}