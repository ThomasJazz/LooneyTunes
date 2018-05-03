import java.lang.Thread;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {

    // variables that are accessible by each thread
    SHARED shareData = new SHARED();
    private String letter, winner = "";
    private int id, WHOLE_CYCLE = count % 4;

    // static variables we only want to be created once and shared by every thread
    private static int rows = 5, columns = 5, count = 0;
    private static int sleep = 500;
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

    public Position getPosition(int id) {
        return tunePositions.get(id);
    }

    public String getWinner(){
        return winner;
    }

    public int getCount(){
        return count;
    }

    /**
     * Returns an integer representation of the type of tile at a given position
     * @param pos   The gameboard position we are checking the value of
     * @return      1 if the tile contains a character, 2 if the tile contains the mountain, 0 if the
     *              tile contains a Carrot/Flag. -1 if the tile is blank.
     */
    public int getTileType(Position pos) {
        String temp = gameBoard[pos.getX()][pos.getY()];

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
                gameBoard[position.getX()][position.getY()].equals("-") + " ");

        return gameBoard[position.getX()][position.getY()].equals("-");
    }

    // static methods that we only want to be called once
    public static void createGameBoard(){
        gameBoard = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard[j][i] = "-";
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
            String srcTemp = gameBoard[source.getX()][source.getY()];
            gameBoard[destination.getX()][destination.getY()] = srcTemp;

            if (srcTemp.equals("M")){

            }
            gameBoard[source.getX()][source.getY()] = "-";
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    // assign a string to a specified location
    public synchronized void setGameTile(Position pos, String str) {
        gameBoard[pos.getX()][pos.getY()] = str;
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
    public synchronized void setItemPositions(Position pos, int i) {
        // use try-catch block to decide whether we need to add a position or change
        // an existing one
        try {
            itemPositions.set(i, pos);
        } catch (IndexOutOfBoundsException e) {
            itemPositions.add(pos);
        }
    }

    public synchronized String[][] getGameBoard(){
        return gameBoard;
    }

    public synchronized void printGameBoard(){
        System.out.println("    0    1    2    3    4");
        System.out.println("    ----------------------");
        for (int i = 0; i < rows; i++) {
            System.out.print(i + " | ");
            for (int j = 0; j < rows; j++) {
                // just need this for board formatting
                if (j == rows-1){
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

    public synchronized void playGame(Position pos, int i){
        try {
            Random generator = new Random();
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
            if (!movePiece(pos,newPos)) {
                System.out.println(this.getName() + " failed while trying to move " + pos.toString() +
                        " --> " + newPos.toString());
                playGame(pos, i);
            } else {
                setTunePosition(newPos, i);
                increment();
                System.out.println(this.getName() + " has successfully moved " + pos.toString() +
                        " --> " + newPos.toString());
                printGameBoard();
            }
            Thread.sleep(sleep);// wait .5 seconds before next cycle
            //printThreads();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}