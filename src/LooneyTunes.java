package CS380LooneyTunes;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LooneyTunes
{
   // ****************************************************
   // **** FIELDS ****
   public ThreadData bThread = new ThreadData("Bugs Bunny (B)", "B");
   public ThreadData tThread = new ThreadData("Tweety (T)", "T");
   public ThreadData dThread = new ThreadData("Taz (D)", "D");
   public ThreadData mThread = new ThreadData("Marvin (M)", "M");
   public String carrot1 = "C";
   public String carrot2 = "C";
   public String mountain = "F";
   private final int columns = 5, rows = 5;
   
   
   // We just use the main method to call our play() method.
   public static void main(String[] args)
   {
      new LooneyTunes().play();
   }
   
   public void play()
   {
      // Set Up Initial Conditions.
      initData();
      System.out.println("\nSetting up gameboard...");
      System.out.println("Initial gameboard:");
      bThread.printGameBoard();
      
      // Start the Tune Threads.
      startTuneThreads();
      

      try
      {
         while (!bThread.getWinner())
         {
         }
         ThreadData.sleep(1);
         bThread.interrupt();
         tThread.interrupt();
         dThread.interrupt();
         mThread.interrupt();
      }
      catch (InterruptedException ex)
      {
         Logger.getLogger(LooneyTunes.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      System.out.println("\n******** GAME OVER ********");
   }
   
   // ****************************************************
   // **** METHODS ****
   
   
   private void initData()
   {
      // Assign Tunes.
      assignToEmptyPosition(bThread);
      assignToEmptyPosition(tThread);
      assignToEmptyPosition(dThread);
      assignToEmptyPosition(mThread);
      
      // Assign Items
      assignToEmptyPosition(carrot1);
      assignToEmptyPosition(carrot2);
      assignToEmptyPosition(mountain);
   }
   
   private void startTuneThreads()
   {
      bThread.start();
      tThread.start();
      dThread.start();
      mThread.start();
   }
   
   private int getRandom(int rangeLow, int rangeHigh)
   {
      Random rand = new Random();

      // Don't let myRand be negative!!
      double myRand = Math.abs(rand.nextInt() / (1.0 + Integer.MAX_VALUE));
      int range = rangeHigh - rangeLow + 1;
      int myRandScaled = (int) ((myRand * range) + rangeLow);

      return myRandScaled;
   }
   
   private void assignToEmptyPosition(ThreadData tune)
   {
      // I used tune.thread.getName() so it returns the name of the thread,
      // like Taz (D), and not the name of the current thread, like Thread-0.
      System.out.println("\tAssigning location for " + tune.thread.getName());

      boolean assigned = false;
      while (!assigned)
      { // loop until we've successfully placed the character

         Position newPos = new Position(getRandom(0, rows - 1),
            getRandom(0, columns - 1));

         // if position is empty, we go ahead and place the thread there
         if (tune.isEmpty(newPos))
         {
            // Place the Tune in it's initil position on the gameBoard
            tune.setGameTile(tune.getTunePosition(), tune.getTuneLetter());
            assigned = true;
            // Move the Tune to the new position.
            tune.setTilePosition(tune.getTunePosition(), newPos);
         }
      }
   }

   private void assignToEmptyPosition(String item)
   {
      // Get the name of the Item.
      String itemName;
      if (item.equals("C"))
         itemName = "Carrot " + item;
      else
         itemName = "Mountain " + item;
      
      System.out.println("\tAssigning location for " + itemName);
      
      boolean assigned = false;
      
      // Loop until we've successfully placed the character.
      while (!assigned)
      {
         Position newPos = new Position(getRandom(0, rows - 1),
            getRandom(0, columns - 1));

         // If position is empty, we go ahead and place the item there.
         if (bThread.isEmpty(newPos))
         {
            // If it's the mountain, then get the mountains initial condition.
            if (item.equals("F"))
            {
               bThread.setGameTile(bThread.getMountPosition(), item);
               bThread.setTilePosition(bThread.getMountPosition(), newPos);
            }
            else
            {
               bThread.setGameTile(newPos, item);
               bThread.setTilePosition(newPos, newPos);
            }
            assigned = true;
            
            
         }
      }
   }
}
