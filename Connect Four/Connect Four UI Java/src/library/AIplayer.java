/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * Copyright 2017 Nathan Rais 
 * 
 *      This file is part of Connect Four.
 *
 *   Connect Four by Nathan Rais is free software 
 *   but is licensed under the terms of the Creative Commons 
 *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *   you may redistribute this as long as you give attribution and do not 
 *   modify any part of this software in any way.
 *
 *   Connect Four is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   CC BY-ND license for more details.
 *
 *   You should have received a copy of the CC BY-ND license along with 
 *   Connect Four. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 *
 */
public class AIplayer {
    //this is the most important one '
	int columnNumber = 0	;	//this is the column the ai will drop a piece into, while its 0 we know to continue looking
	
	boolean stillChecking = true;
        
        ConnectFourFrame game;
	
	int OtherPlayer = 1;
        
        
        public void initialize(ConnectFourFrame ourGame) {
            game = ourGame;
                
            //System.out.println("AI turn");

            // caculate who the other player is
            if (game.ThisPlayer == 1) {
                OtherPlayer = 2;
            } else if (game.ThisPlayer == 2) {
                OtherPlayer = 1; 
            }
        }
	
	//NOTE: a new variable DefenseOnly has been added, when playing defense only the computer checks for potential defensive move but not offensive ones
	//....if no move are found the computer does not go. This is used for the AI hazard check (ensuring that this move will not create a potential hazard in future)
	public int takeTurn(ConnectFourFrame ourGame) {
		int ColumnNumber;
                
                System.out.println("AI turn");
                
                initialize(ourGame);
                
		//OUTPUT System.out.println("Ai turn");
				
		//#### the ai player has to choose a column #### '
		
		//first check if the AI can win
			
			//check if any columns have 3 pieces on top of each other
			
			//check if any rows have 3 pieces next to each other
			
			//check if any diagonals are available to win
			
			//NOTE: the order does NOT matter, if their is more than one than the AI wins no matter which it chooses
			
			//OUTPUT System.out.println("Ai check for wins");
                        
                        //NOTE: that ALL difficulties check for their own wins
		
			ColumnNumber = CheckForWinVertical(game.Board, game.ThisPlayer, 3, true);
			ColumnNumber = CheckForWinHorizontal(game.Board, game.ThisPlayer, 3, true);
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("0Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinDiagonal(game.Board, game.ThisPlayer, 3, true);
                        }
                        
			
                        //OUTPUT System.out.println("and " + stillChecking);
		//second check if the enemy player is going to win
			
			//check if any columns have 3 enemy pieces on top of each other
			
			//check if any rows have 3 enemy pieces next to each other
			
			//check if any diagonals are available to win
			
			//NOTE: the order does NOT matter, if there are more than one loss the AI looses in all circumstances
			
                        
		if (stillChecking == true) {
			//OUTPUT System.out.println("Other player check for wins");
                        //NOTE: on EASY the DIAGONAL WIN IS NOT ALWAYS STOPPED
                        
                        // if it is easy he will block vertical wins unless he is a fool (so 1/2 of the time he will block)
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("1Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinVertical(game.Board, OtherPlayer, 3, true);
                        }
                        
			ColumnNumber = CheckForWinHorizontal(game.Board, OtherPlayer, 3, true);
                        
                        // if it is easy than only if he happens to notice (1/3 of the time) will he block diagonal wins
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("2Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinDiagonal(game.Board, OtherPlayer, 3, true);
                        }
		}
		
                // ---------------- 
                // CHECK FOR DOUBLE MOVES
                // ---------------- 
                
                // only on hard
                if (stillChecking == true && "Hard".equals(game.AIdifficulty)) {
                    AIadvanced AI = new AIadvanced();
                    
                    // first check if you can create a double move
                    ColumnNumber = AI.checkForDoubleMoves(game, game.Board, game.ThisPlayer);
                    
                    stillChecking = !AI.foundX; // if the X has been found (T) then stop checking
                    
                    if (stillChecking == true) {
                    
                        // then check if there are any impending double moves for them
                        ColumnNumber = AI.checkForDoubleMoves(game, game.Board, OtherPlayer);
                        
                        stillChecking = !AI.foundX; // if the X has been found (T) then stop checking
                    }
                    
                    System.out.println("Still Checking:  " + stillChecking);
                    
                    // TODO
                    // - add a double move checker
                    //     -  add a piece to each column
                    //          - then check if their are any 3 in a rows that also have a 3 in a row above it or below it
                    
                }
                
                // ---------------- 

			
		//if nothing is found then check if the enemy is near winning
		
			//check if any rows have 2 enemy pieces
			
			//check if any diagonals have 2 enemy pieces
			
			//check if any columns have 2 enemy pieces
			
			
			//NOTE: the order DOES matter here, if their are more than one option rows are the most likely to allow the other player to win
			//(columns have only one way to win: the top, and diagonals are slightly harder to win: because they have to get very high)
			
		if (stillChecking == true) {
			//OUTPUT System.out.println("Other player check for good moves");
			
                        // if its easy he always blocks horizontal good moves unless he happens to not notice (1/3) of the time
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("3Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinHorizontal(game.Board, OtherPlayer, 2, true);
                        }
                        
                        // if its easy he only blocks diagonal good moves if he happens to notice (1/2 of the time)
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("4Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinDiagonal(game.Board, OtherPlayer, 2, true);
                        }
			ColumnNumber = CheckForWinVertical(game.Board, OtherPlayer, 2, true);
		}
			
		
		//if nothing is found then check if the AI is near winning
			
			//check if any diagonals have 2 pieces
		
			//check if any rows have 2 pieces
                        
			//check if any columns have 2 pieces
			
			//NOTE: the order DOES matter here, the human player is more likely to spot and block stacks and rows of 3 than diagonals
			
		if (stillChecking == true) {
			//OUTPUT System.out.println("Ai check for good moves");
			
                        if (("Easy".equals(game.AIdifficulty) && oneOutOf(100)) | !"Easy".equals(game.AIdifficulty)) {
                            System.out.println("5Doing this because of luck or medium difficulty");
                            ColumnNumber = CheckForWinDiagonal(game.Board, game.ThisPlayer, 2, true);
                        }
			ColumnNumber = CheckForWinHorizontal(game.Board, game.ThisPlayer, 2, true);
			ColumnNumber = CheckForWinVertical(game.Board, game.ThisPlayer, 2, true);
		}
		
		//NOTHING?? Well then just pick one randomly
		if (stillChecking == true) {
			ColumnNumber = RandomRow(game.Board);
		}
	
                
                System.out.println("AI like : x - " + ColumnNumber);
		
		return ColumnNumber;
	}
	
	
	//###################################### '
	
	
	//##       VERTICAL WIN     ##//
	int CheckForWinVertical(int[][] currentBoard, int player, int amount, boolean findFirst) {
	
                int wins = 0;
            
            //OUTPUT System.out.println("check vertical, and - " + stillChecking);
            
		if (stillChecking == true) {
			int topPiece;
			
			//check each column
			for (int x = 1; x <= game.boardColumns; x++) {
			
				//assign variables
				topPiece = 0;		
							
						
				//get the topmost piece ->
					//check the current top spot
					//if its not blank then its the top piece, else move down 1
				for (int y = game.boardRows; y >= 1; y--) {	
					if (currentBoard[x][y] != (game.BLANK)) {
                                                //OUTPUT System.out.println("Not a blank at " + x + "," + y);
						//then stop searching because this is the top most piece
						topPiece = y;
						
						//then if we find a top piece we need to stop the loop (just skip to the end)
						y = 1;
					}
                                }		
			
				//if the column is lower than 3 than there is no way it will have enough to make a 4 in a row
				//but if it is too high then it is pointless also, too high is determined by:
					//#(the amount of spaces above the top piece) + #(the number of spaces we are looking for, which could be below the top piece) != (is not) less than 4
					
				
				//so ensure it is above 3 and enough below the top
				if (topPiece >= amount && game.boardRows - topPiece + amount >= 4) {
								
					//if (the top 3 items in this column are the AI's than that is a possible 4 in a row for the AI
					
					try { 
						if ((currentBoard[x][topPiece] == (player)) &&
							(currentBoard[x][topPiece - 1] == (player) | amount < 2) &&		
							(currentBoard[x][topPiece - 2] == (player) | amount < 3)) {
							
							if (TestFutureMoves(currentBoard, amount, x, topPiece+1)) {
								
								//either the one down piece is yours OR you only want to find 1 piece
								//either the two down piece is yours OR you only want to find 2 pieces
								//And in all circumstances the move you make CANNOT violate the future move policy: this ensures that you don't make a bad move that the enemy can win because of
								
								//FOUND A PLACE TO PUT THE PIECE
								System.out.println("Putting piece " + x + "," + (topPiece+1) + " vertical reasons (" + amount + ") for player " + player);
								
								
								columnNumber = x;
								
                                                                if (findFirst == true) {
                                                                
                                                                    stillChecking = false;
                                                                } else {
                                                                    //otherwise we want to keep track of how many
                                                                    //wins there are
                                                                    
                                                                    wins++;
                                                                    
                                                                    System.out.println("Wins ++ because if a piece is put into " + x + " then there will be a vertical " + amount + " for p " + player);
                                                                }
								
								//end loop check
								//Goto End_of_loop:
								
							}
							
						}
                                        } catch (Exception e) {
						//if it breaks than it must be out of the range of the board
					}							
				}
			}
			
			//End_of_loop:
			
		}
		
                // if we just care about the first win then return it
                if (findFirst == true) { 
                
                    return columnNumber;
                } else {
                    
                    // otherwise return how many wins there are
                    
                    return wins;
                }
	}
		
	
	
