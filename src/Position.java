package CS380LooneyTunes;

public class Position
{
   // **** FIELDS ****
   private int x, y;
   // automagically set our position array to x and y
   private int[] position = new int[2];
   
   // ****************************************************
   // **** CONSTRUCTORS ****
   public Position()
   {
   }

   // non-default constructor
   public Position(int x, int y)
   {
      this.x = x;
      this.y = y;
      position[0] = x;
      position[1] = y;
   }

   // copy constructor
   public Position(Position old)
   {
      this.x = old.getX();
      this.y = old.getY();
      position[0] = x;
      position[1] = y;
   }
   
   // ****************************************************
   // **** METHODS ****
   public int getX()
   {
      return x;
   }

   public int getY()
   {
      return y;
   }

   public int[] getFullPos()
   {
      return position;
   }

   // Set Position with both coordinates.
   public void setPosition(int x, int y)
   {
      this.x = x;
      this.y = y;
      position[0] = x;
      position[1] = y;
   }
   
   // Set Position with a new Position Object.
   public void setPosition(Position newPosition)
   {
      this.x = newPosition.getX();
      this.y = newPosition.getY();
      position[0] = x;
      position[1] = y;
   }

   public boolean isEquals(Position position)
   {
      return (this.x == position.getX() && this.y == position.getY());
   }

   @Override
   public String toString()
   {
      String pos = "[" + x + "," + y + "]";
      return pos;
   }
}
