'
' Created by SharpDevelop.
' User: Nuser
' Date: 30/08/2017
' Time: 2:28 p.m.
' 
' To change this template use Tools | Options | Coding | Edit Standard Headers.
'
Module MainProgram

	' determines the board size, default (7,6)
	Dim boardRows As Integer = 6	'Y'
	Dim boardColumns As Integer = 7	'X'

	' booleans to allow the game to be played repeatedly and allow player turns to repeat
	Dim going As Boolean = true
	Dim GameFinished As Boolean = false
	
	Dim Board(boardColumns, boardRows) As String
	
	'MP or SP with AI
	'NOTE: ai is always player2
	Dim AIgame As Boolean = false
	
	'player can be X or O, O starts
	Dim player1piece As String = "O " 'player 1 is O
	Dim player2piece As String = "X " 'player 2 is X
	
	Dim ThisPlayer As String = player1piece 	
	
	Dim BLANK As String = "[]"	'this is the blank symbol
	
	' The input of the player in row and column
	Dim ValidColumn As Integer
	Dim ValidRow As Integer

	' ########## program ########## '
	Sub Main()
	
		Do While going = true 
		
			InitialiseBoard()
			
			SetUpGame()
			
			OutputBoard()
			
			Do While GameFinished = False
				
				ThisPlayerMakesMove()
				
				OutputBoard()
				
				CheckIfThisPlayerHasWon()
				
				If GameFinished = False Then
					
					SwapThisPlayer()
					
				End If
				
			Loop
			
			Console.ReadLine() 'leave text for player to see
			
			going = CheckForRepeat()
			
		Loop
		
	End Sub
	
	Sub SetupGame()
		ThisPlayer = player1piece
		GameFinished = false
		
		AIgame = CheckForAI()
	End Sub
	
	'check if the game should be played again
	Function CheckForRepeat() As Boolean
			
			Console.Write("Play Again? (Y or N) ") 
			
			If Console.ReadLine() <> "Y" Then
				' end game
				return false
			Else 
				' repeate game
				return true
			End If
	
	End Function
	
	Function CheckForAI() As Boolean
			
			Console.Write("Play singleplayer? (Y or N) ") 
			
			If Console.ReadLine() = "Y" Then
				' with ai
				return true
			Else 
				' without ai
				return false
			End If
	
	End Function
	
	' ####### procedures #########
	
	' fill all the spaces in the board
	Sub InitialiseBoard()
		Dim Row As Integer		'Y'
		Dim Column As Integer	'X'
		
		For Row = 1 To boardRows
			For Column = 1 To boardColumns
				Board(Column, Row) = BLANK 	'assign all the spaces to be nothing to start off with
			Next
		Next
	End Sub
	
	' for each space on the board display the stuff in it
	Sub OutputBoard()
		Dim Row As Integer		'Y'
		Dim Column As Integer	'X'
		
		' remove previous boards
		'' CLEAR CONSOLE ''
		Console.Clear()
		
		For Row = boardRows To 1 Step -1
			For Column = 1 To boardColumns
				Console.Write(Board(Column, Row) + " ") 'output the board square followed by a space for clarity
			Next
			
			' at the end of each row go to the next line
			Console.WriteLine()
		Next
		
		
		Console.WriteLine("AI = {0}", AIgame)
	End Sub
	
	' get input and put a piece into the game
	Sub ThisPlayerMakesMove()
		ValidColumn = ThisPlayerChoosesColumn()					'get the player to choose a column
		ValidRow = FindNextFreePositionInColumn(ValidColumn)	'find out what space is open in that column
		
		Board(ValidColumn, ValidRow) = ThisPlayer 	' fill that space with the current player
	End Sub
		
	' return a column number (this gets the current player to input a valid column number)
	Function ThisPlayerChoosesColumn() As Integer
		Dim ColumnNumberInput As String
		Dim ColumnNumber As Integer
		
		' FOR TESTING '
		' AIgame = CheckForAI()
		' ###################
		
		' if playing with ai and its the ai's turn then the ai goes
		If AIgame = true and ThisPlayer = player2piece Then		  'AI'
																  'AI'
			Dim computer As AIplayer = new AIplayer()			  'AI'
																  'AI'
			ColumnNumber = computer.takeTurn(Board)		 		 'AI'
																  'AI'	
		Else													  'AI'
		
			
			Console.WriteLine("Player " + ThisPlayer + "'s turn")
		
			Do 
				Console.WriteLine("Enter a valid column number:")
				
				ColumnNumberInput = Console.ReadLine()
				
			Loop Until ColumnNumberValid(ColumnNumberInput) = True
			
			ColumnNumber = CInt(ColumnNumberInput)
		End If
		
		Return ColumnNumber
	End Function
	
	' return whether the number entered is actually a column that has a space
	Function ColumnNumberValid(input As String) As Boolean
		Dim ValidColumn As Boolean = False
		
		Dim column As Integer
			
		' convert the input into a integer
		Try
			column = CInt(input)
		Catch ex As Exception
			' if it breaks then it can't convert the string to an integer
			ValidColumn = False
		End Try
		
		' if the input is between 1 and 7 and the ValidColumn has not failed
		If column >= 1 And column <= boardColumns Then
			If Board(column, boardRows) = BLANK ' if the column has an empty space
				ValidColumn = True
			End If
		End IF
		
		Return ValidColumn
	End Function
	
	
	' return a blank space (check upwards in the column selected until a blank space is found)
	Function FindNextFreePositionInColumn(ValidColumn as Integer) As Integer
		Dim ThisRow As Integer = 1
		
		Console.WriteLine("Making move {0}", ValidColumn)
		
		Do While Board(ValidColumn, ThisRow) <> BLANK
			ThisRow = ThisRow + 1	
		Loop
		
		Return ThisRow
	End Function
	
	' ######### CHECK FOR WIN ###########
	
	' check to see if a player has won the game
	Sub CheckIfThisPlayerHasWon()
		Dim WinnerFound As Boolean = False
		
		WinnerFound = CheckHorizontalLineInValidRow()	'check the row
		
		' if it didn't find 4 horizontal then check the vertical
		If WinnerFound = False Then
			WinnerFound = CheckVerticaleLineInValidColumn()	'check the column
		End If
		
		' if it didn't find 4 verticle then check the horizontal
		If WinnerFound = False Then
			WinnerFound = CheckDiagonalLine()
		End If
		
		' if winner found
		If WinnerFound = True Then
			' finish game
			GameFinished = True
			
			Console.WriteLine(ThisPlayer + " is the winner")
			
		Else 
			' check if every space is full
			CheckForFullBoard()
		End If
	End Sub
	
	' flip to the other players turn
	Sub SwapThisPlayer()
		If ThisPlayer = player1piece Then
			ThisPlayer = player2piece
		Else
			ThisPlayer = player1piece
		End If
	End Sub
	
	' ######## win conditions #########
	
	Sub CheckForFullBoard()
		Dim BlankFound As Boolean = False
		
		Dim ThisRow As Integer = 0
		
		' run a loop through each space of the board
		Do
			Dim ThisColumn As Integer = 0
			ThisRow = ThisRow + 1
			
			Do
				ThisColumn = ThisColumn + 1
				
				If Board(ThisColumn, ThisRow) = BLANK Then
					BlankFound = True	' if a blank space is found then record that
				End If
				
			Loop Until ThisColumn = boardColumns OR BlankFound = True
			
		Loop Until ThisRow = boardRows OR BlankFound = True
		
		' if no empty spaces are found its a tie
		If  BlankFound = False Then
			GameFinished = True
			
			Console.WriteLine("It is a draw")
		End If
	End Sub
	
	' Check for Horizontal (left/right) 4 in a row win
	Function CheckHorizontalLineInValidRow() As Boolean
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
	Function CheckVerticaleLineInValidColumn() As Boolean
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
	Function CheckDiagonalLine() As Boolean
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
	
End Module
