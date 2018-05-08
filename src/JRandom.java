import java.util.Random;

public class JRandom {
    public int pubGetRandom(int rangeLow, int rangeHigh) {
        return getRandom(rangeLow, rangeHigh);
    }
    private int getRandom(int rangeLow, int rangeHigh) {
        Random rand = new Random();

        // Don't let myRand be negative!!
        double myRand = Math.abs(rand.nextInt() / (1.0 + Integer.MAX_VALUE));
        int range = rangeHigh - rangeLow + 1;
        int myRandScaled = (int) ((myRand * range) + rangeLow);

        return myRandScaled;
    }
}
