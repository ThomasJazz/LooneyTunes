package cs380looneytoons;

import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {
    
    // variables that are accessible by each thread
    private String letter, winner = "";
    private int id, WHOLE_CYCLE = 0;

    // static variables we only want to be created once and shared by every thread
    private static int rows = 5, columns = 5, count = 0;
    private static int sleep = 100;
    private static String[][] gameBoard;
    private static ArrayList<Position> tunePositions = new ArrayList<>();
    private static ArrayList<Position> itemPositions = new ArrayList<>();

    // this is called once so we don't end up with duplicate gameboards
    static {
        createGameBoard();
        initTunePositions();
        initItemPositions();
    }

    // non-default constructor that initializes necessary variables
    public ThreadData(String name, int id, String letter){
        setName(name);
        this.id = id;
        this.letter = letter;
    }

    /**
     * When we .start() a thread, this run() method is what gets executed. This will have the code needed
     * for each Tune thread to progress through the game
     */
    public void run() {
        try {
            System.out.println("Thread " + Thread.currentThread().getId() +
                    " is running - " + this);

            Thread.sleep(sleep);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    // getter methods dont need to be synchronized
    public String getLetter(){
        return letter;
    }

    public Position getTunePosition(int id) {
        return tunePositions.get(id);
    }

    public String getWinner(){
        return winner;
    }

    public int getCount(){
        return count;
    }

    // i dont think this had to be syncrhonized since it doesn't change any data
    public String[][] getGameBoard(){
        return gameBoard;
    }

    // returns the String contained in a tile
    private String getTileString(Position source){
        return gameBoard[source.getY()][source.getX()].toString();
    }
    /**
     * Returns an integer representation of the type of tile at a given position
     * @param pos   The gameboard position we are checking the value of
     * @return      1 if the tile contains a character, 2 if the tile contains the mountain, 0 if the
     *              tile contains a Carrot/Flag. -1 if the tile is blank.
     */
    public int getTileType(Position pos) {
        String temp = gameBoard[pos.getY()][pos.getX()];

        switch (temp) {
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

    public ArrayList<Position> getTunePositions() {
        return tunePositions;
    }

    public ArrayList<Position> getItemPositions() {
        return itemPositions;
    }

    public boolean isEmpty(Position position){
        // if our gameboard String is empty, we will return true
        System.out.println("\t\tBoard position " + position.toString() + " isEmpty = " +
                gameBoard[position.getY()][position.getX()].equals("-") + " ");

        return gameBoard[position.getY()][position.getX()].equals("-");
    }

    // static methods that we only want to be called once
    public static void createGameBoard(){
        gameBoard = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard[i][j] = "-";
            }
        }
    }

    // these methods we only want to execute once, and we want them to execute predictably
    public synchronized static void initTunePositions(){
        for (int i = 0; i < 4; i++)
            tunePositions.add(new Position(0,0));
    }

    public synchronized static void initItemPositions(){
        for (int i = 0; i < 3; i++)
            itemPositions.add(new Position(0,0));
    }

    // synchronized methods

    /**
     * Transfers the value from the source position to the destination position
     * @param source        The position of the data we want to send
     * @param destination
     * @return
     */
    public synchronized boolean canMovePiece(Position source, Position destination) {
        // if we throw an Exception this means the movement is invalid and we must try again
        try {
            // Checks for IndexOutOfBoundsException by throwing that exception.
            String srcTemp = getTileString(destination);
            
            // Check who can move where.
            switch (getTileType(source)) {
                // Can't move an empty space.
                case -1:
                    return false;
                // If any of the normal character tries to move to an unavailable square.
                case 1: {
                    int tileType = getTileType(destination);
                    return !(tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 6);
                }
                // If Marvin tries to move on the mountain without carrot.
                case 2:
                    return getTileType(destination) != 6;
                // If any normal character with carrot tries to move to an unavailable square.
                case 3: {
                    int tileType = getTileType(destination);
                    if (tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 5)
                        return false;
                    
                    if (tileType == 6)
                        winner = this.getName();
                    return true;
                }
                // Marvin can now step on any square EXCEPT the second carrot.
                case 4:
                {
                    // If Marvin steps on the Mountain, he is the winner.
                    int tileType = getTileType(destination);
                    if (tileType == 5)
                        return false;
                    
                    if (tileType == 6)
                        winner = this.getName();
                    
                    return true;
                }
                case 5:
                    return false;
                // If the mountain tries to move to an unavailable square.
                case 6:
                    return getTileType(destination) == -1;
            }
            
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    // assign a string to a specified location
    public synchronized void setGameTile(Position pos, String str) {
        gameBoard[pos.getY()][pos.getX()] = str;
    }

    /**
     * Our tunePositions array tracks where each tune is so we can more easily access
     * their location
     * @param source        The source position of the current Tune/Mountain.
     * @param destination   The destination position where to place current Tune/Mountain.
     * @param i             The index/id of the looneyTune
     */
    public synchronized void setPosition(Position source, Position destination, int i) {
        // If the destination is a carrot.
        boolean isDestCarrot = getTileType(destination) == 5;
        
        // If the destination is the mountain.
        boolean isDestMountain = getTileType(destination) == 6;
        
        // If the destination is a tune.
        boolean isDestTune = getTileType(destination) == 1;
        
        // If the destination is a tune with a carrot.
        boolean isDestTuneWithCarrot = getTileType(destination) == 2;
        
        
        // **** TUNE ****
        if (getTileType(source) == 1 || getTileType(source) == 2 || getTileType(source) == 3 || getTileType(source) == 4)
        {
            // use try-catch block to decide whether we need to add a position or change
            // an existing one
            try {
                tunePositions.set(i,destination);
            } catch (IndexOutOfBoundsException e) {
                tunePositions.add(source);
            }
        }
        
        // ***** ITEM ****
        else if (getTileType(source) == 5 || getTileType(source) == 6)
        {
            // use try-catch block to decide whether we need to add a position or change
            // an existing one
            try {
                itemPositions.set(i, destination);
            } catch (IndexOutOfBoundsException e) {
                itemPositions.add(source);
            }
        }

        String srtTemp = gameBoard[source.getY()][source.getX()];

        if (destination.isEquals(source))
            setGameTile(destination, srtTemp);
        else
        {
            if (!srtTemp.equals("C") && isDestCarrot)
            {
                srtTemp += "(C)";
                itemPositions.remove(i);
            }
        
            else if (!srtTemp.equals("F") && isDestMountain)
            {
                srtTemp += "(F)";
                itemPositions.remove(i);
            }
        
            else if (!(srtTemp.equals("B") || srtTemp.equals("T") || srtTemp.equals("D")) && isDestTune)
                tunePositions.remove(i);
        
            else if (!(srtTemp.equals("B(C)") || srtTemp.equals("T(C)") || srtTemp.equals("D(C)")) && isDestTuneWithCarrot)
            {
                srtTemp += "(C)";
                tunePositions.remove(i);
            }
            
            setGameTile(destination, srtTemp);
            setGameTile(source, "-");
        }
    }

    public synchronized void printGameBoard(){
        System.out.println("    0    1    2    3    4");
        System.out.println("    ----------------------");
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < columns; j++) {
                // just need this for board formatting
                if (j == columns-1){
                    System.out.print(getGameBoard()[i][j].toString() + " |");
                } else {
                    System.out.print(getGameBoard()[i][j].toString() + "    ");
                }

            }
            if (i != rows-1)
                System.out.println("\n");
        }
        System.out.println("\n    ----------------------\n");
    }

    // have to make a synchronized method for incrementing so we get the correct value
    public synchronized void increment() {
        count++;
    }

    private synchronized void incrementCycle(){
        WHOLE_CYCLE++;
    }

    /**
     * For debugging. Just wanted to make sure I was actually using multiple threads
     * and not just doing things with iterations
     */
    public void printThreads(){
        Set<Thread> threads = Thread.getAllStackTraces().keySet();

        for (Thread t : threads) {
            String name = t.getName();
            Thread.State state = t.getState();
            int priority = t.getPriority();
            String type = t.isDaemon() ? "Daemon" : "Normal";
            System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
        }
    }

    public synchronized void playGame(Position pos, int i, int tries){
        Random generator = new Random();

        // every time we go through  all the characters we increment our cycle
        if (count%tunePositions.size() == 0 && i != 1)
            incrementCycle();

        if (WHOLE_CYCLE%3 == 0 && WHOLE_CYCLE != 0){
            System.out.println("\nCycle #" + WHOLE_CYCLE + ". Relocating Mountain (F)...");
            
            Position randPos = new Position(generator.nextInt(5),generator.nextInt(5));
            while (!canMovePiece(itemPositions.get(0), randPos)){
                randPos.setFullPos(generator.nextInt(5),generator.nextInt(5));
            }
            
            System.out.println("\tMountain tile has moved from " + itemPositions.get(0) + " to " +
                                randPos);
            setPosition(itemPositions.get(0), randPos, 0);
        }

        try {
            Position newPos;
            // randomly decide the next direction to move the Tune
            int walkDir = generator.nextInt(4);

            if (walkDir == 0) { // if we roll a 0, move upwards
                newPos = new Position(pos.getX(),pos.getY()+1);
            } else if (walkDir == 1) { // if we roll a 1, move downwards
                newPos = new Position(pos.getX(),pos.getY()-1);
            } else if (walkDir == 2) { // if we roll a 2, move right
                newPos = new Position(pos.getX()+1,pos.getY());
            } else { // if we roll a 3, move left
                newPos = new Position(pos.getX()-1,pos.getY());
            }
            
            // keep recursing until we have found a valid movement
            if (!canMovePiece(pos,newPos) && tries < 25) {
                System.out.println("\t" + this.getName() + " failed while trying to move " + pos.toString() +
                        " --> " + newPos.toString());

                playGame(pos, i, tries+1);
            } else {
                // if we try to move 50+ times we should assume the player is blocked and cannot
                // make a move. In this case, we move on to the next characters turn
                if (tries >= 25) {
                    System.out.println("\t" + this.getName() + " has failed to move " + tries + " times. " +
                            "\nPlayer has no available moves. Turn is ending.");
                    increment();
                    return;
                } else {
                    ThreadData.this.setPosition(pos,newPos, i);
                    System.out.println("\t" + this.getName() + " has successfully moved " + pos.toString() +
                            " --> " + newPos.toString());
                    printGameBoard();
                    
                    increment(); // increment turn no matter what
                }
                
            }
            
            Thread.sleep(sleep);// wait .5 seconds before next cycle
            //printThreads();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}