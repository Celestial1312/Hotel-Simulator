package ui;

import model.Area;
import model.Grid;
import model.Tile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AreaLabel extends JLabel {
    private final Area area;
    private final Grid grid;

    public AreaLabel(Area area, Grid grid, int tileSize) {
        this.area = area;
        this.grid = grid;

        setLayout(null);
        setBorder(new LineBorder(Color.BLACK));
        setOpaque(true);
        setForeground(Color.BLACK);
        setBackground(getColorForArea(area.getAreaType()));

        setBounds(
                area.getX() * tileSize,
                area.getY() * tileSize,
                area.getWidth() * tileSize,
                area.getHeight() * tileSize
        );
        addTiles(tileSize);
        addAreaNames(tileSize);
    }

    private void addTiles(int tileSize) {
        for (int tileY = 0; tileY < area.getHeight(); tileY++) {
            for (int tileX = 0; tileX < area.getWidth(); tileX++) {
                Tile tile = grid.getTile(area.getX() + tileX, tileY + area.getY());

                TileLabel tileLabel = new  TileLabel(tile, tileX * tileSize, tileY * tileSize, tileSize);
                add(tileLabel);
            }

        }
    }

    private void addAreaNames(int tileSize) {
        AreaNameLabel areaNameLabel = new AreaNameLabel(area, tileSize);
        add(areaNameLabel);
    }

    private Color getColorForArea(String areaType) {
        return switch (areaType) {
            case "Lobby" -> Color.MAGENTA;
            case "Lift" -> Color.RED;
            case "Stairs" -> Color.YELLOW;
            case "Cinema" -> Color.ORANGE;
            case "Restaurant" -> Color.GREEN;
            case "Fitness" -> Color.CYAN;
            case "Room" -> Color.LIGHT_GRAY;
            default -> Color.WHITE;
        };
    }
}
