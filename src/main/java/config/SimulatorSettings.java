package config;

// Deze class bevat instellingen van de simulator sr
public class SimulatorSettings {

    // HTE = Hotel Time Event snelheid
    // Dit bepaalt hoe snel de simulatie loopt
    private int hte;

    // Constructor
    public SimulatorSettings() {

        // Standaard snelheid instellen op 1000 milliseconden co /و
        this.hte = 100; // co
    }

    // Geeft huidige HTE terug / كابسو
    public int getHte() {
        return hte;
    }

    // Verandert de snelheid van de simulatie
    public void setHte(int hte) {
        this.hte = hte;
    }
}