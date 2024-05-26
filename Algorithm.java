public abstract class Algorithm {

    Game game;

    public Algorithm(Game game){
        this.game = game;
    }

    /**
     * Performs a Breadth-First Search (BFS) to find the shortest path from the monster to the rogue.
     * If no path is found, returns the farthest reachable site.
     */
    public Site findPathForMonster() {
        return findPath(game.getMonsterSite(), game.getRogueSite(), false);
    }

    /**
     * Performs a Breadth-First Search (BFS) to find the shortest path from the rogue to the monster.
     * If no path is found, returns the farthest reachable site.
     */
    public Site findPathForRogue() {
        return findPath(game.getRogueSite(), game.getMonsterSite(), true);
    }

    public abstract Site findPath(Site startSite, Site targetSite, boolean reverse);
}
