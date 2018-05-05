package ThreadTest;

import java.util.Arrays;

/**
 * @Author: AlTimofeyev
 * @Date:   May 4, 2018 8:27:10 PM
 * @Desc:   Testing out how to use Threads in Java.
 *          Web site:
 *          http://www.codejava.net/java-core/concurrency/how-to-use-threads-in-java-create-start-pause-interrupt-and-join
 */
public class TestThreadData extends Thread
{
   // Fields
   protected Thread thread;
   public int threadInstance;

   // Constructor
   public TestThreadData(String name, int threadInstnace)
   {
      thread = new Thread(name);
      this.threadInstance = threadInstnace;
   }

   @Override
   public void run()
   {
      // Method run() will run forever until it is interupted.
      for (int i = 0; i < 1; i++, i--)
      {
         try
         {
            System.out.println("\n******************************");
            System.out.println("*** In run() Method of Thread " + threadInstance + " ***");
            System.out.println("THREAD " + thread + " IS RUNNING!");
            System.out.println(this.toString());
            System.out.println("*** Thread " + threadInstance + " Is Going To SLEEP ***");
            Thread.sleep(2000);
            System.out.println("*** Thread " + threadInstance + " Is AWAKE ***");
            System.out.println("******************************\n");
         }
         catch (InterruptedException e)
         {
            System.out.println("THREAD " + threadInstance + " HAS STOPPED");
            System.out.println(Arrays.toString(e.getStackTrace()));
            return; // Exiting the run() method kills/TERMINATES the thread.
         }
      }
   }

   public String getThreadName()
   {
      return thread.getName();
   }

   public long getThreadID()
   {
      return thread.getId();
   }
   
   // Note that thread.getName() is different from Thread.currentThread().getName().
   @Override
   public String toString()
   {
      String report = "Thread Name: " + this.getThreadName() + "\tID: " + this.getThreadID();
      report += "\tState: " + this.getState();
      report += "\tCurrent Thread Name: " + Thread.currentThread().getName();
      report += "\tCurrent Thread State: " + Thread.currentThread().getState();
      return report;
   }
}
