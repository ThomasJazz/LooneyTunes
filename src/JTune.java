/**
 * Project Description goes here
 */
import java.lang.Thread;

public class JTune {
    private void print_board_vertical(int toon_pos[]) {
        char toon_letter[2]={'T','M'};
        for (int row = 0; row < 10; row++){
            for (int agent = 0; agent < 2; agent++){
                System.out.print("|");
                if (toon_pos[agent] == row) {
                    System.out.print(toon_letter[agent] + "|");
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println("\n");
        }
    }

    private void print_board_horizontal(int toon_pos[]) {
        char toon_letter[2]={'T','M'};
        for (int column = 0; column < 10; column++) {
            System.out.print("_ ");
        }
        for (int agent = 0; agent < 2; agent++) {
            System.out.println();
            for(int column = 0; column < 10; column++) {
                if (toon_pos[agent]==column) {
                    System.out.print(toon_letter[agent] + " ");
                } else {
                    System.out.print("_ ");
                }
            }
        }
    }

    private int getRandom(int rangeLow, int rangeHigh) {
        double myRand = rand()/(1.0 + RAND_MAX);
        int range = rangeHigh - rangeLow + 1;
        int myRand_scaled = (myRand * range) + rangeLow;
        return myRand_scaled;
    }

    private void toon_signal(ThreadData *toon) {

    }
    public static void main(String[] args){

    }
}
