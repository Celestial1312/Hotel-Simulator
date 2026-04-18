package ui;

import model.Grid;
import model.Tile;
import simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {

    private final Simulation simulation;

    private static final int TILE_SIZE = 40;

    public GridPanel(Simulation simulation) {
        this.simulation = simulation;

        setLayout(null);

        // ❌ DO NOT access grid here
        setPreferredSize(new Dimension(400, 400));
    }

    public void rebuildGrid() {

        removeAll();

        Grid grid = simulation.getGrid();

        // ✅ SAFE CHECK (VERY IMPORTANT)
        if (grid == null) {
            setPreferredSize(new Dimension(400, 400));
            revalidate();
            repaint();
            return;
        }

        int size = grid.getSize();

        setPreferredSize(new Dimension(
                size * TILE_SIZE,
                size * TILE_SIZE
        ));

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                Tile tile = grid.getTile(y, x);

                if (tile == null || tile.getArea() == null) {
                    continue;
                }

                TileLabel label = new TileLabel(tile);

                label.setBounds(
                        x * TILE_SIZE,
                        y * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );

                add(label);
            }
        }

        revalidate();
        repaint();
    }
}