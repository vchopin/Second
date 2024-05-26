public class Monster {
    private Game game;
    private Dungeon dungeon;
    private int N;
    private Algorithm movePolicy;

    public Monster(Game game) {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
        this.movePolicy = new BFS(this.game);
    }


    // TAKE A BFS LEGAL MOVE
    public Site move() {
        Site bestPath = this.movePolicy.findPathForMonster();
        if (bestPath !=null ) {
            return bestPath;
        }
            Site monster = game.getMonsterSite();
            Site move = null;

            // take random legal move
            int n = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Site site = new Site(i, j);
                    if (dungeon.isLegalMove(monster, site)) {
                        n++;
                        if (Math.random() <= 1.0 / n) move = site;
                    }
                }
            }
            return move;
    }

}
