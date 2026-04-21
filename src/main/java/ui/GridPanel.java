package ui;

import model.Grid;
import model.Tile;
import simulation.Simulation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GridPanel extends JPanel {
    private final Simulation simulation;
    private static final int TILE_SIZE = 40;

    public GridPanel(Simulation simulation) {
        this.simulation = simulation;

        Grid grid = simulation.getGrid();
        int size = grid.getSize();

        setLayout(null);
        setPreferredSize(new Dimension(size * TILE_SIZE, size * TILE_SIZE));

        rebuildGrid();
    }

    public void rebuildGrid() {
        removeAll();

        Grid grid = simulation.getGrid();
        if (grid == null) {
            revalidate();
            repaint();
            return;
        }

        int size = grid.getSize();
        setPreferredSize(new Dimension(size * TILE_SIZE, size * TILE_SIZE));

        createGrid();

        revalidate();
        repaint();
    }

    private void createGrid() {
        Grid grid = simulation.getGrid();
        int size = grid.getSize();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Tile tile = grid.getTile(y, x);

                if(tile.getArea() == null) {
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
    }
}