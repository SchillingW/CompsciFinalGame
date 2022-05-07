import processing.core.PApplet;

public class GridObject<T extends Grid> {

    public final T grid;

    private Vector position;

    public GridObject(Vector position, T grid) {
        this.grid = grid;
        this.position = position;
    }

    public boolean move(Vector amount) {
        Vector newPos = Vector.translate(position, amount);
        if (grid.contains(newPos)) {
            position = Vector.translate(position, amount);
            return true;
        }
        return false;
    }

    public void draw(PApplet applet) {
        grid.cellToWorldPos(position).drawEllipseFromCenter(grid.cellToWorldScale(new Vector(1)), applet);
    }

    public void step() {
        return;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector inDirection(Vector amount) {
        return Vector.translate(getPosition(), amount);
    }
}
