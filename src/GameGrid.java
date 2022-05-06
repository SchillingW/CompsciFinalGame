public class GameGrid extends  Grid {

    public Player player;

    public GameGrid(Vector gridSize, int cellSize) {
        super(gridSize, cellSize);
        player = new Player(this);
    }

    @Override
    public GridObject getObject(int index) {
        return player;
    }

    @Override
    public int getObjectCount() {
        return 1;
    }
}
