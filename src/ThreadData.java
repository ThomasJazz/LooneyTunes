import java.lang.Thread;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {

    // variables that are specific to each thread
    private int id, count, sleep;
    private String letter, winner;
    private int WHOLE_CYCLE, COUNT_GLOBAL;
    private Position[] toonPositions = new Position[4];

    // static variables we only want to be created once
    private static int rows = 5, columns = 5;
    private static String[][] gameBoard;



    // this is called once so we don't end up with duplicate gameboards
    static {
        createGameBoard();
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
            createGameBoard();
            System.out.println("Thread " + Thread.currentThread().getId() +
                    " is running " + this);

            Thread.sleep(sleep);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void setGameTile(Position pos, String str) {
        gameBoard[pos.getX()][pos.getY()] = str;
    }

    public void printGameBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.print(getGameBoard()[i][j].toString() + "  ");
            }
            System.out.println("");
        }
    }

    // this is static because we only want it to be called once
    public static void createGameBoard(){
        System.out.println("Setting up gameboard...");

        gameBoard = new String[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard[j][i] = "-";
            }
        }
    }

    public String getLetter(){
        return letter;
    }

    public String[][] getGameBoard(){
        return gameBoard;
    }

    public String getWinner(){
        return winner;
    }

    public boolean isEmpty(Position position){
        // if our gameboard String is empty, we will return true
        System.out.print(gameBoard[position.getX()][position.getY()].equals("-") + " ");
        System.out.print(gameBoard[position.getX()][position.getY()].toString());
        System.out.println(position.toString());
        return gameBoard[position.getX()][position.getY()].equals("-");
    }

    public synchronized void playGame(){

    }
}