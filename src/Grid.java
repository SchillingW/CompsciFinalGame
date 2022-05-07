import processing.core.PApplet;
import processing.core.PImage;

public abstract class Grid {

    public final PImage tileSprite;

    public final int cellSize;
    public final Vector gridSize;

    public Grid(Vector gridSize, int cellSize, PImage tileSprite) {
        this.tileSprite = tileSprite;
        this.cellSize = cellSize;
        this.gridSize = gridSize;
    }

    public abstract GridObject getObject(int index);
    public abstract int getObjectCount();

    public Vector cellToWorldPos(Vector cell) {
        return Vector.translate(Vector.scale(cell, new Vector(cellSize)), new Vector(cellSize / 2));
    }

    public Vector cellToWorldScale(Vector size) {
        return Vector.scale(size, new Vector(cellSize));
    }

    public boolean contains(Vector cell) {
        return cell.isAbove(new Vector(-1)) && cell.isBelow(gridSize);
    }

    public void draw(PApplet applet) {

        for (int i = 0; i < gridSize.x; i++) {
            for (int j = 0; j < gridSize.y; j++) {
                drawSprite(new Vector(i, j), tileSprite, applet);
            }
        }

        for (int i = 0; i < getObjectCount(); i++) {
            getObject(i).draw(applet);
        }
    }

    public void drawSprite(Vector cell, PImage sprite, PApplet applet) {
        cellToWorldPos(cell).drawSpriteFromCenter(cellToWorldScale(new Vector(1)), sprite, applet);
    }

    public void step() {
        for (int i = 0; i < getObjectCount(); i++) {
            getObject(i).step();
        }
    }
}
