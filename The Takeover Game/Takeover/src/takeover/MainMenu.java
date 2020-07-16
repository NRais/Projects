/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import static takeover.Takeover.fillHashMap;
import takeover.gameFiles.PlayerObject;
import takeover.gameFiles.TheGame;

/**
 *
 * @author Nuser
 */
public class MainMenu extends javax.swing.JFrame {

        // setup media
        //Media hit;
        //MediaPlayer mediaPlayer;
        
        Color c = new Color(255,255,255, 120);
                
        
        HashMap<String, MapObject> mapTable = new HashMap<>();
        
        
        JComboBox playerCombo[];
        JLabel playerLevel[];
        JLabel playerAIButton[];
        JLabel playerImage[];
        JPanel playerPanel[];
        
        Image greenIcon = null;
        Image yellowIcon = null;
        Image redIcon = null;
        Image blueIcon = null;
        Image settingsBackground = null;
        
        Image toggleOnIcon = null;
        Image toggleOffIcon = null;
    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        try{
            UIManager.put("TabbedPane.contentOpaque", false);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
                System.out.println("UIManager Exception : " + e);
        }   
        
        initComponents();
        
        setupPlayerArrays();
        
        
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/gameIcon.png")));
        this.setIconImages(icons);
         
        // initialize JFX so that the media works
        /*final JFXPanel fxPanel = new JFXPanel();
         
        try {
            // call the main song
            hit = new Media(getClass().getResource("/resources/sounds/theme.mp3").toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        // create an instance of our image manipulator class to use to setup the game frames and images
        ImageManipulator i = new ImageManipulator();
        
        // we resize all the images at the start so we can call them whenever we want
        // this speeds up preformance when loading the menu system
        greenIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/game/green-Piece.png")).getImage(), 100, 100);
        yellowIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/game/yellow-Piece.png")).getImage(), 100, 100);
        redIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/game/red-Piece.png")).getImage(), 100, 100);
        blueIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/game/blue-Piece.png")).getImage(), 100, 100);
        toggleOnIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/menu/toggle-on.png")).getImage(), 80, 80);
        toggleOffIcon = i.resizeToBig(new ImageIcon(getClass().getResource("/resources/menu/toggle-off.png")).getImage(), 80, 80);
        settingsBackground = i.resizeImageAspect(this, "/resources/menu/s-panel.png");
        
        // SETUP ICON TABLE
        Takeover.iconTable.put(Takeover.red, redIcon);
        Takeover.iconTable.put(Takeover.green, greenIcon);
        Takeover.iconTable.put(Takeover.blue, blueIcon);
        Takeover.iconTable.put(Takeover.yellow, yellowIcon);
                
        p1Image.setIcon(new ImageIcon(Takeover.iconTable.get(Takeover.playerColor[0])));
        p2Image.setIcon(new ImageIcon(Takeover.iconTable.get(Takeover.playerColor[1])));
        p3Image.setIcon(new ImageIcon(Takeover.iconTable.get(Takeover.playerColor[2])));
        p4Image.setIcon(new ImageIcon(Takeover.iconTable.get(Takeover.playerColor[3])));
                       
        
        i.frameSetBackground(this, "/resources/menu/back.png");
                
        i.resizeImage(mainLabel, this.getWidth(), (int)(this.getWidth() / 4.68), "resources/menu/mainLabel.png");
        i.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");
        i.resizeImage(HomeButton1, 60, 60, "resources/menu/homeArrow.png");
        i.resizeImage(CampaignImage, 410, 220, "resources/menu/light.png");
        
        // SETUP MAP TABLE
        for (String map: mapList) {
            try {
                map = map.substring(0, map.indexOf(".map"));
                
                MapObject thisMap = new MapObject(map); // load the map
                
                // add it to our maps table
                mapTable.put(map, thisMap);
                
                // CHECK WHAT TYPE OF MAP AND DISPLAY ICON
            } catch (Exception e) {
                
            }
        }     
        
        this.add(panel);
        panel.setLocation(0,0);
        panel.setOpaque(false);
        panel.setBackground(new Color(0,0,0,0));
        panel.setSize(this.getWidth(), this.getHeight());

        // setup the other menu frames so they work
        otherFrameCanClose(PlayMenu, true);
        otherFrameCanClose(SettingsMenu, true);
        
        labelClickable(PlayButton, this);
        labelClickable(SettingsButton, this);
        labelClickable(WebsiteButton, this);
        labelClickable(QuickGameButton, PlayMenu);
        labelClickable(CampaignButton, PlayMenu);
        labelClickable(ScenarioGameButton, PlayMenu);
        labelClickable(HomeButton, PlayMenu);
        
        labelClickable(HomeButton1, SettingsMenu);
        labelClickable(ControlsButton, SettingsMenu);
        labelClickable(OptionsButton, SettingsMenu);
        labelClickable(HelpButton, SettingsMenu);
        
        SetupQuickPlayerMenu();
        SetupControlsMenu();
        
        PlayPanel.setBackground(new Color(0,0,0,0));
        QuickPanel.setBackground(new Color(0,0,0,0));
        ScenarioPanel.setBackground(new Color(0,0,0,0));
        CampaignPanel.setBackground(new Color(0,0,0,0));
        GameSwitchPanel.setBackground(c);
        
        SettingsPanel.setBackground(new Color(0,0,0,0));
        OptionsPanel.setBackground(new Color(0,0,0,0));
        HelpPanel.setBackground(new Color(0,0,0,0));
        ControlsPanel.setBackground(new Color(0,0,0,0));
        
        SettingsSwitchPanel.setBackground(c);
        jScrollPane1.setBackground(new Color(0,0,0,0));
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        
    }
    
    
    private void setupPlayerArrays() {
        // setup array of combo boxes and Labels and panels
        playerCombo = new JComboBox[4];
        playerCombo[0] = player1Combo;
        playerCombo[1] = player2Combo;
        playerCombo[2] = player3Combo;
        playerCombo[3] = player4Combo;
        
        playerLevel = new JLabel[4];
        playerLevel[0] = player1Level;
        playerLevel[1] = player2Level;
        playerLevel[2] = player3Level;
        playerLevel[3] = player4Level;
        
        playerAIButton = new JLabel[4];
        playerAIButton[0] = player1AI;
        playerAIButton[1] = player2AI;
        playerAIButton[2] = player3AI;
        playerAIButton[3] = player4AI;
        
        playerImage = new JLabel[4];
        playerImage[0] = p1Image;
        playerImage[1] = p2Image;
        playerImage[2] = p3Image;
        playerImage[3] = p4Image;
        
        playerPanel = new JPanel[4];
        playerPanel[0] = player1Panel;
        playerPanel[1] = player2Panel;
        playerPanel[2] = player3Panel;
        playerPanel[3] = player4Panel;
    }
    
    String[] playersList = new File("players/").list();
    String[] mapList = new File("maps/").list();
    
    public void SetupQuickPlayerMenu() {
        ImageIcon icon = new ImageIcon("resources/SearchIconOrange.png");
        
        // SETUP TABEL PANEL
        
        PlayerTabbedPanel.addTab("Player 1", icon, playerPanel[0],
                                    "Settings for player 1");
        PlayerTabbedPanel.setMnemonicAt(0, KeyEvent.VK_1);
        PlayerTabbedPanel.addTab("Player 2", icon, playerPanel[1],
                                    "Settings for player 2");
        PlayerTabbedPanel.setMnemonicAt(0, KeyEvent.VK_2);
        PlayerTabbedPanel.addTab("Player 3", icon, playerPanel[2],
                                    "Settings for player 3");
        PlayerTabbedPanel.setMnemonicAt(0, KeyEvent.VK_3);
        PlayerTabbedPanel.addTab("Player 4", icon, playerPanel[3],
                                    "Settings for player 4");
        PlayerTabbedPanel.setMnemonicAt(0, KeyEvent.VK_4);
        
        // set the font color to be correct
        PlayerTabbedPanel.setForegroundAt(0, Takeover.playerColor[0]);
        PlayerTabbedPanel.setForegroundAt(1, Takeover.playerColor[1]);
        PlayerTabbedPanel.setForegroundAt(2, Takeover.playerColor[2]);
        PlayerTabbedPanel.setForegroundAt(3, Takeover.playerColor[3]);
                
        PlayerTabbedPanel.setOpaque(false);
        PlayerTabbedPanel.setBackground(c);
        playerPanel[0].setOpaque(false);
        playerPanel[0].setBackground(c);
        playerPanel[1].setOpaque(false);
        playerPanel[1].setBackground(c);
        playerPanel[2].setOpaque(false);
        playerPanel[2].setBackground(c);
        playerPanel[3].setOpaque(false);
        playerPanel[3].setBackground(c);
        
        QuickPanel.repaint();
        PlayMenu.repaint();
                
        File a = new File("here.txt");
            try {
                a.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        // SETUP EXPERIENCE SHIELD ICONS
        // for all the player levels we want the background of a resized black shield
        for (JLabel image: playerLevel) {
            ImageManipulator.resizeImage(image, 40, 40, "resources/menu/shield-black.png");
        }
        
        // SETUP AI BUTTONS
        
        // for each jlabel set it clickable with an action listener
        for (JLabel playerButton : playerAIButton) {
            
            playerButton.setIcon(new ImageIcon(toggleOffIcon));

            playerButton.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent mEvt) {

                    // when clicked figure out which player just clicked
                    int playerNum = Arrays.asList(playerAIButton).indexOf(playerButton); // players are stored 0,1,2,3
                    
                    // we will tell the button (of #playerNum) to be displayed
                    // BUT we will tell it to be displayed as the inverse (!) of whatever it was before (so that way it flips)
                    displayAIButton(!Takeover.playerAI[playerNum], playerButton, playerNum);
                    
                }
            });
        }
        
        // SETUP DROPDOWN BOX FOR MAPS
        // default and first item is random
        MapComboBox.addItem("random");
        MapComboBox.addItem("custom");
        
        // setup renderer to make the icons for the box
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setOpaque(false);
        renderer.setPreferredSize(new Dimension(21, 21));
        MapComboBox.setRenderer(renderer);
        MapComboBox.setMaximumRowCount(5); // max number of items shown
        
        for (String map: mapList) {
            try {
                map = map.substring(0, map.indexOf(".map"));
                
                // add it to the quick drop down box box
                MapComboBox.addItem(map);
                
            } catch (Exception e) {
                
            }
        }
            
            
        // SETUP DROPDOWN BOX FOR PLAYERS IN TABBED PANES
        // create an array of the player files
        // for all the combo boxes add their default item
        for (JComboBox box: playerCombo) {
            box.addItem("New Player");
        }
                
        // add each item in the file array
        for (String player : playersList) {

            // cut the name up to the file extension
            try {
                player = player.substring(0, player.indexOf(".player"));

                // for all the combo boxes add the item
                for (JComboBox box: playerCombo) {
                    box.addItem(player);
                }
            } catch (Exception e) {
                System.out.println("ERROR THE PLAYER FILE IS NOT OF THE CORRECT TYPE");
                // don't add the item to the file
                // just ignore...
            }
        }
        
        // for all the combo boxes add the action listener
        for (int i = 0; i < 4; i ++) {
            dropDownBoxAction(i);
        }
        
                
    }
    
    public void displayAIButton(Boolean AIValue, JLabel playerButton, int playerNum) {
        // check what they are and display it
        if (AIValue == false) {

            playerCombo[playerNum].setEnabled(true);
            
            playerButton.setIcon(new ImageIcon(toggleOffIcon));
            Takeover.playerSetAI(playerNum, false);

            // show player level
            displayPlayerLevel(playerNum);
            
            // set player combo user name
            Takeover.playerSetUser(playerNum, playerCombo[playerNum].getSelectedItem().toString());
            
        }
        else {
            playerCombo[playerNum].setEnabled(false);
            
            playerButton.setIcon(new ImageIcon(toggleOnIcon));
            Takeover.playerSetAI(playerNum, true);

            // hide player level
            playerLevel[playerNum].setText("" + Takeover.AILevel);
            
            // set player combo user name AI PLAYER
            Takeover.playerSetUser(playerNum, "AI Player");
        }

        PlayMenu.repaint();
    }
    
    
    // SETUP CONTROLS MENU
    // --------------
    
    JButton controlsArray[] = new JButton[44];
    
    public void SetupControlsMenu() {
        
        // PLAYER 1
        controlsArray[0] = p1u1Up;
        controlsArray[1] = p1u1Down;
        controlsArray[2] = p1u1Left;
        controlsArray[3] = p1u1Right;
        controlsArray[4] = p1u1Fire;
        controlsArray[5] = p1u2Up;
        controlsArray[6] = p1u2Down;
        controlsArray[7] = p1u2Left;
        controlsArray[8] = p1u2Right;
        controlsArray[9] = p1u2Fire;
        controlsArray[10] = p1Toggle;
        // PLAYER 2
        controlsArray[11] = p2u1Up;
        controlsArray[12] = p2u1Down;
        controlsArray[13] = p2u1Left;
        controlsArray[14] = p2u1Right;
        controlsArray[15] = p2u1Fire;
        controlsArray[16] = p2u2Up;
        controlsArray[17] = p2u2Down;
        controlsArray[18] = p2u2Left;
        controlsArray[19] = p2u2Right;
        controlsArray[20] = p2u2Fire;
        controlsArray[21] = p2Toggle;
        // PLAYER 2
        controlsArray[22] = p3u1Up;
        controlsArray[23] = p3u1Down;
        controlsArray[24] = p3u1Left;
        controlsArray[25] = p3u1Right;
        controlsArray[26] = p3u1Fire;
        controlsArray[27] = p3u2Up;
        controlsArray[28] = p3u2Down;
        controlsArray[29] = p3u2Left;
        controlsArray[30] = p3u2Right;
        controlsArray[31] = p3u2Fire;
        controlsArray[32] = p3Toggle;
        // PLAYER 2
        controlsArray[33] = p4u1Up;
        controlsArray[34] = p4u1Down;
        controlsArray[35] = p4u1Left;
        controlsArray[36] = p4u1Right;
        controlsArray[37] = p4u1Fire;
        controlsArray[38] = p4u2Up;
        controlsArray[39] = p4u2Down;
        controlsArray[40] = p4u2Left;
        controlsArray[41] = p4u2Right;
        controlsArray[42] = p4u2Fire;
        controlsArray[43] = p4Toggle;
                       
        
        for (JButton button : controlsArray) {

            button.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent mEvt) {
                    
                    // when clicked figure out which button just clicked
                    int buttonNum = Arrays.asList(controlsArray).indexOf(button);
                    
                    System.out.println("PLAYER BUTTON " + buttonNum);
                    
                    Takeover.inputer.openControllerFrame(button, buttonNum);
                    
                }
            });
        }
        
    }
    
    
    
    
    // THE FOLLOWING MOUSE ADAPTED PROCEDURE
    // takes a label and a frame and changes it color when it is hovered over
    private void labelClickable(JLabel b, JFrame frame) {
        b.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent mEvt) {
                b.setForeground(Color.white);
                frame.repaint();
            }
            @Override
            public void mouseExited(MouseEvent mEvt) {
                b.setForeground(Color.black);
                frame.repaint();
            }
        });
    }
    
    // THE FOLLOWING ASSIGNS AN ACTION LISTENER TO A COMBOBOX
    private void dropDownBoxAction(int b) {

        // when you choose a player
        playerCombo[b].addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                displayPlayerLevel(b);
                // save the player user
                Takeover.playerSetUser(b, playerCombo[b].getSelectedItem().toString());
            }

        });
    }
    
    // THIS FOLLOWING PROCEDURE ALLOWS ALT JFRAMES TO CLOSE
    // procedure used to allow kill game when you click on the exit button of sub frames we have made
    private void otherFrameCanClose(JFrame frame, Boolean kill) {
        // this procedure allows me to make the rest of the menu frame close this program on exit
        
        // by default it does not exit the program when the close button is hit
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // now we deal with the cases ourself
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (kill) {
                        System.exit(0);
                    } else {
                        // if not kill then dispose
                        frame.dispose();
                        
                        // *reload the main menu
                        //MainMenu.this.setVisible(true);
                        //MainMenu.this.requestFocus();
                    }
                }
        });
    }
    
    // THE FOLLOWING PROCEDURE OPENS JFRAMES
    // then it takes the panel inside them and loads them up
    private void loadWindow (JFrame frame, AlphaContainer panel, String imageName, Boolean hideParent, Image image) {
        if (hideParent) {
            this.setVisible(false);
        }
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/gameIcon.png")));
        frame.setIconImages(icons);
        
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        
        if (imageName == null) {
            ImageManipulator.frameSetBackground(frame, image);
        } else {
            // setup an I for our current state of affairs and then use it
            // NOTE: this doesn't have to be done for static procedures but for a procedure like this it is dynamic and so matter when it is created
            ImageManipulator I = new ImageManipulator();
            
            I.frameSetBackground(frame, imageName);
        }
        
        if (panel != null) {
            frame.add(panel);
            panel.setLocation(0,0);
            panel.setOpaque(false);
            panel.setSize(frame.getWidth(), frame.getHeight());
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

        panel = new takeover.AlphaContainer();
        mainLabel = new javax.swing.JLabel();
        WebsiteButton = new javax.swing.JLabel();
        PlayButton = new javax.swing.JLabel();
        SettingsButton = new javax.swing.JLabel();
        PlayMenu = new javax.swing.JFrame();
        PlayPanel = new takeover.AlphaContainer();
        QuickGameButton = new javax.swing.JLabel();
        CampaignButton = new javax.swing.JLabel();
        GameSwitchPanel = new takeover.AlphaContainer();
        HomeButton = new javax.swing.JLabel();
        ScenarioGameButton = new javax.swing.JLabel();
        CampaignPanel = new takeover.AlphaContainer();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        CampaignImage = new javax.swing.JLabel();
        ScenarioPanel = new takeover.AlphaContainer();
        jLabel6 = new javax.swing.JLabel();
        ScenarioComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        MapPicture1 = new javax.swing.JLabel();
        ScenarioPlayButton = new javax.swing.JButton();
        PlayerTabbedPanel1 = new javax.swing.JTabbedPane();
        ScenarioErrorLabel = new javax.swing.JLabel();
        numberOfPlayers1 = new javax.swing.JSlider();
        numPlayersLabel1 = new javax.swing.JLabel();
        QuickPanel = new takeover.AlphaContainer();
        jLabel1 = new javax.swing.JLabel();
        MapComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        MapPicture = new javax.swing.JLabel();
        QuickGamePlayButton = new javax.swing.JButton();
        PlayerTabbedPanel = new javax.swing.JTabbedPane();
        QuickGameErrorLabel = new javax.swing.JLabel();
        numberOfPlayers = new javax.swing.JSlider();
        numPlayersLabel = new javax.swing.JLabel();
        customBoardField = new javax.swing.JTextField();
        customBoardLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        player4Panel = new takeover.AlphaContainer();
        jLabel14 = new javax.swing.JLabel();
        player4AI = new javax.swing.JLabel();
        p4Image = new javax.swing.JLabel();
        player4Combo = new javax.swing.JComboBox<>();
        player4Level = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        player3Panel = new takeover.AlphaContainer();
        jLabel11 = new javax.swing.JLabel();
        player3AI = new javax.swing.JLabel();
        p3Image = new javax.swing.JLabel();
        player3Level = new javax.swing.JLabel();
        player3Combo = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        player2Panel = new takeover.AlphaContainer();
        jLabel8 = new javax.swing.JLabel();
        player2AI = new javax.swing.JLabel();
        p2Image = new javax.swing.JLabel();
        player2Level = new javax.swing.JLabel();
        player2Combo = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        player1Panel = new takeover.AlphaContainer();
        jLabel5 = new javax.swing.JLabel();
        player1AI = new javax.swing.JLabel();
        p1Image = new javax.swing.JLabel();
        player1Level = new javax.swing.JLabel();
        player1Combo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        SettingsMenu = new javax.swing.JFrame();
        SettingsPanel = new takeover.AlphaContainer();
        HomeButton1 = new javax.swing.JLabel();
        ControlsButton = new javax.swing.JLabel();
        OptionsButton = new javax.swing.JLabel();
        HelpButton = new javax.swing.JLabel();
        jScrollPane1 = new takeover.AlphaScrollContainer();
        SettingsSwitchPanel = new takeover.AlphaContainer();
        ControlsPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        p1u1Up = new javax.swing.JButton();
        p1u1Down = new javax.swing.JButton();
        p1u1Left = new javax.swing.JButton();
        p1u1Right = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        p1u2Up = new javax.swing.JButton();
        p1u2Down = new javax.swing.JButton();
        p1u2Left = new javax.swing.JButton();
        p1u2Right = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        p1u1Fire = new javax.swing.JButton();
        p1Toggle = new javax.swing.JButton();
        p1u2Fire = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        p2u1Right = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        p2u2Up = new javax.swing.JButton();
        p2u2Down = new javax.swing.JButton();
        p2u2Left = new javax.swing.JButton();
        p2u2Right = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        p2u1Fire = new javax.swing.JButton();
        p2Toggle = new javax.swing.JButton();
        p2u2Fire = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        p2u1Up = new javax.swing.JButton();
        p2u1Down = new javax.swing.JButton();
        p2u1Left = new javax.swing.JButton();
        p3u1Down = new javax.swing.JButton();
        p3u1Left = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        p4u2Up = new javax.swing.JButton();
        p4u2Down = new javax.swing.JButton();
        p4u2Left = new javax.swing.JButton();
        p4u2Right = new javax.swing.JButton();
        p3u1Right = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        p4u1Fire = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        p3u2Up = new javax.swing.JButton();
        p3u2Down = new javax.swing.JButton();
        p3u2Left = new javax.swing.JButton();
        p3u2Right = new javax.swing.JButton();
        p4Toggle = new javax.swing.JButton();
        p4u2Fire = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        p4u1Up = new javax.swing.JButton();
        p4u1Down = new javax.swing.JButton();
        p4u1Left = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        p3u1Fire = new javax.swing.JButton();
        p3Toggle = new javax.swing.JButton();
        p3u2Fire = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        p4u1Right = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        p3u1Up = new javax.swing.JButton();
        OptionsPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        HelpPanel = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();

        panel.setBackground(Color.black);
        panel.setMaximumSize(new java.awt.Dimension(800, 600));
        panel.setMinimumSize(new java.awt.Dimension(800, 600));
        panel.setOpaque(false);

        mainLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mainLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/mainLabel.png"))); // NOI18N

        WebsiteButton.setBackground(c);
        WebsiteButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        WebsiteButton.setForeground(Color.black);
        WebsiteButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        WebsiteButton.setText("NathanSoftware");
        WebsiteButton.setOpaque(true);
        WebsiteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WebsiteButtonMouseClicked(evt);
            }
        });

        PlayButton.setBackground(c);
        PlayButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        PlayButton.setForeground(Color.black);
        PlayButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PlayButton.setText("PLAY");
        PlayButton.setOpaque(true);
        PlayButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PlayButtonMouseClicked(evt);
            }
        });

        SettingsButton.setBackground(c);
        SettingsButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        SettingsButton.setForeground(Color.black);
        SettingsButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SettingsButton.setText("SETTINGS");
        SettingsButton.setOpaque(true);
        SettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SettingsButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PlayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SettingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(WebsiteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(PlayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SettingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(WebsiteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );

        PlayPanel.setOpaque(false);
        PlayPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        QuickGameButton.setBackground(c);
        QuickGameButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        QuickGameButton.setForeground(Color.black);
        QuickGameButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        QuickGameButton.setText("QUICK GAME");
        QuickGameButton.setOpaque(true);
        QuickGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QuickGameButtonMouseClicked(evt);
            }
        });

        CampaignButton.setBackground(c);
        CampaignButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        CampaignButton.setForeground(Color.black);
        CampaignButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CampaignButton.setText("CAMPAIGN");
        CampaignButton.setOpaque(true);
        CampaignButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CampaignButtonMouseClicked(evt);
            }
        });

        GameSwitchPanel.setBackground(c);
        GameSwitchPanel.setOpaque(false);

        HomeButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        HomeButton.setForeground(Color.black);
        HomeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/homeArrow-Selected.png"))); // NOI18N
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

        ScenarioGameButton.setBackground(c);
        ScenarioGameButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ScenarioGameButton.setForeground(Color.black);
        ScenarioGameButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ScenarioGameButton.setText("SCENARIO");
        ScenarioGameButton.setOpaque(true);
        ScenarioGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ScenarioGameButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PlayPanelLayout = new javax.swing.GroupLayout(PlayPanel);
        PlayPanel.setLayout(PlayPanelLayout);
        PlayPanelLayout.setHorizontalGroup(
            PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayPanelLayout.createSequentialGroup()
                .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayPanelLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(QuickGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(CampaignButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ScenarioGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlayPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(HomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(96, 96, 96)
                .addComponent(GameSwitchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlayPanelLayout.setVerticalGroup(
            PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GameSwitchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PlayPanelLayout.createSequentialGroup()
                        .addComponent(HomeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86)
                        .addComponent(QuickGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ScenarioGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CampaignButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PlayMenuLayout = new javax.swing.GroupLayout(PlayMenu.getContentPane());
        PlayMenu.getContentPane().setLayout(PlayMenuLayout);
        PlayMenuLayout.setHorizontalGroup(
            PlayMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
            .addGroup(PlayMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PlayMenuLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        PlayMenuLayout.setVerticalGroup(
            PlayMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(PlayMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PlayMenuLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        CampaignPanel.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CAMPAIGN");

        jButton2.setText("PLAY");

        javax.swing.GroupLayout CampaignPanelLayout = new javax.swing.GroupLayout(CampaignPanel);
        CampaignPanel.setLayout(CampaignPanelLayout);
        CampaignPanelLayout.setHorizontalGroup(
            CampaignPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CampaignPanelLayout.createSequentialGroup()
                .addContainerGap(353, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(56, 56, 56))
            .addGroup(CampaignPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(CampaignImage, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CampaignPanelLayout.setVerticalGroup(
            CampaignPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CampaignPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CampaignImage, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(23, 23, 23))
        );

        ScenarioPanel.setOpaque(false);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SCENARIO");

        ScenarioComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel13.setText("Choose Map:");

        MapPicture1.setBackground(c);
        MapPicture1.setOpaque(true);

        ScenarioPlayButton.setText("PLAY");
        ScenarioPlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ScenarioPlayButtonActionPerformed(evt);
            }
        });

        ScenarioErrorLabel.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        ScenarioErrorLabel.setForeground(new java.awt.Color(255, 0, 0));
        ScenarioErrorLabel.setText(" ");

        numberOfPlayers1.setBackground(new java.awt.Color(0, 0, 0));
        numberOfPlayers1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        numberOfPlayers1.setForeground(new java.awt.Color(0, 0, 0));
        numberOfPlayers1.setMaximum(4);
        numberOfPlayers1.setMinimum(1);
        numberOfPlayers1.setMinorTickSpacing(1);
        numberOfPlayers1.setPaintLabels(true);
        numberOfPlayers1.setPaintTicks(true);
        numberOfPlayers1.setSnapToTicks(true);
        numberOfPlayers1.setFocusable(false);
        numberOfPlayers1.setName(""); // NOI18N
        numberOfPlayers1.setOpaque(false);
        numberOfPlayers1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numberOfPlayers1StateChanged(evt);
            }
        });

        numPlayersLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        numPlayersLabel1.setText("NUMBER OF PLAYERS: 4");

        javax.swing.GroupLayout ScenarioPanelLayout = new javax.swing.GroupLayout(ScenarioPanel);
        ScenarioPanel.setLayout(ScenarioPanelLayout);
        ScenarioPanelLayout.setHorizontalGroup(
            ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScenarioPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ScenarioPlayButton)
                .addGap(56, 56, 56))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScenarioPanelLayout.createSequentialGroup()
                .addGroup(ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ScenarioPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PlayerTabbedPanel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ScenarioPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ScenarioPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ScenarioPanelLayout.createSequentialGroup()
                                .addGroup(ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ScenarioComboBox, 0, 185, Short.MAX_VALUE)
                                    .addComponent(numberOfPlayers1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(44, 44, 44))
                            .addGroup(ScenarioPanelLayout.createSequentialGroup()
                                .addComponent(numPlayersLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(MapPicture1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(ScenarioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScenarioErrorLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ScenarioPanelLayout.setVerticalGroup(
            ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScenarioPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ScenarioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ScenarioPanelLayout.createSequentialGroup()
                        .addComponent(ScenarioComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numPlayersLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numberOfPlayers1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(MapPicture1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ScenarioErrorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayerTabbedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ScenarioPlayButton)
                .addGap(23, 23, 23))
        );

        QuickPanel.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUICK GAME");

        MapComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MapComboBoxItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setText("Choose Map:");

        MapPicture.setBackground(c);
        MapPicture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MapPicture.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        MapPicture.setOpaque(true);

        QuickGamePlayButton.setText("PLAY");
        QuickGamePlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuickGamePlayButtonActionPerformed(evt);
            }
        });

        QuickGameErrorLabel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        QuickGameErrorLabel.setForeground(new java.awt.Color(255, 0, 0));
        QuickGameErrorLabel.setText(" ");

        numberOfPlayers.setBackground(new java.awt.Color(0, 0, 0));
        numberOfPlayers.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        numberOfPlayers.setForeground(new java.awt.Color(0, 0, 0));
        numberOfPlayers.setMaximum(4);
        numberOfPlayers.setMinimum(1);
        numberOfPlayers.setMinorTickSpacing(1);
        numberOfPlayers.setPaintLabels(true);
        numberOfPlayers.setPaintTicks(true);
        numberOfPlayers.setSnapToTicks(true);
        numberOfPlayers.setFocusable(false);
        numberOfPlayers.setName(""); // NOI18N
        numberOfPlayers.setOpaque(false);
        numberOfPlayers.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numberOfPlayersStateChanged(evt);
            }
        });

        numPlayersLabel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        numPlayersLabel.setText("NUMBER OF PLAYERS: 4");

        customBoardField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customBoardField.setText("7,7");

        customBoardLabel.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        customBoardLabel.setText("Size:");

        javax.swing.GroupLayout QuickPanelLayout = new javax.swing.GroupLayout(QuickPanel);
        QuickPanel.setLayout(QuickPanelLayout);
        QuickPanelLayout.setHorizontalGroup(
            QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuickPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(QuickGamePlayButton)
                .addGap(56, 56, 56))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuickPanelLayout.createSequentialGroup()
                .addGroup(QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(QuickPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(PlayerTabbedPanel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, QuickPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, QuickPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(QuickPanelLayout.createSequentialGroup()
                                .addComponent(numPlayersLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, QuickPanelLayout.createSequentialGroup()
                                .addGroup(QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(MapComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 191, Short.MAX_VALUE)
                                    .addComponent(numberOfPlayers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(QuickPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(customBoardLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(customBoardField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(44, 44, 44)))
                        .addComponent(MapPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(QuickPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(QuickGameErrorLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        QuickPanelLayout.setVerticalGroup(
            QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuickPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(QuickPanelLayout.createSequentialGroup()
                        .addComponent(MapComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(QuickPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(customBoardField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customBoardLabel))
                        .addGap(18, 18, 18)
                        .addComponent(numPlayersLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numberOfPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(MapPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(QuickGameErrorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayerTabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(QuickGamePlayButton)
                .addGap(30, 30, 30))
        );

        jLabel3.setText("jLabel3");

        player4Panel.setOpaque(false);
        player4Panel.setPreferredSize(new java.awt.Dimension(400, 330));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("PLAYER 4");

        player4AI.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player4AI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player4AI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/toggle-off.png"))); // NOI18N
        player4AI.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        p4Image.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        p4Image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p4ImageMouseClicked(evt);
            }
        });

        player4Combo.setEditable(true);

        player4Level.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        player4Level.setForeground(new java.awt.Color(255, 255, 255));
        player4Level.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/gameIcon.png"))); // NOI18N
        player4Level.setText("1");
        player4Level.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel7.setText("COMPUTER AI:");

        javax.swing.GroupLayout player4PanelLayout = new javax.swing.GroupLayout(player4Panel);
        player4Panel.setLayout(player4PanelLayout);
        player4PanelLayout.setHorizontalGroup(
            player4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player4PanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(player4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player4PanelLayout.createSequentialGroup()
                        .addComponent(p4Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player4Combo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(player4Level, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(player4PanelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(6, 6, 6)
                        .addComponent(player4AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        player4PanelLayout.setVerticalGroup(
            player4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, player4PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addGroup(player4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player4Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p4Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player4Level, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(player4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player4PanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel7))
                    .addGroup(player4PanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(player4AI, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(92, Short.MAX_VALUE))
        );

        player3Panel.setOpaque(false);
        player3Panel.setPreferredSize(new java.awt.Dimension(400, 330));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("PLAYER 3");

        player3AI.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player3AI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player3AI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/toggle-off.png"))); // NOI18N

        p3Image.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        p3Image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p3ImageMouseClicked(evt);
            }
        });

        player3Level.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        player3Level.setForeground(new java.awt.Color(255, 255, 255));
        player3Level.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/gameIcon.png"))); // NOI18N
        player3Level.setText("1");
        player3Level.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        player3Combo.setEditable(true);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel9.setText("COMPUTER AI:");

        javax.swing.GroupLayout player3PanelLayout = new javax.swing.GroupLayout(player3Panel);
        player3Panel.setLayout(player3PanelLayout);
        player3PanelLayout.setHorizontalGroup(
            player3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player3PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(player3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player3PanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(6, 6, 6)
                        .addComponent(player3AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(player3PanelLayout.createSequentialGroup()
                        .addComponent(p3Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player3Combo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(player3Level, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        player3PanelLayout.setVerticalGroup(
            player3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player3PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(player3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player3Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p3Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player3Level, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(player3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player3PanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(player3AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(player3PanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel9)))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        player2Panel.setOpaque(false);
        player2Panel.setPreferredSize(new java.awt.Dimension(400, 330));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("PLAYER 2");

        player2AI.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player2AI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player2AI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/toggle-off.png"))); // NOI18N

        p2Image.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        p2Image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p2ImageMouseClicked(evt);
            }
        });

        player2Level.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        player2Level.setForeground(new java.awt.Color(255, 255, 255));
        player2Level.setText("1");
        player2Level.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        player2Combo.setEditable(true);

        jLabel10.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel10.setText("COMPUTER AI:");

        javax.swing.GroupLayout player2PanelLayout = new javax.swing.GroupLayout(player2Panel);
        player2Panel.setLayout(player2PanelLayout);
        player2PanelLayout.setHorizontalGroup(
            player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player2PanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player2PanelLayout.createSequentialGroup()
                        .addComponent(p2Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player2Combo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(player2Level, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, Short.MAX_VALUE))
                    .addGroup(player2PanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player2AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        player2PanelLayout.setVerticalGroup(
            player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player2PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p2Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player2Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player2Level, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player2AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(player2PanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel10)))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        player1Panel.setOpaque(false);
        player1Panel.setPreferredSize(new java.awt.Dimension(400, 330));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("PLAYER 1");

        player1AI.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player1AI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player1AI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/toggle-off.png"))); // NOI18N

        p1Image.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        p1Image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p1ImageMouseClicked(evt);
            }
        });

        player1Level.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        player1Level.setForeground(new java.awt.Color(255, 255, 255));
        player1Level.setText("1");
        player1Level.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        player1Combo.setEditable(true);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel12.setText("COMPUTER AI:");

        javax.swing.GroupLayout player1PanelLayout = new javax.swing.GroupLayout(player1Panel);
        player1Panel.setLayout(player1PanelLayout);
        player1PanelLayout.setHorizontalGroup(
            player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, player1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player1PanelLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player1AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(player1PanelLayout.createSequentialGroup()
                        .addComponent(p1Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(player1Combo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(player1Level, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        player1PanelLayout.setVerticalGroup(
            player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p1Image, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player1Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(player1Level, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(player1PanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(player1AI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(player1PanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel12)))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        SettingsPanel.setOpaque(false);

        HomeButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        HomeButton1.setForeground(Color.black);
        HomeButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/homeArrow-Selected.png"))); // NOI18N
        HomeButton1.setText("HOME");
        HomeButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HomeButton1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                HomeButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HomeButton1MouseExited(evt);
            }
        });

        ControlsButton.setBackground(c);
        ControlsButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ControlsButton.setForeground(Color.black);
        ControlsButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ControlsButton.setText("CONTROLS");
        ControlsButton.setOpaque(true);
        ControlsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ControlsButtonMouseClicked(evt);
            }
        });

        OptionsButton.setBackground(c);
        OptionsButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        OptionsButton.setForeground(Color.black);
        OptionsButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        OptionsButton.setText("OPTIONS");
        OptionsButton.setOpaque(true);
        OptionsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OptionsButtonMouseClicked(evt);
            }
        });

        HelpButton.setBackground(c);
        HelpButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        HelpButton.setForeground(Color.black);
        HelpButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HelpButton.setText("HELP");
        HelpButton.setOpaque(true);
        HelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HelpButtonMouseClicked(evt);
            }
        });

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setOpaque(false);

        SettingsSwitchPanel.setOpaque(false);
        jScrollPane1.setViewportView(SettingsSwitchPanel);

        javax.swing.GroupLayout SettingsPanelLayout = new javax.swing.GroupLayout(SettingsPanel);
        SettingsPanel.setLayout(SettingsPanelLayout);
        SettingsPanelLayout.setHorizontalGroup(
            SettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsPanelLayout.createSequentialGroup()
                .addGroup(SettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SettingsPanelLayout.createSequentialGroup()
                        .addGroup(SettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SettingsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(HomeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(SettingsPanelLayout.createSequentialGroup()
                                .addGap(188, 188, 188)
                                .addComponent(ControlsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(OptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(HelpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 189, Short.MAX_VALUE))
                    .addGroup(SettingsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SettingsPanelLayout.setVerticalGroup(
            SettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(HomeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(SettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ControlsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HelpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SettingsMenuLayout = new javax.swing.GroupLayout(SettingsMenu.getContentPane());
        SettingsMenu.getContentPane().setLayout(SettingsMenuLayout);
        SettingsMenuLayout.setHorizontalGroup(
            SettingsMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsMenuLayout.createSequentialGroup()
                .addComponent(SettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        SettingsMenuLayout.setVerticalGroup(
            SettingsMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setText("PLAYER 1 CONTROLS");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel16.setText("Move Up");

        jLabel17.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel17.setText("Move Down");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel18.setText("Move Left");

        jLabel19.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel19.setText("Move Right");

        p1u1Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u1Up.setText("X");
        p1u1Up.setToolTipText("");

        p1u1Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u1Down.setText("X");
        p1u1Down.setToolTipText("");

        p1u1Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u1Left.setText("X");
        p1u1Left.setToolTipText("");

        p1u1Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u1Right.setText("X");
        p1u1Right.setToolTipText("");

        jLabel22.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel22.setText("Fire Weapon");

        jLabel23.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel23.setText("Toggle");

        jLabel24.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel24.setText("Move Left");

        jLabel25.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel25.setText("Move Right");

        jLabel26.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel26.setText("Fire Weapon");

        p1u2Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u2Up.setText("X");

        p1u2Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u2Down.setText("X");

        p1u2Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u2Left.setText("X");

        p1u2Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u2Right.setText("X");

        jLabel27.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel27.setText("Move Up");

        jLabel28.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel28.setText("Move Down");

        p1u1Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u1Fire.setText("X");
        p1u1Fire.setToolTipText("");

        p1Toggle.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1Toggle.setText("X");
        p1Toggle.setToolTipText("");

        p1u2Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p1u2Fire.setText("X");

        jLabel29.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("UNIT 1");

        jLabel30.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("UNIT 2");

        p2u1Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u1Right.setText("X");

        jLabel31.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel31.setText("Fire Weapon");

        jLabel32.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel32.setText("Toggle");

        jLabel33.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel33.setText("Move Left");

        jLabel34.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel34.setText("Move Right");

        jLabel35.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel35.setText("Fire Weapon");

        p2u2Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u2Up.setText("X");

        p2u2Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u2Down.setText("X");

        p2u2Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u2Left.setText("X");

        p2u2Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u2Right.setText("X");

        jLabel36.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel36.setText("Move Up");

        jLabel37.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel37.setText("Move Down");

        p2u1Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u1Fire.setText("X");

        p2Toggle.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2Toggle.setText("X");

        p2u2Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u2Fire.setText("X");

        jLabel38.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("UNIT 1");

        jLabel39.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("UNIT 2");

        jLabel40.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel40.setText("PLAYER 2 CONTROLS");

        jLabel41.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel41.setText("Move Up");

        jLabel42.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel42.setText("Move Down");

        jLabel43.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel43.setText("Move Left");

        jLabel44.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel44.setText("Move Right");

        p2u1Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u1Up.setText("X");

        p2u1Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u1Down.setText("X");

        p2u1Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p2u1Left.setText("X");

        p3u1Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u1Down.setText("X");

        p3u1Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u1Left.setText("X");

        jLabel45.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel45.setText("Move Left");

        jLabel46.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel46.setText("Move Right");

        jLabel47.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel47.setText("Fire Weapon");

        p4u2Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u2Up.setText("X");

        p4u2Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u2Down.setText("X");

        p4u2Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u2Left.setText("X");

        p4u2Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u2Right.setText("X");

        p3u1Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u1Right.setText("X");

        jLabel48.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel48.setText("Move Up");

        jLabel49.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel49.setText("Fire Weapon");

        jLabel50.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel50.setText("Move Down");

        jLabel51.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel51.setText("Toggle");

        p4u1Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u1Fire.setText("X");

        jLabel52.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel52.setText("Move Left");

        jLabel53.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel53.setText("Move Right");

        jLabel54.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel54.setText("Fire Weapon");

        p3u2Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u2Up.setText("X");

        p3u2Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u2Down.setText("X");

        p3u2Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u2Left.setText("X");

        p3u2Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u2Right.setText("X");

        p4Toggle.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4Toggle.setText("X");

        p4u2Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u2Fire.setText("X");

        jLabel55.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("UNIT 1");

        jLabel56.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("UNIT 2");

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setText("PLAYER 4 CONTROLS");

        jLabel58.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel58.setText("Move Up");

        jLabel59.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel59.setText("Move Down");

        jLabel60.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel60.setText("Move Left");

        jLabel61.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel61.setText("Move Right");

        p4u1Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u1Up.setText("X");

        p4u1Down.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u1Down.setText("X");

        p4u1Left.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u1Left.setText("X");

        jLabel62.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel62.setText("Move Up");

        jLabel63.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel63.setText("Move Down");

        p3u1Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u1Fire.setText("X");

        p3Toggle.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3Toggle.setText("X");

        p3u2Fire.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u2Fire.setText("X");

        jLabel64.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("UNIT 1");

        jLabel65.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("UNIT 2");

        p4u1Right.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p4u1Right.setText("X");

        jLabel66.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel66.setText("Fire Weapon");

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setText("PLAYER 3 CONTROLS");

        jLabel68.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel68.setText("Toggle");

        jLabel69.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel69.setText("Move Up");

        jLabel70.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel70.setText("Move Down");

        jLabel71.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel71.setText("Move Left");

        jLabel72.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel72.setText("Move Right");

        p3u1Up.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        p3u1Up.setText("X");

        javax.swing.GroupLayout ControlsPanelLayout = new javax.swing.GroupLayout(ControlsPanel);
        ControlsPanel.setLayout(ControlsPanelLayout);
        ControlsPanelLayout.setHorizontalGroup(
            ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlsPanelLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ControlsPanelLayout.createSequentialGroup()
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel58)
                                            .addComponent(jLabel59)
                                            .addComponent(jLabel60)
                                            .addComponent(jLabel61)
                                            .addComponent(jLabel66)
                                            .addComponent(jLabel68))
                                        .addGap(59, 59, 59)
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(p4u1Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p4u1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p4u1Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p4u1Fire, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p4Toggle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p4u1Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jLabel50)
                                    .addComponent(jLabel45)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel47))
                                .addGap(59, 59, 59)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(p4u2Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p4u2Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p4u2Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p4u2Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p4u2Fire, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ControlsPanelLayout.createSequentialGroup()
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel69)
                                            .addComponent(jLabel70)
                                            .addComponent(jLabel71)
                                            .addComponent(jLabel72)
                                            .addComponent(jLabel49)
                                            .addComponent(jLabel51))
                                        .addGap(59, 59, 59)
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(p3u1Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3u1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3u1Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3u1Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3Toggle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p3u1Fire, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62)
                                    .addComponent(jLabel63)
                                    .addComponent(jLabel52)
                                    .addComponent(jLabel53)
                                    .addComponent(jLabel54))
                                .addGap(59, 59, 59)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(p3u2Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p3u2Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p3u2Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p3u2Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p3u2Fire, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ControlsPanelLayout.createSequentialGroup()
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel41)
                                            .addComponent(jLabel42)
                                            .addComponent(jLabel43)
                                            .addComponent(jLabel44)
                                            .addComponent(jLabel31)
                                            .addComponent(jLabel32))
                                        .addGap(59, 59, 59)
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(p2u1Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2u1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2u1Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2u1Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2u1Fire, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p2Toggle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel35))
                                .addGap(59, 59, 59)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(p2u2Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2u2Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2u2Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2u2Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2u2Fire, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ControlsPanelLayout.createSequentialGroup()
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel17)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel22)
                                            .addComponent(jLabel23))
                                        .addGap(59, 59, 59)
                                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(p1u1Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1u1Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1u1Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1u1Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1u1Fire, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p1Toggle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ControlsPanelLayout.createSequentialGroup()
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26))
                                .addGap(59, 59, 59)
                                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(p1u2Right, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p1u2Left, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p1u2Down, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p1u2Up, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p1u2Fire, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        ControlsPanelLayout.setVerticalGroup(
            ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlsPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addGap(13, 13, 13)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(p1u1Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(p1u1Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(p1u1Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(p1u1Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(p1u1Fire)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(p1u2Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(p1u2Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(p1u2Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(p1u2Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(p1u2Fire))))
                .addGap(21, 21, 21)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(p1Toggle))
                .addGap(34, 34, 34)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39))
                .addGap(13, 13, 13)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(p2u1Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(p2u1Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(p2u1Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(p2u1Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(p2u1Fire)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(p2u2Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(p2u2Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(p2u2Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(p2u2Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(p2u2Fire))))
                .addGap(21, 21, 21)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(p2Toggle))
                .addGap(35, 35, 35)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65))
                .addGap(13, 13, 13)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel69)
                            .addComponent(p3u1Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel70)
                            .addComponent(p3u1Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel71)
                            .addComponent(p3u1Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(p3u1Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(p3u1Fire)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel62)
                            .addComponent(p3u2Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel63)
                            .addComponent(p3u2Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel52)
                            .addComponent(p3u2Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(p3u2Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(p3u2Fire))))
                .addGap(21, 21, 21)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(p3Toggle))
                .addGap(34, 34, 34)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56))
                .addGap(13, 13, 13)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(p4u1Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel59)
                            .addComponent(p4u1Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel60)
                            .addComponent(p4u1Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel61)
                            .addComponent(p4u1Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel66)
                            .addComponent(p4u1Fire)))
                    .addGroup(ControlsPanelLayout.createSequentialGroup()
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(p4u2Up))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(p4u2Down))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(p4u2Left))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel46)
                            .addComponent(p4u2Right))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(p4u2Fire))))
                .addGap(21, 21, 21)
                .addGroup(ControlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(p4Toggle))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        OptionsPanel.setPreferredSize(new java.awt.Dimension(700, 500));

        jButton3.setText("jButton3");

        javax.swing.GroupLayout OptionsPanelLayout = new javax.swing.GroupLayout(OptionsPanel);
        OptionsPanel.setLayout(OptionsPanelLayout);
        OptionsPanelLayout.setHorizontalGroup(
            OptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionsPanelLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jButton3)
                .addContainerGap(540, Short.MAX_VALUE))
        );
        OptionsPanelLayout.setVerticalGroup(
            OptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionsPanelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton3)
                .addContainerGap(434, Short.MAX_VALUE))
        );

        jButton4.setText("jButton4");

        javax.swing.GroupLayout HelpPanelLayout = new javax.swing.GroupLayout(HelpPanel);
        HelpPanel.setLayout(HelpPanelLayout);
        HelpPanelLayout.setHorizontalGroup(
            HelpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HelpPanelLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jButton4)
                .addContainerGap(519, Short.MAX_VALUE))
        );
        HelpPanelLayout.setVerticalGroup(
            HelpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HelpPanelLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jButton4)
                .addContainerGap(408, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void WebsiteButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WebsiteButtonMouseClicked
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/"); //assign the URI location
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain); // launch the URI
    }//GEN-LAST:event_WebsiteButtonMouseClicked

    private void PlayButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PlayButtonMouseClicked
        
        loadWindow(PlayMenu, PlayPanel, "/resources/menu/p-panel.png", true, null);
        
    }//GEN-LAST:event_PlayButtonMouseClicked

    private void SettingsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SettingsButtonMouseClicked

        loadWindow(SettingsMenu, SettingsPanel, null, true, settingsBackground);
        
    }//GEN-LAST:event_SettingsButtonMouseClicked

    private void HomeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseClicked
        // unselect foreground, unselect icon
        HomeButton.setForeground(Color.black);
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");
        
        // kill frame
        PlayMenu.dispose();
        
        this.setVisible(true);
        
        GameSwitchPanel.removeAll();
        
        GameSwitchPanel.revalidate();
        GameSwitchPanel.repaint();
        
        PlayMenu.revalidate();
        PlayMenu.repaint();
    }//GEN-LAST:event_HomeButtonMouseClicked

    private void QuickGameButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuickGameButtonMouseClicked
        System.out.println("QUICK GAME");
                
        GameSwitchPanel.removeAll();
        GameSwitchPanel.add(QuickPanel);
        
        GameSwitchPanel.revalidate();
        GameSwitchPanel.repaint();
        
        PlayMenu.revalidate();
        PlayMenu.repaint();
    }//GEN-LAST:event_QuickGameButtonMouseClicked

    private void CampaignButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CampaignButtonMouseClicked
        System.out.println("CAMPAIGN GAME");
        
        GameSwitchPanel.removeAll();
        GameSwitchPanel.add(CampaignPanel);
        
        GameSwitchPanel.revalidate();
        GameSwitchPanel.repaint();
        
        PlayMenu.revalidate();
        PlayMenu.repaint();
        
    }//GEN-LAST:event_CampaignButtonMouseClicked

    private void HomeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseEntered
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow-Selected.png");

            //HomeButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/homeArrow-Selected.png")));
    }//GEN-LAST:event_HomeButtonMouseEntered

    private void HomeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButtonMouseExited
        ImageManipulator.resizeImage(HomeButton, 60, 60, "resources/menu/homeArrow.png");

        //HomeButton.setIcon(new ImageIcon(getClass().getResource("/resources/menu/homeArrow.png")));
    }//GEN-LAST:event_HomeButtonMouseExited

    private void p4ImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p4ImageMouseClicked
        cycleColor(3);
        updateColor(3, p4Image);
    }//GEN-LAST:event_p4ImageMouseClicked

    private void QuickGamePlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuickGamePlayButtonActionPerformed
        // LAUNCH A QUICK GAME
        
        int humanPlayers = 0;
        int validPlayers = Takeover.numberOfPlayers;
        
        // create a set of the player names
        Set<String> a = new HashSet<>();
                
        // then count how many are humanPlayers (HUMAN = if it is a human player, not AI and enabled)
        // FOR EACH ENABLED PLAYER
        // NOTE: less than sign because numPlayer is 1-4 
        for (int i = 0; i < Takeover.numberOfPlayers; i++) {
            
            // IF THEY ARE NOT AI
            if (!Takeover.playerAI[i]) {

                // ADD THEM AS VALID TO THE SET
                a.add(playerCombo[i].getSelectedItem().toString());

                // store that there is a unique player
                humanPlayers ++;
            }
        }        
        
        // MUST HAVE AT LEAST 2 VALID PLAYERS
        if (validPlayers >= 2) {
            // then check if the hashset contains the number of human players 
            // (duplicates will be removed so if there are duplicates it will not be correct)
            
            if (a.size() != humanPlayers) { // IF NOT VALID then two human players have same name
                // ERROR DUPLICATE PLAYERS
                QuickGameErrorLabel.setText("EVERY PLAYER MUST HAVE A DIFFERENT NAME");
            } else { // ELSE IF VALID
                QuickGameErrorLabel.setText(" "); // a single space is used to preserve the margin (if empty the label will flatten)

                createNewPlayers();
                
                
                /// LAUNCH GAME - TODO
                fillHashMap(); // setup the control hashmap with the correct data

                String ourMap = (String)MapComboBox.getSelectedItem();
                
                // we will convert our board into a intger 2d matrix repersenting board squares
                int[][] gameBoard = interpretBoard(ourMap);
                
                // IF VALID BOARD
                if (gameBoard != null) {
                    
                    // ** System.out.println("GAME BOARD: " + gameBoard);
                    
                    // setup the players array as default (from Takeover Settings)
                    PlayerObject[] gamePlayers = PlayerObject.setupDefaultPlayers();

                    TheGame g = new TheGame(gameBoard, gamePlayers);

                    // SETUP KEYLISTENERS
                    Takeover.inputer.addGameInputListeners(g, true); // NOTE: "true" means they are active and do stuff when hit

                }
                ///
            }
        } else {
            QuickGameErrorLabel.setText("MUST HAVE AT LEAST 2 PLAYERS");
        }
        
        PlayMenu.repaint();
        
        Takeover.save();
        
    }//GEN-LAST:event_QuickGamePlayButtonActionPerformed

    private void numberOfPlayersStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numberOfPlayersStateChanged
        // WHEN SLIDER CHANGES - UPDATE TABBED MENU
        if (!numberOfPlayers.getValueIsAdjusting()) { // so it only updates once per click

            Takeover.numPlayersSet(numberOfPlayers.getValue());
            
            updateNumberOfPlayers();
        }
        
    }//GEN-LAST:event_numberOfPlayersStateChanged

    public void updateNumberOfPlayers() {
        numPlayersLabel.setText("NUMBER OF PLAYERS: " + Takeover.numberOfPlayers);

        System.out.println("NP : " + Takeover.numberOfPlayers);

        // for all the players set the enabled/disabled accordingly
        for (int i = 0; i < 4; i ++) {
            // if this is of the players that is enabled then set it up
            if (i < Takeover.numberOfPlayers) {
                PlayerTabbedPanel.setEnabledAt(i, true);
            } else {

                PlayerTabbedPanel.setEnabledAt(i, false);
            }
        }

        // IF YOU ARE ON A DISABLED PLAYER RESET
        // Note: since numberOfPlayers is 1-4 and Index is 0-3 we have a -1
        if (PlayerTabbedPanel.getSelectedIndex() > Takeover.numberOfPlayers - 1) {
            PlayerTabbedPanel.setSelectedIndex(0);
        }

        PlayMenu.repaint();
    }
    
    // METHOD TO LIMIT THE NUMBER OF PLAYERS
    public void setNumPlayersCap(int cap) {
        // certain maps can have more or less players
        
        // we get the cap and modify the bar
        numberOfPlayers.setMaximum(cap);
        numberOfPlayers.setValue(cap);
        
        Takeover.numPlayersSet(cap);
        
        updateNumberOfPlayers();
        
    }
    
    
    private void p3ImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p3ImageMouseClicked
        cycleColor(2);
        updateColor(2, p3Image);
    }//GEN-LAST:event_p3ImageMouseClicked

    private void p1ImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p1ImageMouseClicked
        cycleColor(0);
        updateColor(0, p1Image);
    }//GEN-LAST:event_p1ImageMouseClicked

    private void p2ImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p2ImageMouseClicked
        cycleColor(1);
        updateColor(1, p2Image);
    }//GEN-LAST:event_p2ImageMouseClicked

    private void ScenarioGameButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScenarioGameButtonMouseClicked
        System.out.println("SCENARIO GAME");
        
        GameSwitchPanel.removeAll();
        GameSwitchPanel.add(ScenarioPanel);
        
        GameSwitchPanel.revalidate();
        GameSwitchPanel.repaint();
        
        PlayMenu.revalidate();
        PlayMenu.repaint();
    }//GEN-LAST:event_ScenarioGameButtonMouseClicked

    private void ScenarioPlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ScenarioPlayButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ScenarioPlayButtonActionPerformed

    private void numberOfPlayers1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numberOfPlayers1StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_numberOfPlayers1StateChanged

    private void MapComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MapComboBoxItemStateChanged
        // WHEN YOU CLICK ON THE COMBO BOX
        // figure out what has been checked
        String map = (String)MapComboBox.getSelectedItem();
        
        // figure out what map it is
        switch (map) {
            // FOR RANDOM MAP
            case "random":
                Random generator = new Random();
                // TODO RANDOMLY PICK MAP
                                
                customBoardLabel.setVisible(false);
                customBoardField.setVisible(false);
                
                MapPicture.setIcon(new ImageIcon(getClass().getResource("/resources/menu/random-map.png")));
                
                break;
            
            // FOR CUSTOM MAPS
            case "custom":
                // reset player cap
                setNumPlayersCap(4);
                // SHOW THE OPTION TO DO A CUSTOM BOARD
                customBoardLabel.setVisible(true);
                customBoardField.setVisible(true);
                
                MapPicture.setIcon(new ImageIcon(getClass().getResource("/resources/menu/random-map.png")));
                break;
                
            // FOR ALL DEFAULT MAPS JUST LOAD THEIR MAP OBJECT   
            default:
        
                // WHEN THEY SELECT A MAP WE WANT TO READ IT
                // setup map image
                try {
                    MapPicture.setIcon(mapTable.get(map).MapPicture);
                } catch (Exception e) {
                    // just in case of an image error we ignore it
                }

                setNumPlayersCap(mapTable.get(map).numberOfPlayers); // SET NUMBER OF PLAYERS CORRECTLY

                customBoardLabel.setVisible(false);
                customBoardField.setVisible(false);
                break;
        }
        
        
        PlayMenu.repaint();
    }//GEN-LAST:event_MapComboBoxItemStateChanged

    private void HelpButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HelpButtonMouseClicked
        System.out.println("HELP SETTINGS");

        SettingsSwitchPanel.removeAll();
        SettingsSwitchPanel.add(HelpPanel);

        SettingsSwitchPanel.revalidate();
        SettingsSwitchPanel.repaint();

        SettingsMenu.revalidate();
        SettingsMenu.repaint();
    }//GEN-LAST:event_HelpButtonMouseClicked

    private void OptionsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OptionsButtonMouseClicked
        System.out.println("OPTIONS SETTINGS");

        SettingsSwitchPanel.removeAll();
        SettingsSwitchPanel.add(OptionsPanel);

        SettingsSwitchPanel.revalidate();
        SettingsSwitchPanel.repaint();

        SettingsMenu.revalidate();
        SettingsMenu.repaint();
    }//GEN-LAST:event_OptionsButtonMouseClicked

    private void ControlsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ControlsButtonMouseClicked
        System.out.println("CONTROLS SETTINGS");

        SettingsSwitchPanel.removeAll();
        SettingsSwitchPanel.add(ControlsPanel);
        
        // display the controls from the index into our button array
        Takeover.inputer.displayControls(Takeover.controlsIndex, controlsArray);

        SettingsSwitchPanel.revalidate();
        SettingsSwitchPanel.repaint();

        SettingsMenu.revalidate();
        SettingsMenu.repaint();
    }//GEN-LAST:event_ControlsButtonMouseClicked

    private void HomeButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButton1MouseExited
        ImageManipulator.resizeImage(HomeButton1, 60, 60, "resources/menu/homeArrow.png");
    }//GEN-LAST:event_HomeButton1MouseExited

    private void HomeButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButton1MouseEntered
        ImageManipulator.resizeImage(HomeButton1, 60, 60, "resources/menu/homeArrow-Selected.png");
    }//GEN-LAST:event_HomeButton1MouseEntered

    private void HomeButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeButton1MouseClicked
        // unselect foreground, unselect icon
        HomeButton1.setForeground(Color.black);
        ImageManipulator.resizeImage(HomeButton1, 60, 60, "resources/menu/homeArrow.png");

        // kill frame
        SettingsMenu.dispose();

        // return home menu
        this.setVisible(true);
        
        // SAVE CONTROLS
        InputController.saveControlIndex(Takeover.controlsIndex);

        SettingsSwitchPanel.removeAll();

        SettingsSwitchPanel.revalidate();
        SettingsSwitchPanel.repaint();

        SettingsMenu.revalidate();
        SettingsMenu.repaint();
    }//GEN-LAST:event_HomeButton1MouseClicked
    
    
    
    // when you launch a game we will search the drop down boxes and figure out whose playing. 
    // All the new players will be created as new .player files
    public void createNewPlayers() {
        // if a "new player" choice has been selected
        // and the player is being used
        
        // for each player up to the TOTAL: if it is selected on the "new player" item (item 0) or something they type (item -1) AND NOT the AI
        // then save that new player
        for (int box = 0; box < (Takeover.numberOfPlayers -1); box++) {
            if ((playerCombo[box].getSelectedIndex() == 0 | playerCombo[box].getSelectedIndex() == -1) && !playerCombo[box].equals("AI Player")) {
                savePlayer(playerCombo[box].getSelectedItem().toString());
            }
        }
    }
    
    public void savePlayer(String name) {
        System.out.println("Saving players:");
        
        PrintWriter creator = null;
        try {
            // create directory if it doesn't exist
            File f = new File("players/");
            f.mkdirs();

            // create file if it doesn't exist
            creator = new PrintWriter("players/" + name + ".player", "UTF-8");
            creator.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println("ERROR CANT CREATE PLAYER FILE, access denied");
        }

        // try opening a file writer
        try (BufferedWriter write = new BufferedWriter(new FileWriter("players/" + name + ".player"))) {

            write.write("1\r\n"); // WRITE LEVEL "1" & new line "/r/n"
            write.write("0"); // WRITE EXPERIENCE "0"
            write.close();
        } catch (IOException ex) {
            // error can't write to the save file
            System.out.println("ERROR CANT WRITE TO PLAYER FILE, access denied");
        }
    }
    
    public void cycleColor(int playerNumber) {
        // toggle through the color of the player
        Color color = Takeover.playerColor[playerNumber];
        if (color.equals(Takeover.red)) {
                    color = Takeover.green;
            
        } else if (color.equals(Takeover.green)) {
                    color = Takeover.blue;
            
        } else if (color.equals(Takeover.blue)) {
                    color = Takeover.yellow;
            
        } else if (color.equals(Takeover.yellow)) {
                    color = Takeover.red;
            
        }
        
        Takeover.playerSetColor(playerNumber, color);
       
    } 
    
    public void updateColor(int playerNumber, JLabel label) {
        // find out the player color and update it
        Color color = Takeover.playerColor[playerNumber];
        
        if (color.equals(Takeover.green)) {
                    label.setIcon(new ImageIcon(greenIcon));
            
        } else if (color.equals(Takeover.blue)) {
                    label.setIcon(new ImageIcon(blueIcon));
            
        } else if (color.equals(Takeover.yellow)) {
                    label.setIcon(new ImageIcon(yellowIcon));
            
        } else if (color.equals(Takeover.red)) {
                    label.setIcon(new ImageIcon(redIcon));
            
        }
                
        // set that color to be the color of the tabbed panel text
        PlayerTabbedPanel.setForegroundAt(playerNumber, Takeover.playerColor[playerNumber]);
        
        PlayerTabbedPanel.repaint();
        PlayMenu.repaint();
    }
    
    public void displayPlayerLevel(int player) {
        String Level = "1"; // default level 1 for a new player

        // check if they have choosen "New Player"
        // IF they have then we allow them to type whatever name they want
        if (playerCombo[player].getSelectedIndex() == 0 | playerCombo[player].getSelectedIndex() == -1) {
            // NOTE: the selected item index when you type is -1
            playerCombo[player].setEditable(true);
        } 
        else {
            playerCombo[player].setEditable(false);
            // If they have choosen a different player than we load its level

            // first get the name of the player (from our array) and find its file
            BufferedReader read = null;
            try {
                // the file to read is in the players folder and named the name of the selected player in our list 
                // (except our list has one extra item "New Player"  so we subtract 1)
                String file = "players/" + playersList[playerCombo[player].getSelectedIndex() - 1];
                read = new BufferedReader(new FileReader(file));

                Level = read.readLine();
                read.close();
            } catch (Exception ex) {
                // .. can't load the save file... hmm
                System.out.println("ERROR PLAYER FILE DOESN'T EXIST");
            }
        }

        // assign level if its something new
        // (we run this check so that if they are just typing it doesn't re-assign the level 10x
        if (!playerLevel[player].getText().equals(Level)) {
            playerLevel[player].setText(Level);
            PlayMenu.repaint(); //repaint
        }
    }
    
    
    public static void openWebpage(URI uri) {
        // call the desktop
       Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
       
       // if the call works and there is a supported internet browser available
       if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
           try {
               desktop.browse(uri); // try to launch the uri
           } catch (Exception e) {
           }
       }
    }
    
    // INTERPRET BOARD FROM STRING INTO A INTEGER MATRIX
    public int[][] interpretBoard(String mapName) {
        
        System.out.println("INTERPRETTING BOARD + " + mapName);
        
        int[][] board = null;
                
        // IF WE WANT TO GENERATE A CUSTOM BOARD USE THIS ALGORITHM
        // use an algorithm to create a board with the dimensions the user specified
        // simple board with either 1 or 2 white spaces between the black
        if ("custom".equals(mapName)) {

            // remove all odd characters
            String boardDimensions = customBoardField.getText().replaceAll("[^\\d,]", "");
            
            // split into X and Y
            String[] size = boardDimensions.split(","); // split the text in the field
            
            // TODO VALIDATE PROPERLY
            
            // verify the data is valid 
            // - has two valid items
            // - not greater than 99 in either dimension
            if (size[0] != null && size[1] != null) {
                
                if ((Integer.parseInt(size[0]) <= 99) && (Integer.parseInt(size[1]) <= 99)) {

                    // GENERATE BOARD

                    board = new int[Integer.parseInt(size[0])][Integer.parseInt(size[1])]; // setup board array 

                    int gap = 1; // assume gap of 1 (number of white spaces between the black spaces)

                    // For the dimensions of our board

                    // formula to generate impassible spaces (all other spaces are white)
                    for (int y = 0; y < board[0].length; y++) {
                        for (int x = 0; x < board.length; x++) {

                            //// IF IT IS A CORNER THEN PUT IN A START SPACE
                            // corner is the 0,0 OR 0,Y OR X,Y OR X,0
                            if ((x == board.length -1 | x == 0) && (y == board[0].length -1 | y == 0)) {

                                board[x][y] = 3; // WHITE START

                            }

                            // THIS AUTOMATICALLY GENERATES A BLACK SQUARE UNDER CERTAIN CONDITIONS
                            // black spaces with 1 white space in between each
                            // if the x and y coordinates are both not even then it is an impassible space

                            // ex:
                            // 0,0,0,0,0
                            // 0,1,0,1,0
                            // 0,0,0,0,0
                            else if (gap == 1 && !(x%2 == 0) && !(y%2 == 0)) {

                                board[x][y] = 2; // BLACK

                            }

                            //  THIS AUTOMATICALLY GENERATES A BLACK SQUARE UNDER CERTAIN CONDITIONS
                            // generates black spaces with 2 white spaces in between each

                            // ex:
                            // 0,0,0,0,0,0
                            // 0,1,0,0,1,0
                            // 0,0,0,0,0,0
                            // 0,0,0,0,0,0
                            // 0,1,0,0,1,0
                            //
                            else if (gap == 2 && ((y-1)%3 == 0) && ((x-1)%3) == 0) {

                                board[x][y] = 2; // BLACK

                            }

                            // OTHERWISE PUT IN A WHITE SQUARE
                            else {

                                board[x][y] = 1; // WHITE

                            }

                        }

                    }
                }
                else {      
                    // PRINT ERROR
                    QuickGameErrorLabel.setText("ERROR Board too large (limit of 99)");
                    board = null;
                }
            }
            else {      
                // PRINT ERROR
                QuickGameErrorLabel.setText("ERROR Invalid board specifications (X,Y)");
                board = null;
            }
        } else if ("random".equals(mapName)) {
            // TODO - random map
            System.out.println("Loading Random");
            
        } else {
            
            System.out.println("Loading");
            
            // OTHERWISE LOAD THE TEMPORARY BOARD FOR OUR MAP
            // figure out which map is selected and load its board matrix
            board = mapTable.get(mapName).mapBoard;
            
        }
        
        System.out.println("BOARD: " + Arrays.deepToString(board));
        
        return board;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
            //</editor-fold>
            
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CampaignButton;
    private javax.swing.JLabel CampaignImage;
    private takeover.AlphaContainer CampaignPanel;
    private javax.swing.JLabel ControlsButton;
    private javax.swing.JPanel ControlsPanel;
    private takeover.AlphaContainer GameSwitchPanel;
    private javax.swing.JLabel HelpButton;
    private javax.swing.JPanel HelpPanel;
    private javax.swing.JLabel HomeButton;
    private javax.swing.JLabel HomeButton1;
    private javax.swing.JComboBox<String> MapComboBox;
    private javax.swing.JLabel MapPicture;
    private javax.swing.JLabel MapPicture1;
    private javax.swing.JLabel OptionsButton;
    private javax.swing.JPanel OptionsPanel;
    private javax.swing.JLabel PlayButton;
    private javax.swing.JFrame PlayMenu;
    private takeover.AlphaContainer PlayPanel;
    private javax.swing.JTabbedPane PlayerTabbedPanel;
    private javax.swing.JTabbedPane PlayerTabbedPanel1;
    private javax.swing.JLabel QuickGameButton;
    private javax.swing.JLabel QuickGameErrorLabel;
    private javax.swing.JButton QuickGamePlayButton;
    private takeover.AlphaContainer QuickPanel;
    private javax.swing.JComboBox<String> ScenarioComboBox;
    private javax.swing.JLabel ScenarioErrorLabel;
    private javax.swing.JLabel ScenarioGameButton;
    private takeover.AlphaContainer ScenarioPanel;
    private javax.swing.JButton ScenarioPlayButton;
    private javax.swing.JLabel SettingsButton;
    private javax.swing.JFrame SettingsMenu;
    private takeover.AlphaContainer SettingsPanel;
    private takeover.AlphaContainer SettingsSwitchPanel;
    private javax.swing.JLabel WebsiteButton;
    private javax.swing.JTextField customBoardField;
    private javax.swing.JLabel customBoardLabel;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private takeover.AlphaScrollContainer jScrollPane1;
    private javax.swing.JLabel mainLabel;
    private javax.swing.JLabel numPlayersLabel;
    private javax.swing.JLabel numPlayersLabel1;
    public javax.swing.JSlider numberOfPlayers;
    private javax.swing.JSlider numberOfPlayers1;
    public javax.swing.JLabel p1Image;
    private javax.swing.JButton p1Toggle;
    private javax.swing.JButton p1u1Down;
    private javax.swing.JButton p1u1Fire;
    private javax.swing.JButton p1u1Left;
    private javax.swing.JButton p1u1Right;
    private javax.swing.JButton p1u1Up;
    private javax.swing.JButton p1u2Down;
    private javax.swing.JButton p1u2Fire;
    private javax.swing.JButton p1u2Left;
    private javax.swing.JButton p1u2Right;
    private javax.swing.JButton p1u2Up;
    public javax.swing.JLabel p2Image;
    private javax.swing.JButton p2Toggle;
    private javax.swing.JButton p2u1Down;
    private javax.swing.JButton p2u1Fire;
    private javax.swing.JButton p2u1Left;
    private javax.swing.JButton p2u1Right;
    private javax.swing.JButton p2u1Up;
    private javax.swing.JButton p2u2Down;
    private javax.swing.JButton p2u2Fire;
    private javax.swing.JButton p2u2Left;
    private javax.swing.JButton p2u2Right;
    private javax.swing.JButton p2u2Up;
    public javax.swing.JLabel p3Image;
    private javax.swing.JButton p3Toggle;
    private javax.swing.JButton p3u1Down;
    private javax.swing.JButton p3u1Fire;
    private javax.swing.JButton p3u1Left;
    private javax.swing.JButton p3u1Right;
    private javax.swing.JButton p3u1Up;
    private javax.swing.JButton p3u2Down;
    private javax.swing.JButton p3u2Fire;
    private javax.swing.JButton p3u2Left;
    private javax.swing.JButton p3u2Right;
    private javax.swing.JButton p3u2Up;
    public javax.swing.JLabel p4Image;
    private javax.swing.JButton p4Toggle;
    private javax.swing.JButton p4u1Down;
    private javax.swing.JButton p4u1Fire;
    private javax.swing.JButton p4u1Left;
    private javax.swing.JButton p4u1Right;
    private javax.swing.JButton p4u1Up;
    private javax.swing.JButton p4u2Down;
    private javax.swing.JButton p4u2Fire;
    private javax.swing.JButton p4u2Left;
    private javax.swing.JButton p4u2Right;
    private javax.swing.JButton p4u2Up;
    private takeover.AlphaContainer panel;
    private javax.swing.JLabel player1AI;
    private javax.swing.JComboBox<String> player1Combo;
    private javax.swing.JLabel player1Level;
    private takeover.AlphaContainer player1Panel;
    private javax.swing.JLabel player2AI;
    private javax.swing.JComboBox<String> player2Combo;
    private javax.swing.JLabel player2Level;
    private takeover.AlphaContainer player2Panel;
    public javax.swing.JLabel player3AI;
    private javax.swing.JComboBox<String> player3Combo;
    private javax.swing.JLabel player3Level;
    private takeover.AlphaContainer player3Panel;
    private javax.swing.JLabel player4AI;
    private javax.swing.JComboBox<String> player4Combo;
    private javax.swing.JLabel player4Level;
    private takeover.AlphaContainer player4Panel;
    // End of variables declaration//GEN-END:variables
}
