import java.util.*;

public class DFS extends Algorithm{
    public DFS(Game game) {
        super(game);
    }

    /**
     * Performs a Depth-First Search (DFS) to find a path for the rogue to move away from the monster.
     */
    @Override
    public Site findPath(Site startSite, Site targetSite, boolean reverse) {
        Site rogueSite = game.getRogueSite();
        Site monsterSite = game.getMonsterSite();
        Dungeon dungeon = game.getDungeon();
        Set<Site> visited = new HashSet<>();
        Map<Site, Site> parentMap = new HashMap<>();
        boolean found = dfs(rogueSite, monsterSite, visited, parentMap);
        if (found) {
            return backtrackPath(parentMap, rogueSite, monsterSite);
        }
        return rogueSite; // If no path found, return the current position
    }

    /**
     * Performs a Depth-First Search (DFS) to find the shortest path from startSite to targetSite.
     */
    private boolean dfs(Site currentSite, Site targetSite, Set<Site> visited, Map<Site, Site> parentMap) {
        visited.add(currentSite);

        // Directions for moving in the grid: up, down, left, right
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1} // Up, Down, Left, Right
        };

        // Diagonal directions for moving in the grid
        int[][] diagonalDirections = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
        };

        // Try all four main directions
        for (int[] direction : directions) {
            int newRow = currentSite.i() + direction[0];
            int newCol = currentSite.j() + direction[1];
            Site neighborSite = new Site(newRow, newCol);
            if (!visited.contains(neighborSite) && game.getDungeon().isLegalMove(currentSite, neighborSite)) {
                parentMap.put(neighborSite, currentSite);
                if (neighborSite.equals(targetSite)) {
                    return true;
                }
                boolean found = dfs(neighborSite, targetSite, visited, parentMap);
                if (found) {
                    return true;
                }
            }
        }

        // Try diagonal directions only if current site is a room
        if (game.getDungeon().isRoom(currentSite)) {
            for (int[] direction : diagonalDirections) {
                int newRow = currentSite.i() + direction[0];
                int newCol = currentSite.j() + direction[1];
                Site neighborSite = new Site(newRow, newCol);
                if (!visited.contains(neighborSite) && game.getDungeon().isLegalMove(currentSite, neighborSite)) {
                    parentMap.put(neighborSite, currentSite);
                    if (neighborSite.equals(targetSite)) {
                        return true;
                    }
                    boolean found = dfs(neighborSite, targetSite, visited, parentMap);
                    if (found) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Backtracks the path from the end site to the start site to find the first move.
     */
    private Site backtrackPath(Map<Site, Site> parentMap, Site startSite, Site endSite) {
        LinkedList<Site> path = new LinkedList<>();
        Site currentSite = endSite;
        while (currentSite != null) {
            path.addFirst(currentSite);
            currentSite = parentMap.get(currentSite);
        }
        // Return the first move from the start site
        if (path.size() > 1) {
            Site firstMove = path.get(1);
            return OppositeDirection(startSite, firstMove);
        }
        return startSite;
    }

    /**
     * Finds the opposite direction from the given direction.
     * If the opposite direction is blocked, tries alternative directions.
     */
    private Site OppositeDirection(Site current, Site direction) {
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