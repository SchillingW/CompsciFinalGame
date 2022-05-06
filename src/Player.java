public class Player extends GridObject {

    public Player(Grid grid) {
        super(Vector.shrink(grid.gridSize, new Vector(2)));
    }
}
