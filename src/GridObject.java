import processing.core.PApplet;
import processing.core.PImage;

// object which exists within a cell of a grid
public class GridObject<T extends Grid> {

    // grid object
    public final T grid;

    // sprite image of object
    public PImage sprite;

    // enclosing cell position
    private Vector position;

    // initialize grid object
    public GridObject(Vector position, PImage sprite, T grid) {

        // store settings
        this.grid = grid;
        this.sprite = sprite;
        this.position = position;
    }

    // move player in direction
    public boolean move(Vector amount) {

        // get new position
        Vector newPos = Vector.translate(position, amount);

        // check if position exists within grid
        if (grid.contains(newPos)) {

            // set player position to new position
            position = Vector.translate(position, amount);
            return true;
        }

        // return no movement
        return false;
    }

    // draw object in grid cell
    public void draw(PApplet applet) {

        // make grid draw sprite
        grid.drawSprite(position, sprite, applet);
    }

    // called on every major game step
    public void step() {
        return;
    }

    // getter for player position
    public Vector getPosition() {
        return position;
    }

    // get player position if they moved in direction
    public Vector inDirection(Vector amount) {

        // translate current position by direction
        return Vector.translate(getPosition(), amount);
    }
}
