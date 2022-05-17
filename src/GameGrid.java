import processing.core.PImage;
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

    // score keeping device
    private int score;
    private int currentPerfectFitScore;
    private final int rowFillScoreMul;
    private final int rowFillScoreInc;
    private final int perfectFitScoreStart;
    private final int perfectFitScoreCount;

    // initialize game grid
    public GameGrid(
            Vector gridSize, long spawnSteps, int cellSize,
            int rowFillScoreMul, int rowFillScoreInc, int perfectFitScoreStart, int perfectFitScoreCount,
            PImage tileSprite, Player playerTemplate, BlockRow blockRowTemplate) {

        // initialize grid object
        super(gridSize, cellSize, tileSprite);

        // store settings
        blocks = new ArrayList<>();
        blockRow = new ArrayList<>();
        blockRow.add(blockRowTemplate.asTemplate(this));
        this.blockRowTemplate = blockRowTemplate;
        this.rowFillScoreMul = rowFillScoreMul;
        this.rowFillScoreInc = rowFillScoreInc;
        this.perfectFitScoreStart = perfectFitScoreStart;
        this.perfectFitScoreCount = perfectFitScoreCount;
        currentPerfectFitScore = perfectFitScoreStart;
        score = 0;
        player = playerTemplate.asTemplate(this);
        spawnTimer = new StepDevice(spawnSteps);
    }

    // get any generic object existing in grid
    @Override
    public GridObject getObject(int index) {

        // return next block object
        if (index < blocks.size()) return blocks.get(index);
        index -= blocks.size();

        // return block row at end
        if (index < blockRow.size()) return blockRow.get(index);

        // return player object
        return player;
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

        // checking for perfect fits
        boolean inEmptyRow = true;
        boolean allAboveOtherBlock = true;

        // iterate through blocks
        for (Block block : row.blocks) {

            // change block visuals
            block.setStateInteract();

            // make sure block doesn't land on player
            if (!isOpen(block.getPosition())) player.kill();

            // kill player if below landing block
            for (int i = block.getPosition().y; i < gridSize.y; i++) {
                if (player.getPosition().equals(block.getPosition().setY(i))) player.kill();
            }

            // check for perfect fit in
            if (getBlockAt(block.getPosition().setY(block.getPosition().y + 1)) == null) allAboveOtherBlock = false;
        }

        // iterate through columns
        for (int i = 0; i < gridSize.x; i++) {

            // check for perfect fit above
            if (getBlockAt(row.blocks.get(0).getPosition().setX(i)) != null) inEmptyRow = false;
        }

        // see if fits
        if (!inEmptyRow || allAboveOtherBlock) {
            successPerfectFit();
        } else {
            failPerfectFit();
        }

        // remove block row object
        blockRow.remove(row);

        // add blocks in block row array to grid array
        blocks.addAll(row.blocks);

        // check for new rows formed
        removeRows();
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

    // get whether space in grid is valid
    public boolean isOpen(Vector cell) {

        // make sure space is in grid and nothing is in spot
        return contains(cell) && getBlockAt(cell) == null && (player == null || !cell.equals(player.getPosition()));
    }

    // get whether space in grid is valid
    public boolean isOpenNoPlayer(Vector cell) {

        // make sure space is in grid and nothing is in spot
        return contains(cell) && getBlockAt(cell) == null;
    }

    // add score to counter
    private void addScore(int amount) {

        // add to counter
        score += amount;

        // print new score
        System.out.println(score);
    }

    // called on a perfect block row fit
    private void successPerfectFit() {

        // add to score
        addScore(currentPerfectFitScore);

        // increment current score multiplier
        currentPerfectFitScore += perfectFitScoreCount;
    }

    // called on a failed block row fit
    private void failPerfectFit() {

        // reset fit score multiplier
        currentPerfectFitScore = perfectFitScoreStart;
    }

    // call to check for rows of blocks to remove
    public void removeRows() {

        for (int i = 0; i < gridSize.y; i++) {

            Block[] inRow = new Block[gridSize.x];

            for (Block block : blocks) {
                if (block.getPosition().y == i) inRow[block.getPosition().x] = block;
            }

            boolean shouldRemove = true;
            for (Block block : inRow) {
                shouldRemove = shouldRemove && block != null;
            }

            if (shouldRemove) {

                int rowIndex = gridSize.y - i + rowFillScoreInc;
                addScore(rowIndex * rowIndex * rowFillScoreMul);

                for (Block block : inRow) {
                    blocks.remove(block);
                }
                for (Block block : blocks) {
                    if (block.getPosition().y < i) block.move(Block.gravity);
                }
                if (player.getPosition().y < i) player.move(Block.gravity);

                i--;
            }
        }

        mergeRows();
    }

    // call to check if any rows in grid may be merged
    public void mergeRows() {

        boolean changed = false;

        for (int i = 0; i < gridSize.y - 1; i++) {

            Block[] rowTop = new Block[gridSize.x];
            Block[] rowBottom = new Block[gridSize.x];

            for (Block block : blocks) {
                if (block.getPosition().y == i) rowTop[block.getPosition().x] = block;
                if (block.getPosition().y == i + 1) rowBottom[block.getPosition().x] = block;
            }

            boolean notBlocked = true;
            boolean anyTop = false;
            for (int j = 0; j < gridSize.x; j++) {
                notBlocked = notBlocked && (rowTop[j] == null || (rowBottom[j] == null && !player.getPosition().equals(new Vector(j, i + 1))));
                anyTop = anyTop || rowTop[j] != null;
            }

            if (notBlocked && anyTop) {

                changed = true;

                for (Block block : blocks) {
                    if (block.getPosition().y <= i) block.move(Block.gravity);
                }
                if (player.getPosition().y < i || (rowBottom[player.getPosition().x] == null && player.getPosition().y <= i)) player.move(Block.gravity);

                i--;
            }
        }

        if (changed) removeRows();
    }
}
