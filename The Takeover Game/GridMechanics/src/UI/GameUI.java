package UI;

import core.Board;
import core.Tile;

public class GameUI {

    // show board
    // TODO actual UI
    public void show(Board board) {

        Tile[][] tiles = board.getBoard();


        // display all tiles
        for (Tile[] tileR : tiles) {
            for (Tile t : tileR) {
                drawTile(t);
            }
            drawLine();
        }

    }

    private void drawTile(Tile t) {
        // TODO actual UI
        System.out.print("" + t.getType() + " ");
    }

    private void drawLine() {
        // TODO actual UI
        System.out.println("");
    }
}
