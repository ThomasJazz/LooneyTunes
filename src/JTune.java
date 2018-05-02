/**
 * I think rather than trying to copy over his c++ code we should just try
 * to come up with our own java solution that will produce the example output
 * and then work from there on the rest of the project.
 */
import java.lang.Thread;
import java.util.Random;




public class JTune extends ThreadData {
    protected SHARED shared = new SHARED();
    
    public void initData(ThreadData[] toon)
    {
        shared.WHOLE_CYCLE = 0;
        shared.COUNT_GLOBAL = 0;
        shared.CONDITION_TOON = 2;
        shared.FINISH_LINE = 0;
        shared.TOON_POSITION[0] = shared.TOON_POSITION[1] = 0;
        printBoardHorizontal(shared.TOON_POSITION);
        
        toon[0].condition=2;
	toon[0].id = 0;
	toon[0].position=0;
	toon[0].copy_COUNT_GLOBAL=shared.COUNT_GLOBAL;
	toon[0].copy_FINISH_LINE=shared.FINISH_LINE;
	toon[0].name = "Tweety";

	toon[1].id=1;
	toon[1].condition=0;
	toon[1].position=0;
	toon[1].copy_COUNT_GLOBAL=shared.COUNT_GLOBAL;
	toon[1].copy_FINISH_LINE=shared.FINISH_LINE;
	toon[1].name = "Muttley";

	toon[2].id=2;
	toon[2].condition=1;
	toon[2].position=0;
	toon[2].copy_COUNT_GLOBAL=shared.COUNT_GLOBAL;
	toon[2].copy_FINISH_LINE=shared.FINISH_LINE;
	toon[2].name = "Marvin";
    }
    
    
    
    private void printBoardVertical(int toon_pos[]) {
        char toon_letter[]={'T','M', 'B', 'D'};
        
        for (int row = 0; row < 10; row++){
            
            for (int agent = 0; agent < 4; agent++){
                System.out.print("|");
                if (toon_pos[agent] == row) {
                    System.out.print(toon_letter[agent] + "|");
                }
                else {
                    System.out.print(" |");
                }
            }
            System.out.println("\n");
        }
    }

    private void printBoardHorizontal(int toon_pos[]) {
        char toon_letter[]={'T','M', 'B', 'D'};
        
        for (int column = 0; column < 10; column++) {
            System.out.print("_ ");
        }
        
        for (int agent = 0; agent < 4; agent++) {
            System.out.println();
            
            for(int column = 0; column < 10; column++) {
                if (toon_pos[agent]==column) {
                    System.out.print(toon_letter[agent] + " ");
                }
                else {
                    System.out.print("_ ");
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

//    private void toon_signal(ThreadData toon) {
//      pthread
//    }
//
//    public void run_API(Thread thread) {
//      ThreadData[] characters = new ThreadData[3];
//      setup_time_seed();
//
//      while (!characters.copy_FINISH_LINE) {
//        toon_signal(characters);
//        Sleep *= 2;
//      }
//    }

    public static void main(String[] args){
//        JTune[] test = new JTune{};
//        
//        test
//        initData(testThread)
    }
}
