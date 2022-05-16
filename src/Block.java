import processing.core.PImage;

// block object which falls and the player may swap with
public class Block extends GridObject<GameGrid> {

    // different state sprites
    public final PImage spriteInteract;
    public final PImage spriteInvis;

    // direction blocks fall
    public static final Vector gravity = new Vector(0, 1);

    // initialize block object
    public Block(int column, PImage spriteInteract, PImage spriteInvis, GameGrid grid) {

        // initialize grid object
        super(new Vector(column, 0), spriteInvis, grid);

        // store sprite references
        this.spriteInteract = spriteInteract;
        this.spriteInvis = spriteInvis;
    }

    // initialize block object template without grid
    public Block(PImage spriteInteract, PImage spriteInvis) {
        this(0, spriteInteract, spriteInvis, null);
    }

    // fall one block if nothing below
    public boolean fall(boolean usePlayer) {

        // if player below then stop
        if (usePlayer && inDirection(gravity).equals(grid.player.getPosition())) return false;

        // if other block below then stop
        if (grid.getBlockAt(inDirection(gravity)) != null) return false;

        // otherwise move down
        return move(gravity);
    }

    // make block fall all the way to next ground
    public void fallAll() {

        // loop until ground hit
        while (fall(true)) {}
    }

    // undo a fall
    public boolean unfall() {

        // move block away from gravity
        return move(gravity.negate());
    }

    // create valid block object from template instance
    public Block asTemplate(int column, GameGrid grid) {

        // initialize block with template data and new grid data
        return new Block(column, spriteInteract, spriteInvis, grid);
    }

    // set block to can interact visual state
    public void setStateInteract() {

        // change sprite
        sprite = spriteInteract;
    }

    // set block to cannot interact visual state
    public void setStateInvis() {

        // change sprite
        sprite = spriteInvis;
    }
}
