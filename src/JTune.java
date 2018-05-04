import java.util.Random;
import java.util.ArrayList;

public class JTune {
    // class fields
    private ArrayList<ThreadData> looneyTunes; // this array contains all our Threads
    private ArrayList<String> items;

    private int columns = 5, rows = 5;

    private void initData(ArrayList<ThreadData> tune) {

        // initializing all the threads
        tune.add(0, new ThreadData("Marvin (M)", 0, "M"));
        tune.add(1,new ThreadData("Bugs Bunny (B)", 1, "B"));
        tune.add(2, new ThreadData("Taz (D)", 2, "D"));
        tune.add(3, new ThreadData("Tweety (T)", 3, "T"));

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
                    currTune.setPosition(newPos, newPos, i);
                }
            }
            i++;
        }

        // initializing our Flag and Carrots
        items = new ArrayList<>();
        items.add("F");
        items.add("C");
        items.add("C");

        // assigning positions for our items
        System.out.println("\nAssigning board locations to flag and carrots...");
        int j = 0;
        for (String str: items) {
            System.out.println("\tAssigning location for " + str);
            boolean assigned = false;
            while (!assigned) {

                Position newPos = new Position(getRandom(0,rows-1),
                        getRandom(0,columns-1));
                if (tune.get(0).isEmpty(newPos)) {
                    tune.get(0).setGameTile(newPos, str);
                    assigned = true;
                    tune.get(0).setPosition(newPos, newPos, j);
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

    private void startThreads(ArrayList<ThreadData> tune) {
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
        looneyTunes = new ArrayList<ThreadData>();
        initData(looneyTunes);
        startThreads(looneyTunes);

        System.out.println("\nSetting up gameboard...");
        System.out.println("Initial gameboard:");
        looneyTunes.get(0).printGameBoard();

        for (int i = 0; looneyTunes.get(i%looneyTunes.size()).getWinner().equals("") && i < 40; i++) {
            ThreadData curr = looneyTunes.get(i%looneyTunes.size());
            // make variable instead of having to use i%size a billion times
            int index = i%looneyTunes.size();
            looneyTunes.get(index).setKillTarget(-1);


            System.out.println("Player turn: " + curr.getName());
            looneyTunes.get(index).playGame(curr.getTunePosition(index),index,0);

            if (curr.getKillTarget() != -1) {
                try {
                    String dead = looneyTunes.get(curr.getKillTarget()).getName();
                    System.out.println("*********************************************");
                    System.out.println("Marvin is locked, loaded, and ready to kill." +
                            "\nSay goodbye, " + looneyTunes.get(curr.getKillTarget()).getName() + ".");

                    looneyTunes.get(curr.getKillTarget()).join(); // kill the thread of the player
                    looneyTunes.remove(curr.getKillTarget());
                    System.out.println("Player " + dead + " has been killed successfully.");
                    System.out.println(curr.getName() + "'s new location is " + curr.getTunePosition(0));
                    System.out.println("*********************************************");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Failed killing target.");
                }

                // debugging
                System.out.println("LooneyTunes contents: " + looneyTunes.toString() +
                        "\n" + looneyTunes.get(0).getTunePositions());

            }
        }

        System.out.println("Game ended after "+ looneyTunes.get(0).getCount() + " rounds.");
        System.out.println("Winner: " + looneyTunes.get(0).getWinner());
    }
}