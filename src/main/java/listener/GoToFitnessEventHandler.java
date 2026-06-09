package listener;

import hotelevents.HotelEvent;
import simulation.Simulation;

// Handler voor gasten die naar de fitness gaan
public class GoToFitnessEventHandler implements SimulationEventHandler {

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Constructor
    public GoToFitnessEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    // Controleert of het event GOTO_FITNESS is
    @Override
    public boolean canHandle(HotelEvent event) {

        return event.getEventType()
                .toString()
                .equalsIgnoreCase("GOTO_FITNESS");
    }

    // Verwerkt het fitness event
    @Override
    public void handleEvent(HotelEvent event) {

        // Gast naar fitness sturen
        simulation.goToFitness(event.getGuestId());
    }
}