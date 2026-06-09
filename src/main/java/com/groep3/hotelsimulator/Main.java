package com.groep3.hotelsimulator;

import javax.swing.SwingUtilities;

import simulation.Simulation;

// Main class van de applicatie
// Hier start het hele hotel simulatieprogramma
public class Main {

    // Eerste methode die uitgevoerd wordt wanneer het programma start
    public static void main(String[] args) {

        // invokeLater zorgt ervoor dat de GUI veilig wordt gestart
        // op de Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // Nieuwe simulatie aanmaken
            Simulation simulation = new Simulation();

            // Applicatie starten
            simulation.startApplication();
        });
    }
}