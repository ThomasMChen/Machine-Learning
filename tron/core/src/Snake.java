import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Snake Model Object.
 *
 * <P>Various attributes of a "snake" based on the Tron game, and related behaviour.
 *
 * @author Thomas Chen
 * @version 0.1
 */

public class Snake {
    //Static Variable for global map
    public static Grid map = new Grid(20);

    //Current x & y position of Snake
    public int xpos;
    public int ypos;

    /**
     * Default Constructor for Snake Object
     */
    public Snake() {
        xpos = ThreadLocalRandom.current().nextInt(0, map.getLength());
        ypos = ThreadLocalRandom.current().nextInt(0, map.getLength());
        map.occupy(xpos, ypos);
    }

    /**
     * Moves Snake to specified coordinates.
     * @param x the x position of the destination.
     * @param y the y position of the destination.
     */
    public void move(int x, int y) {
        xpos = x;
        ypos = y;

        map.occupy(xpos, ypos);
    }

    /**
     * Moves Snake one position west.
     * @return whether the move was successful.
     */
    public boolean moveWest() {
        if (map.isInBounds(xpos, ypos - 1)) {
            move(xpos, ypos - 1);
            return true;
        } else
            return false;
    }

    /**
     * Moves Snake one position east.
     * @return whether the move was successful.
     */
    public boolean moveEast() {
        if (map.isInBounds(xpos, ypos + 1)) {
            move(xpos, ypos + 1);
            return true;
        } else
            return false;
    }

    /**
     * Moves Snake one position south.
     * @return whether the move was successful.
     */
    public boolean moveSouth() {
        if (map.isInBounds(xpos + 1, ypos)) {
            move(xpos + 1, ypos);
            return true;
        } else
            return false;
    }

    /**
     * Moves Snake one position north.
     * @return whether the move was successful.
     */
    public boolean moveNorth() {
        if (map.isInBounds(xpos - 1, ypos)) {
            move(xpos - 1, ypos);
            return true;
        } else
            return false;
    }

    /*
    ** ------------------------------------------------------------
    ** FOR TESTING PURPOSES ONLY - TEMPORARY
    ** ------------------------------------------------------------
     */

    /**
     * Moves Snake once in a random direction.
     */
    public void randomMove() {
        int randDir;
        boolean again = true;

        while(again) {
            again = false;
            randDir = ThreadLocalRandom.current().nextInt(0, 4);
            switch (randDir) {
                case 0:
                    if (moveNorth()) {
                        System.out.println("North");
                        break;
                    }
                case 1:
                    if (moveSouth()) {
                        System.out.println("South");
                        break;
                    }
                case 2:
                    if (moveEast()) {
                        System.out.println("East");
                        break;
                    }
                case 3:
                    if (moveWest()) {
                        System.out.println("West");
                        break;
                    }
                default: again = true;
                    break;
            }
        }
    }

    public void lineOfSightMove() {


    }

    /**
     * Static testing method for simulating a game, and saving results.
     */
    public static void simulate() {
        Snake test1 = new Snake();
        //Snake test2 = new Snake();

        int alive = 0;

        do {
            alive = 0;
            if (map.emptyNeightbors(test1.xpos, test1.ypos) > 0) {
                test1.randomMove();
                alive++;

                map.print();
            }

            /*
            if (map.emptyNeightbors(test2.xpos, test2.ypos) > 0) {
                test2.randomMove();
                alive++;
            }
            */


        } while (alive > 0);

        System.out.println("SIM OVER");

        //Save Results
        String numCovered = Integer.toString(map.numCovered());
        String count = numCovered;
        try(FileWriter fw = new FileWriter("output.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(count);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

        map.clearGrid();
    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            simulate();
        }
    }
}
