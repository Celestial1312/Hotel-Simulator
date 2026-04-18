package ui;

import simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame implements ActionListener {

    private final Simulation simulation;
    private GridPanel gridPanel;
    private SidebarPanel sidebarPanel;

    public SimulationFrame(Simulation simulation) {
        this.simulation = simulation;

        setTitle("Hotel-Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        gridPanel = new GridPanel(simulation);
        sidebarPanel = new SidebarPanel(simulation);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gridPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.add(sidebarPanel);
        add(sidePanel, BorderLayout.WEST);


        setVisible(true);
    }

    public void refreshGrid() {
        gridPanel.rebuildGrid();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}



/* package ui;

import controller.SimulatorController;
import simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {

    private final SimulatorController controller;

    public SimulationFrame(Simulation simulation) {

        this.controller = new SimulatorController(simulation);

        setTitle("Hotel-Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        GridPanel gridPanel = new GridPanel(simulation);

        SidebarPanel sidebarPanel = new SidebarPanel(this, controller, simulation);

        add(gridPanel, BorderLayout.CENTER);
        add(sidebarPanel, BorderLayout.WEST);

        setVisible(true);
    }
} */