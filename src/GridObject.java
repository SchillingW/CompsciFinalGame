import processing.core.PApplet;
import processing.core.PImage;

public class GridObject<T extends Grid> {

    public final T grid;

    public PImage sprite;

    private Vector position;

    public GridObject(Vector position, PImage sprite, T grid) {
        this.grid = grid;
        this.sprite = sprite;
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
        grid.drawSprite(position, sprite, applet);
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
