/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

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

public class ConnectFourFrame extends javax.swing.JFrame {

    // determines the board size, default (7,6)
    public int boardRows = 6; //	Y
    public int boardColumns = 7; //	X

    // booleans to allow the game to be played repeatedly and allow player turns to repeat
    public boolean going = true;
    public boolean GameFinished = true;
    public String gameState = "";
    
    public boolean timerRunning = false; 
    // basically after you make a move the AI player waits half a second and then goes
    // but in that time you can already make a second move, so this boolean ensures you do not :)

    public JLabel[][] VisualBoard = new JLabel[boardColumns + 1][boardRows + 1];
    public int[][] Board = new int[boardColumns + 1][boardRows + 1];
    
    public int imgHeight = 40;
    public int imgWidth = 40;
    
    //MP or SP with AI
    //NOTE: ai is always player2
    public boolean AIgame = false;
    public boolean delayAImoves = true;
    public String AIdifficulty = "";
    public int AIdelay = 500;
        
    public boolean AccelerateAIFaster = false;

    //player can be X or O, O starts
    public int player1piece = 1; //player 1
    public int player2piece = 2; //player 2
    
    
    // ----------------------------------------
    public Icon[] TeamColor = new Icon[3];
    
    public Icon[] listOfColors = new Icon[9];
    
    // ----------------------------------------

    public int ThisPlayer = player1piece;	

    public int BLANK = 0;	//this is the blank symbol

    // The input of the player in row and column
    public int ValidColumn;
    public int ValidRow;
    
    public JLabel[] columnButton = new JLabel[7];
    
    // scoring
    public int player1wins = 0;
    public int player2wins = 0;
    public int rounds = 0;
    
    
    /**
     * Creates new form ConnectFourFrame
     */
    public ConnectFourFrame() {
               
        initComponents();
        
        Container c = this.getContentPane();               
        c.setBackground(Color.white);   
        
        // set the frame so it is a good size
        this.setSize(jPanel1.getWidth() + 17, this.getHeight());
        this.setResizable(false);
        this.setLocationRelativeTo(null); //put in middle of screen
        
        this.repaint();
        this.validate();
        
        RestartButton.setFocusable(false);
        AIcheckBox.setFocusable(false);
        SettingsButton.setFocusable(false);
        
        CreateButtons();
                        
        RunCycle();
        
        LicenseLabel.setContentAreaFilled(false);
        LicenseLabel.setBorderPainted(false);
        LicenseLabel.setFocusPainted(false);
        LicenseLabel.setOpaque(false);      
    }
    
    public void CreateButtons() {
        
        // create 7 buttons, 1 for each column
        for (int i = 1; i <= 7; i++) {
            // i - 1 is the actual number, i is the label
            columnButton[i-1] = new JLabel("" + i);
            columnButton[i-1].setLocation(i*50,0); // put each button above a column
            columnButton[i-1].setSize(40, 350); // make the button cover the entire column downwards
            columnButton[i-1].setFont(new java.awt.Font("Tahoma", 1, 14));
            columnButton[i-1].setVisible(true);
            columnButton[i-1].setForeground(Color.pink);
            columnButton[i-1].setVerticalAlignment(javax.swing.SwingConstants.TOP);
            columnButton[i-1].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            columnButton[i-1].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (GameFinished == false && timerRunning == false) {
                        
                        JLabel labelFound = (JLabel) evt.getSource();
                        
                        int row = labelFound.getX()/50;
                        
                        //OUTPUT System.out.println("Row: " + row);
                        
                        if (ThisPlayerMakesMove(row)) {
                            Turn();
                        }
                    }
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {

                    // find the jlabel that was clicked on
                    JLabel labelFound = (JLabel) evt.getSource();
                        
                    // figure out its index by getting the position and / by 50
                    int row = labelFound.getX()/50;

                    columnButton[row-1].setForeground(Color.red);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    // find the jlabel that was clicked on
                    JLabel labelFound = (JLabel) evt.getSource();
                    
                    // figure out its index by getting the position and / by 50
                    int row = labelFound.getX()/50;

                    columnButton[row-1].setForeground(Color.pink);
                }
            });
            
