package ui;

import model.Area;

import javax.swing.*;
import java.awt.*;

public class AreaNameLabel extends JLabel {
    private final Area area;
// co
    public AreaNameLabel(Area area, int tileSize) {
        this.area = area;

        setText(buildLabelText(area));
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setForeground(Color.BLACK);
        setOpaque(false);
        setBounds(
                0,
                0,
                area.getWidth() * tileSize,
                area.getHeight() * tileSize
        );

                setToolTipText(
                "Type: " + area.getAreaType() +
                        ", x=" + area.getX() +
                        ", y=" + area.getY() +
                        ", width=" + area.getWidth() +
                        ", height=" + area.getHeight() +
                        ", guest=" + (area.getGuest() == null ? "none" : area.getGuest().getId())
        );

    }
    // Bouwt de tekst die op de area wordt weergegeven.
    // Voor kamers wordt de sterrenclassificatie toegevoegd (sterrensysteem),
    // bijvoorbeeld "Room ★★★"
    private String buildLabelText(Area area) {
        if (isRoom(area)) {
            return area.getAreaType() + " " + buildStars(area.getClassification());
        }

        return area.getAreaType();
    }

    private boolean isRoom(Area area) {
        return "Room".equalsIgnoreCase(area.getAreaType());
    }

    // Zet een classificatie (1-6) om naar een reeks sterretjes, bijv. 3 -> "★★★"
    private String buildStars(int classification) {
        return "★".repeat(Math.max(classification, 0));
    }

}


