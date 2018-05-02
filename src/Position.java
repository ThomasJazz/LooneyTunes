public class Position {
    private int x, y;
    private int[] position = new int[2];

    public Position(){}
    public Position(int x, int y){
        this.x = x;
        this.y = y;
        position[0] = x;
        position[1] = y;
    }

    public Position(Position old) {
        this.x = old.getX();
        this.y = old.getY();
        this.position = old.getFullPos();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getFullPos(){
        int[] temp = {x,y};
        return temp;
    }

    public void setFullPos(int x, int y) {

    }
}
