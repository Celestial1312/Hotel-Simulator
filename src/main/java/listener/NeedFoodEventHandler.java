package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Handler voor gasten die eten nodig hebben
public class NeedFoodEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public NeedFoodEventHandler(Simulation simulation) {

        // Simulatie opslaan
        this.simulation = simulation;
    }

    // Controleert of het event NEED_FOOD is
    @Override
    public boolean canHandle(HotelEvent event) {

        return event.getEventType()
                .toString()
                .equalsIgnoreCase("NEED_FOOD");
    }

    // Verwerkt het food event
    @Override
    public void handleEvent(HotelEvent event) {

        // Gast eten geven
        simulation.needFood(event.getGuestId());
    }
}