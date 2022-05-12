import processing.core.PApplet;
import java.util.ArrayList;

// row of blocks falling together
public class BlockRow extends GridObject<GameGrid> {

    // list of blocks existing in row
    public final ArrayList<Block> blocks;
    public final Block blockTemplate;

    // timer device for falling
    private final StepDevice fallTimer;

    // randomness rate for spawning blocks in row
    public final double blockSpawnRate;

    // initialize block row
    public BlockRow(double blockSpawnRate, long fallSteps, Block blockTemplate, GameGrid grid) {

        // initialize grid object
        super(new Vector(), null, grid);

        // store settings
        blocks = new ArrayList<>();
        fallTimer = new StepDevice(fallSteps);
        this.blockSpawnRate = blockSpawnRate;
        this.blockTemplate = blockTemplate;

        // populate row with blocks
        if (grid != null) {

            // iterate through grid columns
            for (int i = 0; i < grid.gridSize.x; i++) {

                // randomly decide whether block should spawn here
                if (Math.random() < blockSpawnRate) {

                    // create new block from template and add to grid
                    blocks.add(blockTemplate.asTemplate(i, grid));
                }
            }
        }
    }

    // initialize template block row without grid
    public BlockRow(double blockSpawnRate, long fallSteps, Block blockTemplate) {
        this(blockSpawnRate, fallSteps, blockTemplate, null);
    }

    @Override
    public void draw(PApplet applet) {

        // iterate through blocks in row
        for (Block block : blocks) {

            // draw each block
            block.draw(applet);
        }
    }

    @Override
    public void step() {

        // make parent step
        super.step();

        // if fall timer reached let blocks fall
        if (fallTimer.addInterval(1)) fall();
    }

    // make all blocks in row try to fall
    public boolean fall() {

        if (isBlocked()) return false;

        // iterate through blocks in row
        for (Block block : blocks) {

            // try to fall
            block.fall();
        }

        // check if blocked again
        return !isBlocked();
    }

    // check below row if next fall will work
    public boolean isBlocked() {

        // iterate through blocks in row
        for (Block block : blocks) {

            // check if blocked
            if (!grid.isOpen(block.inDirection(Block.gravity))) {

                // if reached target dissolve row
                grid.dissolveRow(this);
                return true;
            }
        }

        // return not blocked
        return false;
    }

    // create valid row object from template instance
    public BlockRow asTemplate(GameGrid grid) {

        // initialize row with template data and new grid data
        return new BlockRow(blockSpawnRate, fallTimer.intervalsPerStep, blockTemplate, grid);
    }
}
