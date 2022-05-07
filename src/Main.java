import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {

    private GameGrid grid;
    private MultiKeyMap<Character, Vector> dpadMapping;
    private StepDevice stepDevice;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {

        dpadMapping = new MultiKeyMap<>(
                new Character[][] {
                        new Character[] {'d'},
                        new Character[] {'s'},
                        new Character[] {'a'},
                        new Character[] {'w'}
                },
                new Vector[] {
                        new Vector(1, 0),
                        new Vector(0, 1),
                        new Vector(-1, 0),
                        new Vector(0, -1)
                }
        );

        PImage playerRightSprite = loadImage("art\\PlayerRight.png");
        PImage playerLeftSprite = loadImage("art\\PlayerLeft.png");
        PImage blockSprite = loadImage("art\\Block.png");
        PImage tileSprite = loadImage("art\\Tile.png");

        Block blockTemplate = new Block(2, blockSprite);
        Player playerTemplate = new Player(false, 1, playerRightSprite, playerLeftSprite);

        grid = new GameGrid(
                new Vector(9, 11), 16, 0.5, 32,
                tileSprite, playerTemplate, blockTemplate);

        Vector windowSize = grid.cellToWorldScale(grid.gridSize);
        size(windowSize.x, windowSize.y);

        stepDevice = new StepDevice(System.currentTimeMillis(), 250);
    }

    @Override
    public void draw() {
        if (stepDevice.setInterval(System.currentTimeMillis())) grid.step();
        background(0);
        grid.draw(this);
    }

    @Override
    public void keyPressed() {
        Vector dpadInput = dpadMapping.get(key);
        if (dpadInput != null) grid.player.moveDirection = dpadInput;
        grid.player.moveForce();
    }

    @Override
    public void keyReleased() {
        Vector dpadInput = dpadMapping.get(key);
        if (dpadInput != null && dpadInput.equals(grid.player.moveDirection)) grid.player.moveDirection = new Vector();
    }
}
