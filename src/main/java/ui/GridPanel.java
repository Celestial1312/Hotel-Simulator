package ui;

import controller.SimulatorController;
import model.Area;
import model.Grid;
import model.Tile;
import simulation.Simulation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GridPanel extends JPanel {
    private final Simulation simulation;
    private static final int TILE_SIZE = 80;
    private int sizeX = 0;
    private int sizeY = 0;
    public GridPanel(Simulation simulation) {
        this.simulation = simulation;

        Grid grid = simulation.getGrid();
        if(grid != null) {
            sizeX = grid.getSizeX();
            sizeY = grid.getSizeY();
        }

        setLayout(null);
        setPreferredSize(new Dimension(sizeX * TILE_SIZE, sizeY * TILE_SIZE));

        if(grid != null) {
            createGrid();
        }
    }

    public void createGrid() {
        removeAll();

        Grid grid = simulation.getGrid();
        if (grid == null) {
            revalidate();
            repaint();
            return;
        }

        setPreferredSize(new Dimension(
                grid.getSizeX() * TILE_SIZE,
                grid.getSizeY() * TILE_SIZE
        ));

        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);

                if(tile.getArea() == null) {
                    continue;
                }

                Area area = tile.getArea();

                JLabel areaLabel = new AreaLabel(area, grid, TILE_SIZE);
                add(areaLabel);
            }
        }
        revalidate();
        repaint();
    }
}