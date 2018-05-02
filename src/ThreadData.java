package cs380looneytoons;

import java.lang.Thread;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {
  // pthread_t thread_id; we need to come up with a java solution for this
  int thread_num, id, condition, position, copy_FINISH_LINE, copy_COUNT_GLOBAL;
  String name;
  
  
  public void run() {
    try {
      System.out.println("Thread " + Thread.currentThread().getId() +
      " is running");
    }
    
    catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
  }
}