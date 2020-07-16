/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.java.games.input.Component;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Nuser
 */
public class InputController {
    
    // Input Controller
    // 
    //
    // CLASS that allows the implementation of a keyboard and joystick
    //
    //
    // Input Controller
    
    
    
    
    // FUNCTION 1:
    // --- TO GET INPUT AND STORE IT ---
    // provides methods to prompt user and gain input and store it
    //
    // 1. JFrame which is opened (and while open listens for input from the controllers)
        // CALLED openControllerFrame() which requires two parameters
        // A. BUTTON to be updated with key data
        // B. INDEX in the controls array which the key data should be stored
    // 2. Controller listeners which interpret joystick and keyboard input
        // These are behind the scenes code
    // 3. Save and loading to and from a control table actions and their assigned keys
        // CALLED through two public methods to SAVE and LOAD the controls table from a file
    
    
    JFrame listenFrame = new JFrame();
    JLabel keyHitLabel = new JLabel();
        
    // CLASS WHEN CREATED SETUP ALL GUI AND COMPONENTS
    public InputController() {
        
        // setup the gui menu
        setupFrame();
        
        // SETUP JOYSTICK INPUT
        foundControllers = new ArrayList<>();
        // Find what controllers are plugged in
        startupGetControllers();
        
    }
    
    
    
    //////////////////////////
    /////////////////////////
    // JFRAME INPUT WINDOW
    /////////////////////////
    /////////////////////////
    
