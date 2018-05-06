package CS380LooneyTunes;

import java.util.Random;

public class ThreadData extends Thread
{
   // ****************************************************
   // **** FIELDS ****
   protected Thread thread;
   private Position tunePosition;
   private String letter;
   
   private static final Object LOCK;

   // Static variables we only want to be created once and shared by every thread.
   private static int rows = 5, columns = 5, count = 0, wholeCycle = 0;
   private static int sleep = 500;
   private static boolean winner;
   private static String winningTune;
   private static String killTune;
   private static String[][] gameBoard;
   private static Position mountainPosition;

   // This is called once so we don't end up with duplicate gameboards.
   static
   {
      createGameBoard();
      LOCK = new Object();
      winner = false;
      killTune = null;
      winningTune = null;
      mountainPosition = new Position(0, 0);
   }

   // ****************************************************
   // **** CONSTRUCTOR ****
   // Non-default constructor that initializes necessary variables.
   public ThreadData(String name, String letter)
   {
      thread = new Thread(name);
      this.letter = letter;

      // Initial Tune and Mountain position.
      tunePosition = new Position(0, 0);
   }
   
   // ****************************************************
   // **** RUN METHOD ****
   /**
    * When we .start() a thread, this run() method is what gets executed. This will have the code needed
    * for each Tune thread to progress through the game
    */
   @Override
   public void run()
   {
      while (!winner)
      {
         try
         {
            System.out.println("Is there a Winner: " + winner);
            System.out.println("Thread " + Thread.currentThread().getId()
               + " is running - " + this);

            synchronized (LOCK)
            {
               if (killTune == null && !winner)
               {
                  System.out.println("Tune Position " + letter + ": " + tunePosition);
                  playGame(tunePosition, 0);
                  Thread.sleep(sleep);
               }

               if (letter.equals(killTune))
               {
                  killTune = null;
                  throw new InterruptedException();
               }

               else if (winner)
                  throw new InterruptedException();
            }
            System.out.println("CURRENT THREAD: " + thread.getName());
         }
         catch (InterruptedException e)
         {
            System.out.println("Tune " + thread.getName() + " Has Been TERMINATED!");
            return;
         }
      }
   }
   
   // ***************************************************************************************************
   // ***************************************************************************************************
   
