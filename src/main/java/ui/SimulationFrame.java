package ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simulation.Simulation;

public class SimulationFrame extends JFrame {

    private final Simulation simulation;
    private GridPanel gridPanel;
    private SidebarPanel sidebarPanel;

    public SimulationFrame(Simulation simulation) {
        this.simulation = simulation;

        setTitle("Hotel-Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        gridPanel = new GridPanel(simulation);
        sidebarPanel = new SidebarPanel(simulation.getController());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        
        centerPanel.add(gridPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.add(sidebarPanel);
        add(sidePanel, BorderLayout.WEST);

        setVisible(true);
    }

    public void refreshGrid() {
        gridPanel.updatePreferredSize();
        gridPanel.updateLobbyRectangle();
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}