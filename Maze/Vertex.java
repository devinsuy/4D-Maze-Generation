package Maze;
import java.util.ArrayList;

public class Vertex {
    private static int vertexCount = 0;
    protected Vertex parent = null;
    protected ArrayList<Edge> adj;
    //protected ArrayList<Vertex> children;
    protected int t,z,x,y;
    protected int distanceToLeaf = -1;
    protected int vertexNumber;
    //protected byte movement;

    protected boolean visited = false;

    /* A value of increment represents possible movement in the + direction
     on this axis, decrement represents possible movement in the - direction
     otherwise no movement is possible on this axis
     */
    boolean xIncrement, xDecrement;
    boolean yIncrement, yDecrement;
    boolean zIncrement, zDecrement;
    boolean tIncrement, tDecrement;


    public Vertex(int t, int z, int y, int x){
        adj = new ArrayList<Edge>();
        //children = new ArrayList<Vertex>();
        this.t = t;
        this.z = z;
        this.x = x;
        this.y = y;
        vertexNumber = vertexCount++;
        xIncrement = xDecrement = yIncrement = yDecrement = zIncrement
                = zDecrement = tIncrement = tDecrement = false;
    }

    public void createEdge(Vertex v){
        Edge temp = new Edge(v,this);
        adj.add(temp);
        v.adj.add(temp);
    }

    public void createEdge(Edge e){
        adj.add(e);
        e.getVertex(this).adj.add(e);
    }

    public void removeEdge(Vertex b){
        Edge temp;
        for(int i = 0; i < adj.size(); i++){
            temp = this.adj.get(i);
            if (((temp.a.equals(this)) && (temp.b.equals(b))) || ((temp.b.equals(this)) && (temp.a.equals(b)))){
                this.adj.remove(i);
                b.adj.remove(temp);
                break;
            }
        }
    }

    public void showAdj(){
        System.out.print(this + " is adjacent to: ");
        for(int i = 0; i < adj.size(); i++){
            System.out.print(adj.get(i) + ", ");
        }
    }

    @Override
    public String toString() {
        return "#" + vertexNumber + ": (" + t + "," + z + "," + y + "," + x + ")";
    }

    public boolean equals(Vertex b) {
        return (
                (this.t == b.t) && (this.z == b.z) && (this.y == b.y) && (this.x == b.x)
        );
    }
}
