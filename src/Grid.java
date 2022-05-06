import processing.core.PApplet;

public abstract class Grid {

    public int cellSize;
    public Vector gridSize;

    public Grid(Vector gridSize, int cellSize) {
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

    public void draw(PApplet applet) {
        for (int i = 0; i < getObjectCount(); i++) {
            getObject(i).draw(applet, this);
        }
    }
}
