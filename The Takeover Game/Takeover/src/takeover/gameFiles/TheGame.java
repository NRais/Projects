/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover.gameFiles;

import java.awt.Color;
import java.awt.Dimension;
import takeover.*;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author Nuser
 */
public class TheGame extends javax.swing.JFrame {
    
    // ------------
    // STATIC
        private static final int TIMERINCREMENT = 1000; // every tenth of a millesecond
    
    // ------------
    // VARIABLES
    
        private int[][] gameBoard; // as the name implies this is the 2d matrix that repersents our board
    
        private Boolean gameRunning = false; // .. variable defining weather the game is running
        
        private int GameTimer = Takeover.gameTime;
        private int myTimerCountdown = 1000 / TIMERINCREMENT;
    
        // PLAYER VARIABLES
        private PlayerObject players[]; // array of the players
    // ------------
    
    static GraphicsDevice device = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getScreenDevices()[0];

    /**
     * Creates new form TheGame
     * @param newBoard - Integer array which will be created
     * @param newPlayers - which will be used in the game
     */
    public TheGame(int[][] newBoard, PlayerObject[] newPlayers) {
        // KEYLISTENERS - When the game is launched the key listeners are activated ALREADY (from MainMenu)
        
        gameBoard = newBoard; // already generated integer board
        players = newPlayers; // already filled and setup array of players
        
        // setup jframe interface
        initialiseGUI();
        
        // setup board grid
        initialiseBoard(true, gameBoard);
        
        // setup Players
        initialisePlayers();
        
        // allow you to see the results of the newly setup gui and board
        Panel.repaint();
        Panel.setVisible(true);
                
        // GAME THREAD - timer thread where game runs continually
        gameRunning = true;
        gameThread();
        
    }
    
    
    private void initialiseGUI() {
        // resize this image to the screen size and then set it up as the background picture
        Image background = ImageManipulator.resizeToBig(new ImageIcon(getClass().getResource("/resources/menu/random-map.png")).getImage(), getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
        ImageManipulator.frameSetBackground(this, background);
        
        // setup gui
        initComponents();
        
        
        // then re-add the board components to the frame so they work
            this.add(everything);
            everything.setLocation(0,0);
            everything.setOpaque(false);
            everything.setSize(getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
        
        // then set the frame size to be the screen
        this.setBounds(0, 0, getToolkit().getScreenSize().width,
               getToolkit().getScreenSize().height);
        this.setFocusable(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
                
        // setup home button
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");
                
        // setup the game panels size
        Panel.setSize(everything.getWidth() - sidePanel.getWidth() - 50, everything.getHeight() - topPanel.getHeight() - 50);
        
        // setup side bar
        
        // score labels
        TScore[0][0] = T1Score;
        TScore[1][0] = T2Score;
        TScore[2][0] = T3Score;
        TScore[3][0] = T4Score;
        // team name labels
        TScore[0][1] = label1;
        TScore[1][1] = label2;
        TScore[2][1] = label3;
        TScore[3][1] = label4;
        // sector score labels
        TScore[0][2] = T1Sectors;
        TScore[1][2] = T2Sectors;
        TScore[2][2] = T3Sectors;
        TScore[3][2] = T4Sectors;
        
        // assign label team colors
        
        for (int i = 0; i < 4; i++) {
            // for all the enabled players
            if (i < Takeover.numberOfPlayers) {
                TScore[i][1].setText(Takeover.playerUser[i]);       // set user name
                TScore[i][1].setForeground(Takeover.playerColor[i]); // set user color
                TScore[i][2].setForeground(Takeover.playerColor[i]); // set sector color
                // show
                TScore[i][1].setVisible(true);
                TScore[i][0].setVisible(true);
                TScore[i][2].setVisible(true);
            }
            // for all the disabled players
            else {
                // hide
                TScore[i][1].setVisible(false);
                TScore[i][0].setVisible(false);
                TScore[i][2].setVisible(false);
            }
        }
    }
    
    // remove all square to make a new board
    public void deleteSquares() {
        int totalSquares = xNumSquares*yNumSquares;
        System.out.println("Total squares: " + totalSquares);
        
        try {
            for (int yCor = 0; yCor < yNumSquares; yCor ++) {
                for (int xCor = 0; xCor < xNumSquares; xCor ++) {
                    squareArray[xCor][yCor].setVisible(false);
                    squareArray[xCor][yCor] = null;
                }
                
            }
            
            gameRunning = false;
            
            // RESET DIRECTION
            for (PlayerObject player : players) {
                for (UnitObject myUnit : player.myUnits) {
                    myUnit.myDirection = "";
                }
            }
        } catch (Exception e) {}
        
        T1Score.setText("0");
        T2Score.setText("0");
        
        
    }
        
    
    ////// GENERATE BOARD //////
        
    JLabel[][] squareArray = null;
        
    private static int bufferWidthValue = 0; //the buffer around the board
    private static int boundaryWidthValue = 1; //the boundary between each square ****
    private static int topBufferWidthValue = 0; //the buffer at the top of the board
    private static int rightBufferWidthValue = 0; //the buffer on the right of the board
    private static int eachSquareSize = 0;
    private static int eachPlayerSize = 0;
    
    
    private static int xNumSquares = -1;
    private static int yNumSquares = -1;
    
    // pass a BOOLEAN if true it will automatically GENERATE a board for you :)
    // setup visible board gui
    public void initialiseBoard(Boolean override, int[][] overrideMatrix) {
        
        // SETUP DIMENSIONS OF BOARD
        xNumSquares = overrideMatrix.length;
        yNumSquares = overrideMatrix[0].length;
        
        // set starting positions for the first square (the following squares will be based upon that)
        int xValue = (topBufferWidthValue + boundaryWidthValue); // start positions
        int yValue = (boundaryWidthValue);
        
        int rowCount = 1; //the row we are currently creating
        
        // DETERMINE THE SIZE OF THE SQUARES (because we have some amount which needs to fit into our panel)
        
        rightBufferWidthValue = sidePanel.getWidth();
        
        //System.out.println("Panel width " + Panel.getWidth() + " right buffer " + rightBufferWidthValue + " side panel x " + sidePanel.getWidth());
        
        int eachSquareSizeX = ((Panel.getWidth() 
                - bufferWidthValue/2 + boundaryWidthValue - rightBufferWidthValue) / xNumSquares) 
                - boundaryWidthValue;
        
        int eachSquareSizeY = ((Panel.getHeight() 
                - topBufferWidthValue - bufferWidthValue*2 + boundaryWidthValue) / yNumSquares) 
                - boundaryWidthValue;
        
        // ** System.out.println("essY = " + eachSquareSizeY + " essX = " + eachSquareSizeX);
        
        
        // the squares need to be square so whichever size is smaller is used
        // if x is bigger than all the squares are set to y size and vice versa
        if (eachSquareSizeX > eachSquareSizeY) {
            eachSquareSize = eachSquareSizeY;
        } else {
            eachSquareSize = eachSquareSizeX;
        }
                
        // ------
                
        int numSquaresPerRow = xNumSquares;
        
        // ** System.out.println("Size of ea. square: " + eachSquareSize +
        // **         " Number of squares per row: " + numSquaresPerRow + 
        // **         " Total squares: " + totalSquares);
        
        // setup SQUARE array 
        squareArray = new JLabel[xNumSquares][yNumSquares];
        
        // ** System.out.println("X,Y: " + xNumSquares + ":" + yNumSquares);
        
                
        // for EACH SQUARE ON BOARD
        // iterate through every square and set it up on the board
        for(int y = 0; y < yNumSquares; y++) {    
            
            for (int x = 0; x < xNumSquares; x++) {
            
                // setup JLabel
                squareArray[x][y] = new JLabel();
                
                squareArray[x][y].setSize(eachSquareSize,eachSquareSize);

                
                // ** System.out.println("Square " + x + "," + y + "= *" + overrideMatrix[x][y] + "*" + "Square x,y values:" + xValue + "," + yValue);

                // ASSIGN THE SQUARE AS THE NEXT THING IN THE MATRIX
                squareSetColor(x, y, overrideMatrix[x][y]);
                
                // ## TYPE FOR A STARTING SPACE ##
                if (overrideMatrix[x][y] == 3) {
                    System.out.println("NEW SPACE");
                    //  add a starting position to our list
                    startingSpaces.add(new Dimension(x,y));
                }
                
                // set the squares position on the board
                squareArray[x][y].setLocation(xValue,yValue);

                // increment the position for the next square
                xValue += boundaryWidthValue + eachSquareSize;



                if (rowCount == numSquaresPerRow) { //if you have reached the end of row then bump down to next row
                    // ** System.out.println("End of row");

                    yValue += boundaryWidthValue + eachSquareSize;
                    xValue = (topBufferWidthValue + boundaryWidthValue);

                    rowCount = 0;

                    // ** System.out.println("Will change next x,y values to: " + xValue + "," + yValue);
                } 

                // another square has been added to the row
                
                rowCount++;

                squareArray[x][y].setVisible(true);

                Panel.add(squareArray[x][y]);
                Panel.repaint();

                // ** System.out.println("Next column");
            }
            
            // ** System.out.println("Next row");
            
            
        }
        
        
    }    
    
    // List to store all the starting spaces
    private final List<Dimension> startingSpaces = new ArrayList<>();
    
    // calculate a player to go in the starting space
    public void randomlyAssignStartSpaces() {
                
        // create a temporary array to store what spaces we still have left to assign to the players
        List<Dimension> temp = new ArrayList<>();        
        
        temp.addAll(startingSpaces);
        
        
        Random rnd = new Random();
           
        // loop through all players and give them a space from the list
        for (int i = 0; i < Takeover.numberOfPlayers; i ++) {
            
            
            // randomly generate which space to next assign
            int space = rnd.nextInt(temp.size()); // generate a number up to the total number of starting spaces optional
            
            // assign all their units to start there
            for (UnitObject myUnit : players[i].myUnits) {
                myUnit.myStartSpace[0] = temp.get(space).width;
                myUnit.myStartSpace[1] = temp.get(space).height;
            }
            
            // then don't assign that one any more
            int remainingSpaces = temp.size();
            int remainingPlayers = Takeover.numberOfPlayers - i;
            
            // only if we have enough spaces remaining will we remove this one
            // IF NOT we will leave this space so it can be assigned to multiple players
            if (remainingSpaces >= remainingPlayers) {
                
                System.out.println("REMOVING " + i);
                temp.remove(space);
            }
        }
        
    }
    
    public void squareSetColor(int x, int y, int matrixNumber) {

        
        switch (matrixNumber) {
            case 0:
                // then its a null square same as background
                squareArray[x][y].setOpaque(false);
                squareArray[x][y].setBackground(new Color(240,240,240));
                break;
            case 2:
                // then its a impassible square BLACK
                squareArray[x][y].setOpaque(true);
                squareArray[x][y].setBackground(Color.black);
                squareArray[x][y].setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                break;
            case 1: case 3: 
                // NOTE: '3' is a starting space but its still just a normal white space
                // then its a normal square WHITE
                squareArray[x][y].setOpaque(true);
                squareArray[x][y].setBackground(Color.white);
                squareArray[x][y].setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                break;
            default:
                // then its a normal square WHITE
                
                squareArray[x][y].setBackground(Color.white);
                squareArray[x][y].setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                break;
        }
        
    }
    
    // VISIBLE GAME GUI
    JLabel[][] TScore = new JLabel[4][3];
        
    
    // setup players and units
    private void initialisePlayers() { // STILL TODO
        // NOTE: as of now we have an array which is storing all the players and units 
        // (but so far they are not existing as entities on the JFrame)
        
        // SETUP THE SIZE OF EACH PLAYER for the frame
        eachPlayerSize = eachSquareSize;
        
        // randomly give all the units starting spaces
        randomlyAssignStartSpaces();
               
        // for each PLAYER'S UNIT create it and set it visible
        // for players up to the numberOfPlayers
        for (PlayerObject player : players) {
            // for units up to the number of units each player has
            for (UnitObject myUnit : player.myUnits) {
                // create new JLabel for our piece
                // set it to be the correct size
                myUnit.setSize(eachPlayerSize, eachPlayerSize);
                myUnit.setOpaque(false);
                
                // get the image that relates to this players color 
                Image i = ImageManipulator.resizeToBig(Takeover.iconTable.get(player.myTeamColor), eachPlayerSize, eachPlayerSize);
                
                // set the players image
                myUnit.setIcon(new ImageIcon(i));
                
                // add it to the panel (on top)
                Panel.add(myUnit, 0);
                                
                // set the starting location to be the same as that of the JLabel of the X,Y grid coordinates it is equal to
                myUnit.setLocation(squareArray[myUnit.myStartSpace[0]][0].getX(), squareArray[0][myUnit.myStartSpace[1]].getY());
                
                myUnit.setVisible(true);
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        everything = new javax.swing.JPanel();
        Panel = new javax.swing.JPanel();
        sidePanel = new javax.swing.JPanel();
        teamScorePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        T1Sectors = new javax.swing.JLabel();
        T2Sectors = new javax.swing.JLabel();
        T3Sectors = new javax.swing.JLabel();
        T4Sectors = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        T3Score = new javax.swing.JLabel();
        T1Score = new javax.swing.JLabel();
        T4Score = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        T2Score = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        HomeButton = new javax.swing.JLabel();

        everything.setOpaque(false);
        everything.setPreferredSize(new java.awt.Dimension(1200, 800));

        Panel.setOpaque(false);
        Panel.setLayout(null);

        sidePanel.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Broadway", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECTORS");

        T1Sectors.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        T1Sectors.setText("5");
        T1Sectors.setToolTipText("");

        T2Sectors.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        T2Sectors.setText("5");
        T2Sectors.setToolTipText("");

        T3Sectors.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        T3Sectors.setText("5");
        T3Sectors.setToolTipText("");

        T4Sectors.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        T4Sectors.setText("5");
        T4Sectors.setToolTipText("");

        javax.swing.GroupLayout teamScorePanelLayout = new javax.swing.GroupLayout(teamScorePanel);
        teamScorePanel.setLayout(teamScorePanelLayout);
        teamScorePanelLayout.setHorizontalGroup(
            teamScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamScorePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(teamScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(teamScorePanelLayout.createSequentialGroup()
                        .addComponent(T1Sectors, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(T2Sectors, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, teamScorePanelLayout.createSequentialGroup()
                        .addComponent(T3Sectors, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(T4Sectors, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        teamScorePanelLayout.setVerticalGroup(
            teamScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamScorePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(teamScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(T1Sectors)
                    .addComponent(T2Sectors))
                .addGap(29, 29, 29)
                .addGroup(teamScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(T3Sectors)
                    .addComponent(T4Sectors))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        timerLabel.setFont(new java.awt.Font("Broadway", 1, 36)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(255, 255, 255));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/timer-chip.png"))); // NOI18N
        timerLabel.setText("120");
        timerLabel.setIconTextGap(-105);

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(teamScorePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(teamScorePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179))
        );

        topPanel.setOpaque(false);

        T3Score.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        T3Score.setText("0");

        T1Score.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        T1Score.setText("0");

        T4Score.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        T4Score.setText("0");

        label1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label1.setText("Team 1");

        label2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label2.setText("Team 2");

        T2Score.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        T2Score.setText("0");

        label4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label4.setText("Team 4");

        label3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        label3.setText("Team 3");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T1Score, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T2Score, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T3Score, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(T4Score, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(label2)
                            .addComponent(label3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(T1Score)
                            .addComponent(T2Score)
                            .addComponent(T3Score)))
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addComponent(label4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(T4Score)))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        HomeButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        HomeButton.setForeground(new java.awt.Color(255, 255, 255));
        HomeButton.setText("HOME");
        HomeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HomeButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HomeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HomeButtonMouseExited(evt);
            }
        });

        javax.swing.GroupLayout everythingLayout = new javax.swing.GroupLayout(everything);
        everything.setLayout(everythingLayout);
        everythingLayout.setHorizontalGroup(
            everythingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(everythingLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(everythingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(everythingLayout.createSequentialGroup()
                        .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(everythingLayout.createSequentialGroup()
                        .addComponent(HomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        everythingLayout.setVerticalGroup(
            everythingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(everythingLayout.createSequentialGroup()
                .addGroup(everythingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(everythingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(HomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(everythingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(everythingLayout.createSequentialGroup()
                        .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 133, Short.MAX_VALUE))
                    .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 184, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseClicked
        // unselect foreground, unselect icon
        HomeButton.setForeground(Color.black);
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");
        
        // kill frame
        this.dispose();
                
    }//GEN-LAST:event_HomeButtonMouseClicked

    private void HomeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseEntered
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow-Selected.png");
    }//GEN-LAST:event_HomeButtonMouseEntered

    private void HomeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseExited
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");
    }//GEN-LAST:event_HomeButtonMouseExited

    
    private void gameThread() {
        Timer runningGame = new Timer(); // recreate timer
        /// ACTIVATE A TASK TO CHECK FOR INPUT
        runningGame.schedule(new TimerTask() {
            @Override
            public void run() { 
                
                // ONLY IF NOT PAUSED OR INTRO/EXIT
                if (gameRunning) {
                    
                    // DECREMENT THE TIMER
                    TimerCounting();
                    
                    
                    // FOR ALL PLAYER UNITS
                    // loop through every active players and all their active units
                    for (int player = 0; player < Takeover.numberOfPlayers ; player++) {
                        for (int unit = 0; unit < Takeover.defaultNumberOfUnits; unit++) {
                        
                            // IF AI UNIT RUN AI CLASS
                            if (players[player].myUnits[unit].aiControlled) {
                                // set the AI units direction with the AI class
                                players[player].myUnits[unit].myDirection = AIOperator.calculateDirection(player, unit, gameBoard);
                            }
                        
                            MoveUnit(player, unit);
                        }
                    }
                    
                }

            }
        }, 0, TIMERINCREMENT);
    }
    
    private void TimerCounting() {
        
        // if the timer's countdown has reached 0 then we know it has been a second
        if (myTimerCountdown <= 0) {
            // reduce actual game timer
            GameTimer = GameTimer - 1;

            // update game timer label
            // TODO make it so it always has 3 characters
            timerLabel.setText("" + GameTimer);
            
            // reset it so it will last a second
            myTimerCountdown = 1000 / TIMERINCREMENT;
        } else {
            
            // otherwise decrement the timercountdown towards the end result
            myTimerCountdown = myTimerCountdown - TIMERINCREMENT;
        }
        
        // CHECK FOR GAME END
        if (GameTimer <= 0) {
            gameRunning = false;
        }
    }
    
    
    // PROCEDURE - moves a players unit the direction he is currently going (as stored in his playerDirections variable)
    private void MoveUnit(int playerNum, int unitNum) {
        
        // IF IT IS TIME TO MOVE - THEN GO FOR IT
        if (players[playerNum].myUnits[unitNum].myTimerCount == 0) {
            
            // MOVE UNIT
            // update unit coordinates
            //players[playerNum].myUnits[unitNum].location[0] = 0; // TODO UPDATE UNIT COORDINATES

            // update unit image

            //
            
            // RESET MOVECOUNTER
            players[playerNum].myUnits[unitNum].myTimerCount = players[playerNum].myUnits[unitNum].myTime;
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HomeButton;
    private javax.swing.JPanel Panel;
    private javax.swing.JLabel T1Score;
    private javax.swing.JLabel T1Sectors;
    private javax.swing.JLabel T2Score;
    private javax.swing.JLabel T2Sectors;
    private javax.swing.JLabel T3Score;
    private javax.swing.JLabel T3Sectors;
    private javax.swing.JLabel T4Score;
    private javax.swing.JLabel T4Sectors;
    private javax.swing.JPanel everything;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel teamScorePanel;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
