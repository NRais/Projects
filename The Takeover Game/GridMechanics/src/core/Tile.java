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

    /*
     * initialize default
     */
    public Tile() {
        this(5, 5, 1024);
    }

    /*
     * initialize specifics
     */
    public Tile(int totalTilesX, int totalTilesY, double screenSize) {

        // TODO assign tile hieght width based upon screen size and total tiles

    }

    /*
     * draw the board onto the UI
     */
    public void draw() {

    }

}
