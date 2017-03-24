/**
 * Grid Model Object.
 *
 * <P>Various attributes of square matrix used for the Tron game.
 *
 * @author Thomas Chen
 * @version 0.1
 */

public class Grid {

    //2D Matrix Representing Tron Grid
    public boolean[][] grid;

    /**
     * Constructs a Grid object. The Grid is always a square matrix
     * of dimensions n x n.
     * @param n the length and width of the grid
     */
    public Grid(int n) {
        this.grid = new boolean[n][n];

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                grid[i][j] = false;
            }
        }
    }

    /**
     * ASCII Print function for Grid Class, prints Grid with "-"
     * horizontal dividers and "|" vertical dividers.
     */
    public void print() {
        printDiv();
        for (int i = 0; i < grid.length; i++) {
            System.out.print("|");
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == true) {
                    System.out.print("X");
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                    System.out.print("|");
                }
            }
            printDiv();
        }
    }

    /**
     * Helper function for print() function to encapsulate logic
     * of printing dynamic horizontal dividers.
     */
    public void printDiv() {
        System.out.println();
        for (int i = 0; i <= grid.length * 2 ; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Checks whether given coordinate is within the bounds of the
     * matrix of the grid.
     * @param x the x position of the coordinate.
     * @param y the y position of the coordinate.
     * @return whether the coordinate is within the bounds.
     */
    public boolean isInBounds(int x, int y) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid.length && !grid[x][y]) {
            return true;
        } else
            return false;
    }

    /**
     * Accessor method for length or width of grid.
     * @return the length or width of the grid
     */
    public int getLength() {
        return grid.length;
    }

    /**
     * Occupies the index position of the grid at a specific
     * coordinate by setting boolean value to true.
     * @param x the x position of the coordinate.
     * @param y the y position of the coordinate.
     */
    public void occupy(int x, int y) {
        grid[x][y] = true;
    }

    /**
     * Returns the number of empty adjacent positions to a
     * given coordinate.
     * @param x the x position of the coordinate.
     * @param y the y position of the coordinate.
     * @return the number of empty adjacent positions.
     */
    public int emptyNeightbors(int x, int y) {
        int numEmpty = 0;

        if (isInBounds(x, y-1) && !grid[x][y - 1]) {
            numEmpty++;
        }

        if (isInBounds(x, y+1) && !grid[x][y + 1]) {
            numEmpty++;
        }

        if (isInBounds(x-1, y) && !grid[x - 1][y]) {
            numEmpty++;
        }

        if (isInBounds(x+1, y) && !grid[x + 1][y]) {
            numEmpty++;
        }

        return numEmpty;
    }

    /**
     * Returns the total number of occupied positions.
     * @return total number of occupied positions.
     */
    public int numCovered() {
        int covered = 0;

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if(grid[i][j]) {
                    covered++;
                }
            }
        }

        return covered;
    }

    /**
     * Clears grid
     */
    public void clearGrid() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                grid[i][j] = false;
            }
        }
    }
}

