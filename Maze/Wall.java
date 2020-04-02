package Maze;

public class Wall {
    Vertex a, b;
    protected boolean checked;

    // There is a wall in the section between vertex a and b
    public Wall(Vertex a, Vertex b){
        this.a = a;
        this.b = b;
        checked = false;
    }

    @Override
    public String toString() {
        return "Wall from " + a + " to " + b;
    }

}
