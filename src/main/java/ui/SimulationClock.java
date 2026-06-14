package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.Timer;

public class SimulationClock extends JLabel {
    private int hte;
    private int elapsedSimMillis = 0;
    private final Timer simTimer;

    public SimulationClock(int hte) {
        this.hte = hte;

        setText("Simulation Time: 00:00:00.000");
        setFont(new Font("Arial", Font.BOLD, 20));
        setForeground(Color.BLUE);

        simTimer = new Timer(this.hte, e -> {
            elapsedSimMillis += this.hte;

            int hours = elapsedSimMillis / 3600000;
            int minutes = (elapsedSimMillis % 3600000) / 60000;
            int seconds = (elapsedSimMillis % 60000) / 1000;
            int millis = elapsedSimMillis % 1000;

            String time = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
            setText("Simulation Time: " + time);
        });
    }

    public void start() {
        simTimer.start();
    }

    public void stop() {
        simTimer.stop();
    }

    public void reset() {
        simTimer.stop();
        elapsedSimMillis = 0;
        setText("Simulation Time: 00:00:00.000");
    }

    public void setHte(int hte) {
        this.hte = hte;
        simTimer.setDelay(hte);
        simTimer.setInitialDelay(hte);
    }
}
