package ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import handler.GridMouseEventHandler;
import model.Area;
import model.Grid;
import model.Guest;
import model.Cleaner;
import model.Elevator;
import model.SubTile;
import model.Tile;
import simulation.Simulation;

import java.awt.*;
import java.net.CookieStore;

public class GridPanel extends JPanel {

    private final Simulation simulation;
    private final GridMouseEventHandler mouseEventHandler;
    private final Rectangle lobbyRectangle;
    private final int tileSize = 80;

    public GridPanel(Simulation simulation) {
        this.simulation = simulation;
        this.mouseEventHandler = new GridMouseEventHandler(this, simulation);
        this.lobbyRectangle = new Rectangle();

        addMouseListener(mouseEventHandler);
        addMouseMotionListener(mouseEventHandler);

        updatePreferredSize();
        updateLobbyRectangle();
    }

    public void updatePreferredSize() {
        Grid grid = simulation.getGrid();

        if (grid == null) {
            setPreferredSize(new Dimension(0, 0));
            return;
        }

        setPreferredSize(new Dimension(
                grid.getSizeX() * tileSize,
                grid.getSizeY() * tileSize));
    }

    public void updateLobbyRectangle() {
        Grid grid = simulation.getGrid();

        if (grid == null) {
            return;
        }

        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile == null) {
                    continue;
                }

                Area area = tile.getArea();

                if (area == null) {
                    continue;
                }

                if (area.getAreaType().equalsIgnoreCase("lobby")) {
                    lobbyRectangle.setBounds(
                            area.getX() * tileSize,
                            area.getY() * tileSize,
                            area.getWidth() * tileSize,
                            area.getHeight() * tileSize);
                    return;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Grid grid = simulation.getGrid();

        if (grid == null) {
            return;
        }

        drawAreas(g, grid);
        drawElevator(g);
        drawPeople(g, grid);
    }

    public void drawAreas(Graphics g, Grid grid) {
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile == null || tile.getArea() == null) {
                    continue;
                }

                Area area = tile.getArea();

                drawArea(g, area);
                drawAreaName(g, area);
            }
        }
    }

    public void drawElevator(Graphics g) {
        Elevator elevator = simulation.getElevator();

        if (elevator == null) {
            return;
        }

        Tile liftTile = findLiftTileOnLevel(elevator.getCurrentLevel());

        if (liftTile == null) {
            return;
        }

        int x = liftTile.getX() * tileSize;
        int y = liftTile.getY() * tileSize;

        g.setColor(Color.darkGray);
        g.fillRect(x + 10, y + 10, tileSize - 20, tileSize - 20);

        g.setColor(Color.WHITE);
        g.drawRect(x + 16, y + 18, tileSize - 32, tileSize - 32);
    }

    public void drawArea(Graphics g, Area area) {
        int y = area.getY() * tileSize;
        int x = area.getX() * tileSize;
        int width = area.getWidth() * tileSize;
        int height = area.getHeight() * tileSize;

        g.setColor(getColorForArea(area.getAreaType()));
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        for (int subY = 0; subY < area.getHeight(); subY++) {
            for (int subX = 0; subX < area.getWidth(); subX++) {
                int drawY = y + subY * tileSize;
                int drawX = x + subX * tileSize;

                g.drawRect(drawX, drawY, tileSize, tileSize);
            }
        }
    }

    public void drawAreaName(Graphics g, Area area) {
        int y = area.getY() * tileSize;
        int x = area.getX() * tileSize;
        int width = area.getWidth() * tileSize;
        int height = area.getHeight() * tileSize;

        String text = area.getAreaType();

        g.setColor(Color.BLACK);
        g.setFont(getFont());

        FontMetrics fm = g.getFontMetrics();

        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();

        g.drawString(text, textX, textY);
    }

    public void drawPeople(Graphics g, Grid grid) {
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);

                if (tile == null) {
                    continue;
                }

                drawPeopleInTile(g, tile, y, x);
            }
        }
    }

    public void drawPeopleInTile(Graphics g, Tile tile, int y, int x) {
        SubTile[][] subTiles = tile.getSubTiles();

        int rows = subTiles.length;
        int cols = subTiles[0].length;

        int subTileWidth = tileSize / rows;
        int subTileHeight = tileSize / cols;

        for (int subY = 0; subY < rows; subY++) {
            for (int subX = 0; subX < cols; subX++) {
                SubTile subTile = subTiles[subY][subX];

                int subTileY = y * tileSize + subY * subTileHeight;
                int subTileX = x * tileSize + subX * subTileWidth;

                if (subTile.getPerson() instanceof Guest) {
                    g.setColor(Color.BLUE);
                    g.fillOval(subTileX + subTileWidth / 4, subTileY + subTileHeight / 4, subTileWidth / 2,
                            subTileHeight / 2);
                }

                if (subTile.getPerson() instanceof Cleaner) {
                    g.setColor(Color.GREEN);
                    g.fillOval(subTileX + subTileWidth / 4, subTileY + subTileHeight / 4, subTileWidth / 2,
                            subTileHeight / 2);
                }
            }
        }
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

    private Tile findLiftTileOnLevel(int level) {
        Grid grid = simulation.getGrid();

        if (grid == null) {
            return null;
        }

        for (int x = 0; x < grid.getSizeX(); x++) {
            Tile tile = grid.getTile(x, level);

            if (tile == null || tile.getArea() == null) {
                continue;
            }

            if (tile.getArea().getAreaType().equalsIgnoreCase("lift")) {
                return tile;
            }
        }

        return null;
    }

    public Rectangle getLobbyRectangle() {
        return lobbyRectangle;
    }
}