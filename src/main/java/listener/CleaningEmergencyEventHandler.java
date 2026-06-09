package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Handler voor schoonmaak noodgevallen
public class CleaningEmergencyEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public CleaningEmergencyEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    // Controleert of dit een CLEANING_EMERGENCY event is
    @Override
    public boolean canHandle(HotelEvent event) {

        return event.getEventType()
                .toString()
                .equalsIgnoreCase("cleaning_emergency");
    }

    // Verwerkt het event
    @Override
    public void handleEvent(HotelEvent event) {

        // Start schoonmaak noodgeval
        simulation.cleaningEmergency(event.getGuestId());
    }
}