package config;

// Deze class bevat instellingen van de simulator
public class SimulatorSettings {

    // HTE = Hotel Time Event snelheid
    // Dit bepaalt hoe snel de simulatie loopt
    private int hte;

    // Constructor
    public SimulatorSettings() {

        // Standaard snelheid instellen op 1000 milliseconden
        this.hte = 1000;
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