    // GUI JFRAME:
    // method to create all the gui components of the JFrame which prompts user input
    // It has a panel a label "PRESS ANY KEY" and a button to confirm your press
    private void setupFrame() {
        // create components to go in the jframe
        JPanel jPanel1 = new JPanel();
        JLabel jLabel20 = new JLabel();
        JLabel jLabel21 = new JLabel();
        JButton jButton1 = new JButton();
        
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/menu/icons/unit.png"))); // NOI18N
        jLabel20.setText("jLabel20");
        jLabel20.setFocusable(false);

        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setText("PRESS ANY KEY");
        jLabel21.setFocusable(false);

        jButton1.setText("CONTINUE");
        jButton1.setFocusable(false);
        // SETUP CONFIRM BUTTON ACTION LISTENER
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {     
                // WHEN CONFIRM BUTTON IS CLICKED
                
                // hide frame
                listenFrame.dispose();
                
                // SAVE THE KEY::
                
                // update the button pressed
                editableKey.setText(currentKey);

                // update this in the controls index
                
                // SAVE key in the last item of the controls index for this button
                int lastElement = Takeover.controlsIndex[0].length - 1;
                System.out.println("X,Y : " + controlItem + " ," + lastElement);
                Takeover.controlsIndex[controlItem][lastElement] = "" + currentKey;

                keyHitLabel.setText(" "); // reset key hit label


                // STOP CHECKING FOR JOYSTICK INPUT
                System.out.println("CANCEL JOYSTICK LISTENER");
                Joystick_Input_Thread.cancel();
    
            }
        });

        keyHitLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        keyHitLabel.setText(" ");
        
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFocusable(false);
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(keyHitLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jButton1)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(keyHitLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(listenFrame.getContentPane());
        listenFrame.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
        // add input listeners to this JFrame
        // (NOTE: we are adding in-active listeners - which means they merely display text and don't launch actions)
        addGameInputListeners(listenFrame, false);
    }
    
    // JFRAME FOR CONTROLLER
    private JButton editableKey;
    private int controlItem;
    private String currentKey = "";
    
    // store controllers in this array
    private ArrayList<net.java.games.input.Controller> foundControllers;
    
    // Timer to run the joystick input thread
    private Timer Joystick_Input_Thread = new Timer();
        
    // ## PROCEDURE WHICH INITIATES CONTROLLER FRAME ##
    // when you launch this it brings up the window
    // allows you to press buttons
    // and gives you an option to confirm and save the key into the controller matrix
    public void openControllerFrame(JButton j, int index) {
        // figure out which button was hit
        
        System.out.println("BUTTON " + index + " added");
        
        editableKey = j; // GET BUTTON
        controlItem = index; // GET INDEX IN CONTROLS ARRAY

        // THEN SETUP JOYSTICK INPUT
        // add a listener (Note: boolean "false" means we don't fire actions when the keys are pressed but instead display the keys)
        addJoystickListener(false);

        listenFrame.setTitle("Change Controls");
        listenFrame.setSize(250,150);
        listenFrame.setVisible(true);
        listenFrame.setLocationRelativeTo(null);
        listenFrame.setResizable(false);
    }
    
    private void startupGetControllers() {
        net.java.games.input.Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

            for (net.java.games.input.Controller controller : controllers) {
                if (
                        controller.getType() == net.java.games.input.Controller.Type.STICK ||
                        controller.getType() == net.java.games.input.Controller.Type.GAMEPAD ||
                        controller.getType() == net.java.games.input.Controller.Type.WHEEL ||
                        controller.getType() == net.java.games.input.Controller.Type.FINGERSTICK
                        )
                {
                    // Add new controller to the list of all controllers.
                    foundControllers.add(controller);
                }
            }
    }
    
    /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the left/top 
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     * 
     * @param axisValue
     * @return value of axis in percentage.
     */
    private int getAxisValueInPercentage(float axisValue)
    {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }
    
    
    
    //////////////////////////
    /////////////////////////
    // SAVES KEYS AND CONTROLS
    /////////////////////////
    /////////////////////////
    
    //
    // 3 PUBLIC STATIC METHODS WHICH CAN BE CALLED TO:
    // - SAVE A CONTROLS INDEX TABLE
    // - LOAD A CONTROLS INDEX TABLE
    // - SHOW THE CONTENTS OF AN INDEX TABLE IN BUTTONS
    //
    
    // display the controls index in buttons array
    public void displayControls(String[][] controlsIndex, JButton[] controlsArray) {
        // for every button in the array
        for (int i = 0; i < 44; i++) {
            // set it to have the correct text
            // (correct text stored in [x][LASTINDEX])
            try {
                // figure out the text for the key
                // (TEXT = last item in the controls table)
                controlsArray[i].setText(controlsIndex[i][controlsIndex[0].length - 1]);
            } catch (Exception e) {
                System.out.println("ERROR could not display key text");
            }
        }
    }
    
    // save to the file the controls index
    public static void saveControlIndex(String[][] controlsIndex) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("save.controls"));
            
            // iterate through the controls array and store them all into a file
            for (int x = 0; x < controlsIndex.length; x++) {
                   
                String line = "";
                
                // iterate through all the other components (seperate with commas)
                for (int y = 0; y < controlsIndex[0].length; y++) {
                    // seperate items with commas
                    if (y != 0) {
                        line = line + ",";
                    }
                    line = line + controlsIndex[x][y];
                }
                
                
                System.out.println("Saving controls: " + line);
                if (x != 0) {
                    writer.write("\r\n" + line); // write a next line char and the line
                } else {
                    writer.write(line); // write a next line char and the line
                }
            }
            
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR cannot save controls");
            // file permissions error...
        }
    }
    // get from file the controls index of the give format and return it
    public static String[][] loadControlIndex(String[][] controlsIndex) {
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("save.controls"));
            
            int x = 0;
            // read each line of the controls file until the end
            String nextLine = reader.readLine();
            while (nextLine != null) {
                   
                // split the line and fill the controlsIndex array
                String[] items = nextLine.split(",");
                
                for (int i = 0; i < controlsIndex[0].length; i++) {
                    // fill line of the controls array with the data items
                    controlsIndex[x][i] = items[i];
                    // ** System.out.println("X: " + x + " ,i " + i + " , " + items[i]);
                }
                 
                // go to the next question
                x ++;
                nextLine = reader.readLine();
            }
            reader.close();
        }
        catch (IOException ex) {
            // NO FILE
            // Load Default Controls File
            System.out.println("ERROR COULD LOAD SAVE - LOADING DEFAULT");
            try {
                // try to copy the default
                Files.copy(new File("default.controls").toPath(), new File("save.controls").toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                loadControlIndex(controlsIndex);
            } catch (IOException ex1) {
                Logger.getLogger(Takeover.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } 
        
        return controlsIndex;
    }
    
    
    
    
    // FUNCTION 2:
    // ----------------
    // --- TO LISTEN FOR INPUT AND LAUNCH ACTIONS ---
    // methods to listen for input and figure out what actions should occur
    //
    // 1. Launching Joylistener and Keyboard listener for a game
    //
    // 2. Controller listeners identify key presses
        // Continually search for input
        // When found check the Hash and figure out what action is fired
        // Return that action to the game
    
    
    /////////////////////////
    /////////////////////////
    // LISTENERS FOR A GAME
    /////////////////////////
    /////////////////////////
    
    public void addGameInputListeners(JFrame gameFrame, Boolean activeAction) {
    
        // 1. Add keyboard Listener
        
        /// ADD A KEY LISTENER TO FRAME
        addKeyListeners(gameFrame, activeAction);
        
        // 2. Add Joystick Listener
        addJoystickListener(activeAction);
        
    }
    
    private void addKeyListeners(JFrame f, Boolean activeAction) {
        // listen for key pressed events
        f.addKeyListener(new KeyListener()
        {
              @Override 
              public void keyPressed(KeyEvent e)
              {         
                // We are listening for keys being pressed
                  
                // get the key pressed's code and fire its action
                fireAction(KeyEvent.getKeyText(e.getKeyCode()), activeAction);
              }

            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        /// WHEN CLOSE FRAME IT KILLS JOYSTICK LISTENER
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
                public void windowClosing(java.awt.event.WindowEvent e) {

                        f.dispose();
                        
                        // stop listening for joystick
                        Joystick_Input_Thread.cancel();
                }
        });
    }
    private void addJoystickListener(Boolean activeAction) {
        
        Joystick_Input_Thread = new Timer(); // recreate timer
        /// ACTIVATE A TASK TO CHECK FOR INPUT
        Joystick_Input_Thread.schedule(new TimerTask() {
            @Override
            public void run() { 

                // If at least one controller was found we start firing action for the keys.
                if(!foundControllers.isEmpty()) {
                    getControllerData(activeAction);
                }

            }
        }, 0, 100);
    }
    
    private void getControllerData(Boolean activeAction){
        String keyPressed;

        // Currently selected controller.
        for (net.java.games.input.Controller selectedController : foundControllers) {


            // Pull controller for current data, and break while loop if controller is disconnected.
            if( !selectedController.poll() ){
                Joystick_Input_Thread.cancel(); // end control search
                break;
            }

            // X axis and Y axis
            String xAxisPercentage = "";
            String yAxisPercentage = "";


            String controller = " " + selectedController.getName();
            controller = controller.replace(",", ""); // REMOVE ANY "," in the controller name because they dont work in the CSV save file

            /////////////////////
            // Go through all components of the controller.
            Component[] components = selectedController.getComponents();
            for(int i=0; i < components.length; i++)
            {

                // reset key pressed (so that if nothing is hit nothing will fire)
                keyPressed = "";
                
                Component component = components[i];
                Component.Identifier componentIdentifier = component.getIdentifier();

                // BUTTONS
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    // Button index

                    String buttonIndex;
                    buttonIndex = component.getIdentifier().toString();

                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    else {
                        // get the key pressed's code and store it
                        keyPressed = " J " + buttonIndex + controller;
                    }

                }

                // HAT SWITCH
                else if(componentIdentifier == Component.Identifier.Axis.POV){
                    float hatSwitchPosition = component.getPollData();

                    String hatDirection = "";

                    if(Float.compare(hatSwitchPosition, Component.POV.OFF) != 0) {

                        if(Float.compare(hatSwitchPosition, Component.POV.UP) == 0){

                            hatDirection = "UP";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.DOWN) == 0){

                            hatDirection = "DOWN";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.LEFT) == 0){

                            hatDirection = "LEFT";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.RIGHT) == 0){

                            hatDirection = "RIGHT";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.UP_LEFT) == 0){

                            hatDirection = "UP LEFT";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.UP_RIGHT) == 0){

                            hatDirection = "UP RIGHT";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.DOWN_LEFT) == 0){

                            hatDirection = "DOWN LEFT";
                        }else if(Float.compare(hatSwitchPosition, Component.POV.DOWN_RIGHT) == 0){

                            hatDirection = "DOWN RIGHT";
                        }
                        // get the key pressed's code and store it
                        keyPressed = "HAT " + hatDirection + controller;
                    }

                }

                // AXES
                // when we have gotten to an axes button we observe what position it is in and store it
                else if(component.isAnalog()){
                    float axisValue = component.getPollData();
                    // get axis value than convert its percentage into an X-Y coordinate system
                    int axisValueInPercentage = getAxisValueInPercentage(axisValue)/49 - 1;


                    // X axis
                    // store the X axis position
                    if(componentIdentifier == Component.Identifier.Axis.X) {
                        switch (axisValueInPercentage) {
                            case 0:
                                xAxisPercentage = "";
                                break;
                            case 1:
                                xAxisPercentage = "RIGHT";
                                break;
                            case -1:
                                xAxisPercentage = "LEFT";
                                break;
                            default:
                                break;
                        }

                    }
                    // Y axis
                    // store the Y axis position
                    else if(componentIdentifier == Component.Identifier.Axis.Y) {
                        switch (axisValueInPercentage) {
                            case 0:
                                yAxisPercentage = "";
                                break;
                            case 1:
                                yAxisPercentage = "DOWN";
                                break;
                            case -1:
                                yAxisPercentage = "UP";
                                break;
                            default:
                                break;
                        }

                    }
                }

                // if something has been pressed
                if (!keyPressed.equals("")) {
                    ////
                    //// THEN FIRE THAT KEYS ACTION
                    ////
                    fireAction(keyPressed, activeAction);
                }
            }
            
            // CHECK FOR AXIS MOTION
            // at the end once we have observed all the buttons current states we know if the axis has been hit
            // IF axis percentages ARE NOT NOTHING then we no that the Axis is being hit
            if (!(xAxisPercentage.equals("") && yAxisPercentage.equals(""))) {
                // get the key pressed's code and store it
                keyPressed = yAxisPercentage + " " + xAxisPercentage + " AXES " + controller;
                
                ////
                //// THEN FIRE THAT KEYS ACTION
                ////
                fireAction(keyPressed, activeAction);
            }
        }
    }
    
    private void fireAction(String keyPressed, Boolean activeAction) {
        // TWO DIFFERENT OPTIONS:
        // EITHER display the keyPressed
        // OR fire the keyPressed action
        if (!activeAction) {
            
            currentKey = keyPressed;
            
            keyHitLabel.setText(currentKey);
        } else {
            
            System.out.println("FIRE ACTION FOR : " + keyPressed);

            // IF THAT KEY HAS AN ACTION
            if (Takeover.controlsMap.containsKey(keyPressed)) {

                System.out.println("PLAYER " + Takeover.controlsMap.get(keyPressed).getPlayer() + " UNIT " + Takeover.controlsMap.get(keyPressed).getUnit() + "  ACTION : " + Takeover.controlsMap.get(keyPressed).getCommand());
            }
        }
        
    }

}
