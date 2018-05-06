public class LooneyTunes extends JRandom { // turns out he wants us to inherit from a class, so I made it the Random one
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
      
      // Make sure that the game keeps going until all the threads
      // have terminated. Then move on.
      while(!(bThread.getState().equals(Thread.State.TERMINATED) && tThread.getState().equals(Thread.State.TERMINATED)
         && dThread.getState().equals(Thread.State.TERMINATED) && mThread.getState().equals(Thread.State.TERMINATED)))
      {}
      
      // Tell the person watching who won the game.
      System.out.println("\n******** GAME OVER ********");
      System.out.println("Winner: " + tThread.getWinnerName());
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
   
   private void assignToEmptyPosition(ThreadData tune)
   {
      // I used tune.thread.getName() so it returns the name of the thread,
      // like Taz (D), and not the name of the current thread, like Thread-0.
      System.out.println("\tAssigning location for " + tune.thread.getName());

      boolean assigned = false;
      while (!assigned)
      { // loop until we've successfully placed the character

         Position newPos = new Position(pubGetRandom(0, rows - 1),
            pubGetRandom(0, columns - 1));
         
         // if position is empty, we go ahead and place the thread there
         if (tune.isEmpty(newPos))
         {
            assigned = true;
            // Move the Tune to the new position.
            tune.setTilePosition(newPos, newPos, tune.getTuneLetter());
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
         Position newPos = new Position(pubGetRandom(0, rows - 1),
            pubGetRandom(0, columns - 1));

         // If position is empty, we go ahead and place the item there.
         if (bThread.isEmpty(newPos))
         {
            // If it's the mountain, then get the mountains initial condition.
            if (item.equals("F"))
               bThread.setTilePosition(newPos, newPos, "F");
            
            else
               bThread.setGameTile(newPos, "C");
            
            assigned = true;
         }
      }
   }
}
