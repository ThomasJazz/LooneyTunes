/**
 * I think rather than trying to copy over his c++ code we should just try
 * to come up with our own java solution that will produce the example output
 * and then work from there on the rest of the project.
 */
import java.util.Random;

public class JTune extends ThreadData {
    // class fields
    private SHARED shared = new SHARED();
    private String[][] gameBoard;
    private ThreadData[] looneyTunes; // this array contains all our Threads
    private String[] pieces;
    private int columns = 5, rows = 5;

    private void initData(ThreadData[] tune) {

        gameBoard = new String[columns][rows];
        // set the whole board to be empty
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard[j][i] = "-";
            }
        }
        // initialize some of the shared data
        shared.WHOLE_CYCLE = 0;
        shared.COUNT_GLOBAL = 0;
        shared.FINISH_LINE = 0;

        // initializing our Flag and Carrots
        pieces = new String[3];
        pieces[0] = "C";
        pieces[1] = "C";
        pieces[2] = "F";

        for (String str:pieces){
            boolean assigned = false;
            while (!assigned) {
                Position newPos = new Position(getRandom(0,rows-1),getRandom(0,columns-1));
                if (isEmpty(newPos)){
                    setGameTile(newPos, str);
                    assigned = true;
                }
            }
        }

        // initializing all the threads
        tune[0] = new ThreadData("Bugs Bunny", 0, "B");

        tune[1] = new ThreadData("Taz", 1, "D");

        tune[2] = new ThreadData("Tweety", 2, "T");

        tune[3] = new ThreadData("Marvin", 3, "M");


        // Assign the locations for all the tune threads
        for (ThreadData currTune:tune) {
            boolean assigned = false;
            while (!assigned) {
                Position newPos = new Position(getRandom(0,rows-1),getRandom(0,columns-1));
                if (isEmpty(newPos)){
                    setGameTile(newPos, currTune.getLetter());
                    assigned = true;
                }
            }
        }
    }

    private void printGameBoard(){
        System.out.println("Initial gameboard...");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.print(gameBoard[i][j].toString() + "  ");
            }
            System.out.println("");
        }
    }

    private int getRandom(int rangeLow, int rangeHigh) {
        Random rand = new Random();
        
        // Don't let myRand be negative!!
        double myRand = Math.abs(rand.nextInt() / (1.0 + Integer.MAX_VALUE));
        int range = rangeHigh - rangeLow + 1;
        int myRandScaled = (int) ((myRand * range) + rangeLow);
        
        return myRandScaled;
    }

    private boolean isEmpty(Position position){
        // if our gameboard String is empty, we will return true
        System.out.print(gameBoard[position.getX()][position.getY()].equals("-") + " ");
        System.out.print(gameBoard[position.getX()][position.getY()].toString());
        System.out.println(position.toString());
        return gameBoard[position.getX()][position.getY()].equals("-");
    }

    private void startThreads(ThreadData[] tune){
        // start all of our toons
        for (ThreadData currTune:tune){
            currTune.start();
        }
    }

    /**
     *
     * @param tune
     * @param position
     * @return
     */
    public boolean moveTune(ThreadData tune, Position position) {
        gameBoard[position.getX()][position.getY()] = tune.getLetter();
        /*
        if (tune.getLetter().equals("M")){
            gameBoard[position.getX()][position.getY()] = tune.getLetter();
        } else if (tune.getLetter().equals("B") || tune.getLetter().equals("D")
                || tune.getLetter().equals("T")) {
            gameBoard[position.getX()][position.getY()] = tune.getLetter();
        }
        */
        return true;
    }

    private void setGameTile(Position pos, String str) {
        gameBoard[pos.getX()][pos.getY()] = str;
    }

    public static void main(String[] args){
        new JTune().play();
    }
    public void play(){
        looneyTunes = new ThreadData[4];
        initData(looneyTunes);
        startThreads(looneyTunes);
        printGameBoard();
    }
}
