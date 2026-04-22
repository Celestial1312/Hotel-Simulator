package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.Timer;

public class RealTimeClock extends JLabel{
    private final Timer realTimeTimer;

    public RealTimeClock() {

        setText("Current Time: 00:00:00");
        setFont(new Font("Arial", Font.BOLD, 24));
        setForeground(Color.BLACK);

        realTimeTimer = new Timer(1000, e -> {
            java.time.LocalTime now = java.time.LocalTime.now();
            String time = String.format("%02d:%02d:%02d",
                    now.getHour(),
                    now.getMinute(),
                    now.getSecond());
            setText("Current Time: " + time);
        });

        realTimeTimer.start();
    }
}
