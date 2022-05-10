import processing.core.PApplet;
import processing.core.PImage;

// generic grid class for games
public abstract class Grid {

    // sprite for each tile in grid
    public final PImage tileSprite;

    // size of grid
    public final int cellSize;
    public final Vector gridSize;

    // initialize grid object
    public Grid(Vector gridSize, int cellSize, PImage tileSprite) {

        // store settings
        this.tileSprite = tileSprite;
        this.cellSize = cellSize;
        this.gridSize = gridSize;
    }

    // abstract methods for getting objects in grid
    public abstract GridObject getObject(int index);
    public abstract int getObjectCount();

    // convert position from grid space to screen space
    public Vector cellToWorldPos(Vector cell) {
        return Vector.translate(Vector.scale(cell, new Vector(cellSize)), new Vector(cellSize / 2));
    }

    // convert scale factor from grid space to screen space
    public Vector cellToWorldScale(Vector size) {
        return Vector.scale(size, new Vector(cellSize));
    }

    // check if cell is within the bounds of grid
    public boolean contains(Vector cell) {
        return cell.isAbove(new Vector(-1)) && cell.isBelow(gridSize);
    }

    // draw game visuals
    public void draw(PApplet applet) {

        // iterate through grid spaces
        for (int i = 0; i < gridSize.x; i++) {
            for (int j = 0; j < gridSize.y; j++) {

                // draw tile in space
                drawSprite(new Vector(i, j), tileSprite, applet);
            }
        }

        // iterate through grid objects
        for (int i = 0; i < getObjectCount(); i++) {

            // draw object
            getObject(i).draw(applet);
        }
    }

    // draw sprite on screen based on grid based info
    public void drawSprite(Vector cell, PImage sprite, PApplet applet) {

        // draw sprite after converting position and scale to screen space
        cellToWorldPos(cell).drawSpriteFromCenter(cellToWorldScale(new Vector(1)), sprite, applet);
    }

    // called every time a major time step is passed
    public void step() {

        // iterate through grid objects
        for (int i = 0; i < getObjectCount(); i++) {

            // make objects step
            getObject(i).step();
        }
    }
}
