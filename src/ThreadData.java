import java.lang.Thread;

// In Java we use multithreading by extending "Thread" class
public class ThreadData extends Thread {
  Thread thread = new Thread();
  int id, copyCountGlobal, sleep;
  Position position;
  String letter;

  public ThreadData(){}

  /**
   * Non-default constructor that sets the name, id, condition, and position of
   * the thread.
   * @param name
   * @param id
   */
  public ThreadData(String name, int id, String letter){
    setName(name);
    this.id = id;
    this.letter = letter;
    position = new Position();
    sleep = 1000;
  }

  public void run() {
    try {
      System.out.println("Thread " + Thread.currentThread().getId() +
      " is running " + this);
      thread.sleep(sleep);
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
  }

  public void setPosition(Position pos) {
    position.setFullPos(pos.getX(),pos.getY());
  }

  public String getLetter(){
    return letter;
  }
  public int[] getPosition(){
    return position.getFullPos();
  }
}
