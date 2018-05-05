package CS380LooneyTunes;

import java.util.Arrays;
import java.util.Random;

public class ThreadData extends Thread
{
   // ****************************************************
   // **** FIELDS ****
   protected Thread thread;
   private Position tunePosition;
   private String letter;
   private String winningPlayer = "[BLANK]";

   // Static variables we only want to be created once and shared by every thread.
   private static int rows = 5, columns = 5, count = 0, wholeCycle = 0;
   private static int sleep = 1000;
   private static boolean winner = false;
   private static String[][] gameBoard;
   private static Position mountainPosition;

   // This is called once so we don't end up with duplicate gameboards.
   static
   {
      createGameBoard();
   }

   // ****************************************************
   // **** CONSTRUCTOR ****
   // Non-default constructor that initializes necessary variables.
   public ThreadData(String name, String letter)
   {
      thread = new Thread(name);
      this.letter = letter;
      
      // Initial Tune and Mountain position.
      tunePosition = new Position(0,0);
      mountainPosition = new Position(0,0);
   }
   
   // ****************************************************
   // **** METHODS ****
   /**
    * When we .start() a thread, this run() method is what gets executed. This will have the code needed
    * for each Tune thread to progress through the game
    */
   public void run()
   {
      try
      {
         while (!winner)
         {
            System.out.println("Thread " + Thread.currentThread().getId()
               + " is running - " + this);

            playGame(tunePosition, 0);
            Thread.sleep(sleep);
         }
      }
      catch (InterruptedException e)
      {
         System.out.println(Arrays.toString(e.getStackTrace()));
         if(winner)
         {}
         else
         {}
      }
   }


   
   
