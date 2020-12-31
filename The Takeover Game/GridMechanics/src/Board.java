public class Board {

    Tile[][] tiles;


    public Board() {
        this(9,9);
    }

    public Board(int x, int y) {
        tiles = new Tile[x][y];
    }


    public void build() {

        // initialize all tiles
        for (Tile[] tileR : tiles) {
            for (Tile t : tileR) {
                t = new Tile();
            }
        }
    }

    public void display() {

        // display all tiles
        for (Tile[] tileR : tiles) {
            for (Tile t : tileR) {
                t.draw();
            }
        }
    }

}
