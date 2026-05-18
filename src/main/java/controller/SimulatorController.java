package controller;

import java.io.File;
import java.util.HashMap;

import model.Guest;
import simulation.Simulation;

public class SimulatorController {
    private final Simulation simulation;

    public SimulatorController(Simulation simulation) {
        this.simulation = simulation;
    }

    public void loadLayout(File file) {
        simulation.loadGridFromJsonFile(file);
    }

    public void startScenario(int scenarioId) {
        simulation.startScenario(scenarioId);
    }

    public void pauseScenario() {
        simulation.pauseScenario();
    }

    public int getHte() {
        return simulation.getSettings().getHte();
    }

    public void setHte(int hte) {
        simulation.getSettings().setHte(hte);
    }
    public HashMap<Integer, Guest> getGuests() {
        return simulation.getGuests();
    }
}

