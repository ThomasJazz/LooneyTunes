package ThreadTest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author: AlTimofeyev
 * @Date:   May 4, 2018 8:27:32 PM
 * @Desc:   Testing out how to use Threads in Java.
 *          (Don't delete package (line 1) yet and look at where to place breakpoints.)
 *          Set code breakpoints on:
 *          line 24 - testThread2.start(); .
 *          line 44 - System.out.println(testThread1); .
 *          
 *          Web site:
 *          http://www.codejava.net/java-core/concurrency/how-to-use-threads-in-java-create-start-pause-interrupt-and-join
 */
public class TestJTune
{
   public static void main(String[] args)
   {
      // Create two threads with a name and instance of TestThreadData.
      TestThreadData testThread1 = new TestThreadData("NumNums", 1);
      TestThreadData testThread2 = new TestThreadData("BAAAAAM", 2);
      
      System.out.println("******* STARTING THREADS 1 AND 2 *******");
      testThread1.start();
      testThread2.start();
      
      // Now try and wait for before interupting the threads.
      try
      {
         // After starting the threads, wait 10 seconds and then interupt them.
         TestThreadData.sleep(10000);
         testThread1.interrupt(); // Interupt the first thread while it's asleep in run(). (TERMINATE THREAD)
         //TestThreadData.sleep(4000); // This part is unneeded.
         testThread2.interrupt(); // Interupt the second thread while it's asleep in run(). (TERMINATE THREAD)
      }
      catch (InterruptedException ex)
      {
         // Don't know what this is.
         // But judginng by the naming, I'm guessing the interruption is logged
         // somewhere according to the class name --> TestJTune.class.getName().
         Logger.getLogger(TestJTune.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      // Print the toString() method.
      System.out.println(testThread1);
      System.out.println(testThread2);
   }
}
