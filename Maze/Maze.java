package Maze;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;


public class Maze {
    protected static int BOARD_SIZE;
    private static int distance;
    protected Vertex[][][][] vertices;
    protected ArrayList<Wall> walls;
    protected ArrayList<Edge> edges;
    // "Sets" represent connected traversable pathways of the maze isolated off by walls
    //protected ArrayList<Vertex> sets;
    protected int numSets = 0; // Since amount of set merges is constant, we can just use a counter instead

    public Maze(){
        Scanner reader;
        try{
            reader = new Scanner(new File("parameter.txt"));
            BOARD_SIZE = reader.nextInt();
            reader.close();
        }
        catch(IOException io){
            io.printStackTrace();
        }

        long startTime = System.nanoTime();
        vertices = new Vertex[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
        //edges = new ArrayList<Edge>();
        walls = new ArrayList<Wall>();
        //sets = new ArrayList<Vertex>();

        initializeVertices();
        System.out.println("INITIAL COUNT IS: " + walls.size());
        generateMaze();
        writeBytes();
        long executionTime = System.nanoTime() - startTime;
        System.out.println("Complete, elapsed time: " + executionTime + " ns");
    }

    /**
     * Creates (BOARD_SIZE)^4 vertices. Initially, each vertex is placed in it's
     * own "set" with walls around each on all sides
     */
    public void initializeVertices(){
        Vertex temp;
        for(int t = 0; t < BOARD_SIZE; t++){
            for(int z = 0; z < BOARD_SIZE; z++){
                for(int y = 0; y < BOARD_SIZE; y++){
                    for(int x = 0; x < BOARD_SIZE; x++){
                        temp = new Vertex(t,z,y,x);
                        if(x > 0){
                            //edges.add(new Edge(temp, vertices[t][z][y][x-1]));
                            walls.add(new Wall(temp, vertices[t][z][y][x-1]));
                            //temp.createEdge(vertices[t][z][y][x-1]);
                        }
                        if(y > 0){
                            //edges.add(new Edge(temp, vertices[t][z][y-1][x]));
                            walls.add(new Wall(temp, vertices[t][z][y-1][x]));
                            //temp.createEdge(vertices[t][z][y-1][x]);
                        }
                        if(z > 0){
                            //edges.add(new Edge(temp, vertices[t][z-1][y][x]));
                            walls.add(new Wall(temp, vertices[t][z-1][y][x]));
                            //temp.createEdge(vertices[t][z-1][y][x]);
                        }
                        if(t > 0){
                            //edges.add(new Edge(temp, vertices[t-1][z][y][x]));
                            walls.add(new Wall(temp, vertices[t-1][z][y][x]));
                            //temp.createEdge(vertices[t-1][z][y][x]);
                        }
                        vertices[t][z][y][x] = temp;
                        numSets++;

                        //sets.add(temp);
                    }
                }
            }
        }
        System.out.println((vertices.length*vertices.length*vertices.length*vertices.length)
                + " VERTICES INITIALIZED FOR BOARD SIZE: " + BOARD_SIZE);
        //System.out.println("INITIAL SET SIZE: " + sets.size());
    }

    /**
     * Displays the number of walls present in the maze
     * (This is the maximum # at initialization time)
     * iterates through and displays the position of each
     */
    public void showWalls(){
        System.out.println("Wall count: " + walls.size());
        for(int i = 0; i < walls.size(); i++){
            System.out.println(walls.get(i));
        }
    }

    /**
     * Iterates through and prints each vertex
     */
    public void showVertices() {
        int count = 0;
        for(int t = 0; t < BOARD_SIZE; t++){
            for (int z = 0; z < BOARD_SIZE; z++){
                for (int y = 0; y < BOARD_SIZE; y++){
                    for(int x = 0; x < BOARD_SIZE; x++){
                        System.out.print("#" + ++count + ": " + vertices[t][z][y][x] + "\t");
                    }
                    System.out.println("Y INCREMENTED\n");
                }
                System.out.println("Z INCREMENTED\n");
            }
            System.out.println("T INCREMENTED\n");
        }
    }

    /**
     * Given two vertices, removes the wall that separates the two vertices
     * @param a The first vertex the wall separates
     * @param b The other vertex
     */
    public void removeWall(Vertex a, Vertex b){
        Wall temp;
        for(int i = 0; i < walls.size(); i++){
            temp = walls.get(i);
            // If the wall matches the one we are looking for:
            if (((temp.a == a) && (temp.b == b)) || ((temp.b == a) && (temp.a == b))){
                walls.remove(temp);
                break;
            }
        }
    }

    /**
     * Performs recursive call to traverse upwards
     * until the root of a particular node is found
     * @param v The vertex to find the root of
     * @return The root of the subtree
     */
    public Vertex findRoot(Vertex v){
        if(v.parent == null){
            return v;
        }
        else{
            distance++;
            return findRoot(v.parent);
        }
    }

    /**
     * Keeps track of the number of recursive calls necessary to reach
     * the root from a given vertex using the findRoot method, assigns
     * this value to a member of the root vertex
     * @param v The given vertex to start from
     * @return The root vertex reached from the given vertex
     */
    public Vertex calculateDistanceToRoot(Vertex v){
        distance = 0;
        Vertex root = findRoot(v);
        if(distance > root.distanceToLeaf){
            root.distanceToLeaf = distance;
        }
        // "Path Compression"
        if(!root.equals(v)){
            v.parent = root;
        }
        return root;
    }

    /**
     * Merges two sets only if they are disjoint, assigning the root
     * of the smaller set of the two as a child of the root of the other
     * @param a Randomly selected vertex
     * @param b Randomly selected vertex adjacent (connected) to vertex a
     */
    public void joinSets(Vertex a, Vertex b){
        Vertex rootA = calculateDistanceToRoot(a);
        Vertex rootB = calculateDistanceToRoot(b);
        Edge temp;

        /* Points smaller subtree's parent to root when merging to help reduce
           number of recursive calls needed to find the root of a vertex in a set
           (creates a tree with less height to traverse)*/
        if(rootA != rootB){
            if(rootA.distanceToLeaf < rootB.distanceToLeaf){
                rootA.parent = rootB;
                //rootB.children.add(rootA);

                //sets.remove(rootA);
            }
            else{
                rootB.parent = rootA;
                //rootA.children.add(rootB);

                //sets.remove(rootB);
            }

            // Add an edge between the two adjacent vertices as the sets are merged
            temp = new Edge(a,b);
            //edges.add(temp);
            a.createEdge(temp);
            numSets--;
        }

    }

    /**
     * Iterates through edges of the maze and removes the given edge
     * @param e The edge used to pass the vertices
     */
    public void removeEdge(Edge e){
        for(Edge x : edges){
            if(x.equals(e)){
                edges.remove(x);
                break;
            }
        }
    }

    /**
     * Iterates through walls placed in the maze and
     * removes the corresponding edges, places the final
     * list of edges into their corresponding adjacency lists
     */
    public void updateEdges(){
        for(Wall w : walls){
            removeEdge(new Edge(w.a, w.b));
        }
    }

    /**
     * Randomly selects a wall and checks to see if it's vertices are in the same
     * set (traversable path). If so the wall is moved to the end of the list and
     * has been "checked". If they are part of two different paths (findRoot returns
     * two different vertices) then joinSets method is called and they are "merged"
     * and the wall object separating vertex 1 and vertex 2 is "checked and an edge is
     * created between the two vertices.
     *
     * This ensures that no cycle (when a wall separating two vertices of the same
     * "set" is removed) ever occurs. Random removal of walls following this convention
     * of disjoint sets until only a single set remains leads to a maze that utilizes every vertex
     * (maze section) without creating a loop since all possible walls are placed at initialization.
     */
    // FIXME Pass index of wall object to be removed if same set so that we have an updated list of
    //  walls at the end of generation
    public void generateMaze(){
        Wall tempWall, lastWall;
        Random rand = new Random();
        int randIndex;
        int wallsEndIndex = walls.size() - 1;

        /* A pathway from start to exit must exist when the board of
           traversable sections can be represented by a single set*/
        while(numSets > 1){
            randIndex = rand.nextInt(wallsEndIndex + 1);
            tempWall = walls.get(randIndex);
            joinSets(tempWall.a, tempWall.b);
            // Swaps the chosen wall with the last wall in the list since it is now "checked"
            // indexing is also adjusted so that it is not selected again
            lastWall = walls.get(wallsEndIndex);
            walls.set(wallsEndIndex, tempWall);
            walls.set(randIndex, lastWall);
            wallsEndIndex--;
        }

        //System.out.println("\nFINAL EDGE COUNT IS: " + edges.size());
        System.out.println("\nMaze generation complete!\n");
        System.out.println("Writing bytes . . .\n");
    }

    /**
     * Iterates through a given vertex's adjacency list and checks
     * possible movement on each axis. A byte in the form:
     * (t-)(t+) (z-)(z+) (y-)(y+) (x-)(x+) is used to represent such.
     * @param v The given vertex to check movement for
     * @return The formatted possible movements byte
     */
    public byte createByte(Vertex v){
        Integer decimalValue = 0;

        int vertexT = v.t;
        int vertexZ = v.z;
        int vertexY = v.y;
        int vertexX = v.x;
        Vertex temp;

        /* Checks possible movement from a given vertex by iterating
           through its edges and setting movement conditions
         */
        for (Edge e : v.adj){
            temp = e.getVertex(v);
            if(vertexT < temp.t){v.tIncrement = true;} // t+ movement
            else if(vertexT > temp.t){v.tDecrement = true;} // t- movement
            else if(vertexZ < temp.z){v.zIncrement = true;} // z+ movement
            else if(vertexZ > temp.z){v.zDecrement = true;} // z- movement
            else if(vertexY < temp.y){v.yIncrement = true;} // y+ movement
            else if(vertexY > temp.y){v.yDecrement = true;} // y- movement
            else if(vertexX < temp.x){v.xIncrement = true;} // x+ movement
            else if(vertexX > temp.x){v.xDecrement = true;} //  x- movement
        }

        /* In 1 byte string possible movement is represented as
           (t-)(t+) (z-)(z+) (y-)(y+) (x-)(x+)
           Where a 0 in a position represents possible movement in that
           direction on the corresponding axis, while a 1 represents not

           EX: 11 11 10 01
           No possible t or z movement, movement only in + direction along
           the y axis, and only in - direction along the x axis
         */
        if(!v.xIncrement){ // Bit position 2^0
            decimalValue +=1;
        }
        if(!v.xDecrement){ // 2^1
            decimalValue +=2;
        }
        if(!v.yIncrement){ // 2^2
            decimalValue +=4;
        }
        if(!v.yDecrement){ // 2^3
            decimalValue +=8;
        }
        if(!v.zIncrement){ // 2^4
            decimalValue +=16;
        }
        if(!v.zDecrement){ // 2^5
            decimalValue +=32;
        }
        if(!v.tIncrement) { // 2^6
            decimalValue += 64;
        }
        if(!v.tDecrement){ // 2^7
            decimalValue += 128;
        }

        return decimalValue.byteValue();
    }

    /**
     * Writes the possible movements byte to a maze.txt for
     * every vertex of the maze from (0,0,0,0) : (N-1,N-1,N-1,N-1)
     */
    public void writeBytes(){
        FileOutputStream fw;
        Vertex temp;
        byte b;

        try{
            fw = new FileOutputStream(new File("maze.txt"));
            for(int t = 0; t < BOARD_SIZE; t++){
                for(int z = 0; z < BOARD_SIZE; z++){
                    for(int y = 0; y < BOARD_SIZE; y++){
                        for(int x = 0; x < BOARD_SIZE; x++){
                            temp = vertices[t][z][y][x];
                            b = createByte(temp);
                            //temp.movement = b;
                            fw.write(b);
                        }
                    }
                }
            }
            fw.close();
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Iterates through vertices and prints each vertex's edges,
     * printing the number of edges at the end
     */
    public void showEdges() {
        Vertex temp;
        int edgeCount = 0;
        for(int t = 0; t < BOARD_SIZE; t++){
            for (int z = 0; z < BOARD_SIZE; z++) {
                for (int y = 0; y < BOARD_SIZE; y++) {
                    for (int x = 0; x < BOARD_SIZE; x++) {
                        temp = vertices[t][z][y][x];
//                        System.out.println("VERTEX " + temp + "has edges:");
                        for (Edge e : temp.adj) {
//                            System.out.println(e);
                            edgeCount++;
                        }
                    }
                }
            }
        }
        System.out.println("\nEDGE COUNT IS: " + edgeCount);
    }
}
