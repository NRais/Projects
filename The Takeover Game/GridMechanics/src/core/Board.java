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
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                tiles[x][y] = new Tile(x, y);
            }
        }

        mapObjectToTileArray(map);
    }

    /*
     * **** DISPLAY ****
     */
    public Tile[][] getBoard() {
        return tiles;
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
