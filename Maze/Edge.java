package Maze;

public class Edge {
    protected Vertex a;
    protected Vertex b;

    // Represents an undirected edge between two vertices
    public Edge(Vertex aSrc, Vertex bSrc){
        a = aSrc;
        b = bSrc;
    }

    /**
     * Given a vertex on one end of the edge, return the other
     * vertex part of the edge
     * @param v The given vertex
     * @return The other vertex
     */
    public Vertex getVertex(Vertex v){
    	if(v.equals(a)) {
    		return b;
    	}
    	else {
    		return a;
    	}
    }

    @Override
    public String toString() {
        return "Edge from vertex " + a + " <-> " + b;
    }

    public boolean equals(Edge e) {
        return (
                ((this.a == e.a) && (this.b == e.b)) || ((this.a == e.b) && (this.b == e.a))
        );
    }
}
