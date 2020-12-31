package core;

public class MapObject {

    // THE MAP OBJECT CLASS
    // contains 3 important things
    // - NUMBER OF PLAYERS the map supports
    // - BOARD of the current map (2d array)
    // - IMAGE the maps little picture

    private int[][] mapBoard = null;
    private int numberOfPlayers = 0;
    //** TODO public ImageIcon MapPicture = null;

    /*
     * Initialize Map Object
     */
    public MapObject(int x, int y) {
        createMapFromDimensions(x, y);
    }
    public MapObject(String filePath) {
        // TODO
    }

    public int[][] getMap() {
        return mapBoard;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    private void loadMapFromFile() {
        // TODO
    }

    private void createMapFromDimensions(int totalX, int totalY) {

        mapBoard = new int[totalX][totalY];

        final int GAP = 1; // assume gap of 1 (number of white spaces between the black spaces)

        // formula to generate impassible spaces (all other spaces are white)
        for (int y = 0; y < totalY; y++) {
            for (int x = 0; x < totalX; x++) {

                //// IF IT IS A CORNER THEN PUT IN A START SPACE
                // corner is the 0,0 OR 0,Y OR X,Y OR X,0
                if ((x == mapBoard.length -1 | x == 0) && (y == mapBoard[0].length -1 | y == 0)) {

                    mapBoard[x][y] = Tile.TYPE_NORMAL_START_SPACE; // WHITE START

                }

                // THIS AUTOMATICALLY GENERATES A BLACK SQUARE UNDER CERTAIN CONDITIONS
                // black spaces with 1 white space in between each
                // if the x and y coordinates are both not even then it is an impassible space

                // ex:
                // 0,0,0,0,0
                // 0,1,0,1,0
                // 0,0,0,0,0
                else if (GAP == 1 && !(x%2 == 0) && !(y%2 == 0)) {

                    mapBoard[x][y] = Tile.TYPE_FILLED; // BLACK

                }

                //  THIS AUTOMATICALLY GENERATES A BLACK SQUARE UNDER CERTAIN CONDITIONS
                // generates black spaces with 2 white spaces in between each

                // ex:
                // 0,0,0,0,0,0
                // 0,1,0,0,1,0
                // 0,0,0,0,0,0
                // 0,0,0,0,0,0
                // 0,1,0,0,1,0
                //
                else if (GAP == 2 && ((y-1)%3 == 0) && ((x-1)%3) == 0) {

                    mapBoard[x][y] = Tile.TYPE_FILLED; // BLACK

                }

                // OTHERWISE PUT IN A WHITE SQUARE
                else {

                    mapBoard[x][y] = Tile.TYPE_NORMAL; // WHITE

                }

            }

        }
    }


}