	//##    HORIZONTAL WIN   ## '
	public int CheckForWinHorizontal(int[][] currentBoard, int player, int amount, boolean findFirst) {

            //OUTPUT System.out.println("Check horizontal and - " + stillChecking);
            
            int wins = 0;
            
            
            if (stillChecking == true) { //if we haven't found something yet
                    
                    entire_loop:
                    //check each row
                    for (int y = 1; y <= game.boardRows; y++) { // >>>
                        
                        //System.out.println("Checking row " + y);
                        
                        int count = 0;

                        //check if their are enough of your pieces in this row to qualify
                        for (int x = 1; x <= game.boardColumns; x++) {
                            
                                //count if it is the AI's
                                if (currentBoard[x][y] == (player)) {
                                        count = count + 1;
                                }

                        }
                        

                        //if their are enough to qualify then it is possible to put a piece in this row (note: if there are not less than #game.boardColumns than the row is full)

                        //(but if for example we are looking for a 3 in a row and there are only 2 of your pieces than there aint gonna be any 3 in a row here)
                        if (count >= amount && count < game.boardColumns) {

                            //System.out.println("Enough to qualify in row " + y);

                            //check each space if ->
                                    //this space is game.BLANK & the space under this is not ;)
                                    //your left pieces + your pieces to the right are = 3 (or more)

                            List<Integer> al = Arrays.asList(4,5,3,6,2,7,1);
                             
                            //INWARDS to OUT
                            for (int alElement = 0; alElement < al.size(); alElement++) {
                                int x = al.get(alElement);
                                
                                //for each space in the row count how many pieces (of the same team) are adjacent
                                int numberOfYourAdjacent = 0;
                                int numberOfFreeAdjacent = 0;
                                
                                boolean stableBeneath = true; // if the try crashes it will be the bottom of the board which counts
                                try {
                                     stableBeneath = currentBoard[x][y - 1] != game.BLANK | y - 1 == 0 ; // if below is not blank (0) or if below is the bottom of the board
                                } catch (Exception e) {}
                                
                                //OUTPUT System.out.println("below " + x + "," + (y) + " = " + stableBeneath);
                                
                                if (currentBoard[x][y] == (game.BLANK) && stableBeneath) {//if the space is blank then it could be a canidate for the ai's piece
                                                  //but if the space underneath is blank (which can happen) then the ai will drop down 
                                    
                                    //System.out.println("Blank and stable " + x + "," + y);       

                                        //i use for loops because it saves time writing a billion if's

                                        //
                                        //boolean flatOutChecker = false; // this is a variable that looks for plain and simple (and stupid) moves that often beet the ai
                                        // this particular boolean is used to check for wins on either side so a space that has 
                                        
                                        // !!!!!!!! LOOP TO CHECK LEFT AND RIGHT !!!!!!!!!
                                        // this first loop runs twice first it times everything by -1 and then by 1
                                        // this allows the if statements to check all the spaces to the left (-) of the current space, and then all the spaces to the right (+)
                                        for (int direction = -1; direction <= 1; direction = direction + 2) {
                                            
                                            // after/before checking a direction reset the cap
                                            // basically the cap ensures pieces that are sperated with blanks such as:  1 _ 1 _ 1
                                            // are not counted, because even though to the computer it might seem that there are 3 adjacent pieces
                                            // if it puts a piece into here 1 1 1 _ 1 he hasn't actually won (despite now having 4 pieces)
                                            // ... thats what the cap is making sure spaces seperated by BLANKS are NOT counted
                                            boolean cap = false;
                                            
                                            //this loop runs from 1 to 3 checking each space in the row 1 away, 2 away, 3 away
                                            for (int spaces = 1; spaces <= 3; spaces++) {
                                                

                                                try {
                                                    // the space we check is the middle (x,y) ofset by #space * #direction
                                                    // if it is yours than count it
                                                    // (unless we have already found a blank, in which case the cap will be TRUE)
                                                    if (currentBoard[x + spaces*direction][y] == (player) && !cap) {

                                                            numberOfYourAdjacent = numberOfYourAdjacent + 1;
                                                            
                                                    } else {
                                                        // if it is not your and not a blank then it is either: 
                                                        //		the other player, 
                                                        //		or the edge, 
                                                        // so stop counting the spaces
                                                        if (currentBoard[x + spaces*direction][y] == (game.BLANK)) {
                                                            
                                                            cap = true;
                                                            
                                                            //###  STRATEGY POINT ### '
                                                            //but for offensive strategy we will not count blanks if there is nothing under them
                                                            //AND they are the other players
                                                                                    
                                                            // blank under 
                                                            boolean blankUnder = false;
                                                            
                                                            try {
                                                                blankUnder = currentBoard[x + spaces*direction][y - 1] == (game.BLANK);
                                                            } catch (Exception e) {
                                                                //OUTPUT System.out.println("on the bottom");
                                                            } // if it fails then it will still be false (cause it will only fail if it is the edge (i.e. done))
                                                            
                                                            if (!blankUnder) {
                                                                numberOfFreeAdjacent = numberOfFreeAdjacent + 1;
                                                            }

                                                        } else {
                                                            //stop counting (skip to the end)
                                                            break;
                                                        }

                                                    }

                                                } catch(Exception e) {
                                                    //if it doesn't work than that is the edge of the board

                                                    //so skip to the end of the loop
                                                    break;
                                                }

                                            }
                                        }

                                }

                                //OUTPUT System.out.println("In Here");
                                
                                //if the number of adjacent is more than we are looking for
                                //AND if number of free and your adjacent is enough to make a lineUp4 (note: numberfree + number adjacent only have to be 3 because the space we are entering makes the fourth)
                                //	(all this is checked for because it may be that their are 2 adjacent on the right but all the rest are full, then it is pointless to play here)
                                if (numberOfYourAdjacent >= amount && (numberOfFreeAdjacent + numberOfYourAdjacent >= 3))  {
                                        if (TestFutureMoves(currentBoard, amount, x, y)) {

                                                //NOTE: the TestFutureMoves is a boolean function that returns true as long as the other player can't win in the space above this space

                                                //FOUND A PLACE TO PUT THE PIECE
                                                columnNumber = x;
                                                System.out.println("Putting piece " + x + "," + y + " horizontal reasons (" + amount + ") for player " + player);
                                                
                                                // if we only care about the first one then stop checking
                                                if (findFirst == true) {
                                                                
                                                    stillChecking = false;
                                                    
                                                    //end loop search
                                                    break entire_loop;
                                                } else {
                                                    //otherwise we want to keep track of how many
                                                    //wins there are

                                                    wins++;

                                                    System.out.println("Wins ++ because if a piece is put into " + x + " then there will be a horizontal " + amount + " for p " + player);
                                                }


                                        }
                                }

                            }		

                        }


                    }
            }

            // if we just care about the first win then return it
            if (findFirst == true) { 

                return columnNumber;
            } else {

                // otherwise return how many wins there are

                return wins;
            }
	}
	
	
	
