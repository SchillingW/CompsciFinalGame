import processing.core.PImage;

import java.lang.reflect.Array;
import java.util.ArrayList;

// grid object for this game
public class GameGrid extends Grid {

    // player object reference
    public final Player player;

    // block row object
    public ArrayList<BlockRow> blockRow;
    public final BlockRow blockRowTemplate;

    // list ob blocks existing in grid
    public final ArrayList<Block> blocks;

    // timer device for block spawning
    private final StepDevice spawnTimer;

    // initialize game grid
    public GameGrid(
            Vector gridSize, long spawnSteps, int cellSize,
            PImage tileSprite, Player playerTemplate, BlockRow blockRowTemplate) {

        // initialize grid object
        super(gridSize, cellSize, tileSprite);

        // store settings
        blocks = new ArrayList<>();
        blockRow = new ArrayList<>();
        this.blockRowTemplate = blockRowTemplate;
        player = playerTemplate.asTemplate(this);
        spawnTimer = new StepDevice(spawnSteps);
    }

    // get any generic object existing in grid
    @Override
    public GridObject getObject(int index) {

        // if trying to get first object give player
        if (index <= 0) return player;

        // otherwise return next block object
        if (index - 1 < blocks.size()) return blocks.get(index - 1);

        // otherwise return block row at end
        return blockRow.get(index - 1 - blocks.size());
    }

    // get number of generic objects in grid
    @Override
    public int getObjectCount() {

        // number of blocks plus one player
        return blocks.size() + blockRow.size() + 1;
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

        // spawn new block row at top
        blockRow.add(blockRowTemplate.asTemplate(this));
    }

    // if block was hit release blocks and remove row object
    public void dissolveRow(BlockRow row) {

        // add blocks in block row array to grid array
        blocks.addAll(row.blocks);

        // remove block row object
        blockRow.remove(row);
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
