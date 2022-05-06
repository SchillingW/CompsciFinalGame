import processing.core.PApplet;

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

    public static Vector translate(Vector vector1, Vector vector2) {
        return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    public static Vector scale(Vector vector1, Vector vector2) {
        return new Vector(vector1.x * vector2.x, vector1.y * vector2.y);
    }

    public static Vector shrink(Vector vector1, Vector vector2) {
        return new Vector(vector1.x / vector2.x, vector1.y / vector2.y);
    }

    public void drawEllipseFromCenter(Vector size, PApplet applet) {
        applet.ellipse(x, y, size.x, size.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
