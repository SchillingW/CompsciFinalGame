import processing.core.PApplet;
import processing.core.PImage;

public class Vector {

    public final int x;
    public final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(int factor) {
        this(factor, factor);
    }

    public Vector() {
        this(0);
    }

    public boolean isAbove(Vector min) {
        return x > min.x && y > min.y;
    }

    public boolean isBelow(Vector max) {
        return x < max.x && y < max.y;
    }

    public Vector negate() {
        return Vector.scale(this, new Vector(-1));
    }

    public static Vector translate(Vector vector1, Vector vector2) {
        return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    public static Vector scale(Vector vector1, Vector vector2) {
        return new Vector(vector1.x * vector2.x, vector1.y * vector2.y);
    }

    public static Vector shrink(Vector vector1, Vector vector2) {
        return new Vector(vector1.x / vector2.x, vector1.y / vector2.y);
    }

    public void drawSpriteFromCenter(Vector size, PImage sprite, PApplet applet) {
        Vector corner = Vector.translate(this, Vector.shrink(size, new Vector(2)).negate());
        applet.image(sprite, corner.x, corner.y, size.x, size.y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector vector = (Vector) other;
        return x == vector.x && y == vector.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
