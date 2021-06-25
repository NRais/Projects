/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

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
public class Winning {
    
    
        // NOTE: alll the sub procedures here have 1 parameter: ConnectFourFrame game
        // this parameter passes the current frame and all its variables to this Winning class
        // this allows the Winning class to access and update (where necessary) the variables and labels of the game
    
        // TIEING
        // if the board is completely full (no more blank spaces) then the game is a tie
        public void CheckForFullBoard(ConnectFourFrame game) {
		boolean BlankFound = false;
                		
		int ThisRow = 1;
		
		// run a loop through each space of the board
                
                // while its less than the number of rows (and it is at the start (1)) then check
                // Or if a blank is found, then stop checking
		while (ThisRow <= game.boardRows && BlankFound != true) {
                    
                    int ThisColumn = 1;

                    // while this is less than the number of columns
                    while (ThisColumn <= game.boardColumns && BlankFound != true) {
                        
                        if (game.Board[ThisColumn][ThisRow] == (game.BLANK)  ) {
                                BlankFound = true;	// if a blank space is found then record that
                        }

                        ThisColumn = ThisColumn + 1;
                        
                    }
			
                    ThisRow = ThisRow + 1;
                }
		
		// if no empty spaces are found its a tie
		if (  BlankFound == false  ) {
			game.GameFinished = true;
                        
                        game.gameState = "Tie";
                        
                        game.SetupTheLabels();
                        
                        // if the AI cycle is on and going restart
                        if (game.AIcycle.isSelected() && game.AIvAI.isSelected()) {
                            game.RunCycle();
                        }   
		}
	}
    
        // Check for Horizontal (left/right) 4 in a row win
	public boolean CheckHorizontalLineInValidRow(ConnectFourFrame game, int[][] Board, int Xposition, int Yposition, int ThisPlayer) {
		int i;
		boolean WinnerFound = false;
                
                		
		// check the row this current piece is in (Yposition)
		// search each item in the row and see if there are 4 items that are ThisPlayer//s next to each other
		
		// this is done by checking the first 4 items on the left of the row and then seeing if each their are 3 items adjacent to it on the left
		// of course the space X, Y may not have a piece in it yet so we will just pretend that it does
                // this is done by saying if: [i][Y] has your piece |OR| if [i][Y] is the space we are pretending has a piece -> then it counts 
                for ( i = 1; i <= (game.boardColumns - 3); i++ ) {
                    try {
			if ( ((Board[i][Yposition] == (ThisPlayer) | i == Xposition) &&
                                (Board[i + 1][Yposition] == (ThisPlayer) | i+1 == Xposition) &&
                                (Board[i + 2][Yposition] == (ThisPlayer) | i+2 == Xposition)&&
                                (Board[i + 3][Yposition] == (ThisPlayer) | i+3 == Xposition))  ) {
				
				WinnerFound = true;
			}
                    } catch (Exception e) {} // if it breaks than no win that way
		}
		
		return WinnerFound;
	}
	
	// Check for Verticle (up/down) 4 in a row win
	public boolean CheckVerticaleLineInValidColumn(ConnectFourFrame game, int[][] Board, int Xposition, int Yposition, int ThisPlayer) {
		boolean WinnerFound = false;
		
		// if you are lower than 4 than there is no way you have 4 tiles on top of each other
		// so ensure it is between 4 and the max number
		if ( Yposition >= 4 && Yposition <= game.boardRows  ) {
			
			// if ( the top 4 items in this column are the same player than that is 4 in a row
			if ( (Board[Xposition][Yposition] == (ThisPlayer) &&
                                Board[Xposition][Yposition - 1] == (ThisPlayer) &&
                                Board[Xposition][Yposition - 2] == (ThisPlayer) &&
                                Board[Xposition][Yposition - 3] == (ThisPlayer))  ) {
				
				WinnerFound = true;
				
			}
		}
		
		return WinnerFound;
	}
	
	// Check for diagonal 4 in a row
	public boolean CheckDiagonalLine(ConnectFourFrame game, int[][] Board, int Xposition, int Yposition, int ThisPlayer) {
		boolean WinnerFound = false;
		
		int counter = 0;
			
			
		// we know the current piece that has been inserted Xposition,Yposition
		// from that piece we want to check every space diagonally around it for the same pieces (pieces of thisPlayer)
		
		//// ////
		// this loop runs through all the spaces in a diagonal line (centered at validcolumn,validrow) first all the negative ones from -1,-1 to -3,-3 than all the positive ones from 1,1 to 3,3
		// for each space it adds 1 (if the space is yours), and stops if the space is the other players or BLANK
		
		// then it does the same thing agan but this time with the y value negated so from -1,1 to -3,3 and then from 1,-1 to 3,-3
		
		// at the end if their are 3 or more spaces that it has counted than thats good!
		for ( int direction = 1 ; direction >= -1 ; direction = direction - 2 ) {
			for ( int multiplier = -1 ; multiplier <= 1 ; multiplier = multiplier + 2 ) {
				for ( int position = 1; position <= 3; position++ ) {
					
						try {
							if ( (Board[Xposition + position*multiplier][Yposition + position*multiplier*direction]) == (ThisPlayer)  ) {
								// if the diagonal is the correct player than that means we have 2 spaces in a row
								// now check to see if there are anymore in that diagonal direction
								
								counter = counter + 1;
								
							} else {
								// if its not ThisPlayer then stop searching (skip to the end)
								
								position = 3;
								
							}
                                                } catch (Exception e) {						
							// there is no space here so just ignore it (and stop checking)
								
							position = 3;
						}
					// ------
				}
			}
			
                        // if more than 3 peices have been found then its is a win!
			if ( counter >= 3  ) {
		
				WinnerFound = true;
			
			} else {
				// if there isn//t a winner than reset the counter and check the other direction
				
				counter = 0;
			}
			
		}
		
		return WinnerFound;
	}
}
