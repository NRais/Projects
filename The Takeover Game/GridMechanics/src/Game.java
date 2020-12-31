import core.Board;
import core.MapObject;

public class Game {

    public Board board;

    /**
     * On launch create the base of the game
     */
    public Game() {

        board = new Board(9,9);
        board.build(new MapObject(9,9));
        board.display();

    }


}
