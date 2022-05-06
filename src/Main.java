import processing.core.PApplet;

public class Main extends PApplet {

    public GameGrid grid;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        grid = new GameGrid(new Vector(9, 11), 32);
        Vector windowSize = grid.cellToWorldScale(grid.gridSize);
        size(windowSize.x, windowSize.y);
    }

    @Override
    public void draw() {
        background(0);
        grid.draw(this);
    }
}
