package core;

public class Tile {

    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_NORMAL_START_SPACE = 2;
    public static final int TYPE_FILLED = 9;


    private int HEIGHT;
    private int WIDTH;

    private int x;
    private int y;

    private int type;

    public void setType(int TYPE) {
        this.type = TYPE;
    }

    public int getType() {
        return this.type;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    /*
     * initialize default
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
