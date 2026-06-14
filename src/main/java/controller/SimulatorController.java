package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Guest;
import simulation.Simulation;

// Controller van de simulator
// Deze class verbindt de GUI met de Simulation class
public class SimulatorController { // sr

    // Referentie naar de simulatie
    private final Simulation simulation;

    // Co
    public SimulatorController(Simulation simulation) {
        this.simulation = simulation;
    }

    // Laadt een hotel layout vanuit een JSON bestand sos.4
    public void loadLayout(File file) {

        // Stuurt bestand door naar de simulatie
        simulation.loadGridFromJsonFile(file);
    }

    // Start een scenario sos.5
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

    // Evacueert alle gasten naar de lobby
    public void evacuate() {
        simulation.evacuateEmergency(-1);
    }

    // Stuurt een willekeurige gast naar de cinema
    public void goToCinema() {
        simulation.goToCinema(getRandomGuestId());
    }

    // Stuurt een willekeurige gast naar de fitness
    public void goToFitness() {
        simulation.goToFitness(getRandomGuestId());
    }

    // Stuurt een willekeurige gast naar het restaurant (food)
    public void needFood() {
        simulation.needFood(getRandomGuestId());
    }

    // Kiest een willekeurige gast-ID uit alle huidige gasten
    // Geeft -1 terug als er geen gasten zijn
    private int getRandomGuestId() {
        List<Integer> ids = new ArrayList<>(simulation.getGuests().keySet());

        if (ids.isEmpty()) {
            return -1;
        }

        return ids.get((int) (Math.random() * ids.size()));
    }
}