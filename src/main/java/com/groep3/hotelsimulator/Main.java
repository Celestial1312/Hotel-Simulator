package com.groep3.hotelsimulator;

import simulation.Simulation;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Simulation simulation = new Simulation();
            simulation.startApplication();
        });
    }
}


/* package com.groep3.hotelsimulator;

import simulation.Simulation;
import ui.SimulationFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Simulation simulation = new Simulation();

            // ✅ THIS is what starts your program
            new SimulationFrame(simulation);
        });
    }
}*/