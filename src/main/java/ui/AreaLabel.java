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
        setBackground(getColorForArea(area));

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

    // Bepaalt de achtergrondkleur van een area.
    // Voor kamers wordt de kleur bepaald door de sterrenclassificatie (sterrensysteem):
    // hogere classificatie -> rijkere/goudere kleur, lagere classificatie -> grijzer.
    private Color getColorForArea(Area area) {
        if ("Room".equalsIgnoreCase(area.getAreaType())) {
            return getColorForStarRating(area.getClassification());
        }

        return switch (area.getAreaType()) {
            case "Lobby" -> Color.MAGENTA;
            case "Lift" -> Color.RED;
            case "Stairs" -> Color.YELLOW;
            case "Cinema" -> Color.ORANGE;
            case "Restaurant" -> Color.GREEN;
            case "Fitness" -> Color.CYAN;
            default -> Color.WHITE;
        };
    }

    // Sterrensysteem: vertaalt een classificatie (1-6 sterren) naar een kleur.
    // Lagere sterren = lichtgrijs (eenvoudige kamer)
    // Hogere sterren = goud (luxe kamer)
    private Color getColorForStarRating(int stars) {
        return switch (stars) {
            case 1 -> new Color(211, 211, 211); // lichtgrijs - 1 ster
            case 2 -> new Color(198, 214, 224); // lichtblauw-grijs - 2 sterren
            case 3 -> new Color(173, 216, 230); // lichtblauw - 3 sterren
            case 4 -> new Color(176, 224, 196); // mintgroen - 4 sterren
            case 5 -> new Color(255, 223, 128); // goud-licht - 5 sterren
            case 6 -> new Color(255, 200, 70);  // goud - 6 sterren (top luxe)
            default -> Color.LIGHT_GRAY;
        };
    }
}
