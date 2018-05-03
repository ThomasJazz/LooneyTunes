package cs380looneytoons;

public class Position {
    private int x, y;
    // automagically set our position array to x and y
    private int[] position = {x,y};

    public Position(){}

    // non-default constructor
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    // copy constructor
    public Position(Position old) {
        this.x = old.getX();
        this.y = old.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getFullPos() {
        return position;
    }

    // set both coordinates of the position at once
    public void setFullPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString(){
        String pos = "[" + x + "," + y + "]";
        return pos;
    }
}
