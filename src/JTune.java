/**
 * I think rather than trying to copy over his c++ code we should just try
 * to come up with our own java solution that will produce the example output
 * and then work from there on the rest of the project.
 */
import java.util.Random;

public class JTune extends ThreadData {
    protected SHARED shared = new SHARED();
    private String[][] gameBoard;
    private ThreadData[] looneyTunes; // this array contains all our Threads
    int columns = 5, rows = 5;

    public void initData(ThreadData[] tune) {

        gameBoard = new String[columns][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameBoard[j][i] = "-";
            }
        }
        // initialize some of the shared data
        shared.WHOLE_CYCLE = 0;
        shared.COUNT_GLOBAL = 0;
        shared.FINISH_LINE = 0;

        // initializing all the threads
        tune[0] = new ThreadData("Bugs Bunny", 0, "B");

        tune[1] = new ThreadData("Taz", 1, "D");

        tune[2] = new ThreadData("Tweety", 2, "T");

        tune[3] = new ThreadData("Marvin", 3, "M");


        // Assign the locations for all the tune threads
        for (int i = 0; i < tune.length; i++){
            boolean placed = false;
            Position position = new Position();

            // keep trying new squares as long as the tune still has not been placed
            while (!placed) {
                position.setFullPos(getRandom(0,5),getRandom(0,5));
                placed = isEmpty(position);
            }

            // finally, we assign the position to our thread
            tune[i].setPosition(position);
        }
    }

    private void printBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.print(gameBoard[i][j].get);
            }
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
        return gameBoard[position.getX()][position.getY()].equals("-");
    }

    private void startThreads(ThreadData[] tune){
        // start all of our toons
        for (ThreadData currTune:tune){
            currTune.start();
        }
    }

    public boolean moveTune(ThreadData tune, Position position) {
        if (tune.getLetter().equals("M")){
            gameBoard[position.getX()][position.getY()] = tune.getLetter();
        } else if (tune.getLetter().equals("B")){

        }
    }

    public static void main(String[] args){
        new JTune().play();
    }
    public void play(){
        looneyTunes = new ThreadData[4];
        initData(looneyTunes);
        startThreads(looneyTunes);
    }
}
