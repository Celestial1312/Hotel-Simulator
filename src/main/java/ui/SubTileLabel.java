package ui;

import model.SubTile;
import model.Tile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SubTileLabel extends JLabel {
    private final SubTile subTile;

    public SubTileLabel(SubTile subTile, int y, int x, int width, int height) {
        this.subTile = subTile;

        setLayout(null);
        setOpaque(false);
        setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(subTile.getGuest() != null) {
            g.setColor(Color.BLUE);
            g.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        }
    }
}
