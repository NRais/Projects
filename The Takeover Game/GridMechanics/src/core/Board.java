package core;

public class Board {

    Tile[][] tiles;


    public Board(int x, int y) {
        tiles = new Tile[x][y];
    }


    /*
     * ***** BUILD ******
     */
    public void build(MapObject map) {

        // initialize all tiles
        for (Tile[] tileR : tiles) {
            for (Tile t : tileR) {
                t = new Tile();
            }
        }

        mapObjectToTileArray(map);
    }

    /*
     * **** DISPLAY ****
     */
    public void display() {

        // display all tiles
        for (Tile[] tileR : tiles) {
            for (Tile t : tileR) {
                t.draw();
            }
        }
    }

    /*
     * one to one transfer of map integers to tile types
     */
    private void mapObjectToTileArray(MapObject map) {

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {

                // transfer the map to the tiles
                tiles[x][y].setType(
                                    map.getMap()[x][y]
                                    );
            }
        }

    }

}