            BoardPanel.add(columnButton[i-1]);
            
        }
        
    }
    
    public void RunCycle() {
        
            // next round
            rounds++;
            SetupTheLabels();
            
            gameState = "";
            
            this.repaint();
            this.validate();
            
            BoardPanel.repaint();
            BoardPanel.validate();
        
            InitialiseBoard();

            SetupGame();

            OutputBoard();
            
            
            //setup player label so it is back to normal after a win
            
            //OUTPUT System.out.println("Players " + ThisPlayer);
            SetupTheLabels(); 
            
            // if its computer vs. computer then it goes and doesn't wait for you
            if (AIvAI.isSelected() == true) {
                Turn();
            }
    }
    
	public void SetupGame() {
            
            AIdelay = 500;
            ThisPlayer = player1piece;
            GameFinished = false;
            
            if (EasyButton.isSelected()) {
                AIdifficulty = "Easy";
            } else if (MediumButton.isSelected()) {
                AIdifficulty = "Medium";
            } else if (HardButton.isSelected()) {
                AIdifficulty = "Hard";
            }
            
            AIgame = AIcheckBox.isSelected();
            
            // Assign all the possible colors
            
            
            // store locations to the colors
            String[] paths = new String[9];
            
            paths[0] = "resources/pieceRed.png";
            paths[1] = "resources/pieceGreen.png";
            paths[2] = "resources/pieceBlue.png";
            paths[3] = "resources/pieceYellow.png";
            paths[4] = "resources/pieceOrange.png";
            paths[5] = "resources/piecePurple.png";
            paths[6] = "resources/pieceGray.png";
            paths[7] = "resources/piecePink.png";
            paths[8] = "resources/pieceBlack.png"; // path to default neutral color
            
            // go through and assign each item of the listOfColors array to an image
            for (int i = 0; i < 9; i ++) {
                try {
                    Image temp = ImageIO.read(ClassLoader.getSystemResource(paths[i]));
                    // scale the image so it fits in the space
                    temp = temp.getScaledInstance(imgWidth, imgHeight, java.awt.Image.SCALE_SMOOTH);
                    
                    if (i != 8) {
                        // the first 8 are assigned to the list of colors
                        listOfColors[i] = new ImageIcon(temp);
                    } else {
                        // the last one, black, is assigned to TeamColor[0]. Because team color neutral is always black
                        TeamColor[0] = new ImageIcon(temp);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ConnectFourFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            updateModeLabel();
            
            // if these haven't yet been assigned then assign them
            // if they have been assigned then they don't need reassignment
            // (this is to deal with the problem occuring that both players are set to 1 color
            // (for example Blue. Then if the game is restarted and the colors are reassigned
            // (they will both be assigned to blue. But they shouldn't be so don't let them
            if (TeamColor[1] == null) {
                TeamColor[1] = listOfColors[Player1ColorBox.getSelectedIndex()];; // player 1 (o)
            }
            if (TeamColor[2] == null) {
                TeamColor[2] = listOfColors[Player2ColorBox.getSelectedIndex()];; // player 2 (x)
            }

        }
        
        
        public void Turn() {
            OutputBoard();
            //OUTPUT System.out.println("Players " + ThisPlayer);
            SetupTheLabels();
            
            CheckIfThisPlayerHasWon();
            
            // if noone has won (and the game isn't done) then swap sides
            if (GameFinished == false) {

                    SwapPlayers();
                    AIturn();
                    
                    SetupTheLabels();

            } else {
                        
                // if the game is over and AI infinite cycle is on and we are playing AI vs AI then restart
                if (AIvAI.isSelected() && AIcycle.isSelected()) {
                    RunCycle();
                }
            }
        }
		
	// ####### procedures #########
	
	// fill all the spaces in the board
	public void InitialiseBoard() {
                int Row;	//Y
		int Column;	//X
                		
		for (Row = 1; Row <= boardRows; Row++) {
			for (Column = 1; Column <= boardColumns; Column++) {
				Board[Column][Row] = BLANK; 	//assign all the spaces to be nothing to start off with
                                
                                try {
                                    BoardPanel.remove(VisualBoard[Column][Row]);
                                } catch (Exception e) {} // for repeat times clear the old board before adding a new board
                                    
                                VisualBoard[Column][Row] = new JLabel("");
                                //VisualBoard[Column][Row].setBackground(Color.black);
                                VisualBoard[Column][Row].setIcon(TeamColor[Board[Column][Row]]);
                                BoardPanel.add(VisualBoard[Column][Row]);
                        }
                }
                
                // for all the 0th row lines (which are not valid) assign them to be -1
                for (Column = 1; Column <= boardColumns; Column++) {
                    Board[Column][0]= -1;
                }
        }
	
	// for each space on the board display the stuff in it
	public void OutputBoard() {
                int Row;	//Y
		int Column;	//X
		
		// remove previous boards
		// CLEAR CONSOLE 
		//////Console.Clear()
		
		for (Row = 1; Row <= boardRows; Row++) {
			for (Column = 1; Column <= boardColumns; Column++) {
				//OUTPUT System.out.print(Board[Column][Row] + " "); //output the board square followed by a space for clarity
                                
                                VisualBoard[Column][Row].setSize(40,40);
                                VisualBoard[Column][Row].setOpaque(true);
                                VisualBoard[Column][Row].setLocation((Column)*50, 7*50 - (Row)*50 - 25);
                                //VisualBoard[Column][Row].setBackground(Color.black);
                                VisualBoard[Column][Row].setIcon(TeamColor[Board[Column][Row]]);
                                VisualBoard[Column][Row].setVisible(true);
                                VisualBoard[Column][Row].setIcon(TeamColor[Board[Column][Row]]);
                                
                                BoardPanel.repaint();
                                BoardPanel.validate();
                        }
			
			// at the end of each row go to the next line
			//OUTPUT//OUTPUT System.out.println("");
                        
                }		
		
		//OUTPUT//OUTPUT System.out.println("AI = " + AIgame);
        }
	
	//// get input and put a piece into the game
	public boolean ThisPlayerMakesMove(int X) {
		ValidColumn = X;					//get the player to choose a column
		ValidRow = FindNextFreePositionInColumn(ValidColumn);	////find out what space is open in that column
		
                Boolean ValidMove = false;
                
                // if it is -1 then the row is full so this move does not count
                if (ValidRow != -1) {
                
                    System.out.println("Piece in " + ValidColumn + "," + ValidRow);
                    Board[ValidColumn][ValidRow] = ThisPlayer; 	// fill that space with the current player
                    ValidMove = true;
                } else {
                    //OUTPUT System.out.println("Actually not making a move, its full");
                }
                
                //AnimatePiece(ValidColumn, ValidRow);
                
                return ValidMove; // send back ValidMove, determining wether the board is updated or not
        }
        
        /////////////TODO////////////
        
        public void AnimatePiece(int Column, int Row) {
            // color then uncolor each piece down the row
            for (int i = boardRows; i > Row; i--) {
                // set the piece colored
                Board[Column][i] = ThisPlayer;
                
                OutputBoard();
                
                //OUTPUT System.out.println("doing stuff " + Column + " , " + i);
                
                
                // uncolor it
                Board[Column][i] = 0;
                
                OutputBoard();
                
                //OUTPUT System.out.println("doing stuff " + Column + " , " + i);
            }
        }
        
        ////////////////////////
		
	
	// return whether the number entered is actually a column that has a space
	public boolean ColumnNumberValid(String input) {
            boolean validColumn = false;

            int column = 0;

            // convert the input into a integer
            try {
                column = Integer.parseInt(input);
            } catch(Exception e) {
                // if it breaks then it cant convert the string to an integer
                validColumn = false;
            }

            // if the input is between 1 and 7 and the ValidColumn has not failed
            if (column >= 1 && column <= boardColumns) {
                if (Board[column][boardRows] == (BLANK)) { // if the column has an empty space
                        validColumn = true;
                }
            }

            return validColumn;
        }
	
	
	// return a blank space (check upwards in the column selected until a blank space is found)
	public int FindNextFreePositionInColumn(int ValidColumn) {
		int ThisRow = 1;
		
		//OUTPUT System.out.println("Making move " + ValidColumn);
		
                try {
                    while (Board[ValidColumn][ThisRow] != (BLANK)) { 
                            ThisRow = ThisRow + 1;	
                    }
                } catch (Exception e) {
                    // if it breaks then it is full
                    ThisRow = -1;
                }
		
		return ThisRow;
        }
	
	// ######### CHECK FOR WIN ###########
	
	// check to see if a player has won the game
	public void CheckIfThisPlayerHasWon() {
		boolean WinnerFound = false;
                
                System.out.println("Checking for win");
                
                Winning winCheck = new Winning();
		
		WinnerFound = winCheck.CheckHorizontalLineInValidRow(this, Board, ValidColumn, ValidRow, ThisPlayer);	//check the row
		
		// if it didnt find 4 horizontal then check the vertical
		if (WinnerFound == false) {
			WinnerFound = winCheck.CheckVerticaleLineInValidColumn(this, Board, ValidColumn, ValidRow, ThisPlayer);	//check the column
		}
		
		// if it didnt find 4 verticle then check the horizontal
		if (WinnerFound == false) {
			WinnerFound = winCheck.CheckDiagonalLine(this, Board, ValidColumn, ValidRow, ThisPlayer);
		}
		
                // ------- WINNER FOUND ------
		// if winner found
		if (WinnerFound == true) {
			// finish game
			GameFinished = true;
			
			//OUTPUT System.out.println(ThisPlayer + " is the winner");
                                                
                        // just get the color of the winner, because saying Player 1 wins is not as helpful
                        if (ThisPlayer == 1) {
                            
                                player1wins ++;
                                
                                gameState = "player1";
                            
                            
                        } else {
                            
                            player2wins++;
                            
                            gameState = "player2";
                        }
                        
                        SetupTheLabels();
                        
                        // AI RESTART AUTOMATICALLY
                        // if the AI cycle is on and going restart
                        if (AIcycle.isSelected() && AIvAI.isSelected()) {
                            RunCycle();
                        }
			
		} else { 
			// check if every space is full
			winCheck.CheckForFullBoard(this);
		}
                
        }
	
	// flip to the other players turn
	public void AIturn() {
            
                int ColumnNumber;

                AIplayer computer = new AIplayer();	

                
                        
                // CHECK IF THERE IS AN AI
                // if there is then he goes
                // otherwise it waits for you to go
            
                // if the next player is an AI then he just goes
                if (AIgame == true && (ThisPlayer == player2piece | AIvAI.isSelected())) {  
                        //OUTPUT System.out.println("Ai going");
                        
                        // ** NOTE: if this is in here it only runs on AI turns, if it is outside the loop then it tells you suggestions as well
                        ColumnNumber = computer.takeTurn(this);                    
                        
                        //OUTPUT System.out.println("Dif " + AIdifficulty);
                        
                        // COMPUTER MAKE MOVE
                        
                        // if the delay is on the delay otherwise just go
                        if (delayAImoves) {
                            timerRunning = true;
                            ActionListener taskPerformer = new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent evt) {
                                    timerRunning = false; 

                                    ThisPlayerMakesMove(ColumnNumber);
                                    
                                    // if accelerate is true then the ai speeds up and gets faster and faster
                                    if (AIvAI.isSelected() == true &&  AccelerateAIFaster && AIdelay > 150) {
                                        AIdelay = AIdelay - 50;
                                    }

                                    Turn();	
                                }
                            };
                            Timer myTimer = new Timer(AIdelay, taskPerformer);

                            myTimer.start();
                            myTimer.setRepeats(false);
                        }
                        		
                        else {
                            
                            ThisPlayerMakesMove(ColumnNumber);

                            Turn();
                        }
                } else {

                    //OUTPUT System.out.println("Human going");
                }
	}
        
        public void SetupTheLabels() {
            
            // setup colors
            // FIGURE OUT WHAT COLOR TO USE

            String player1ColorString = Player1ColorBox.getItemAt(Player1ColorBox.getSelectedIndex());
            String player2ColorString = Player2ColorBox.getItemAt(Player2ColorBox.getSelectedIndex());
            
                       
            Color colorP1 = Color.black;
            Color colorP2 = Color.black;

            try {

                // try to find a Class in the java.awt.color class that is equal to the string we have "player"
                // (.toLowerCase() is because the colors are all stored in lowercase)
                Field field1 = Class.forName("java.awt.Color").getField(player1ColorString.toLowerCase());
                colorP1 = (Color)field1.get(null);

            } catch (Exception e) {
                // No color founds
            }
            
            try {
                // same thing
                Field field2 = Class.forName("java.awt.Color").getField(player2ColorString.toLowerCase());
                colorP2 = (Color)field2.get(null);
                
            } catch (Exception e) {
                // No color found
            }
            
            // purple doesn't work... so manually override
            if (player1ColorString.equals("Purple")) {
                colorP1 = new Color(179,66,244);
            }
            
            if (player2ColorString.equals("Purple")) {
                colorP2 = new Color(179,66,244);
            }
            
            
            // Setup Labels
                
            SetupScoreLabels(player1ColorString, player2ColorString, colorP1, colorP2);
            SetupMainLabel(player1ColorString, player2ColorString, colorP1, colorP2);
        }
        
        public void SetupScoreLabels(String player1ColorString, String player2ColorString, Color player1Color, Color player2Color) {
            // setup round label
            RoundLabel.setText("Round: " + rounds);
            
            
            player1score.setText("" + player1wins);
            player2score.setText("" + player2wins);
            
            
            // if the player didn't choose them both to be the same!
            if (!player1ColorString.equals(player2ColorString)) {
                
                // then assign the text to be the correct color
                player1label.setForeground(player1Color);
                player2label.setForeground(player2Color);
                
                // and set the text to say the right color
                player1label.setText(player1ColorString);
                player2label.setText(player2ColorString);
            
            }
        }
        
        public void SetupMainLabel(String player1ColorString, String player2ColorString, Color player1Color, Color player2Color) {
            
            // if the game is running then change the label otherwise don't do anything
            if (GameFinished == false) {
                
                // if the font is not correct then fix it!
                if (PlayerLabel.getFont() != new java.awt.Font("Tahoma", 2, 11)) {
                    PlayerLabel.setFont(new java.awt.Font("Tahoma", 2, 11));
                    PlayerLabel.setForeground(Color.black);
                    PlayerLabel.setText("");
                }

                // if its player 1's turn
                if ( ThisPlayer == (player1piece)  ) {

                        // get the name of player1
                        PlayerLabel.setText(player1ColorString + " Player choose a column");
                } else {
                    // if its player 2's turn

                        // get the name of player2
                        PlayerLabel.setText(player2ColorString + " Player choose a column");
                }
                
            }
            else {
                // if the game is over then we either print a tie or a win for a certain player
                switch (gameState) {
                    case "Tie":
                        
                        PlayerLabel.setText("The Game is a Draw");
                        PlayerLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
                        PlayerLabel.setForeground(Color.black);
                        //OUTPUT System.out.println("It is a draw");
                        
                        
                        break;
                        
                    case "player1":
                        
                        PlayerLabel.setText("PLAYER " + player1ColorString + " is the WINNER");
                        PlayerLabel.setFont(new java.awt.Font("Tahoma", 1, 16));
                        
                        PlayerLabel.setForeground(player1Color); // should be the color of the winner
                        
                        break;
                    case "player2":
                        
                        
                        PlayerLabel.setText("PLAYER " + player2ColorString + " is the WINNER");
                        PlayerLabel.setFont(new java.awt.Font("Tahoma", 1, 16));
                        
                        PlayerLabel.setForeground(player2Color); // should be the color of the winner
                        
                        break;
                    default:
                        break;
                }
            }
        }
        
        public void SwapPlayers() {
            if ( ThisPlayer == (player1piece)  ) {
                    ThisPlayer = player2piece;
            } else {
                    ThisPlayer = player1piece;
            }
        }

	

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: do { NOT modify this code. The content of this method is always
     * regenerated by the for (m Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SettingsFrame = new javax.swing.JFrame();
        AIcheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        Player1ColorBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        Player2ColorBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        ErrorLabel = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        EasyButton = new javax.swing.JRadioButton();
        MediumButton = new javax.swing.JRadioButton();
        HardButton = new javax.swing.JRadioButton();
        AIvAI = new javax.swing.JCheckBox();
        AccelerateAI = new javax.swing.JCheckBox();
        AIcycle = new javax.swing.JCheckBox();
        ClearScoreButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        PlayerLabel = new javax.swing.JLabel();
        RestartButton = new javax.swing.JButton();
        BoardPanel = new javax.swing.JPanel();
        SettingsButton = new javax.swing.JButton();
        modeLabel = new javax.swing.JLabel();
        player1label = new javax.swing.JLabel();
        player2label = new javax.swing.JLabel();
        player1score = new javax.swing.JLabel();
        player2score = new javax.swing.JLabel();
        RoundLabel = new javax.swing.JLabel();
        LicenseLabel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        AIcheckBox.setSelected(true);
        AIcheckBox.setText("Play against the computer (on/off)");
        AIcheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AIcheckBoxActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Settings");

        Player1ColorBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Red", "Green", "Blue", "Yellow", "Orange", "Purple", "Gray", "Magenta" }));
        Player1ColorBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Player1ColorBoxItemStateChanged(evt);
            }
        });

        jLabel2.setText("Choose Player 1's color");

        Player2ColorBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Red", "Green", "Blue", "Yellow", "Orange", "Purple", "Gray", "Magenta" }));
        Player2ColorBox.setSelectedIndex(2);
        Player2ColorBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Player2ColorBoxItemStateChanged(evt);
            }
        });

        jLabel3.setText("Choose Player 2's color");

        ErrorLabel.setForeground(new java.awt.Color(255, 0, 0));

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Delay AI moves");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        EasyButton.setText("Easy");
        EasyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EasyButtonActionPerformed(evt);
            }
        });

        MediumButton.setText("Medium");
        MediumButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MediumButtonActionPerformed(evt);
            }
        });

        HardButton.setSelected(true);
        HardButton.setText("Hard");
        HardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HardButtonActionPerformed(evt);
            }
        });

        AIvAI.setText("AI vs. AI");
        AIvAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AIvAIActionPerformed(evt);
            }
        });

        AccelerateAI.setText("Accelerate AI");
        AccelerateAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccelerateAIActionPerformed(evt);
            }
        });

        AIcycle.setText("Infinite match cycle");
        AIcycle.setToolTipText("Note: Infinite cycle causes errors");
        AIcycle.setEnabled(false);

        ClearScoreButton.setText("Clear Score");
        ClearScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearScoreButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SettingsFrameLayout = new javax.swing.GroupLayout(SettingsFrame.getContentPane());
        SettingsFrame.getContentPane().setLayout(SettingsFrameLayout);
        SettingsFrameLayout.setHorizontalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SettingsFrameLayout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                    .addGroup(SettingsFrameLayout.createSequentialGroup()
                        .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SettingsFrameLayout.createSequentialGroup()
                                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(AIcheckBox)
                                    .addGroup(SettingsFrameLayout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(MediumButton)
                                            .addComponent(EasyButton)
                                            .addComponent(HardButton))))
                                .addGap(18, 18, 18)
                                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(AccelerateAI)
                                    .addComponent(AIvAI)
                                    .addComponent(AIcycle)))
                            .addGroup(SettingsFrameLayout.createSequentialGroup()
                                .addComponent(Player1ColorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2))
                            .addGroup(SettingsFrameLayout.createSequentialGroup()
                                .addComponent(Player2ColorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClearScoreButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SettingsFrameLayout.setVerticalGroup(
            SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AIcheckBox)
                    .addComponent(AIvAI))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EasyButton)
                    .addComponent(AccelerateAI))
                .addGap(2, 2, 2)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MediumButton)
                    .addComponent(AIcycle))
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SettingsFrameLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SettingsFrameLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(HardButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1)))
                .addGap(18, 18, 18)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Player1ColorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SettingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Player2ColorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(ClearScoreButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PlayerLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        PlayerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PlayerLabel.setText("Label");

        RestartButton.setText("Restart");
        RestartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestartButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BoardPanelLayout = new javax.swing.GroupLayout(BoardPanel);
        BoardPanel.setLayout(BoardPanelLayout);
        BoardPanelLayout.setHorizontalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );
        BoardPanelLayout.setVerticalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );

        SettingsButton.setText("Settings");
        SettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsButtonActionPerformed(evt);
            }
        });

        modeLabel.setText("singleplayer");

        player1label.setText("Player 1");

        player2label.setText("Player 2");

        player1score.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        player1score.setText("(score)");

        player2score.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        player2score.setText("(score)");

        RoundLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        RoundLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RoundLabel.setText("Round: 1");
        RoundLabel.setToolTipText("");

        LicenseLabel.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        LicenseLabel.setForeground(new java.awt.Color(102, 102, 102));
        LicenseLabel.setText("This software and all of its components are copyrighted under the Creative Commons Attribution-NoDerivatives 4.0 License");
        LicenseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LicenseLabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player2label, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(player2score, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RestartButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SettingsButton)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(LicenseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(36, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(40, 40, 40)
                            .addComponent(modeLabel))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(91, 91, 91)
                            .addComponent(BoardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(160, 160, 160)
                            .addComponent(PlayerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(player1label)
                        .addComponent(player1score)
                        .addComponent(RoundLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(10, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RestartButton)
                    .addComponent(SettingsButton))
                .addGap(56, 56, 56)
                .addComponent(player2label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(player2score)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 312, Short.MAX_VALUE)
                .addComponent(LicenseLabel))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(PlayerLabel)
                            .addGap(27, 27, 27)
                            .addComponent(modeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(BoardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(65, 65, 65)
                            .addComponent(RoundLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(player1label)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(player1score)))
                    .addContainerGap(28, Short.MAX_VALUE)))
        );

        jLabel4.setFont(new java.awt.Font("Sylfaen", 0, 11)); // NOI18N
        jLabel4.setText("Nathan Software");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Sylfaen", 0, 11)); // NOI18N
        jLabel5.setText("Version 1.0.0");

        jLabel6.setFont(new java.awt.Font("Algerian", 0, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 204));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Connect Four");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(158, 158, 158))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AIcheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AIcheckBoxActionPerformed
            AIgame = AIcheckBox.isSelected();
            
            updateModeLabel();
    }//GEN-LAST:event_AIcheckBoxActionPerformed

    private void RestartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestartButtonActionPerformed
        //TODO fix this ->
        // you can not restart in the middle of the AI playing itself because that breaks stuff??
        if (!AIvAI.isSelected() | GameFinished == true) {
            RunCycle();
        }
    }//GEN-LAST:event_RestartButtonActionPerformed

    private void SettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsButtonActionPerformed
        // open the settings frame
        
        SettingsFrame.setVisible(true);
        SettingsFrame.setLocationRelativeTo(null);
        SettingsFrame.setSize(400,350);
        
        ErrorLabel.setText("");
    }//GEN-LAST:event_SettingsButtonActionPerformed
    
    
    private void Player1ColorBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Player1ColorBoxItemStateChanged
        
            ErrorLabel.setText("");
            
            // if you don't pick the same color as the other guy then good
            if (Player1ColorBox.getSelectedIndex() != Player2ColorBox.getSelectedIndex()) {
                TeamColor[1] = listOfColors[Player1ColorBox.getSelectedIndex()];

                OutputBoard();
                SetupTheLabels();
            } else {
                // tell them that it doesn't work
                ErrorLabel.setText("Choose a different color");
            }
        
            //OUTPUT System.out.println("Players " + ThisPlayer);
    }//GEN-LAST:event_Player1ColorBoxItemStateChanged

    
    private void Player2ColorBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Player2ColorBoxItemStateChanged
        
            ErrorLabel.setText("");
            
            // if you don't pick the same color as the other guy then good
            if (Player2ColorBox.getSelectedIndex() != Player1ColorBox.getSelectedIndex()) {
                TeamColor[2] = listOfColors[Player2ColorBox.getSelectedIndex()];

                OutputBoard();
                
                SetupTheLabels();
            } else {
                // tell them that it doesn't work
                ErrorLabel.setText("Choose a different color");
            }
            
            //OUTPUT System.out.println("Players " + ThisPlayer);
    }//GEN-LAST:event_Player2ColorBoxItemStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        delayAImoves = jCheckBox1.isSelected();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void EasyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EasyButtonActionPerformed
        // if you've choosen easy then set it up
        if (EasyButton.isSelected()) {
            AIdifficulty = "Easy";
            MediumButton.setSelected(false);
            HardButton.setSelected(false);
        } else if (!MediumButton.isSelected() && !HardButton.isSelected()) {
            // if you try to unselect easy without selecting another option than too bad
            EasyButton.setSelected(true);
        }
    }//GEN-LAST:event_EasyButtonActionPerformed

    private void MediumButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MediumButtonActionPerformed
        // if you've choosen medium then set it up
        if (MediumButton.isSelected()) {
            AIdifficulty = "Medium";
            EasyButton.setSelected(false);
            HardButton.setSelected(false);
        } else if (!EasyButton.isSelected() && !HardButton.isSelected()) {
            // if you try to unselect medium without selecting another option than too bad
            MediumButton.setSelected(true);
        }
    }//GEN-LAST:event_MediumButtonActionPerformed

    private void HardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HardButtonActionPerformed
        // if you've choosen hard then set it up
        if (HardButton.isSelected()) {
            AIdifficulty = "Hard";
            EasyButton.setSelected(false);
            MediumButton.setSelected(false);
        } else if (!MediumButton.isSelected() && !EasyButton.isSelected()) {
            // if you try to unselect hard without selecting another option than too bad
            HardButton.setSelected(true);
        }
    }//GEN-LAST:event_HardButtonActionPerformed

    private void AIvAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AIvAIActionPerformed
        // if the AI is not in the middle of his turn then stop
        
        // if you click the button it will finish the round regardless, and then hand over control to you
        if (AIvAI.isSelected()) {
            AIturn();
        }
    }//GEN-LAST:event_AIvAIActionPerformed

    private void AccelerateAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccelerateAIActionPerformed
        AccelerateAIFaster = AccelerateAI.isSelected();
    }//GEN-LAST:event_AccelerateAIActionPerformed

    private void ClearScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearScoreButtonActionPerformed
        rounds = 1;
        player1wins = 0;
        player2wins = 0;
        
        SetupTheLabels();
    }//GEN-LAST:event_ClearScoreButtonActionPerformed

    private void LicenseLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseClicked
        URI domain = null;
        try {
            domain = new URI("https://creativecommons.org/licenses/by-nd/4.0/"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(ConnectFourFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_LicenseLabelMouseClicked

    private void LicenseLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseEntered
        LicenseLabel.setForeground(Color.CYAN);
    }//GEN-LAST:event_LicenseLabelMouseEntered

    private void LicenseLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LicenseLabelMouseExited
        LicenseLabel.setForeground(new Color(102,102,102));
    }//GEN-LAST:event_LicenseLabelMouseExited

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        URI domain = null;
        try {
            domain = new URI("http://www.nathansoftware.com/wordpress/connect-four"); //launches the password help window
        } catch (URISyntaxException ex) {
            Logger.getLogger(ConnectFourFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel4.setForeground(Color.CYAN);
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel4.setForeground(Color.black);
    }//GEN-LAST:event_jLabel4MouseExited

    public static void openWebpage(URI uri) {
       Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
       if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
           try {
               desktop.browse(uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
    
    public void updateModeLabel() {
        if (AIgame == true) {
            modeLabel.setText("Singleplayer");
        } else {
            modeLabel.setText("Multiplayer");
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* if ( Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * for ( details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConnectFourFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConnectFourFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConnectFourFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectFourFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConnectFourFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AIcheckBox;
    public javax.swing.JCheckBox AIcycle;
    public javax.swing.JCheckBox AIvAI;
    public javax.swing.JCheckBox AccelerateAI;
    private javax.swing.JPanel BoardPanel;
    private javax.swing.JButton ClearScoreButton;
    private javax.swing.JRadioButton EasyButton;
    private javax.swing.JLabel ErrorLabel;
    private javax.swing.JRadioButton HardButton;
    private javax.swing.JButton LicenseLabel;
    private javax.swing.JRadioButton MediumButton;
    private javax.swing.JComboBox<String> Player1ColorBox;
    private javax.swing.JComboBox<String> Player2ColorBox;
    public javax.swing.JLabel PlayerLabel;
    private javax.swing.JButton RestartButton;
    private javax.swing.JLabel RoundLabel;
    private javax.swing.JButton SettingsButton;
    private javax.swing.JFrame SettingsFrame;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JLabel modeLabel;
    private javax.swing.JLabel player1label;
    private javax.swing.JLabel player1score;
    private javax.swing.JLabel player2label;
    private javax.swing.JLabel player2score;
    // End of variables declaration//GEN-END:variables
}
