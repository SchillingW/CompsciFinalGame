import processing.core.PApplet;

public class GridObject {

    public int drawSize;
    public Vector position;

    public GridObject(Vector position, int drawSize) {
        this.drawSize = drawSize;
        this.position = position;
    }

    public GridObject(Vector position) {
        this(position, 1);
    }

    public void draw(PApplet applet, Grid grid) {
        grid.cellToWorldPos(position).drawEllipseFromCenter(grid.cellToWorldScale(new Vector(drawSize)), applet);
    }
}
