/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import takeover.gameFiles.PlayerCommand;
import java.awt.Color;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuser
 */

public class Takeover {
    
    // SETUP the save file
    // the properties file is initiated here
    static Properties properties = new Properties();
    //////////////////////////////////////////
    
    public static String defaultMaps = "|cyber|exo|fallout|"; // string for the maps to be shown as default

    
    public static Color[] playerColor = new Color[4]; // four players, starting at index 0
    
    // this is static color variables for the various colors that are possible
    // these are ACTUALLY STATIC
        public static Color red = Color.red;
        public static Color yellow = Color.yellow.darker();
        public static Color blue = Color.blue;
        public static Color green = Color.green.darker().darker();

        
    public static String[] playerUser = new String[4]; // four players, starting at index 0
    
    
    public static int numberOfPlayers = 4; // up to four players
    
    public static int defaultNumberOfUnits = 2;
    public static int[] numberOfUnits = new int[4]; // between 1 and 4 units per player for each player (0,1,2,3)
    public static int defaultSpeedTime = 100;
    
    public static int gameTime = 120;
    
    // AI VARIABLES
    // NOTE: if a player is AI it will not save its experience
    public static Boolean[] playerAI = new Boolean[4]; // four players, starting at index 0, if they are Computer controlled
    public static int AILevel = 10; // this can be changed in settings to make harder or easier game
    
    
    // CONTROLS SETTINGS
    // 2d array
    // Player# Unit# Action#     Control#
    public static InputController inputer = new InputController();
    
    public static String[][] controlsIndex = new String[44][4];
    
    public static HashMap<Color, Image> iconTable = new HashMap<>();
    
    // HASH MAP
    // Key = string (of what keypressed)
    // Value = command (of what to do when it is hit)
    public static HashMap<String, PlayerCommand> controlsMap = new HashMap<>();
    /**
     * @param args the command line arguments
     */
        
    
    public static void main(String[] args) {
                
        loadProperties();
        controlsIndex = InputController.loadControlIndex(controlsIndex);
            
             
        MainMenu m = new MainMenu();
        
        setupGui(m);
    
        m.setSize(800,600);
        m.setVisible(true);
        m.setLocationRelativeTo(null);
        m.setResizable(false);
    }
    
    public static void fillHashMap() {
        // assign the hash map with the command and its control
        for (String[] controlsIndex1 : controlsIndex) {
            controlsMap.putIfAbsent(controlsIndex1[3], new PlayerCommand(Integer.parseInt(controlsIndex1[0]), Integer.parseInt(controlsIndex1[1]), controlsIndex1[2]));
        }
    }
    
    public static void loadProperties() {
        try {
            properties.load(new FileInputStream("save.properties"));
            
            setupVariables();
        } catch (Exception e) {
            // if there is NO SAVE FILE
            System.out.println("NO SAVE FILE");
            
            // so it won't bother trying to load a save file
            // instead create a new save file
            setupDefaultSave();
            save();
            
            // then load all that
            setupVariables();
        }
    }
    
    private static void setupGui(MainMenu m) {
        // for all the combo boxes setup the saved selection
        for (int i = 0; i < m.playerCombo.length; i++) {
            //assign the box to the right user
            m.playerCombo[i].setSelectedItem(playerUser[i]);
        }
        
        // for all the player colors setup the correct color
        for (int i = 0; i < m.playerAIButton.length; i++) {
            //assign the box to the right user
            m.displayAIButton(playerAI[i], m.playerAIButton[i], i);
        }
        
        // setup the correct user colors
        m.updateColor(0, m.p1Image);
        m.updateColor(1, m.p2Image);
        m.updateColor(2, m.p3Image);
        m.updateColor(3, m.p4Image);
        
        // setup the correct player number
        // adjust the bar and it will setup everything automatically
        m.numberOfPlayers.setValue(numberOfPlayers);
    }
    
    private static void setupVariables() {
        defaultSpeedTime = Integer.parseInt(properties.getProperty("defaultSpeedTime"));
        
        defaultNumberOfUnits = Integer.parseInt(properties.getProperty("numberOfUnits"));
        numberOfUnits[0] = defaultNumberOfUnits;
        numberOfUnits[1] = defaultNumberOfUnits;
        numberOfUnits[2] = defaultNumberOfUnits;
        numberOfUnits[3] = defaultNumberOfUnits;
        
        numberOfPlayers = Integer.parseInt(properties.getProperty("numberOfPlayers"));
        
        playerColor[0] = new Color(Integer.parseInt(properties.getProperty("color0")));
        playerColor[1] = new Color(Integer.parseInt(properties.getProperty("color1")));
        playerColor[2] = new Color(Integer.parseInt(properties.getProperty("color2")));
        playerColor[3] = new Color(Integer.parseInt(properties.getProperty("color3")));
        
        playerUser[0] = properties.getProperty("user0");
        playerUser[1] = properties.getProperty("user1");
        playerUser[2] = properties.getProperty("user2");
        playerUser[3] = properties.getProperty("user3");
        
        playerAI[0] = Boolean.parseBoolean(properties.getProperty("aiplayer0"));
        playerAI[1] = Boolean.parseBoolean(properties.getProperty("aiplayer1"));
        playerAI[2] = Boolean.parseBoolean(properties.getProperty("aiplayer2"));
        playerAI[3] = Boolean.parseBoolean(properties.getProperty("aiplayer3"));
    }
    
    private static void setupDefaultSave() {
        properties.setProperty("defaultSpeedTime", "" + defaultSpeedTime);
        
        properties.setProperty("numberOfUnits", "" + defaultNumberOfUnits);
        
        properties.setProperty("numberOfPlayers", "" + numberOfPlayers);
        
        properties.setProperty("color0", "" + red.getRGB());
        properties.setProperty("color1", "" + green.getRGB());
        properties.setProperty("color2", "" + yellow.getRGB());
        properties.setProperty("color3", "" + blue.getRGB());
        
        properties.setProperty("user0", "New Player");
        properties.setProperty("user1", "New Player");
        properties.setProperty("user2", "New Player");
        properties.setProperty("user3", "New Player");
        
        properties.setProperty("aiplayer0", "false");
        properties.setProperty("aiplayer1", "false");
        properties.setProperty("aiplayer2", "false");
        properties.setProperty("aiplayer3", "false");
    }
    
    
    // NOTE this is called when the Play button is hit or when settings are changed
    public static void save() {
        System.out.println("Saving");
        try {
            properties.store(new FileOutputStream("save.properties"), null);
        } catch (IOException ex) {
            Logger.getLogger(Takeover.class.getName()).log(Level.SEVERE, null, ex);
            // couldn't find file
        }
    }
    
    /////////////////////////////////
    // METHODS TO ASSIGN VARIABLES EXTERNALLY //
    ////////////////////////////////
    
    
    public static void playerSetColor(int player, Color color) {
        playerColor[player] = color;
        
        properties.setProperty("color" + player, "" + color.getRGB());
    }
    public static void playerSetUser(int player, String user) {
        playerUser[player] = user;
        
        properties.setProperty("user" + player, "" + user);
    }
    public static void playerSetAI(int player, Boolean ai) {
        playerAI[player] = ai;
        
        properties.setProperty("aiplayer" + player, "" + ai);
    }
   
    public static void numPlayersSet(int players) {
        numberOfPlayers = players;
        
        properties.setProperty("numberOfPlayers", "" + players);
    }
    
}

