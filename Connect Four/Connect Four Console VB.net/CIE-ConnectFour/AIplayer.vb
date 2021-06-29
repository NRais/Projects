'
' Created by SharpDevelop.
' User: Nuser
' Date: 2/10/2017
' Time: 3:40 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Public Class AIplayer

	' this is the most important one '
	Dim columnNumber As Integer = 0		' this is the column the ai will drop a piece into, while its 0 we know to continue looking
	
	Dim stillChecking As Boolean = True
	
	Dim boardColumns As Integer = 7
	Dim boardRows As Integer = 6
	
	Dim AIplayer As String = "X "
	Dim OtherPlayer As String = "O "
	Dim BLANK As String = "[]"
	
	' NOTE: a new variable DefenseOnly has been added, when playing defense only the computer checks for potential defensive move but not offensive ones
	' ....if no move are found the computer does not go. This is used for the AI hazard check (ensuring that this move will not create a potential hazard in future)
	Function takeTurn(currentBoard(,) As String) As Integer
		Dim columnNumber As Integer
				
		Console.WriteLine("Ai turn")
				
		' #### the ai player has to choose a column #### '
		
		' first check if the AI can win
			
			'' check if any columns have 3 pieces on top of each other
			
			'' check if any rows have 3 pieces next to each other
			
			'' check if any diagonals are available to win
			
			' NOTE: the order does NOT matter, if their is more than one than the AI wins no matter which it chooses
			
			Console.WriteLine("Ai check for wins")
		
			columnNumber = CheckForWinVertical(currentBoard, AIplayer, 3)
			columnNumber = CheckForWinHorizontal(currentBoard, AIplayer, 3)
			columnNumber = CheckForWinDiagonal(currentBoard, AIplayer, 3)
			
		' second check if the enemy player is going to win
			
			'' check if any columns have 3 enemy pieces on top of each other
			
			'' check if any rows have 3 enemy pieces next to each other
			
			'' check if any diagonals are available to win
			
			' NOTE: the order does NOT matter, if there are more than one loss the AI looses in all circumstances
			
		If stillChecking = True Then
			Console.WriteLine("Other player check for wins")
			
			columnNumber = CheckForWinVertical(currentBoard, OtherPlayer, 3)
			columnNumber = CheckForWinHorizontal(currentBoard, OtherPlayer, 3)
			columnNumber = CheckForWinDiagonal(currentBoard, OtherPlayer, 3)
		End If
		

			
		' if nothing is found then check if the enemy is near winning
		
			'' check if any rows have 2 enemy pieces
			
			'' check if any diagonals have 2 enemy pieces
			
			'' check if any columns have 2 enemy pieces
			
			
			' NOTE: the order DOES matter here, if their are more than one option rows are the most likely to allow the other player to win
			' (columns have only one way to win: the top, and diagonals are slightly harder to win: because they have to get very high)
			
		If stillChecking = True Then
			Console.WriteLine("Ai check for good moves")
			
			columnNumber = CheckForWinHorizontal(currentBoard, AIplayer, 2)
			columnNumber = CheckForWinDiagonal(currentBoard, AIplayer, 2)
			columnNumber = CheckForWinVertical(currentBoard, AIplayer, 2)
		End If
			
		
		' if nothing is found then check if the AI is near winning
		
			'' check if any diagonals have 2 pieces
			
			'' check if any rows have 2 pieces
			
			'' check if any columns have 2 pieces
			
			' NOTE: the order DOES matter here, the human player is more likely to spot and block stacks and rows of 3 than diagonals
			
		If stillChecking = True Then
			Console.WriteLine("Other player check for good moves")
			
			columnNumber = CheckForWinDiagonal(currentBoard, OtherPlayer, 2)
			columnNumber = CheckForWinHorizontal(currentBoard, OtherPlayer, 2)
			columnNumber = CheckForWinVertical(currentBoard, OtherPlayer, 2)
		End If
		
		' NOTHING?? Well then just pick one randomly
		If stillChecking = True Then
			columnNumber = RandomRow(currentBoard)
		End If
	
		
		Return columnNumber
	End Function
	
	
	' ###################################### '
	
	
	''##       VERTICAL WIN     ##''
	Function CheckForWinVertical(currentBoard(,) As String, player As String, amount As Integer) As Integer
	
		If stillChecking = True Then
			Dim topPiece As Integer
			
			
			' check each column
			For x = 1 to boardColumns 
			
				' assign variables
				topPiece = 0			
							
						
				' get the topmost piece ->
					' check the current top spot
					' if its not blank then its the top piece, else move down 1
				For y = boardRows To 1 Step -1			
					If currentBoard(x,y) <> BLANK Then
						' then stop searching because this is the top most piece
						topPiece = y
						
						' then if we find a top piece we need to stop the loop (just skip to the end)
						y = 1
					End If
				Next		
			
				' if the column is lower than 3 than there is no way it will have enough to make a 4 in a row
				' but if it is too high then it is pointless also, too high is determined by:
					' #(the amount of spaces above the top piece) + #(the number of spaces we are looking for, which could be below the top piece) <> (is not) less than 4
					
				
				' so ensure it is above 3 and enough below the top
				If topPiece >= amount And boardRows - topPiece + amount >= 4 Then
								
					' If the top 3 items in this column are the AI's than that is a possible 4 in a row for the AI
					
					Try 
						If ((currentBoard(x, topPiece) = player) And
							(currentBoard(x, topPiece - 1) = player Or amount < 2) And		
							(currentBoard(x, topPiece - 2) = player Or amount < 3)) Then
							
							If TestFutureMoves(currentBoard, amount, x, topPiece + 1) Then
								
								' either the one down piece is yours OR you only want to find 1 piece
								' either the two down piece is yours OR you only want to find 2 pieces
								' And in all circumstances the move you make CANNOT violate the future move policy: this ensures that you don't make a bad move that the enemy can win because of
								
								' FOUND A PLACE TO PUT THE PIECE
								Console.WriteLine("Putting piece vertical reasons ({1}) for player {0}", player, amount)
								
								
								columnNumber = x
								
								stillChecking = false
								
								' end loop check
								Goto End_of_loop:
								
							End If
							
						End If
					Catch ex As Exception
						' if it breaks than it must be out of the range of the board
					End Try							
				End IF
			Next
			
			End_of_loop:
			
		End If
		
		return columnNumber
	End Function
		
	
	
	' ##    HORIZONTAL WIN   ## '
	Function CheckForWinHorizontal(currentBoard(,) As String, player As String, amount As Integer) As Integer
	
		If stillChecking = True Then
		
			If columnNumber = 0 Then ' if we haven't found something yet
			
				' check each row
				For y = 1 To boardRows
							
					Dim count As Integer = 0
					
					' check if their are enough of your pieces in this row to qualify
					For x = 1 To boardColumns
						
						' count if it is the AI's
						If currentBoard(x,y) = player Then
							count = count + 1
						End If
						
					Next
					
					
					' if their are enough to qualify then it is possible to put a piece in this row (note: if there are not less than #boardColumns than the row is full)
					
					' (but if for example we are looking for a 3 in a row and there are only 2 of your pieces than there aint gonna be any 3 in a row here)
					If count >= amount and count < boardColumns Then
					
					
						
						' check each space if ->
							' this space is BLANK & the space under this is not ;)
							' your left pieces + your pieces to the right are = 3 (or more)
							
						' COUNT FROM INWARDS OUT
						For Each x In {4,3,5,2,6,1,7}
						
							' for each space in the row count how many pieces (of the same team) are adjacent
							Dim numberOfYourAdjacent As Integer = 0
							Dim numberOfFreeAdjacent As Integer = 0
							
							If currentBoard(x,y) = BLANK And currentBoard(x,y - 1) <> BLANK Then	' if the space is blank then it could be a canidate for the ai's piece
																								' but if the space underneath is blank (which can happen) then the ai will drop down
															
								'' i use for loops because it saves time writing a billion if's
								
								' this first loop runs twice first it times everything by -1 and then by 1
								' this allows the if statements to check all the spaces to the left (-) of the current space, and then all the spaces to the right (+)
								For direction = -1 To 1 Step 2
								
									' this loop runs from 1 to 3 checking each space in the row 1 away, 2 away, 3 away
									For spaces = 1 To 3 
										
										Try
											' the space we check is the middle (x,y) ofset by #space * #direction
											' if it is yours than count it
											If (currentBoard(x + spaces*direction,y) = player) Then
											
												numberOfYourAdjacent = numberOfYourAdjacent + 1
											Else
												' if it is not your and not a blank then it is either: 
												'		the other player, 
												'		or the edge, 
												' so stop counting the spaces
												If (currentBoard(x + spaces*direction,y) = BLANK) Then
												
													' ###  STRATEGY POINT ### '
													' but for offensive strategy we will not count blanks if there is nothing under them
													' AND they are the other players
													If (currentBoard(x + spaces*direction,y - 1) <> BLANK) Then
												
														numberOfFreeAdjacent = numberOfFreeAdjacent + 1
													End If
													
												Else
													' stop counting (skip to the end)
													spaces = 3
												End If
											
											End If
											
										Catch ex As Exception
											' if it doesn't work than that is the edge of the board
											
											' so skip to the end of the loop
											spaces = 3
										End Try
										
									Next
								Next
								
							End If
													
							' if the number of adjacent is more than we are looking for
							' AND if number of free and your adjacent is enough to make a lineUp4 (note: numberfree + number adjacent only have to be 3 because the space we are entering makes the fourth)
							'	(all this is checked for because it may be that their are 2 adjacent on the right but all the rest are full, then it is pointless to play here)
							If numberOfYourAdjacent >= amount And (numberOfFreeAdjacent + numberOfYourAdjacent >= 3)  Then
								If TestFutureMoves(currentBoard, amount, x, y) Then
									
									' NOTE: the TestFutureMoves is a boolean function that returns true as long as the other player can't win in the space above this space
									
									' FOUND A PLACE TO PUT THE PIECE
									columnNumber = x
									
									stillChecking = false
									
									Console.WriteLine("Putting piece {2},{3} horizontal reasons ({1}) for player {0}", player, amount,x,y)
									
									' end loop search
									Goto End_of_loop:
									
								End If
							End If
							
						Next		
						
					End If
				
				
				Next
				
				End_of_loop:
				
			End If
			
		End If
		
		return columnNumber
	End Function
	
	
	
	' ##    DIAGONAL WIN   ## '
	Function CheckForWinDiagonal(currentBoard(,) As String, player As String, amount As Integer) As Integer
		
		If stillChecking = True Then
		
			Dim counter As Integer = 0
			Dim blankCounter As Integer = 0
			
			Dim cap As Boolean = false
			
			' for each space, that has a space under it that could be dropped onto, we want to check how many their are diagonally adjacent
			
			' INWARDS to OUT
			For Each x In {4,3,5,2,6,1,7}
				For y = 1 To boardRows
				
					If stillChecking = True Then
				
						If currentBoard(x,y - 1) <> BLANK Then
						
							'Console.WriteLine("Checking {0},{1} cause it might work", x,y)
						
							' for each of these spaces we want to count how many diagonally adjacent their are that fit the criteria
							' first the similar diagonal (-1,-1 to -3,-3 and 1,1 to 3,3) then the other diagonal (1,-1 to 3,-3 and -1,1 to -3,3)
							
							' for each of these diagonals we loop through each space and count if it is our players, if it isn't we stop counting
							' then at the end if the counter is the number we are looking for then we put the piece in
				
							For direction = 1 To -1 Step -2
								For multiplier = -1 To 1 Step 2
									For position = 1 To 3
										
											Try 
												If (currentBoard(x + position*multiplier, y + position*multiplier*direction)) = player Then
													' if the diagonal is the correct player than that means we have 2 spaces in a row
													' now check to see if there are anymore in that diagonal direction
													
													''' TODO this doesn't seem right????
													If cap <> true Then
													
														counter = counter + 1
														
													End If
													
												Else
													' if its not blank then count that in the other counter
													If (currentBoard(x + position*multiplier, y + position*multiplier*direction)) = BLANK Then
													
														blankCounter = blankCounter + 1
														
														'Console.WriteLine("Found a blank at {0},{1}", x + position*multiplier, y + position*multiplier*direction)
														
														' and this directions counter is capped (no more adjacents should be counted because their is a piece in the way)
														cap = true
														
													Else
														' if its the other player then stop searching (skip to the end)
														position = 3											
														
													End If
													
												End If
											Catch ex As Exception						
												' there is no space here so just ignore it (and stop checking)
													
												position = 3
											End Try
										
										' reset the cap at the end of each diagonal direction (ne,se,sw,nw)
										cap = false
										' ------
									Next
								Next
								
								' if we counted enough And their are enough blank spaces adjacent that it matters
								' (enough is defined by 1 (for the space your in) + counter (the number you've found) + blankCounter (the number left available))
								' (and enough needs to be at least 4)
								Dim enough As Boolean = 1 + counter + blankCounter >= 4
								Dim inTheBoard As Boolean = false
								
								Try
									If currentBoard(x,y) = BLANK Then
										inTheBoard = True
									End If
								Catch ex As Exception
									' well i guess its aint in th' board ehh ;)
								End Try
								
								' if the amount that has been counted (#counter) is more than the amount we are looking for AND
								' enough blank spaces and your spaces that a 4 in a row could be made AND
								' if the space we are trying to put it in is blank (not above the top of the board)
								If counter >= amount And enough And inTheBoard Then
									If TestFutureMoves(currentBoard, amount, x, y) Then
										' NOTE: the TestFutureMoves is a boolean function that returns true if the other player can't win if you put this piece in
										
										' FOUND A PLACE TO PUT THE PIECE
										columnNumber = x
										
										stillChecking = false
										
										Console.WriteLine("Putting piece in {2},{3} for diagonal reasons ({1}) for player {0}", player, amount, x, y)
									
										' end loop check
										Goto End_of_loop:
									
									End If
								Else
									' if there isn't a winner than reset the counter and check the other direction
									
									counter = 0
									blankCounter = 0
								End If
								
							Next
						End If
						
					End If
					
				Next
			Next
			
			End_of_loop:
			
		End If
		
		return columnNumber
	End Function
	
	
	
	
	' RANDOMIZER '
	' this function gets a random number
	Function RandomRow(currentBoard(,) As String) As Integer
		Dim randomizer As New Random ' new random class
		Dim number As Integer
		
		' run the randomizer until it picks a space that doesn't have a piece in every space
		' but we will just check the top space (x,y) > (#column randomized , #number of rows in the board)
		Do Until currentBoard(number, boardRows) = BLANK
		
			number = randomizer.Next(1,boardColumns) ' generate a random number from 1 to (number of columns) and assign it to number
			
		Loop
		
		Console.WriteLine("Putting piece random")
		
		Return number
	End Function
	
	
	' ### TEST FOR FUTURE MOVES ### '
	Function TestFutureMoves(currentBoard(,) As String, amount As Integer, currentMoveX As Integer, currentMoveY As Integer) As Boolean
		Dim continuing As Boolean = true
										
		' basically what we are testing is: if the space above this space will allow the other player to win then don't put a piece here
		
		' Unless of course this space is priority 3 (which means that the other player will win here also)
		If amount <> 3 Then
				
			Console.WriteLine("Test Future Moves")
		
			Console.WriteLine("Test piece put into {0},{1}", currentMoveX, currentMoveY)
			
			' if the space being checked is not the top of the board
			
			If currentMoveY	< boardRows Then	
			
				' if the piece right above this piece will let the other player win
				If CheckIfThisPlayerHasWon(currentBoard, OtherPlayer, currentMoveX, currentMoveY + 1) Then
					' we should probably not put a piece here
					
					Console.WriteLine("Skipping potential move because of hazard")
					
					' so do we skip this space
					continuing = false
				End If
			End If
		End If
		
		Return continuing
	End Function
	
	Function CheckIfThisPlayerHasWon(currentBoard(,) As String, ThisPlayer As String, ValidColumn As Integer, ValidRow As Integer) As Boolean
		Dim WinnerFound As Boolean = False
		
		WinnerFound = CheckHorizontalLineInValidRow(currentBoard, ValidColumn,ValidRow, ThisPlayer)	'check the row
		
		Console.WriteLine("Player {0} would win? {3} ( if piece put in {1},{2}", ThisPlayer, ValidColumn, ValidRow, WinnerFound)
		
		' if it didn't find 4 horizontal then check the vertical
		If WinnerFound = False Then
			WinnerFound = CheckVerticaleLineInValidColumn(currentBoard, ValidColumn,ValidRow, ThisPlayer) 'check the column
			Console.WriteLine("Player {0} would win? {3} ( if piece put in {1},{2}", ThisPlayer, ValidColumn, ValidRow, WinnerFound)
		End If
		
		' if it didn't find 4 verticle then check the horizontal
		If WinnerFound = False Then
			WinnerFound = CheckDiagonalLine(currentBoard, ValidColumn,ValidRow, ThisPlayer)
			Console.WriteLine("Player {0} would win? {3} ( if piece put in {1},{2}", ThisPlayer, ValidColumn, ValidRow, WinnerFound)
		End If
		
		Return WinnerFound
	End Function
	
	
	' ### CHECK FOR WIN ### '
	
	
	' Check for Horizontal (left/right) 4 in a row win
	Function CheckHorizontalLineInValidRow(Board(,) As String, ValidColumn As Integer, ValidRow As Integer, ThisPlayer As String) As Boolean
		Dim i As Integer
		Dim WinnerFound As Boolean = False
		
		' check the row this current piece is in (ValidRow)
		' search each item in the row and see if there are 4 items that are ThisPlayer's next to each other
		
		' this is done by checking the first 4 items on the left of the row and then seeing if each their are 3 items adjacent to it on the left
		For i = 1 To (boardColumns - 3)
		
			If (Board(i, ValidRow) = ThisPlayer And
				Board(i + 1, ValidRow) = ThisPlayer And
				Board(i + 2, ValidRow) = ThisPlayer And
				Board(i + 3, ValidRow) = ThisPlayer) Then
				
				WinnerFound = True
			End If
		Next
		
		Return WinnerFound
	End Function
	
	' Check for Verticle (up/down) 4 in a row win
	Function CheckVerticaleLineInValidColumn(Board(,) As String, ValidColumn As Integer, ValidRow As Integer, ThisPlayer As String) As Boolean
		Dim WinnerFound As Boolean = False
		
		' if you are lower than 4 than there is no way you have 4 tiles on top of each other
		' so ensure it is between 4 and the max number
		If ValidRow >= 4 And ValidRow <= boardRows Then
			
			' If the top 4 items in this column are the same player than that is 4 in a row
			If (Board(ValidColumn, ValidRow) = ThisPlayer And
				Board(ValidColumn, ValidRow - 1) = ThisPlayer And
				Board(ValidColumn, ValidRow - 2) = ThisPlayer And
				Board(ValidColumn, ValidRow - 3) = ThisPlayer) Then
				
				WinnerFound = True
				
			End If
		End IF
		
		Return WinnerFound
	End Function
	
	' Check for diagonal 4 in a row
	Function CheckDiagonalLine(Board(,) As String, ValidColumn As Integer, ValidRow As Integer, ThisPlayer As String) As Boolean
		Dim WinnerFound As Boolean = False
		
		Dim counter As Integer = 0
			
			
		' we know the current piece that has been inserted ValidColumn,ValidRow
		' from that piece we want to check every space diagonally around it for the same pieces (pieces of thisPlayer)
		
		'' ''
		' this loop runs through all the spaces in a diagonal line (centered at validcolumn,validrow) first all the negative ones from -1,-1 to -3,-3 than all the positive ones from 1,1 to 3,3
		' for each space it adds 1 (if the space is yours), and stops if the space is the other players or BLANK
		
		' then it does the same thing agan but this time with the y value negated so from -1,1 to -3,3 and then from 1,-1 to 3,-3
		
		' at the end if their are 3 or more spaces that it has counted than thats good!
		For direction = 1 To -1 Step -2
			For multiplier = -1 To 1 Step 2
				For position = 1 To 3
					
						Try 
							If (Board(ValidColumn + position*multiplier, ValidRow + position*multiplier*direction)) = ThisPlayer Then
								' if the diagonal is the correct player than that means we have 2 spaces in a row
								' now check to see if there are anymore in that diagonal direction
								
								counter = counter + 1
								
							Else
								' if its not ThisPlayer then stop searching (skip to the end)
								
								position = 3
								
							End If
						Catch ex As Exception						
							' there is no space here so just ignore it (and stop checking)
								
							position = 3
						End Try
					' ------
				Next
			Next
			
			If counter = 3 Then
		
				WinnerFound = True
			
			Else
				' if there isn't a winner than reset the counter and check the other direction
				
				counter = 0
			End If
			
		Next
		
		Return WinnerFound
	End Function
	
	
End Class