   // ****************************************************
   // **** METHODS ****
   // Static methods that we only want to be called once.
   public static void createGameBoard()
   {
      gameBoard = new String[rows][columns];
      for (int i = 0; i < rows; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            gameBoard[i][j] = "-";
         }
      }
   }
   
   public boolean getWinner()
   {
      return winner;
   }
   
   public String getWinnerName()
   {
      return winningTune;
   }
   
   public String getKillTune()
   {
      return killTune;
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
   
   // ********************************************************************
   // ALL SYNCHRONIZED METHODS BELLOW
   // ********************************************************************
   
   /*
    * Just prints out the game board.
    */
   public synchronized void printGameBoard()
   {
      synchronized (LOCK)
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
               {
                  board += gameBoard[i][j] + " |";
               }
               else
               {
                  board += gameBoard[i][j] + "    ";
               }
            }

            if (i != rows - 1)
            {
               board += "\n";
            }
         }

         board += "\n    ----------------------\n";

         System.out.println(board);
      }
   }

   

   /*
    * Only set the winner if there hasn't been a winner yet.
    *
    * @param tuneName The name of the tune who's the winner.
    */
   private synchronized void setWinner(String tuneName)
   {
      synchronized (LOCK)
      {
         if (winner == false)
         {
            winner = true;
            winningTune = tuneName;
         }
      }
   }

   

   /* 
    * Updaate the Count and Whole Cycle and returns true
    * if there was a Whole Cycle.
    */
   private synchronized boolean updateCountAndWholeCycle()
   {
      synchronized (LOCK)
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
   }
   
   /*
    * Sets the tune to be killed at the end of this thread's turn.
    * 
    * @param tuneLetter Letter of the tune to be killed.
    */
   private synchronized void setKillTune(String tuneLetter)
   {
      synchronized (LOCK)
      {
         if (killTune == null)
         {
            killTune = tuneLetter;
         }
      }
   }
   
   

   /**
    * Returns an integer representation of the type of tile at a given position.
    *
    * @param pos The gameboard position we are checking the value of.
    *
    * @return 1 if the tile contains a character, 2 if the tile contains the mountain, 0 if the
    *         tile contains a Carrot/Flag. -1 if the tile is blank.
    */
   public synchronized int getTileType(Position pos)
   {
      synchronized (LOCK)
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
            case "M(C)(C)":
               return 4;
            case "C":
               return 5;
            case "F":
               return 6;
            case "B(C)(F)":
            case "D(C)(F)":
            case "T(C)(F)":
            case "M(C)(F)":
            case "M(C)(C)(F)":
               return 7;
               
            default:
               return -1;
         }
      }
   }
   
   /*
    * Determines if a piece can be moved from the source position
    * to the destination position.
    * 
    * @param source The source Position Object.
    * @param destination The destination Position Object.
    */
   public synchronized boolean canMovePiece(Position source, Position destination)
   {
      synchronized (LOCK)
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
                     setWinner(thread.getName());
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
                     setWinner(thread.getName());
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
   }

   /**
    * Set the new position of the tune or item.
    *
    * @param source           The source position of the current Tune/Mountain.
    * @param destination      The destination position where to place current Tune/Mountain.
    * @param sourceTileString The String in the source tile of the game board.
    */
   public synchronized void setTilePosition(Position source, Position destination, String sourceTileString)
   {
      synchronized (LOCK)
      {
         // If the destination and source are the same Position.
         // THIS IS ONLY SUPPOSED TO HAPPEN IF IT'S SETTING THE INITIAL POSITIONS.
         if (destination.isEquals(source))
         {
            setGameTile(destination, sourceTileString);
            if (sourceTileString.equals("F"))
            {
               mountainPosition.setPosition(destination);
            }
            else
            {
               tunePosition.setPosition(destination);
            }
         }

         // Else, if they are different.
         else
         {
            String destTileString = gameBoard[destination.getY()][destination.getX()];
            int destTileType = getTileType(destination);

            // If the destination is a carrot.
            boolean isDestCarrot = destTileType == 5;

            // If the destination is the mountain.
            boolean isDestMountain = destTileType == 6;

            // If the destination is a tune.
            boolean isDestTune = destTileType == 1;

            // If the destination is a tune with a carrot.
            boolean isDestTuneWithCarrot = destTileType == 3;

            // If one of the tunes tries to get a carrot.
            if (!sourceTileString.equals("C") && isDestCarrot)
            {
               sourceTileString += "(C)";
               letter = sourceTileString;
            }

            // If one of the tunes tries to get on the mountain.
            else if (!sourceTileString.equals("F") && isDestMountain)
            {
               sourceTileString += "(F)";
               letter = sourceTileString;
            }

            // If Marvin tries to kill one of the other tunes.
            else if ((sourceTileString.equals("M") || sourceTileString.equals("M(C)") || sourceTileString.equals("M(C)(C)")) && isDestTune)
            {
               System.out.println("MARVIN LETTER IS: " + sourceTileString); // This line For debugging
               if (destTileString.equals("B"))
               {
                  setKillTune("B");
               }
               else if (destTileString.equals("T"))
               {
                  setKillTune("T");
               }
               else
               {
                  setKillTune("D");
               }
            }

            // If Marvin tries to kill one of the other tunes that have carrots.
            else if ((sourceTileString.equals("M") || sourceTileString.equals("M(C)")) && isDestTuneWithCarrot)
            {
               sourceTileString += "(C)";
               letter = sourceTileString;

               if (destTileString.equals("B(C)"))
               {
                  setKillTune("B(C)");
               }
               else if (destTileString.equals("T(C)"))
               {
                  setKillTune("T(C)");
               }
               else
               {
                  setKillTune("D(C)");
               }
            }

            // Set the new tile at Position destination with the new sourceTileString.
            setGameTile(destination, sourceTileString);

            // Replace the old source Position with empty ("-") string.
            setGameTile(source, "-");

            // If it's a tune, then set the tune's new Position.
            if (getTileType(destination) == 1 || getTileType(destination) == 2 || getTileType(destination) == 3 || getTileType(destination) == 4 || getTileType(destination) == 7)
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
   }

   /*
    * Assign a string to a specified location.
    * 
    * @param pos The position on the gameboard where to set the new tile.
    * @param str The string representation of the tile to be set.
    */
   public synchronized void setGameTile(Position pos, String str)
   {
      synchronized (LOCK)
      {
         gameBoard[pos.getY()][pos.getX()] = str;
      }
   }

   /*
    * Returns the String contained in a tile on the gameboard.
    * 
    * @param source The position where the string is located on gameboard.
    */
   private synchronized String getTileString(Position source)
   {
      synchronized (LOCK)
      {
         return gameBoard[source.getY()][source.getX()];
      }
   }
   
   /*
    * THE MAIN METHOD THAT RUNS THE WHOLE GAME!!!
    * 
    * @param source The position of the calling thread's tune on the gameboard.
    * @param tries The ammount of tries this player has tried to play the game.
    *              Initially set to 0 tries.
    */
   public synchronized void playGame(Position source, int tries)
   {
      synchronized (LOCK)
      {
         // Don't play the game if there is tune to be TERMINATED
         // or if a winner is already decided.
         if (killTune != null || winner == true)
            return;
         
         // Randomly decide the next direction to move the Tune.
         // initialSource is a copy of the source Position used only for
         // printing purpses becuase setTilePosition updates source Position.
         Random generator = new Random();
         Position newPos, initialSource = new Position(source);
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
         if (!canMovePiece(source, newPos))
         {
            System.out.println("\t" + thread.getName() + " failed while trying to move " + initialSource.toString()
               + " --> " + newPos.toString());

            // If we try to move 25+ times we should assume the player is blocked and cannot
            // make a move. In this case, we move on to the next characters turn
            if (tries >= 25)
            {
               System.out.println("\t" + thread.getName() + " has failed to move " + tries + " times. "
                  + "\nPlayer has no available moves. Turn is ending.");
               updateCountAndWholeCycle();
               return;
            }

            playGame(source, ++tries);
         }

         else
         {
            setTilePosition(source, newPos, letter);
            System.out.println("\t" + thread.getName() + " has successfully moved " + initialSource.toString()
               + " --> " + newPos.toString());

            // Print the game board.
            printGameBoard();
         }
         
         if (wholeCycle % 3 == 0 && wholeCycle != 0 && tries == 0 && !winner)
         {
            System.out.println("\nCycle #" + wholeCycle + ". Relocating Mountain (F)...");
            Position randPos = new Position(generator.nextInt(5), generator.nextInt(5));

            while (!canMovePiece(mountainPosition, randPos))
            {
               randPos = new Position(generator.nextInt(5), generator.nextInt(5));
            }

            System.out.println("Mountain has moved from " + mountainPosition.toString()
               + " to " + randPos.toString());
            setTilePosition(mountainPosition, randPos, getTileString(mountainPosition));

            // Print the game board.
            printGameBoard();
         }
      }
   }
}