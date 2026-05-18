package controller;

import java.io.File;

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

    public void togglePauseScenario() {
        simulation.togglePauseScenario();
    }

    public boolean isPaused() {
        return simulation.isPaused();
    }
    
    public int getHte() {
        return simulation.getSettings().getHte();
    }

    public void setHte(int hte) {
        simulation.getSettings().setHte(hte);
    }
}

