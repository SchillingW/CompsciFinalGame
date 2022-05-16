import processing.core.PApplet;
import processing.core.PImage;

// block dropping game with processing library
public class Main extends PApplet {

    // reference to game grid
    private GameGrid grid;

    // input mapping for movement directions
    private MultiKeyMap<Character, Vector> dpadMapping;
    private Character upwardsKey;

    // timer device for game steps
    private StepDevice stepDevice;

    // run application
    public static void main(String[] args) {
        PApplet.main("Main");
    }

    // called on application start
    @Override
    public void settings() {

        // map input keys to movement directions
        upwardsKey = 'p';
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

        // initialize sprite images
        PImage playerRightSprite = loadImage("art\\PlayerRight.png");
        PImage playerLeftSprite = loadImage("art\\PlayerLeft.png");
        PImage blockSpriteInteract = loadImage("art\\Block.png");
        PImage blockSpriteInvis = loadImage("art\\BlockSmall.png");
        PImage tileSprite = loadImage("art\\Tile.png");
        PImage dangerTileSprite = loadImage("art\\DangerTile.png");

        // create templates with settings for player and block objects to be created
        Block blockTemplate = new Block(blockSpriteInteract, blockSpriteInvis);
        BlockRow blockRowTemplate = new BlockRow(0.5, 4, dangerTileSprite, blockTemplate);
        Player playerTemplate = new Player(false, 1, playerRightSprite, playerLeftSprite, this);

        // initialize game grid object with gameplay settings
        grid = new GameGrid(
                new Vector(6, 6), 16, 40,
                tileSprite, playerTemplate, blockRowTemplate);

        // set window dimensions based on grid size
        Vector windowSize = grid.cellToWorldScale(grid.gridSize);
        size(windowSize.x, windowSize.y);

        // start game timer
        stepDevice = new StepDevice(System.currentTimeMillis(), 250);
    }

    // called every frame of gameplay
    @Override
    public void draw() {

        // step forward in time
        if (stepDevice.setInterval(System.currentTimeMillis())) grid.step();

        // redraw background to black
        background(0);

        // draw game visuals
        grid.draw(this);
    }

    // called when key input press detected
    @Override
    public void keyPressed() {

        // get desired input direction
        Vector dpadInput = dpadMapping.get(key);

        // if direction is valid set player to move in that direction
        if (dpadInput != null) grid.player.moveDirection = dpadInput;

        // make player move on input
        grid.player.moveForce();

        // if key is for upwards movement tell player
        if (key == upwardsKey) grid.player.upwards = true;
    }

    // called when kep input release detected
    @Override
    public void keyReleased() {

        // get released input direction
        Vector dpadInput = dpadMapping.get(key);

        // if released direction is current direction remove the direction
        if (dpadInput != null && dpadInput.equals(grid.player.moveDirection)) grid.player.moveDirection = new Vector();

        // if key is for upwards movement tell player
        if (key == upwardsKey) grid.player.upwards = false;
    }
}
