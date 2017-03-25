import java.util.concurrent.ThreadLocalRandom;

public class Bot {

  //TODO: HERE! IMPORTANT! Input your AUTH_KEY below!
  final static String AUTH_KEY = "AYABW1490393761176";

  static Integer[][] gameBoard;
  static int myPlayerNumber;
  static int totalPlayers;
  static int myCurrentRow = 6;
  static int myCurrentColumn = 0;

  public static void main(String[] args) {
    System.out.println("I am alive");
    Connection.connect();


}

  /*===========================================
    Start writing your code here!
    ===========================================*/
  public static String makeMove() {
      if (gameBoard == null || gameBoard.length == 0 || gameBoard[0].length == 0) {
          System.out.println("Game board was messed up coming into public void makeMove()");
          return "FAILURE";
      }
      //Return UP, DOWN, LEFT, or RIGHT to move that direction
      if (myPlayerNumber == 2) {
          myCurrentColumn = 9;
      }

      for (int i = 0; i < gameBoard.length; i++) {
          for (int j = 0; j < gameBoard[i].length; j++) {
              if (gameBoard[i][j] == myPlayerNumber) {
                  myCurrentRow = i;
                  myCurrentColumn = j;
                  System.out.println("CURRENT POSITION:" + i + "," + j);
                  return randomStrat();

              }
          }
      }
    return "DOWN";
  }



  //Small helper Method
  public static boolean isInsideBoard(int i, int j) {
    if (i >= gameBoard.length || j >= gameBoard.length) {
      return false;
    } else if (i < 0 || j < 0) {
      return false;
    }
    return true;
    }

    public static String randomStrat() {
        int randDir;
        boolean again = true;

        while (again) {
            again = false;
            randDir = ThreadLocalRandom.current().nextInt(0, 4);
            System.out.println(randDir);
            String temp = "FAIL";
            switch (randDir) {
                case 0:
                    if (isInsideBoard(myCurrentRow - 1, myCurrentColumn) && isSafe(myCurrentRow - 1, myCurrentColumn)) {
                        return "UP";
                    }
                case 1:
                    if (isInsideBoard(myCurrentRow + 1, myCurrentColumn) && isSafe(myCurrentRow + 1, myCurrentColumn)) {
                        return "DOWN";
                    }
                case 2:
                    if (isInsideBoard(myCurrentRow, myCurrentColumn + 1) && isSafe(myCurrentRow, myCurrentColumn + 1)) {
                        return "LEFT";
                    }
                case 3:
                    if (isInsideBoard(myCurrentRow, myCurrentColumn - 1) && isSafe(myCurrentRow, myCurrentColumn - 1)) {
                        return "RIGHT";
                    }
                default:
                    again = true;
                    break;
            }
        }
        return "FAILURE";
    }

    public static boolean isSafe(int x, int y) {
      if ( gameBoard[x][y] == 0)
          return true;
      return false;
    }

    public static String greedyStrat() {
      int[][] possibleMoves = generateNeighborCoordinates();

      int bestMove = -1;
      int numNeighbors = -1;

      for (int i  = 0; i < possibleMoves.length; i++) {
          System.out.println(possibleMoves[i][0] + "," + possibleMoves[i][1]);
          int temp = numEmptyNeightbors(possibleMoves[i][0], possibleMoves[i][1]);
          System.out.println("Num empty Neighbors: "+ temp);

          if (temp >= numNeighbors) {
              numNeighbors = temp;
              bestMove = i;
          }
      }
        System.out.println("FINAL BEST: "+ bestMove);

        switch (bestMove) {
            case 0:
                return moveDown();
            case 1:
                return moveUp();
            case 2:
                return moveRight();
            case 3:
                return moveLeft();
            default:
                break;
        }

        System.out.println("BROKEN " + bestMove);
      return "FAILURE";
    }

    public static int[][] generateNeighborCoordinates() {
        int[][] neighbors = new int[][]{
            {myCurrentRow + 1, myCurrentColumn}, // DOWN
            {myCurrentRow - 1, myCurrentColumn}, // UP
            {myCurrentRow, myCurrentColumn + 1}, // RIGHT
            {myCurrentRow, myCurrentColumn - 1}  // LEFT

        };
        return neighbors;
    }


    public static int numEmptyNeightbors(int x, int y) {

        if (isInsideBoard(x,y) && gameBoard[x][y] != 0)
            return 0;

        int numEmpty = 0;

        if (isInsideBoard(x, y-1) && gameBoard[x][y - 1] == 0) {
            numEmpty++;
        }

        if (isInsideBoard(x, y+1) && gameBoard[x][y + 1] == 0) {
            numEmpty++;
        }

        if (isInsideBoard(x-1, y) && gameBoard[x - 1][y] == 0) {
            numEmpty++;
        }

        if (isInsideBoard(x+1, y) && gameBoard[x + 1][y] == 0) {
            numEmpty++;
        }

        return numEmpty;
    }


    public static String moveLeft() {
      if (isInsideBoard(myCurrentColumn - 1, myCurrentRow) && gameBoard[myCurrentColumn - 1][myCurrentRow] == 0) {
          System.out.println("LEFT");

          return "LEFT";
      } else
          System.out.println("LEFT-F");

        return randomStrat();
    }

    public static String moveRight() {
        if (isInsideBoard(myCurrentColumn + 1, myCurrentRow) && gameBoard[myCurrentColumn + 1][myCurrentRow] == 0) {
            System.out.println("RIGHT");

            return "RIGHT";
        } else {
            System.out.println("RIGHT-F");

        return randomStrat(); }
    }

    public static String moveDown() {
        if (isInsideBoard( myCurrentColumn, myCurrentRow + 1) && gameBoard[myCurrentColumn][myCurrentRow + 1] == 0) {
            System.out.println("DOWN");

            return "DOWN";
        } else {
            System.out.println("DOWN-F");

        return randomStrat(); }
    }

    public static String moveUp() {
        if (isInsideBoard(myCurrentColumn, myCurrentRow - 1) && gameBoard[myCurrentColumn][myCurrentRow - 1] == 0) {
            System.out.println("UP");
            return "UP";
        } else {
            System.out.println("UP-F");

            return randomStrat(); }
    }


}
