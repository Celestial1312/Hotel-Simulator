package ui;

import java.util.HashMap;

import javax.swing.JFrame;

import simulation.Simulation;

public class SimulationOverViewFrame extends JFrame {
    private final Simulation simulation;

    public SimulationOverViewFrame(Simulation simulation) {
        this.simulation = simulation;

        setTitle("Simulation Overview");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new SimulationOverViewPanel(simulation));
        setVisible(true);
    }
}