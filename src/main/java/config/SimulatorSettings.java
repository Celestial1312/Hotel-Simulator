package config;

public class SimulatorSettings {

    // HTE = Hotel Time Event snelheid
    // Dit bepaalt hoe snel de simulatie loopt
    private int hte;

    public SimulatorSettings() {
        this.hte = 500;
    }

    // Geeft huidige HTE terug
    public int getHte() {
        return hte;
    }

    // Verandert de snelheid van de simulatie
    public void setHte(int hte) {
        this.hte = hte;
    }
}