	//##    DIAGONAL WIN   ## '
	public int CheckForWinDiagonal(int[][] currentBoard, int player, int amount, boolean findFirst) {
            
            int wins = 0;
            
            ///////OUTPUT System.out.println("Check diagonal Yand - " + stillChecking);
		
		if (stillChecking == true) {
		
			int counter = 0;
			int blankCounter = 0;
			
			boolean cap = false;
			
			//for each space, that has a space under it that could be dropped onto, we want to check how many their are diagonally adjacent
			
                        List<Integer> al = Arrays.asList(4,5,3,6,2,7,1);
                                                
                        entire_loop:
			//INWARDS to OUT
			for (int alElement = 0; alElement < al.size(); alElement++) {
                            
                        
                            int x = al.get(alElement);
                            
                            for (int y = 1; y <= game.boardRows; y++) {

                                if (stillChecking == true) {

                                    // create a boolean
                                    boolean blankBelow = true;
                                    
                                    // check if there is a space under the current space
                                    // if there IS were bad (false) otherwise were good (true
                                    try {
                                        blankBelow = currentBoard[x][y - 1] != (game.BLANK);
                                    } catch (Exception e) {} // if it breaks than it is the bottom row (with nothing underneath) so were good (stay true)
                                    
                                    if (blankBelow) {
                        

                                        ////OUTPUT System.out.println("Checking {0},{1} cause it might work", x,y)

                                        //for each of these spaces we want to count how many diagonally adjacent their are that fit the criteria
                                        //first the similar diagonal (-1,-1 to -3,-3 and 1,1 to 3,3) then the other diagonal (1,-1 to 3,-3 and -1,1 to -3,3)

                                        //for each of these diagonals we loop through each space and count if it is our players, if it isn't we stop counting
                                        //then at the end if the counter is the number we are looking for then we put the piece in

                                        for (int direction = 1; direction >= -1; direction = direction - 2) {
                                            for (int multiplier = -1; multiplier <= 1; multiplier = multiplier + 2) {
                                                for (int position = 1; position <= 3; position++) {

                                                    try { 
                                                            if ((currentBoard[x + position*multiplier][y + position*multiplier*direction]) == (player)) {
                                                                    //if the diagonal is the correct player than that means we have 2 spaces in a row
                                                                    //now check to see if there are anymore in that diagonal direction

                                                                    //TODO this doesn't seem right????
                                                                    if (cap != true) {

                                                                            counter = counter + 1;
                                                                            
                                                                            //OUTPUT System.out.println("counter ++");

                                                                    }

                                                            } else {
                                                                    //if its not blank then count that in the other counter
                                                                    if ((currentBoard[x + position*multiplier][y + position*multiplier*direction]) == (game.BLANK)) {

                                                                            blankCounter = blankCounter + 1;
                                                                            
                                                                            //OUTPUT System.out.println("blank counter ++");

                                                                            //and this directions counter is capped (no more adjacents should be counted because their is a piece in the way)
                                                                            cap = true;

                                                                    } else {
                                                                        
                                                                            ////OUTPUT System.out.println("found an opposite piece, killing this direction");
                                                                        
                                                                            //if its the other player then stop searching (skip to the end)
                                                                            break;										

                                                                    }

                                                            }
                                                    } catch(Exception e) {						
                                                            //there is no space here so just ignore it (and stop checking)

                                                            break;
                                                    }

                                                    
                                                    //------
                                                }
                                                //reset the cap at the end of each diagonal direction (ne,se,sw,nw)
                                                //Now it has finished searching *1,*2,*3 of one direction so it resets the cap before it starts on the next direction

                                                //OUTPUT System.out.println("Cap reset");
                                                cap = false;
                                            }

                                            //if we counted enough && their are enough blank spaces adjacent that it matters
                                            //(enough is defined by 1 (for the space your in) + counter (the number you've found) + blankCounter (the number left available))
                                            //(and enough needs to be at least 4)
                                            //OUTPUT System.out.println("Count : " + counter + " , blanks: " + blankCounter);
                                            
                                            boolean enough = 1 + counter + blankCounter >= 4;
                                            boolean inTheBoard = false;
                                            
                                            try {
                                                    if (currentBoard[x][y] == (game.BLANK)) {
                                                            inTheBoard = true;
                                                    }
                                            } catch(Exception e) {
                                                    //well i guess its aint in th//board ehh ;)
                                            }

                                            //if the amount that has been counted (#counter) is more than the amount we are looking for AND
                                            //enough blank spaces and your spaces that a 4 in a row could be made AND
                                            //if the space we are trying to put it in is blank (not above the top of the board)
                                            if (counter >= amount && enough && inTheBoard) {
                                                    if (TestFutureMoves(currentBoard, amount, x, y)) {
                                                        //NOTE: the TestFutureMoves is a boolean function that returns true if the other player can't win if you put this piece in

                                                        //FOUND A PLACE TO PUT THE PIECE
                                                        columnNumber = x;

                                                        System.out.println("Putting piece in " + x + "," + y + " for diagonal reasons (" + amount + ") for player " + player);

                                                        // if we only care about the first one then stop checking
                                                        if (findFirst == true) {

                                                            stillChecking = false;

                                                            //end loop check
                                                            break entire_loop;
                                                        } else {
                                                            //otherwise we want to keep track of how many
                                                            //wins there are

                                                            wins++;

                                                           System.out.println("Wins ++ because if a piece is put into " + x + " then there will be a diagonal " + amount + " for p " + player);
                                                        }


                                                    }
                                            } else {
                                                //if there isn't a winner than reset the counter and check the other direction

                                                counter = 0;
                                                blankCounter = 0;
                                            }

                                        }
                                    }

                                }

                            }
			}
			
			
		}
		
		// if we just care about the first win then return it
            if (findFirst == true) { 

                return columnNumber;
            } else {

                // otherwise return how many wins there are

                return wins;
            }
	}
	
	
	
	
	//RANDOMIZER '
	//this function gets a random number
	public int RandomRow(int[][] currentBoard) {
		int randomX;
                
                boolean willNotMakeThemLose;
                int attempts = 0;
		
		//run the randomizer until it picks a space that DOES NOT have a piece in every space
		//(we will just check the top space (x,y) > (#column randomized , #number of rows in the board))
                //AND EITHER does NOT cause the AI to loose
                //OR the attempts run out (for example if there are 0 good moves than search 10 times but then give up)
		System.out.println("giving random a try");
                
                do {
                    
                    attempts ++; // increase the counter
                    
                    randomX = randomizer(game.boardColumns); //generate a random number from 1 to (number of columns) and assign it to number
			                                        
                    // if this returns True than it is a safe move
                    willNotMakeThemLose = (TestFutureMoves(currentBoard, 0, randomX, -1));
                    
                    System.out.println("attempts = " + attempts);
                    
                    // keep searching while the space is not blank OR while the space will make them loose and we still care
                } while(currentBoard[randomX][game.boardRows] != (game.BLANK) | (willNotMakeThemLose == false && attempts <= 100) ); //
		
		System.out.println("Putting piece random at " + randomX + " and Blank = " + (currentBoard[randomX][game.boardRows] == (game.BLANK)));
		
                
		return randomX;
	}
        

