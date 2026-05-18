package ui;

import model.Guest;
import model.Cleaner;
import model.SubTile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SubTileLabel extends JLabel {
    private final SubTile subTile;

    public SubTileLabel(SubTile subTile, int y, int x, int width, int height) {
        this.subTile = subTile;

        setLayout(null);
        setOpaque(false);
        // setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(subTile.getPerson() instanceof Guest) {
            g.setColor(Color.BLUE);
            g.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        }
        if(subTile.getPerson() instanceof Cleaner) {
            g.setColor(Color.GREEN);
            g.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
        }
    }
}