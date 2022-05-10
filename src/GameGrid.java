import processing.core.PImage;
import java.util.ArrayList;

// grid object for this game
public class GameGrid extends Grid {

    // player object reference
    public final Player player;

    // list ob blocks existing in grid
    public final ArrayList<Block> blocks;

    // timer device for block spawning
    private final StepDevice spawnTimer;

    // template object for spawning blocks
    private final Block blockTemplate;

    // randomness rate for spawning blocks in row
    public final double blockSpawnRate;

    // initialize game grid
    public GameGrid(
            Vector gridSize, long spawnSteps, double blockSpawnRate, int cellSize,
            PImage tileSprite, Player playerTemplate, Block blockTemplate) {

        // initialize grid object
        super(gridSize, cellSize, tileSprite);

        // initialize empty block list
        blocks = new ArrayList<>();

        // initialize player to this grid from template
        player = playerTemplate.asTemplate(this);

        // initialize timer device
        spawnTimer = new StepDevice(spawnSteps);

        // store settings
        this.blockTemplate = blockTemplate;
        this.blockSpawnRate = blockSpawnRate;
    }

    // get any generic object existing in grid
    @Override
    public GridObject getObject(int index) {

        // if trying to get first object give player
        if (index == 0) return player;

        // otherwise return next block object
        return blocks.get(index - 1);
    }

    // get number of generic objects in grid
    @Override
    public int getObjectCount() {

        // number of blocks plus one player
        return blocks.size() + 1;
    }

    // called on game step
    @Override
    public void step() {

        // call grid step
        super.step();

        // step in time for spawn timer
        if (spawnTimer.addInterval(1)) spawn();
    }

    // spawn row of blocks in grid
    public void spawn() {

        // loop through grid columns
        for (int i = 0; i < gridSize.x; i++) {

            // randomly decide whether block should spawn here
            if (Math.random() < blockSpawnRate) {

                // create new block from template and add to grid
                blocks.add(blockTemplate.asTemplate(i, this));
            }
        }
    }

    // get block at position in grid
    public Block getBlockAt(Vector cell) {

        // iterate through blocks
        for (Block block : blocks) {

            // if position matches return block
            if (cell.equals(block.getPosition())) return block;
        }

        // if no block found return null
        return null;
    }
}