        public int randomizer(int x) {
            Random random = new Random();

            // we take x (the number of items that will be randomized) 
            // now it randomizes between 0 and x-1
            // then we add 1
            // now it has randomized between 1 and x (like we want :)
            return random.nextInt(x) + 1; //randomizes a number from 1 to x
        }
        
        public boolean oneOutOf(int chance) {
            Random random = new Random();
            
            // we run a randomizer and only 1/3 of the time he notices
            // randomize a number between 1 and 3
            int value = random.nextInt(chance);
            
            boolean heNotices = value == 1; // 1/chance of the time heNotices true
            
            return heNotices;
        }
	
	
	//### TEST FOR FUTURE MOVES ### ' 
        
        // for random this is::  currentBoard, significance, X, Y
        
	public boolean TestFutureMoves(int[][] currentBoard, int significance, int currentMoveX, int currentMoveY) {
		boolean willNotLOOSE = true;
                
               System.out.println("Test future Moves: y - " + currentMoveY);
		
                // if its -1 thats not a valid y location so find the top piece in the column
                if (currentMoveY == -1) {
                    
                    int ThisRow = 1;

                    //OUTPUT System.out.println("Validating move " + currentMoveX);

                    try {
                        while (currentBoard[currentMoveX][ThisRow] != (game.BLANK)) { 
                                ThisRow = ThisRow + 1;	
                        }
                    } catch (Exception e) {
                        // if it stop then their are no spaces above the piece so it obviously won't cause a loss
                       // because of that finish up end this test
                       willNotLOOSE = true;
                       significance = 3;
                    }
                    
                    currentMoveY = ThisRow;
                    
                    //System.out.println("Top piec: " + currentMoveY);
                }
                
		//basically what we are testing is: if the space above this space will allow the other player to win then don't put a piece here
		
		//Unless of course this space is priority 3 (which means that the other player will win here also)
		if (significance != 3 && !"Easy".equals(game.AIdifficulty)) {
				
			//////OUTPUT System.out.println("Test Future Moves");
		
			System.out.println("Test piece put into " + currentMoveX + "," + (currentMoveY + 1));
			
			//if the space being checked is not the top of the board
			
			if (currentMoveY < game.boardRows) {	
			
				//if the piece right above this piece will let the other player win
				if (CheckIfThisPlayerHasWon(currentBoard, OtherPlayer, currentMoveX, currentMoveY + 1)) {
					//we should probably not put a piece here
					
					System.out.println("Skipping potential move because of hazard");
					
					//so do we skip this space
					willNotLOOSE = false;
				}
                                
                                // TODO make this work
                                /*else {
                                    System.out.println("TESTING AI ADVANCED");
                                    
                                    AIadvanced AI = new AIadvanced();
                                    
                                    // alternatively if by putting a piece in X, Y will allow the Enemy to do something
                                    // that lets them make a double move (= unstopable) then it is a bad move!
                                    if (AI.testForFutureDoubleMoves(game, currentMoveX, currentMoveY) == true) {
                                        
                                        System.out.println("Skipping potential move because of future DBL");
					
					//so do we skip this space
					willNotLOOSE = false;
                                    }
                                }*/
			}
		}
		
		return willNotLOOSE;
	}
	
