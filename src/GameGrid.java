import java.util.ArrayList;

public class GameGrid extends Grid {

    public final Player player;
    public final ArrayList<Block> blocks;

    private final StepDevice spawnTimer;

    private final Block blockTemplate;

    public final double blockSpawnRate;

    public GameGrid(
            Vector gridSize, long spawnSteps, double blockSpawnRate, int cellSize,
            Player playerTemplate, Block blockTemplate) {

        super(gridSize, cellSize);
        blocks = new ArrayList<>();
        player = playerTemplate.asTemplate(this);
        spawnTimer = new StepDevice(spawnSteps);
        this.blockTemplate = blockTemplate;
        this.blockSpawnRate = blockSpawnRate;
    }

    @Override
    public GridObject getObject(int index) {
        if (index == 0) return player;
        return blocks.get(index - 1);
    }

    @Override
    public int getObjectCount() {
        return blocks.size() + 1;
    }

    @Override
    public void step() {
        super.step();
        if (spawnTimer.addInterval(1)) spawn();
    }

    public void spawn() {
        for (int i = 0; i < gridSize.x; i++) {
            if (Math.random() < blockSpawnRate) {
                blocks.add(blockTemplate.asTemplate(i, this));
            }
        }
    }

    public Block getBlockAt(Vector cell) {
        for (Block block : blocks) {
            if (cell.equals(block.getPosition())) return block;
        }
        return null;
    }
}
