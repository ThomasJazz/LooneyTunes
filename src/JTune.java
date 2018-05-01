/**
 * I think rather than trying to copy over his c++ code we should just try
 * to come up with our own java solution that will produce the example output
 * and then work from there on the rest of the project.
 */
import java.lang.Thread;
private int N = 3, TWEETY = 0, MUTTLEY = 1, sleep = 1000;

// In Java we use multithreading by extending "Thread" class
class LooneyTune extends Thread {
  // pthread_t thread_id; we need to come up with a java solution for this
  int thread_num, condition, position, copy_FINISH_LINE, copy_COUNT_GLOBAL;
  String name;

  public void run() {
    try {
      System.out.println("Thread " + Thread.currentThread().getID() +
      " is running");
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
  }
}

class SHARED {
  int CONDITION_TOON, WHOLE_CYCLE, COUNT_GLOBAL, FINISH_LINE;
  int[] FROZEN = new int[2];
  int[] TOON_POSITION = new int[2];
  char[] winner = new char[15];
}
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
      pthread
    }

    public void run_API(Thread thread) {
      LooneyTune[] characters = new LooneyTune[3];
      setup_time_seed();

      while (!characters.copy_FINISH_LINE) {
        toon_signal(characters);
        Sleep *= 2;
      }
    }

    public static void main(String[] args){

    }
}
