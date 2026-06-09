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

    // Constructor
    public SimulatorController(Simulation simulation) {
        this.simulation = simulation;
    }

    // Laadt een hotel layout vanuit een JSON bestand
    public void loadLayout(File file) {

        // Stuurt bestand door naar de simulatie
        simulation.loadGridFromJsonFile(file);
    }

    // Start een scenario
    public void startScenario(int scenarioId) {

        // Scenario starten in simulatie
        simulation.startScenario(scenarioId);
    }

    // Pauzeert of hervat de simulatie
    public void togglePauseScenario() {

        // Wisselt tussen pause en resume
        simulation.togglePauseScenario();
    }

    // Controleert of simulatie gepauzeerd is
    public boolean isPaused() {

        // Geeft status terug
        return simulation.isPaused();
    }

    // Geeft huidige HTE waarde terug
    public int getHte() {

        // HTE ophalen uit settings
        return simulation.getSettings().getHte();
    }

    // Verandert HTE snelheid
    public void setHte(int hte) {

        // Nieuwe snelheid instellen
        simulation.getSettings().setHte(hte);
    }

    // Geeft alle gasten terug
    public HashMap<Integer, Guest> getGuests() {

        // HashMap teruggeven met alle gasten
        return simulation.getGuests();
    }
}