import processing.core.PImage;

// player object for game
public class Player extends GridObject<GameGrid> {

    // timer device for movement
    public final StepDevice moveTimer;

    // sprites for face directions
    public PImage spriteRight;
    public PImage spriteLeft;

    // direction player will move on next movement
    public Vector moveDirection;

    // should player move on timer or freely by key press
    public final boolean moveOnStep;

    // initialize player object
    public Player(boolean moveOnStep, long moveSteps, PImage spriteRight, PImage spriteLeft, GameGrid grid) {

        // initialize grid object
        super(Vector.shrink(grid == null ? new Vector() : grid.gridSize, new Vector(2)), spriteRight, grid);

        // if not template and has grid fall to ground
        if (grid != null) fall();

        // store settings
        moveTimer = new StepDevice(moveSteps);
        this.spriteLeft = spriteLeft;
        this.spriteRight = spriteRight;
        moveDirection = new Vector();
        this.moveOnStep = moveOnStep;
    }

    // initialize player object as template without grid
    public Player(boolean moveOnStep, long moveSteps, PImage spriteRight, PImage spriteLeft) {
        this(moveOnStep, moveSteps, spriteRight, spriteLeft, null);
    }

    // called on major game step
    @Override
    public void step() {

        // make parent step
        super.step();

        // if movement timer reached and moves on timer then move
        if (moveTimer.addInterval(1) && moveOnStep) moveInput();
    }

    // called on every move input
    public void moveForce() {

        // if moves on input and not timer then move
        if (!moveOnStep) moveInput();
    }

    // move player based on input direction
    public void moveInput() {

        // make player walk in direction
        walkPlayer(moveDirection);

        // turn player sprite to face movement direction
        if (moveDirection.x == 1) {
            sprite = spriteRight;
        } else if (moveDirection.x == -1) {
            sprite = spriteLeft;
        }
    }

    // make player walk in direction
    public boolean walkPlayer(Vector amount) {

        // get block object player will hit
        Block target = grid.getBlockAt(inDirection(amount));

        // if block exists then swap with player
        if (target != null) target.move(amount.negate());

        // try to move
        if (!move(amount)) return false;

        // fall to ground
        fall();

        // return valid movement
        return true;
    }

    // move player in direction if nothing blocking
    public boolean movePlayer(Vector amount) {

        // if block in way do not move
        if (grid.getBlockAt(inDirection(amount)) != null) return false;

        // otherwise try to move
        return move(amount);
    }

    // fall to ground level
    public int fall() {

        // initialize distance counter
        int count = 0;

        // try to move until blocked
        while (movePlayer(new Vector(0, 1))) count++;

        // return distance
        return count;
    }

    // create valid player object from template instance
    public Player asTemplate(GameGrid grid) {

        // initialize new player with template and grid data
        return new Player(moveOnStep, moveTimer.intervalsPerStep, spriteRight, spriteLeft, grid);
    }
}
