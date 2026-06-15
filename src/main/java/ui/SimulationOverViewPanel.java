package ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import simulation.Simulation;

import model.Guest;

public class SimulationOverViewPanel extends JPanel {
    private final Simulation simulation;
    private HashMap<Integer, Guest> guests = new HashMap<>();

    public SimulationOverViewPanel(Simulation simulation) {
        this.simulation = simulation;

        setSize(400, 400);

        showSimulationData(simulation);
    }

    private void showSimulationData(Simulation simulation) {
        guests = simulation.getGuests();

        for (Map.Entry<Integer, Guest> entry : guests.entrySet()) {

            Integer guestId = entry.getKey();

            JLabel dataLabel = new JLabel();
            dataLabel.setText("Guest = " + guestId.toString());

            add(dataLabel);
            setVisible(true);
        }
    }
}
