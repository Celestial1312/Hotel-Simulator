package com.groep3.hotelsimulator;

import simulation.Simulation;

import javax.swing.*;

public class Main {

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Simulation simulation = new Simulation();
            simulation.start();
        });
    }
}