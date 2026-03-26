package ui;

import model.Area;
import model.Tile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TileLabel extends JLabel {
    private Tile tile;

    public TileLabel(Tile tile) {
        this.tile = tile;

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(new LineBorder(Color.BLACK));
        setOpaque(true);

        Area area = tile.getArea();
        if(area == null) {
            setText("");
            setForeground(Color.WHITE);
            return;
        }

        setText(area.getAreaType());
        setBackground(getColorForArea(area.getAreaType()));

        boolean top = tile.getUp(tile) == null || tile.getUp(tile).getArea() != area;
        boolean bottom = tile.getDown(tile) == null || tile.getDown(tile).getArea() != area;
        boolean left = tile.getLeft(tile) == null || tile.getLeft(tile).getArea() != area;
        boolean right = tile.getRight(tile) == null || tile.getRight(tile).getArea() != area;

        Border border = BorderFactory.createMatteBorder(
                top ? 2 : 0,
                left ? 2 : 0,
                bottom ? 2 : 0,
                right ? 2 : 0,
                Color.BLACK
        );
        setBorder(border);

        setToolTipText(
                "Type: " + area.getAreaType() +
                        ", x=" + area.getX() +
                        ", y=" + area.getY() +
                        ", width=" + area.getWidth() +
                        ", height=" + area.getHeight()
        );
    }

    private Color getColorForArea(String areaType) {
        switch (areaType) {
            case "Cinema":
                return Color.ORANGE;
            case "Restaurant":
                return Color.GREEN;
            case "Fitness":
                return Color.CYAN;
            case "Room":
                return Color.LIGHT_GRAY;
            default:
                return Color.WHITE;
        }
    }

    public Tile getTile() {
        return tile;
    }
}
