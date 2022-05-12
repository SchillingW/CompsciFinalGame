import processing.core.PImage;

// block object which falls and the player may swap with
public class Block extends GridObject<GameGrid> {

    // direction blocks fall
    public static final Vector gravity = new Vector(0, 1);

    // initialize block object
    public Block(int column, PImage sprite, GameGrid grid) {

        // initialize grid object
        super(new Vector(column, 0), sprite, grid);
    }

    // initialize block object template without grid
    public Block(PImage sprite) {
        this(0, sprite, null);
    }

    // fall one block if nothing below
    public boolean fall() {

        // if player below then stop
        if (inDirection(gravity).equals(grid.player.getPosition())) return false;

        // if other block below then stop
        if (grid.getBlockAt(inDirection(gravity)) != null) return false;

        // otherwise move down
        return move(gravity);
    }

    // undo a fall
    public boolean unfall() {

        // move block away from gravity
        return move(gravity.negate());
    }

    // create valid block object from template instance
    public Block asTemplate(int column, GameGrid grid) {

        // initialize block with template data and new grid data
        return new Block(column, sprite, grid);
    }
}
