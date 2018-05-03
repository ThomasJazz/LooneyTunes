/**
 * I think rather than trying to copy over his c++ code we should just try
 * to come up with our own java solution that will produce the example output
 * and then work from there on the rest of the project.
 */
import java.util.Random;

public class JTune {
    // class fields
    private ThreadData[] looneyTunes; // this array contains all our Threads
    private String[] pieces;

    private int columns = 5, rows = 5;

    private void initData(ThreadData[] tune) {

        // initializing all the threads
        tune[0] = new ThreadData("Bugs Bunny", 0, "B");
        tune[1] = new ThreadData("Taz", 1, "D");
        tune[2] = new ThreadData("Tweety", 2, "T");
        tune[3] = new ThreadData("Marvin", 3, "M");

        // Assign the locations for all the tune threads
        System.out.println("Assigning board locations to all threads...");
        int i = 0;
        for (ThreadData currTune:tune) {
            System.out.println("\tAssigning location for " + currTune.getName());

            boolean assigned = false;
            while (!assigned) {

                Position newPos = new Position(getRandom(0,rows-1),
                        getRandom(0,columns-1));

                if (currTune.isEmpty(newPos)){
                    currTune.setGameTile(newPos, currTune.getLetter());
                    assigned = true;
                    tune[0].setTunePosition(i, newPos);
                }
            }
            i++;
        }

        // initializing our Flag and Carrots
        pieces = new String[3];
        pieces[0] = "C";
        pieces[1] = "C";
        pieces[2] = "F";

        // assigning positions for our pieces
        System.out.println("\nAssigning board locations to flag and carrots...");
        for (String str:pieces) {
            System.out.println("\tAssigning location for " + str);
            boolean assigned = false;
            while (!assigned) {

                Position newPos = new Position(getRandom(0,rows-1),getRandom(0,columns-1));
                if (tune[0].isEmpty(newPos)){
                    tune[0].setGameTile(newPos, str);
                    assigned = true;
                }
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

    private void startThreads(ThreadData[] tune){
        // start all of our toons
        for (ThreadData currTune:tune)
            currTune.start();
    }

    // we just use the main method to call our play() method
    public static void main(String[] args){
        new JTune().play();
    }

    // set up the threads and then loop to play the game
    public void play(){
        looneyTunes = new ThreadData[4];
        initData(looneyTunes);
        System.out.println("\nSetting up gameboard...");
        startThreads(looneyTunes);

        System.out.println("Initial gameboard:");
        looneyTunes[0].printGameBoard();

        int i = 0;
        while (looneyTunes[i%4].getWinner().equals("") && i < 5) {
            System.out.println("Player turn: " + looneyTunes[i%4].getName());
            looneyTunes[i%4].playGame(looneyTunes[i%4].getPosition(i%4));
            i++;
        }
    }
}
