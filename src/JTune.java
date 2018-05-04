import java.util.Random;
import java.util.ArrayList;

public class JTune {
    // class fields
    private ThreadData[] looneyTunes; // this array contains all our Threads
    private ArrayList<String> items;

    private int columns = 5, rows = 5;

    private void initData(ThreadData[] tune) {

        // initializing all the threads
        tune[0] = new ThreadData("Bugs Bunny (B)", 0, "B");
        tune[1] = new ThreadData("Taz (D)", 1, "D");
        tune[2] = new ThreadData("Tweety (T)", 2, "T");
        tune[3] = new ThreadData("Marvin (M)", 3, "M");

        // Assign the locations for all the tune threads
        System.out.println("Assigning board locations to all threads...");
        int i = 0; // track which character we're on for array storage
        for (ThreadData currTune:tune) {
            System.out.println("\tAssigning location for " + currTune.getName());

            boolean assigned = false;
            while (!assigned) { // loop until we've successfully placed the character

                Position newPos = new Position(getRandom(0,rows-1),
                        getRandom(0,columns-1));

                // if position is empty, we go ahead and place the thread there
                if (currTune.isEmpty(newPos)) {
                    currTune.setGameTile(newPos, currTune.getLetter());
                    assigned = true;
                    tune[0].setTunePosition(newPos, i);
                }
            }
            i++;
        }

        // initializing our Flag and Carrots
        items = new ArrayList<>();
        items.add("C");
        items.add("C");
        items.add("F");

        // assigning positions for our items
        System.out.println("\nAssigning board locations to flag and carrots...");
        int j = 0;
        for (String str: items) {
            System.out.println("\tAssigning location for " + str);
            boolean assigned = false;
            while (!assigned) {

                Position newPos = new Position(getRandom(0,rows-1),
                        getRandom(0,columns-1));
                if (tune[0].isEmpty(newPos)) {
                    tune[0].setGameTile(newPos, str);
                    assigned = true;
                    tune[0].setItemPosition(newPos, j);
                }
            }
            j++;
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

    private void startThreads(ThreadData[] tune) {
        System.out.println("Starting threads...");
        // start all of our toons
        for (ThreadData currTune:tune)
            currTune.start();
        try {
            for (ThreadData currTune:tune) {
                // join these threads together so they execute predictably
                currTune.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // we just use the main method to call our play() method
    public static void main(String[] args) {
        new JTune().play();
    }

    // set up the threads and then loop to play the game
    public void play() {
        looneyTunes = new ThreadData[4];
        initData(looneyTunes);
        startThreads(looneyTunes);

        System.out.println("\nSetting up gameboard...");
        System.out.println("Initial gameboard:");
        looneyTunes[0].printGameBoard();

        int i = 0;
        // loops 4 times because we don't yet have a way of choosing a winner
        while (looneyTunes[i%looneyTunes[0].getTunePositions().size()].getWinner().equals("") && i < 20) {

            // make variable instead of having to use i%size a billion times
            int index = i%looneyTunes[0].getTunePositions().size();

            System.out.println("Player turn: " + looneyTunes[index].getName());
            looneyTunes[index].playGame(looneyTunes[index].getTunePosition(index),index,0);
            i++;
        }
        System.out.println("Game ended after "+ looneyTunes[0].getCount() + " rounds.");
    }
}