import processing.core.PApplet;
import processing.core.PImage;

// 2d vector object
public class Vector {

    // directional components
    public final int x;
    public final int y;

    // initialize vector
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // initialize vector with equal components
    public Vector(int factor) {
        this(factor, factor);
    }

    // initialize neutral vector
    public Vector() {
        this(0);
    }


    // check if vector is above desired position in both directions
    public boolean isAbove(Vector min) {
        return x > min.x && y > min.y;
    }

    // check if vector is below desired position in both directions
    public boolean isBelow(Vector max) {
        return x < max.x && y < max.y;
    }

    // negate both components of vector
    public Vector negate() {
        return Vector.scale(this, new Vector(-1));
    }

    // translate vector by another
    public static Vector translate(Vector vector1, Vector vector2) {
        return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    // scale vector by another
    public static Vector scale(Vector vector1, Vector vector2) {
        return new Vector(vector1.x * vector2.x, vector1.y * vector2.y);
    }

    // shrink vector by another
    public static Vector shrink(Vector vector1, Vector vector2) {
        return new Vector(vector1.x / vector2.x, vector1.y / vector2.y);
    }

    // draw sprite centered at vector position
    public void drawSpriteFromCenter(Vector size, PImage sprite, PApplet applet) {

        // get top left corner of sprite from center position and size
        Vector corner = Vector.translate(this, Vector.shrink(size, new Vector(2)).negate());

        // draw sprite on screen
        applet.image(sprite, corner.x, corner.y, size.x, size.y);
    }

    // check if vectors are equal by components
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector vector = (Vector) other;
        return x == vector.x && y == vector.y;
    }

    // get point notation of vector as string
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
