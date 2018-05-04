import java.lang.Thread;
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
    public synchronized boolean movePiece(Position source, Position destination) {
        // if we throw an Exception this means the movement is invalid and we must try again
        try {
            switch (getTileType(source)) {
                // If any of the normal character try to move to an unavailable square.
                case 1: {
                    int tileType = getTileType(destination);
                    if (tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 6)
                        throw new IndexOutOfBoundsException();
                }
                // If Marvin tries to move on the mountain without carrot.
                case 2: {
                    if (getTileType(destination) == 6)
                        throw new IndexOutOfBoundsException();
                }
                // If any normal character with carrot try to move to an unavailable square.
                case 3: {
                    int tileType = getTileType(destination);
                    if (tileType == 1 || tileType == 2 || tileType == 3 || tileType == 4 || tileType == 5)
                        throw new IndexOutOfBoundsException();
                    //else
                        //winner = getLetter()
                }
                case 4:
                    //if (getTileType(destination)==6)
                        //winner = ;
                case 5:
                    if (getTileType(destination)==6)
                        winner = "Marvin";
                case 6:
                    if (getTileType(destination)==6)
                        winner = "Marvin";
            }


            String srcTemp = gameBoard[source.getY()][source.getX()];
            gameBoard[destination.getY()][destination.getX()] = srcTemp;
            gameBoard[source.getY()][source.getX()] = "-";
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
     * @param pos   The position we are assigning to the looneyTune
     * @param i     The index/id of the looneyTune
     */
    public synchronized void setTunePosition(Position pos, int i) {
        // use try-catch block to decide whether we need to add a position or change
        // an existing one
        try {
            tunePositions.set(i,pos);
        } catch (IndexOutOfBoundsException e) {
            tunePositions.add(pos);
        }

    }

    // same as above method but for flags/carrots and the mountain
    public synchronized void setItemPosition(Position pos, int i) {
        // use try-catch block to decide whether we need to add a position or change
        // an existing one
        try {
            itemPositions.set(i, pos);
        } catch (IndexOutOfBoundsException e) {
            itemPositions.add(pos);
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
            boolean moved = false;

            while (!moved){
                Position randPos = new Position(generator.nextInt(4),generator.nextInt(4));
                if (isEmpty(randPos)){
                    System.out.println("\tMountain tile has moved from " + itemPositions.get(2) + " to " +
                    randPos);
                    movePiece(itemPositions.get(2), randPos);
                    setItemPosition(randPos,2);
                    moved = true;

                }
            }
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
            if (!movePiece(pos,newPos) && tries < 25) {
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
                    setTunePosition(newPos, i);
                    System.out.println("\t" + this.getName() + " has successfully moved " + pos.toString() +
                            " --> " + newPos.toString());
                    printGameBoard();
                }
                increment(); // increment turn no matter what
            }
            Thread.sleep(sleep);// wait .5 seconds before next cycle
            //printThreads();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}