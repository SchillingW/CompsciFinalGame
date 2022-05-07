public class Block extends GridObject<GameGrid> {

    private final StepDevice fallTimer;

    public Block(int column, long fallSteps, GameGrid grid) {
        super(new Vector(column, 0), grid);
        fallTimer = new StepDevice(fallSteps);
    }

    public Block(long fallSteps) {
        this(0, fallSteps, null);
    }

    @Override
    public void step() {
        super.step();
        if (fallTimer.addInterval(1)) fall();
    }

    public boolean fall() {
        Vector gravity = new Vector(0, 1);
        if (inDirection(gravity).equals(grid.player.getPosition())) return false;
        if (grid.getBlockAt(inDirection(gravity)) != null) return false;
        return move(gravity);
    }

    public Block asTemplate(int column, GameGrid grid) {
        return new Block(column, fallTimer.intervalsPerStep, grid);
    }
}
