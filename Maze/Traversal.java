package Maze;

import java.util.Random;

public class Traversal {
    private Maze m;

    public Traversal(){
        m = new Maze();
    }

    public boolean checkVisited(){
        Vertex temp;
        boolean pass = true;
        for(int t = 0; t < Maze.BOARD_SIZE; t++){
            if(!pass){
                break;
            }
            for(int z = 0; z < Maze.BOARD_SIZE; z++){
                if(!pass){
                    break;
                }
                for(int y = 0; y < Maze.BOARD_SIZE; y++){
                    if(!pass){
                        break;
                    }
                    for(int x = 0; x < Maze.BOARD_SIZE; x++){
                        temp = m.vertices[t][z][y][x];
                        if(!temp.visited){
                            //System.out.println("VERTEX " + temp + " WAS NOT VISITED");
                            pass = false;
                            break;
                        }
                        else{
                            temp.visited = false;
                        }
                    }
                }
            }
        }
        //checkNum++;
        if(pass){
            //System.out.println("SUCCESSFUL CHECK STARTING @ Vertex " + checkNum);
            return true;
        }
        else{
           // System.out.println("CHECK FAILED @ Vertex " + checkNum);
            return false;
        }
    }

   public void explore(Vertex v){
        Vertex temp;
        v.visited = true;
       if(v.adj.size() > 0) {
           for(Edge e : v.adj){
               temp = e.getVertex(v);
               if(!temp.visited){
                   explore(temp);
               }
           }
       }
   }

   public void showVertices(){
       for(int t = 0; t < Maze.BOARD_SIZE; t++) {
           for (int z = 0; z < Maze.BOARD_SIZE; z++) {
               for (int y = 0; y < Maze.BOARD_SIZE; y++) {
                   for (int x = 0; x < Maze.BOARD_SIZE; x++) {
                       System.out.println(m.vertices[t][z][y][x]);
                   }
               }
           }
       }
   }

   public void checkRandom(int numChecks){
        int randT, randZ, randY, randX;
        Random rand = new Random();
        Vertex temp;
        boolean pass;
        for(int i = 0; i < numChecks; i++){
            randT = rand.nextInt(Maze.BOARD_SIZE);
            randZ = rand.nextInt(Maze.BOARD_SIZE);
            randY = rand.nextInt(Maze.BOARD_SIZE);
            randX = rand.nextInt(Maze.BOARD_SIZE);
            temp = m.vertices[randT][randZ][randY][randX];
            explore(temp);
            pass = checkVisited();
            if(pass){
                System.out.println("VERTEX " + temp + " PASS");
            }
            else{
                System.out.println("FAILED AT " + temp);
            }
        }
   }

//    public boolean traverseAll(Vertex v){
//        int vertexToCheck = 0;
//
//        while(vertexToCheck < 625){
//            if(vertexToCheck == v.vertexNumber){
//                vertexToCheck++;
//                System.out.println("INCREMENTING CHECK TO: " + vertexToCheck);
//            }
//            if(!explore(v,vertexToCheck)){
//                return false;
//            } else{
//                System.out.println("VERTEX #" + vertexToCheck + " REACHED FROM VERTEX " + v.vertexNumber);
//                vertexToCheck++;
//            }
//        }
//        return true;
//    }

    public void checkConnectivity(){
        Vertex temp;
        boolean passed = true;
        for(int t = 0; t < Maze.BOARD_SIZE; t++){
            if(!passed){
                break;
            }
            for(int z = 0; z < Maze.BOARD_SIZE; z++){
                if(!passed){
                    break;
                }
                for(int y = 0; y < Maze.BOARD_SIZE; y++){
                    if(!passed){
                        break;
                    }
                    for(int x = 0; x < Maze.BOARD_SIZE; x++){
                        temp = m.vertices[t][z][y][x];
                        //System.out.println("TEMP IS: " + temp);
                        explore(temp);
                        passed = checkVisited();
                        if(!passed){
                            break;
                        }
                        System.out.println("CHECKED " + temp);
                        //System.out.println("END");
                    }
                }
            }
        }
        if(!passed){
            System.out.println("FAILED");
        }
        else{
            System.out.println("GRAPH IS CONNECTED");
        }

    }
}
