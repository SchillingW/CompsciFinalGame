import processing.core.PImage;

public class Player extends GridObject<GameGrid> {

    public final StepDevice moveTimer;

    public PImage spriteRight;
    public PImage spriteLeft;

    public Vector moveDirection;
    public final boolean moveOnStep;

    public Player(boolean moveOnStep, long moveSteps, PImage spriteRight, PImage spriteLeft, GameGrid grid) {
        super(Vector.shrink(grid == null ? new Vector() : grid.gridSize, new Vector(2)), spriteRight, grid);
        if (grid != null) fall();
        moveTimer = new StepDevice(moveSteps);
        this.spriteLeft = spriteLeft;
        this.spriteRight = spriteRight;
        moveDirection = new Vector();
        this.moveOnStep = moveOnStep;
    }

    public Player(boolean moveOnStep, long moveSteps, PImage spriteRight, PImage spriteLeft) {
        this(moveOnStep, moveSteps, spriteRight, spriteLeft, null);
    }

    @Override
    public void step() {
        super.step();
        if (moveTimer.addInterval(1) && moveOnStep) moveInput();
    }

    public void moveForce() {
        if (!moveOnStep) moveInput();
    }

    public void moveInput() {
        walkPlayer(moveDirection);
        if (moveDirection.x == 1) {
            sprite = spriteRight;
        } else if (moveDirection.x == -1) {
            sprite = spriteLeft;
        }
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
        return new Player(moveOnStep, moveTimer.intervalsPerStep, spriteRight, spriteLeft, grid);
    }
}
