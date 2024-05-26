public class Rogue {
    private Game game;
    private Dungeon dungeon;
    private int N;

    private Algorithm movePolicy1;
    private Algorithm movePolicy2;
    public Rogue(Game game) {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
        this.movePolicy1 = new BFS(this.game);
        this.movePolicy2 = new DFS(this.game);
    }


    // TAKE A RANDOM LEGAL MOVE
    // YOUR MAIN TASK IS TO RE-IMPLEMENT THIS METHOD TO DO SOMETHING INTELLIGENT
    public Site move() {
        Site bfsPath = this.movePolicy1.findPathForRogue();
        Site dfsPath = this.movePolicy2.findPathForRogue();
        if (bfsPath == null && dfsPath != null) {
            return dfsPath;
        }
        if (dfsPath == null && bfsPath != null) {
            return bfsPath;
        }
        if (dfsPath != null) {
            if (bfsPath.manhattanTo(game.getMonsterSite()) >= dfsPath.manhattanTo(game.getMonsterSite())) {
                return bfsPath;
            } else {
                return dfsPath;
            }
        }

        //Random
        Site rogue   = game.getRogueSite();
        Site move    = null;

        // take random legal move
        int n = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Site site = new Site(i, j);
                if (dungeon.isLegalMove(rogue, site)) {
                    n++;
                    if (Math.random() <= 1.0 / n) move = site;
                }
            }
        }
        return move;
    }

}
