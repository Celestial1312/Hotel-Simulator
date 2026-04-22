package com.groep3.hotelsimulator;

import javax.swing.SwingUtilities;

import simulation.Simulation;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Simulation simulation = new Simulation();
            simulation.startApplication();
        });
    }
}