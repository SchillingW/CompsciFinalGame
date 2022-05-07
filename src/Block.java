import processing.core.PImage;

public class Block extends GridObject<GameGrid> {

    private final StepDevice fallTimer;

    public Block(int column, long fallSteps, PImage sprite, GameGrid grid) {
        super(new Vector(column, 0), sprite, grid);
        fallTimer = new StepDevice(fallSteps);
    }

    public Block(long fallSteps, PImage sprite) {
        this(0, fallSteps, sprite, null);
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
        return new Block(column, fallTimer.intervalsPerStep, sprite, grid);
    }
}