   // Static methods that we only want to be called once.
   public static void createGameBoard()
   {
      gameBoard = new String[rows][columns];
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
            gameBoard[i][j] = "-";
      }
   }

   public synchronized void printGameBoard()
   {
      String board = "    0    1    2    3    4";
      board += "\n    ----------------------\n";
      for (int i = 0; i < rows; i++)
      {
         board += i + " | ";
         for (int j = 0; j < columns; j++)
         {
            // Just need this for board formatting.
            if (j == columns - 1)
               board += gameBoard[i][j].toString() + " |";
            else
               board += gameBoard[i][j].toString() + "    ";
         }
         
         if (i != rows - 1)
            board += "\n";
      }
      
      board += "\n    ----------------------\n";
      
      System.out.println(board);
   }
   
   public boolean getWinner()
   {
      return winner;
   }
   
   // Updaate the Count and Whole Cycle and returns true
   // if there was a Whole Cycle.
   private boolean updateCountAndWholeCycle()
   {
      count++;
      count = count % 4;
      
      if (count == 0)
      {
         wholeCycle++;
         return true;
      }
      
      return false;
   }

   /**
    * Returns an integer representation of the type of tile at a given position.
    *
    * @param pos The gameboard position we are checking the value of.
    *
    * @return 1 if the tile contains a character, 2 if the tile contains the mountain, 0 if the
    *         tile contains a Carrot/Flag. -1 if the tile is blank.
    */
   public int getTileType(Position pos)
   {
      String temp = gameBoard[pos.getY()][pos.getX()];

      switch (temp)
      {
         case "B":
         case "D":
         case "T":
            return 1;
         case "M":
            return 2;
         case "B(C)":
         case "D(C)":
         case "T(C)":
            return 3;
         case "M(C)":
            return 4;
         case "C":
            return 5;
         case "F":
            return 6;

         default:
            return -1;
      }
   }

   public synchronized boolean canMovePiece(Position source, Position destination)
   {
      // If we throw an Exception this means the movement is invalid and we must try again.
      try
      {
         // Checks for IndexOutOfBoundsException by throwing that exception.
         String srcTemp = getTileString(destination);

         // Check who can move where.
         switch (getTileType(source))
         {
            // Can't move an empty space.
            case -1:
               return false;
            // If any of the normal character tries to move to an unavailable square.
            case 1:
            {
               int tileType = getTileType(destination);
               return !(tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 6);
            }
            // If Marvin tries to move on the mountain without carrot.
            case 2:
               return getTileType(destination) != 6;
            // If any normal character with carrot tries to move to an unavailable square.
            case 3:
            {
               int tileType = getTileType(destination);
               if (tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 5)
               {
                  return false;
               }

               if (tileType == 6)
               {
                  winner = true;
                  winningPlayer = thread.getName();
               }
               return true;
            }
            // Marvin can now step on any square EXCEPT the second carrot.
            case 4:
            {
               // If Marvin steps on the Mountain, he is the winner.
               int tileType = getTileType(destination);
               if (tileType == 5)
               {
                  return false;
               }

               if (tileType == 6)
               {
                  winner = true;
                  winningPlayer = thread.getName();
               }

               return true;
            }
            case 5:
               return false;
            // If the mountain tries to move to an unavailable square.
            case 6:
               return getTileType(destination) == -1;
         }

         return true;
      }
      catch (IndexOutOfBoundsException e)
      {
         return false;
      }
   }

   /**
    * Set the new position of the tune or item.
    *
    * @param source      The source position of the current Tune/Mountain.
    * @param destination The destination position where to place current Tune/Mountain.
    */
   public synchronized void setTilePosition(Position source, Position destination)
   {
      String sourceTileString = gameBoard[source.getY()][source.getX()];
      
      // If the destination and source are the same Position.
      if (destination.isEquals(source))
         setGameTile(destination, sourceTileString);
      
      // Else, if they are different.
      else
      {
         // If the destination is a carrot.
         boolean isDestCarrot = getTileType(destination) == 5;

         // If the destination is the mountain.
         boolean isDestMountain = getTileType(destination) == 6;

         // If the destination is a tune.
         boolean isDestTune = getTileType(destination) == 1;

         // If the destination is a tune with a carrot.
         boolean isDestTuneWithCarrot = getTileType(destination) == 2;
         
         // If one of the tunes tries to get a carrot.
         if (!sourceTileString.equals("C") && isDestCarrot)
            sourceTileString += "(C)";
         
         // If one of the tunes tries to get on the mountain.
         else if (!sourceTileString.equals("F") && isDestMountain)
         {
            sourceTileString += "(F)";
            winner = true;
         }

         // If Marvin tries to kill one of the other tunes that have carrots.
         else if (!(sourceTileString.equals("B(C)") || sourceTileString.equals("T(C)") || sourceTileString.equals("D(C)")) && isDestTuneWithCarrot)
            sourceTileString += "(C)";
         
         // Set the new tile at Position destination with the new sourceTileString.
         setGameTile(destination, sourceTileString);
         
         // Replace the old source Position with empty ("-") string.
         setGameTile(source, "-");
         
         // If it's a tune, then set the tune's new Position.
         if (getTileType(destination) == 1 || getTileType(destination) == 2 || getTileType(destination) == 3 || getTileType(destination) == 4)
         {
            tunePosition.setPosition(destination);
         }

         // If it's the mountian, then set the mountains new position.
         if (getTileType(destination) == 6)
         {
            mountainPosition.setPosition(destination);
         }
         
         // Update the count and Cycle
         updateCountAndWholeCycle();
      }
   }

   // Assign a string to a specified location.
   public synchronized void setGameTile(Position pos, String str)
   {
      gameBoard[pos.getY()][pos.getX()] = str;
   }
   
   // Returns the String contained in a tile
   private String getTileString(Position source)
   {
      return gameBoard[source.getY()][source.getX()].toString();
   }
   
   public String getTuneLetter()
   {
      return letter;
   }
   
   public Position getTunePosition()
   {
      return tunePosition;
   }
   
   public Position getMountPosition()
   {
      return mountainPosition;
   }
   
   public boolean isEmpty(Position position)
   {
      // If our gameboard String is empty, we will return true.
      System.out.println("\t\tBoard position " + position.toString() + " isEmpty = "
         + gameBoard[position.getY()][position.getX()].equals("-") + " ");

      return gameBoard[position.getY()][position.getX()].equals("-");
   }

   public synchronized void playGame(Position source, int tries)
   {
      Random generator = new Random();

      // Randomly decide the next direction to move the Tune.
      Position newPos;
      int walkDir = generator.nextInt(4);
      
      switch (walkDir)
      {
         // if we roll a 0, move upwards
         case 0:
            newPos = new Position(source.getX(), source.getY() + 1);
            break;
         // if we roll a 1, move downwards
         case 1:
            newPos = new Position(source.getX(), source.getY() - 1);
            break;
         // if we roll a 2, move right
         case 2:
            newPos = new Position(source.getX() + 1, source.getY());
            break;
         // if we roll a 3, move left
         default:
            newPos = new Position(source.getX() - 1, source.getY());
            break;
      }

      // Keep recursing until we have found a valid movement
      if (!canMovePiece(source, newPos) && tries < 25)
      {
         System.out.println("\t" + thread.getName() + " failed while trying to move " + source.toString()
            + " --> " + newPos.toString());

         playGame(source, ++tries);
      }

      else
      {
         // If we try to move 50+ times we should assume the player is blocked and cannot
         // make a move. In this case, we move on to the next characters turn
         if (tries >= 25)
         {
            System.out.println("\t" + thread.getName() + " has failed to move " + tries + " times. "
               + "\nPlayer has no available moves. Turn is ending.");
            updateCountAndWholeCycle();
            return;
         }
         
         else
         {
            setTilePosition(source, newPos);
            System.out.println("\t" + thread.getName() + " has successfully moved " + source.toString()
               + " --> " + newPos.toString());
            printGameBoard();
         }
      }

      if (wholeCycle % 3 == 0 && wholeCycle != 0 && tries == 0)
      {
         System.out.println("\nCycle #" + wholeCycle + ". Relocating Mountain (F)...");
         Position randPos = new Position(generator.nextInt(5), generator.nextInt(5));

         while (!canMovePiece(mountainPosition, randPos))
            randPos = new Position(generator.nextInt(5), generator.nextInt(5));

         System.out.println("Mountain has moved from " + mountainPosition.toString()
            + " to " + randPos.toString());
         setTilePosition(mountainPosition, randPos);

      }
   }
}
