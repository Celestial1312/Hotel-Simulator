package controller;

import java.io.File;
import java.util.HashMap;
import model.Guest;
import simulation.Simulation;

// Controller van de simulator
// Deze class verbindt de GUI met de Simulation class
public class SimulatorController {

    // Referentie naar de simulatie
    private final Simulation simulation;

    public SimulatorController(Simulation simulation) {
        this.simulation = simulation;
    }

    // Laadt een hotel layout vanuit een JSON bestand sos.4
    public void loadLayout(File file) {
        simulation.loadGridFromJsonFile(file);
    }

    // Start een scenario sos.5
    public void startScenario(int scenarioId) {
        simulation.startScenario(scenarioId);
    }

    // Pauzeert of hervat de simulatie
    public void togglePauseScenario() {
        simulation.togglePauseScenario();
    }

    public void stopScenario() {
        simulation.stopScenario();
    }

    // Kijkt of de simulatie gepauzeerd is
    public boolean isPaused() {
        return simulation.isPaused();
    }

    // Geeft huidige HTE waarde terug
    public int getHte() {
        return simulation.getSettings().getHte();
    }

    // Verandert HTE snelheid
    public void setHte(int hte) {
        simulation.getSettings().setHte(hte);
        simulation.refreshHte();
    }
    
    // Geeft alle gasten terug
    public HashMap<Integer, Guest> getGuests() {
        return simulation.getGuests();
    }
}
