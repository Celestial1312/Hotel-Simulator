package pathfinding;

import model.SubTile;
import model.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarPathFinding {

    public List<SubTile> findPath(SubTile start, SubTile target) {
        if (start == null || target == null ||  !isWalkable(target, target)) {
            return Collections.emptyList();
        }

        int allowedLevel = start.getParentTile().getY();

        if(target.getParentTile().getY() != allowedLevel) {
            return Collections.emptyList();
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<SubTile, SubTile> cameFrom = new HashMap<>();
        Map<SubTile, Integer> gScores = new HashMap<>();

        gScores.put(start, 0);
        openSet.add(new Node(start, heuristic(start, target)));

        while (!openSet.isEmpty()) {
            SubTile current = openSet.poll().subTile;

            if (current == target) {
                return reconstructPath(cameFrom, current);
            }

            int currentGScore = gScores.getOrDefault(current, Integer.MAX_VALUE);

            for (SubTile neighbor : getNeighbors(current)) {
                if(neighbor.getParentTile().getY() != allowedLevel) {
                    continue;
                }

                if (!isWalkable(neighbor, target)) {
                    continue;
                }

                int tentativeGScore = currentGScore + 1;
                if (tentativeGScore >= gScores.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScores.put(neighbor, tentativeGScore);
                openSet.add(new Node(neighbor, tentativeGScore + heuristic(neighbor, target)));
            }
        }

        return Collections.emptyList();
    }

    public List<SubTile> findPathToTile(SubTile start, Tile targetTile) {
        if (start == null || targetTile == null || targetTile.getArea() == null) {
            return Collections.emptyList();
        }

        List<SubTile> shortestPath = Collections.emptyList();

        for (SubTile[] row : targetTile.getSubTiles()) {
            for (SubTile target : row) {
                List<SubTile> path = findPath(start, target);
                if (!path.isEmpty() && (shortestPath.isEmpty() || path.size() < shortestPath.size())) {
                    shortestPath = path;
                }
            }
        }

        return shortestPath;
    }

    private boolean isWalkable(SubTile subTile, SubTile target) {
        return subTile != null && (subTile == target || subTile.isWalkable());
    }

    private List<SubTile> getNeighbors(SubTile subTile) {
        List<SubTile> neighbors = new ArrayList<>(4);
        addIfPresent(neighbors, subTile.getUp());
        addIfPresent(neighbors, subTile.getDown());
        addIfPresent(neighbors, subTile.getLeft());
        addIfPresent(neighbors, subTile.getRight());
        return neighbors;
    }

    private void addIfPresent(List<SubTile> neighbors, SubTile subTile) {
        if (subTile != null) {
            neighbors.add(subTile);
        }
    }

    private int heuristic(SubTile current, SubTile target) {
        return Math.abs(current.getGlobalX() - target.getGlobalX())
                + Math.abs(current.getGlobalY() - target.getGlobalY());
    }

    private List<SubTile> reconstructPath(Map<SubTile, SubTile> cameFrom, SubTile current) {
        List<SubTile> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    private static class Node implements Comparable<Node> {
        private final SubTile subTile;
        private final int fScore;

        private Node(SubTile subTile, int fScore) {
            this.subTile = subTile;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(fScore, other.fScore);
        }
    }
    
}
