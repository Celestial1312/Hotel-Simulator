package handler;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import ui.GridPanel;
import ui.SimulationOverViewFrame;

import simulation.Simulation;

public class GridMouseEventHandler implements MouseInputListener {

    private final GridPanel gridPanel;
    private final Simulation simulation;

    public GridMouseEventHandler(GridPanel gridPanel, Simulation simulation) {
        this.gridPanel = gridPanel;
        this.simulation = simulation;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gridPanel.getLobbyRectangle().contains(e.getPoint())) {
            SimulationOverViewFrame simulationOverViewFrame = new SimulationOverViewFrame(simulation);
            simulationOverViewFrame.setVisible(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
