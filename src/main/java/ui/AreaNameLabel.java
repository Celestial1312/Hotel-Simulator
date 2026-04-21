package ui;

import model.Area;

import javax.swing.*;
import java.awt.*;

public class AreaNameLabel extends JLabel {
    private final Area area;

    public AreaNameLabel(Area area, int tileSize) {
        this.area = area;

        setText(area.getAreaType());
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
    }

}
