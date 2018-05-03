import java.lang.Thread;
import java.util.Random;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {

    // variables that are accessible by each thread
    private String letter, winner = "";
    private int id, count, sleep, WHOLE_CYCLE = count % 4;

    // static variables we only want to be created once
    private static int rows = 5, columns = 5;
    private static String[][] gameBoard;
    private static Position[] tunePositions = new Position[4];
    private static Position[] itemPositions = new Position[3];

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
        sleep = 1000;
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

    public String getLetter(){
        return letter;
    }

    public Position getPosition(int id) {
        return tunePositions[id];
    }

    public String getWinner(){
        return winner;
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
        for (Position currPos:tunePositions)
            currPos = new Position();
    }

    public synchronized static void initItemPositions(){
        for (Position currPos:itemPositions)
            currPos = new Position();
    }

    // synchronized methods
    public synchronized boolean movePiece(Position source, Position destination) {
        // if we throw an Exception this means the movement is invalid and we must try again
        try {
            String temp = gameBoard[source.getX()][source.getY()];
            gameBoard[destination.getX()][destination.getY()] = temp;
            gameBoard[source.getX()][source.getY()] = "-";
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Returns an integer representation of the type of tile at a given position
     * @param pos   The gameboard position we are checking the value of
     * @return      1 if the tile contains a character, 2 if the tile contains the mountain, 0 if the
     *              tile contains a Carrot/Flag. -1 if the tile is blank.
     */
    public synchronized int getTileType(Position pos) {
        String temp = gameBoard[pos.getX()][pos.getY()];
        boolean isCharacter = false;

        if (temp.equals("B") || temp.equals("D") || temp.equals("T") || temp.equals("M")) {
            return 1;
        } else if (temp.equals("F")) {
            return 2;
        } else if (temp.equals("C")) {
            return 0;
        } else {
            return -1;
        }
    }

    public synchronized void setGameTile(Position pos, String str) {
        gameBoard[pos.getX()][pos.getY()] = str;
    }

    public synchronized void setTunePosition(Position pos, int i) {
        tunePositions[i] = pos;
    }

    public synchronized void setItemPositions(Position pos, int i) {
        itemPositions[i] = pos;
    }

    public synchronized String[][] getGameBoard(){
        return gameBoard;
    }

    public synchronized void printGameBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.print(getGameBoard()[i][j].toString() + "  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public synchronized void increment() {
        count++;
    }

    public synchronized void playGame(Position pos, int i){
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
            System.out.println(this.getName() + " failed while trying to move from " + pos.toString() +
                    " to " + newPos.toString());
            playGame(pos, i);
        } else {
            setTunePosition(newPos,i);
            increment();
            System.out.println(this.getName() + " has successfully moved from " + pos.toString() +
                    " to " + newPos.toString());
            printGameBoard();
        }
    }
}