	public boolean CheckIfThisPlayerHasWon(int[][] currentBoard, int ThisPlayer, int ValidColumn, int ValidRow) {
		boolean WinnerFound;
		
                Winning winCheck = new Winning();
                
		WinnerFound = winCheck.CheckHorizontalLineInValidRow(game, currentBoard, ValidColumn,ValidRow, ThisPlayer);	//'check the row
		
		System.out.println("Player " + ThisPlayer + " would win? " + WinnerFound + " ( if piece put in " + ValidColumn + "," + ValidRow);
		
		//if it didn't find 4 horizontal then check the vertical
		if (WinnerFound == false) {
			WinnerFound = winCheck.CheckVerticaleLineInValidColumn(game, currentBoard, ValidColumn,ValidRow, ThisPlayer); // 'check the column
			System.out.println("Player " + ThisPlayer + " would win? " + WinnerFound + " ( if piece put in " + ValidColumn + "," + ValidRow);
		}
		
		//if it didn't find 4 verticle then check the horizontal
		if (WinnerFound == false) {
			WinnerFound = winCheck.CheckDiagonalLine(game, currentBoard, ValidColumn,ValidRow, ThisPlayer);
			System.out.println("Player " + ThisPlayer + " would win? " + WinnerFound + " ( if piece put in " + ValidColumn + "," + ValidRow);
		}
		
		return WinnerFound;
	}
}
