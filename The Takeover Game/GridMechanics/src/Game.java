public class Game {

    public Board board;

    /**
     * On launch create the base of the game
     */
    public Game() {

        board = new Board();
        board.build();
        board.display();
    }


}
