public class Player extends GridObject<GameGrid> {

    public final StepDevice moveTimer;

    public Vector moveDirection;

    public Player(long moveSteps, GameGrid grid) {
        super(Vector.shrink(grid == null ? new Vector() : grid.gridSize, new Vector(2)), grid);
        moveTimer = new StepDevice(moveSteps);
        if (grid != null) fall();
        moveDirection = new Vector();
    }

    public Player(long moveSteps) {
        this(moveSteps, null);
    }

    @Override
    public void step() {
        super.step();
        if (moveTimer.addInterval(1)) walkPlayer(moveDirection);
    }

    public boolean walkPlayer(Vector amount) {
        Block target = grid.getBlockAt(inDirection(amount));
        if (target != null) target.move(amount.negate());
        if (!move(amount)) return false;
        fall();
        return true;
    }

    public boolean movePlayer(Vector amount) {
        if (grid.getBlockAt(inDirection(amount)) != null) return false;
        return move(amount);
    }

    public int fall() {
        int count = 0;
        while (movePlayer(new Vector(0, 1))) count++;
        return count;
    }

    public Player asTemplate(GameGrid grid) {
        return new Player(moveTimer.intervalsPerStep, grid);
    }
}
