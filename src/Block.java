import processing.core.PImage;

// block object which falls and the player may swap with
public class Block extends GridObject<GameGrid> {

    // timer device for falling
    private final StepDevice fallTimer;

    // initialize block object
    public Block(int column, long fallSteps, PImage sprite, GameGrid grid) {

        // initialize grid object
        super(new Vector(column, 0), sprite, grid);

        // store settings
        fallTimer = new StepDevice(fallSteps);
    }

    // initialize block object template without grid
    public Block(long fallSteps, PImage sprite) {
        this(0, fallSteps, sprite, null);
    }

    // called on every major game step
    @Override
    public void step() {

        // make parent step
        super.step();

        // if fall timer reached fall a block
        if (fallTimer.addInterval(1)) fall();
    }

    // fall one block if nothing below
    public boolean fall() {

        // get fall direction
        Vector gravity = new Vector(0, 1);

        // if player below then stop
        if (inDirection(gravity).equals(grid.player.getPosition())) return false;

        // if other block below then stop
        if (grid.getBlockAt(inDirection(gravity)) != null) return false;

        // otherwise move down
        return move(gravity);
    }

    // create valid block object from template instance
    public Block asTemplate(int column, GameGrid grid) {

        // initialize block with template data and new grid data
        return new Block(column, fallTimer.intervalsPerStep, sprite, grid);
    }
}
