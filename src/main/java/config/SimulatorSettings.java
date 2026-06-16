package config;

public class SimulatorSettings {

    private int hte;
    private int scenarioId;

    public SimulatorSettings() {
        this.hte = 1000;
        this.scenarioId = 1;
    }

    public int getHte() {
        return hte;
    }

    public void setHte(int hte) {
        this.hte = hte;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }
}