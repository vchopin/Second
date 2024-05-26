import java.util.*;

public class BFS extends Algorithm{
    public BFS(Game game) {
        super(game);
    }


    /**
     * Performs a Breadth-First Search (BFS) to find the shortest path from startSite to targetSite.
     * If reverse is true, it finds the path and then returns the move in the opposite direction.
     */
    public Site findPath(Site startSite, Site targetSite, boolean reverse) {
        Dungeon dungeon = game.getDungeon();

        Queue<Site> queue = new LinkedList<>();
        Set<Site> visitedSites = new HashSet<>();
        Map<Site, Site> parentMap = new HashMap<>();
        Site farthestReachableSite = startSite;

        queue.add(startSite);
        visitedSites.add(startSite);
        parentMap.put(startSite, null);

        // Directions for moving in the grid: up, down, left, right
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
        };

        // Diagonal directions for moving in the grid
        int[][] diagonalDirections = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
        };

        while (!queue.isEmpty()) {
            Site currentSite = queue.poll();
            if (currentSite.equals(targetSite)) {
                return backtrackPath(parentMap, startSite, targetSite, reverse);
            }

            // Check all four main directions
            for (int[] direction : directions) {
                int newRow = currentSite.i() + direction[0];
                int newCol = currentSite.j() + direction[1];
                Site neighborSite = new Site(newRow, newCol);
                if (dungeon.isLegalMove(currentSite, neighborSite) && !visitedSites.contains(neighborSite)) {
                    visitedSites.add(neighborSite);
                    parentMap.put(neighborSite, currentSite);
                    queue.add(neighborSite);
                    farthestReachableSite = neighborSite; // Update farthest reachable site
                }
            }

            // Check diagonal directions only if current site is a room
            if (dungeon.isRoom(currentSite)) {
                for (int[] direction : diagonalDirections) {
                    int newRow = currentSite.i() + direction[0];
                    int newCol = currentSite.j() + direction[1];
                    Site neighborSite = new Site(newRow, newCol);
                    if (dungeon.isLegalMove(currentSite, neighborSite) && !visitedSites.contains(neighborSite)) {
                        visitedSites.add(neighborSite);
                        parentMap.put(neighborSite, currentSite);
                        queue.add(neighborSite);
                        farthestReachableSite = neighborSite; // Update farthest reachable site
                    }
                }
            }
        }
        // If no path to target, return the farthest reachable site
        return backtrackPath(parentMap, startSite, farthestReachableSite, reverse);
    }

    /**
     * Backtracks the path from the end site to the start site to find the first move.
     * If reverse is true, it finds the move in the opposite direction.
     */
    private Site backtrackPath(Map<Site, Site> parentMap, Site startSite, Site endSite, boolean reverse) {
        LinkedList<Site> path = new LinkedList<>();
        Site currentSite = endSite;
        while (currentSite != null) {
            path.addFirst(currentSite);
            currentSite = parentMap.get(currentSite);
        }
        if (reverse) {
            // If reverse, find the opposite direction move

            return path.size() > 1 ? OppositeDirection(path.get(1)) : null;

        } else {
            // Return the first move from the start site
            return path.size() > 1 ? path.get(1) : null;
        }
    }

    private Site OppositeDirection(Site direction) {
        Site current = game.getRogueSite();
        Dungeon dungeon = game.getDungeon();

        int I = current.i();
        int J = current.j();

        int dirI = direction.i();
        int dirJ = direction.j();

        Site opposite = null;

        // Check opposite direction
        if (I - 1 == dirI && J == dirJ) {
            opposite = new Site(I + 1, J);
        } else if (I - 1 == dirI && J + 1 == dirJ) {
            opposite = new Site(I + 1, J - 1);
        } else if (I == dirI && J - 1 == dirJ) {
            opposite = new Site(I, J + 1);
        } else if (I == dirI && J + 1 == dirJ) {
            opposite = new Site(I, J - 1);
        } else if (I + 1 == dirI && J - 1 == dirJ) {
            opposite = new Site(I - 1, J + 1);
        } else if (I + 1 == dirI && J == dirJ) {
            opposite = new Site(I - 1, J);
        } else if (I + 1 == dirI && J + 1 == dirJ) {
            opposite = new Site(I - 1, J - 1);
        } else {
            opposite = new Site(I + 1, J + 1);
        }

        // Check if the opposite move is legal
        if (dungeon.isLegalMove(current, opposite)) {
            return opposite;
        }

        // If the opposite move is not legal, try alternative directions
        // First, try horizontal alternatives if the initial move was vertical
        if (I - 1 == dirI || I + 1 == dirI) {
            int[][] horizontalAlternatives = {
                    {0, 1}, {0, -1} // Left, Right
            };
            for (int[] altDir : horizontalAlternatives) {
                int newRow = I + altDir[0];
                int newCol = J + altDir[1];
                Site alternative = new Site(newRow, newCol);
                if (dungeon.isLegalMove(current, alternative)) {
                    return alternative;
                }
            }
        }

        // Then, try vertical alternatives if the initial move was horizontal
        if (J - 1 == dirJ || J + 1 == dirJ) {
            int[][] verticalAlternatives = {
                    {1, 0}, {-1, 0} // Up, Down
            };
            for (int[] altDir : verticalAlternatives) {
                int newRow = I + altDir[0];
                int newCol = J + altDir[1];
                Site alternative = new Site(newRow, newCol);
                if (dungeon.isLegalMove(current, alternative)) {
                    return alternative;
                }
            }
        }

        // Finally, try diagonal alternatives if the initial move was diagonal
        int[][] diagonalAlternatives = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonals
        };
        for (int[] altDir : diagonalAlternatives) {
            int newRow = I + altDir[0];
            int newCol = J + altDir[1];
            Site alternative = new Site(newRow, newCol);
            if (dungeon.isLegalMove(current, alternative)) {
                return alternative;
            }
        }

        // If no legal move is found, return the current position
        return null;
    }
}
