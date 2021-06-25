/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 *
 * @author Nuser
 */
public class AIadvanced {
    
    boolean foundX = false;
    
    public boolean testForFutureDoubleMoves(ConnectFourFrame game, int futureColumn, int futureRow) {
        // : this is what happens :
        
        
        boolean badMove = false;
        
        // generate a fake board
        int[][] testFutureBoard = new int[game.boardColumns + 1][game.boardRows + 1];
        

        for (int Row = 1; Row <= game.boardRows; Row++) {
                for (int Column = 1; Column <= game.boardColumns; Column++) {
                        testFutureBoard[Column][Row] = game.Board[Column][Row]; //assign all the spaces to be the same as the current board
                }
        }
        
        // put your piece in
        testFutureBoard[futureColumn][futureRow] = game.ThisPlayer;
        
        // then add a ENEMY piece to each column of that future board and see if it wins a lot
        // (i.e. check for doube moves)
        
        System.out.println("With piece in " + futureColumn + "," + futureRow + "checking for double");
        
        if (checkForDoubleMoves(game, testFutureBoard, game.ThisPlayer) != -1) {
            // if doubleMoves are found in any of the spaces then it is a bad move
            
            System.out.println("Oh nooo...");
            badMove = true;
        }
        
        
        return badMove;
    }
    
    public int checkForDoubleMoves(ConnectFourFrame game, int[][] currentBoard, int thisPlayer) {
        // before choosing a space check if any of the spaces would allow double moves
        
        int ColumnNumber = -1;
        int wins = 0;
        
        int[][] fakeBoard = new int[game.boardColumns + 1][game.boardRows + 1];
        
        System.out.println("!!!! #### Double move check");
        
        // FOR each column on the board
        
            // put a ENEMY piece in
            
                // THEN check each column and see how many times the enemy can win
                
                    // IF the enemy can win more than once then THAT IS A DOUBLE MOVE space
                    
                        // SO BLOCK IT!!!
                        
        for (int i = 1; i < game.boardColumns; i++) {
            
            // generate fake board
            // (clear any previous fake boards that have been made)
            for (int Row = 1; Row <= game.boardRows; Row++) {
                    for (int Column = 1; Column <= game.boardColumns; Column++) {
                            fakeBoard[Column][Row] = currentBoard[Column][Row]; 	//assign all the spaces to be the same as the current board
                    }
            }
            
            // get next free position in column (i) and put a piece in
            int rowY = FindNextFreePositionInColumn(fakeBoard, i);
            
            // if the rows not full then put the piece in
            if (rowY != -1) {
                fakeBoard[i][rowY] = thisPlayer;
                System.out.println("Fakely piece in " + i + "," + rowY);
            }
            
            
            
            // now check the fake board to see how many wins the ENEMY has (with that particular piece in it)
            
            AIplayer aiCheck = new AIplayer();
            
            aiCheck.initialize(game); // setup the AI player's variables
            
            //System.out.println("win V");
            wins = wins + aiCheck.CheckForWinVertical(fakeBoard, thisPlayer, 3, false);
            //System.out.println("win H");
            wins = wins + aiCheck.CheckForWinHorizontal(fakeBoard, thisPlayer, 3, false);
            //System.out.println("win D");
            wins = wins + aiCheck.CheckForWinDiagonal(fakeBoard, thisPlayer, 3, false);
            
            if (wins > 1) {
                // DOUBLE MOVE SPACE AHHH!!! put a piece in here!
                ColumnNumber = i;
                
                System.out.println("Double move space: " + i + " with " + wins + " wins");
                
                foundX = true;
                
                break;
            }
            
            wins = 0;
            System.out.println("Wins = 0");
        }
        
        
        return ColumnNumber;
    }
    
    // return a blank space (check upwards in the column selected until a blank space is found)
    public int FindNextFreePositionInColumn(int[][] Board, int ValidColumn) {
            int ThisRow = 1;

            //OUTPUT System.out.println("Making move " + ValidColumn);

            try {
                while (Board[ValidColumn][ThisRow] != 0) { 
                        ThisRow = ThisRow + 1;	
                }
            } catch (Exception e) {
                // if it breaks then it is full
                ThisRow = -1;
            }

            return ThisRow;
    }
    
}
