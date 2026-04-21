package ui;

import model.SubTile;
import model.Tile;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TileLabel extends JLabel {
    private final Tile tile;

    public TileLabel(Tile tile, int x, int y, int tileSize) {
        this.tile = tile;

        setLayout(null);
        setOpaque(false);
        setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y,  tileSize, tileSize);

        addSubTiles(tileSize);
    }

    private void addSubTiles(int tileSize) {
        SubTile[][] subTiles = tile.getSubTiles();

        int rows = subTiles.length;
        int cols = subTiles[0].length;

        int subTileWidth = tileSize / rows;
        int subTileHeight = tileSize / cols;

        for (int subY = 0; subY < rows; subY++) {
            for (int subX = 0; subX < cols; subX++) {
                SubTile subTile = subTiles[subY][subX];

                SubTileLabel subTileLabel = new SubTileLabel(subTile, subY * subTileHeight, subX * subTileWidth, subTileWidth, subTileHeight);
                add(subTileLabel);
            }
        }
    }
}
