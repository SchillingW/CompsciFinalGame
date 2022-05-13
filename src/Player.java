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
    public boolean upwards;

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
        upwards = false;
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

        // turn player sprite to face movement direction
        if (moveDirection.x == 1) {
            sprite = spriteRight;
        } else if (moveDirection.x == -1) {
            sprite = spriteLeft;
        }

        // make player move
        if (upwards) {
            phasePlayer(moveDirection);
        } else {
            swapPlayer(moveDirection);
        }
    }

    // make player climb in direction
    public void phasePlayer(Vector amount) {

        // check which direction player is moving
        if (amount.x != 0) {

            // check if space is already empty
            if (grid.getBlockAt(inDirection(amount)) == null) {

                // move player to space
                move(amount);
                fall();

            } else {

                // loop through cells upwards until grid edge reached
                for (Vector i = amount; grid.contains(inDirection(i)); i = Vector.translate(i, Block.gravity.negate())) {

                    // check if empty space found
                    if (grid.getBlockAt(inDirection(i)) == null) {

                        // move player above ground
                        move(i);
                        break;
                    }
                }
            }

        } else if (amount.y == -1) {

            // player starts out of block
            boolean inBlock = false;

            // loop through cells upwards until grid edge reached
            for (Vector i = new Vector(); grid.contains(inDirection(i)); i = Vector.translate(i, Block.gravity.negate())) {

                // check if block in spot
                boolean wasInBlock = inBlock;
                inBlock = grid.getBlockAt(inDirection(i)) != null;

                // check if just reached new empty space
                if (!inBlock && wasInBlock) {

                    // move player above ground
                    move(i);
                    break;
                }
            }

        } else {

            // loop through cells downwards until grid edge reached
            for (Vector i = Block.gravity; grid.contains(inDirection(i)); i = Vector.translate(i, Block.gravity)) {

                // check if empty cell reached
                if (grid.getBlockAt(inDirection(i)) == null) {

                    // move player to empty cell
                    move(i);
                    fall();
                    break;
                }
            }
        }
    }

    // make player walk in direction
    public void swapPlayer(Vector amount) {

        // check which direction to move
        if (amount.y == -1) {

            // find next obstacle above player
            Vector i;
            for (i = amount; grid.isOpen(inDirection(i)); i = Vector.translate(i, amount)) {}

            // move object below player
            Block target = grid.getBlockAt(inDirection(i));
            if (target != null) target.move(i.negate());

            // move player above object
            move(amount);
            fall();

        } else {

            // get block next to player
            Block target = grid.getBlockAt(inDirection(amount));
            if (target != null) target.move(amount.negate());

            // swap player with block
            move(amount);
            fall();
        }
    }

    // fall to ground level
    public int fall() {

        // initialize distance counter
        int count = 0;

        // try to move until blocked
        while (grid.isOpen(inDirection(Block.gravity)) && move(Block.gravity)) count++;

        // return distance
        return count;
    }

    // create valid player object from template instance
    public Player asTemplate(GameGrid grid) {

        // initialize new player with template and grid data
        return new Player(moveOnStep, moveTimer.intervalsPerStep, spriteRight, spriteLeft, grid);
    }
}
