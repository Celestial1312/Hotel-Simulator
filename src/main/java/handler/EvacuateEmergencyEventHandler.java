package handler;

import java.util.List;

import hotelevents.HotelEvent;
import model.Guest;
import model.SubTile;
import model.Tile;
import pathfinding.AStarPathFinding;
import simulation.Simulation;

public class EvacuateEmergencyEventHandler implements SimulationEventHandler {

    private final Simulation simulation;
    private boolean evacuating;

    public EvacuateEmergencyEventHandler(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public boolean canHandle(HotelEvent event) {
        return event.getEventType().toString().equalsIgnoreCase("EVACUATE");
    }

    @Override
    public void handleEvent(HotelEvent event) {
        evacuating = true;
        // Look up the lobby tile once, before looping — not once per guest
        Tile lobbyTile = simulation.findAreaType("lobby");

        if (lobbyTile == null) {
            return;
        }

        for (Guest guest : simulation.getGuests().values()) {
            // Skip null entries defensively
            if (guest == null) {
                continue;
            }

            SubTile currentSubtile = guest.getCurrentSubTile();

            List<SubTile> path = new AStarPathFinding().findPathToTile(currentSubtile, lobbyTile);

            // If no path exists for this guest, skip them — don't stop the whole evacuation
            if (path.isEmpty()) {
                continue;
            }

            path.remove(0);

            guest.setCheckingOut(true);
            guest.setTargetTile(lobbyTile);
            guest.setPath(path);
        }
    }
}