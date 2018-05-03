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
        // 2d arrays have rows before columns like[row
        return "[" + y + "," + x + "]";
    }
}
