import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

// row of blocks falling together
public class BlockRow extends GridObject<GameGrid> {

    // list of blocks existing in row
    public final ArrayList<Block> blocks;
    public final Block blockTemplate;

    // timer device for falling
    private final StepDevice fallTimer;

    // randomness rate for spawning blocks in row
    public final int minBlockNum;
    public final int maxBlockNum;

    // danger indicator tile
    public final PImage dangerSprite;

    // initialize block row
    public BlockRow(int minBlockNum, int maxBlockNum, long fallSteps,
                    PImage dangerSprite, Block blockTemplate, GameGrid grid) {

        // initialize grid object
        super(new Vector(), null, grid);

        // store settings
        blocks = new ArrayList<>();
        fallTimer = new StepDevice(fallSteps);
        this.minBlockNum = minBlockNum;
        this.maxBlockNum = maxBlockNum;
        this.blockTemplate = blockTemplate;
        this.dangerSprite = dangerSprite;

        // put blocks in row
        populateRow();
    }

    // initialize template block row without grid
    public BlockRow(int minBlockNum, int maxBlockNum, long fallSteps,
                    PImage dangerSprite, Block blockTemplate) {
        this(minBlockNum, maxBlockNum, fallSteps, dangerSprite, blockTemplate, null);
    }

    // add members to row
    private void populateRow() {

        if (grid != null) {

            int numBlocks = (int)(Math.random() * (maxBlockNum - minBlockNum + 1) + minBlockNum);

            boolean[] toAdd = new boolean[grid.gridSize.x];

            for (int i = 0; i < numBlocks; i++) {

                int spot = (int)(Math.random() * (grid.gridSize.x - i));

                for (int j = 0; j <= spot; j++) {
                    if (toAdd[spot]) spot++;
                }

                toAdd[spot] = true;
            }

            for (int i = 0; i < grid.gridSize.x; i++) {
                if (toAdd[i]) blocks.add(blockTemplate.asTemplate(i, grid));
            }
        }
    }

    @Override
    public void draw(PApplet applet) {

        // initialize hit counter
        boolean blockWillHit = false;

        // iterate through blocks in row
        for (Block block : blocks) {

            // check if any block will hit
            if (!grid.isOpenNoPlayer(block.getPosition().setY(block.getPosition().y + 2))) blockWillHit = true;
        }

        // iterate through blocks in row
        for (Block block : blocks) {

            // draw if any blok is about to hit
            if (blockWillHit) {

                // iterate through spaces below block
                for (int i = block.getPosition().y; i < grid.gridSize.y; i++) {

                    // draw danger indicator in cell
                    grid.drawSprite(block.getPosition().setY(i), dangerSprite, applet);
                }
            }

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
            block.fall(false);
        }

        // check if blocked again
        return !isBlocked();
    }

    // check below row if next fall will work
    public boolean isBlocked() {

        // iterate through blocks in row
        for (Block block : blocks) {

            // check if blocked
            if (!grid.isOpenNoPlayer(block.inDirection(Block.gravity))) {

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
        return new BlockRow(minBlockNum, maxBlockNum, fallTimer.intervalsPerStep, dangerSprite, blockTemplate, grid);
    }
}
