/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * Copyright 2020 Nathan Rais 
 * 
 *      This file is part of The Desktop Stopwatch Timer application.
 *
 *   The Desktop Stopwatch Timer by Nathan Rais is free software 
 *   but is licensed under the terms of the Creative Commons 
 *   Attribution license (CC BY 4.0). Under the CC BY license 
 *   you may redistribute this as long as you give attribution.
 *
 *   The Stopwatch Timer is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   CC BY license for more details.
 *
 *   You should have received a copy of the CC BY license along with 
 *   The Stopwatch Timer. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 *
 */
public class TimeFrame extends javax.swing.JFrame {

    // start of the clock at 0
    public int clockHours = 0;
    public int clockMinutes = 0;
    public int clockSeconds = 0;
    
    
    public String selectedSoundLocation =  "";
    public String defaultLocation = "/resources/alarm.wav";
    
    
    public boolean going = false; // boolean to store when the timer is on
    
    // variables for settings that can be activated
    public boolean sleepComputer = false;
    public boolean lockComputer = false;
    public boolean shutdownComputer = false;
    
    public boolean launchOnStartup = false;
    public boolean startOnLaunch = false;
    
    public boolean warning = true;
    public int warningAmount = 120;
    
    public TimeFrame OurTimeFrame = null;
        
    public boolean defaultSound = true;
    
    public String thePlace = "";
    
    // the properties file is initiated here
    Properties properties = new Properties();
    
    Clip clip = null; // the sound clip for the audio is initiated here
    
    public void setBackground(JFrame frame, JPanel panel, String texture) {
        // ----- set the background image
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource(texture));
        
        Image myImage = icon.getImage();

        // scale image:
        myImage = myImage.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH);

        // create a label and assign it the image we made
        JLabel j = new JLabel(new ImageIcon(myImage));
        j.setLayout( null );
        
        // set the content frame of our background to be our image
        frame.setContentPane(j);
        frame.setResizable(false);
        
        // --------
        frame.add(panel); // LOAD EVERYTHING ONTO THE MAIN FRAME

        // make the stuff actually show up:
        panel.setLocation(0,0);
        panel.setOpaque(false);
        panel.setSize(frame.getWidth(), frame.getHeight());
    }
    // procedure used to allow kill game when you click on the exit button of sub frames we have made
    public void otherFrameCanClose(JFrame frame, Boolean kill) {
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
                        
                        // and also show are main frame again
                        OurTimeFrame.setVisible(true);
                    }
                }
        });
    }
    
    /**
     * Creates new form TimerFrame
     */
    public TimeFrame() {
        initComponents();
        this.setSize(480,365);
                
        try {
            // setup the location variable which is used by the save info
            thePlace = TimeFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        // cut the string from front to the end not including the last char which is a dot
        thePlace = thePlace.substring(1);

        System.out.println(thePlace);
        
        if (thePlace.contains(".")) {
            // when we create our pathway for our file we check if it has a dot
            // it should have a dot in which case we cut off after the last / and that gives us our folder directory
            thePlace = thePlace.substring(0, thePlace.lastIndexOf("/") + 1);
        } else {
            // if it doesn't have a dot then that is a problem...
            // we will worry about it later
        }
        
        System.out.println(thePlace);
        // --------------- //
        
        OurTimeFrame = this; // setup our time frame so you can get it back again when you access the settings menu
        
        setBackground(this,mainPanel,"/resources/stopwatch-background.png");
        
        
        // set the icon
        List<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/timer-logo-small.png")));
        this.setIconImages(icons);
        
        this.setLocationRelativeTo(null);
        
        
        // hide button background
        /*settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setOpaque(false);  
        
        hideUntilAlarmButton.setContentAreaFilled(false);
        hideUntilAlarmButton.setBorderPainted(false);
        hideUntilAlarmButton.setFocusPainted(false);
        hideUntilAlarmButton.setOpaque(false);   */
        
        ButtonWebsite.setContentAreaFilled(false);
        ButtonWebsite.setBorderPainted(false);
        ButtonWebsite.setFocusPainted(false);
        ButtonWebsite.setOpaque(false);   
        ButtonWebsite.setSize(this.getWidth() - settingsButton.getWidth()*2, ButtonWebsite.getHeight());
        ButtonWebsite.setLocation(this.getWidth() - settingsButton.getWidth() - ButtonWebsite.getWidth(),ButtonWebsite.getY());
    
        
        
        // SAVE FILE STUFF //
            Boolean isAFile = true;

            // try to see if there is a save file
            try {
                properties.load(new FileInputStream(thePlace + "save.properties"));
            } catch (Exception e) {
                // if there is NO SAVE FILE
                System.out.println("NO SAVE FILE");
                isAFile = false;
                // so it won't bother trying to load a save file
                // instead create a new save file
                setupDefaultSave();
                save();
            }

            // load the save info file
            if (isAFile == true) {
                try {
                    warningAmount = Integer.parseInt(properties.getProperty("warningAmount"));

                    warning = Boolean.parseBoolean(properties.getProperty("warningBoolean"));
                    sleepComputer = Boolean.parseBoolean(properties.getProperty("sleepComputerBoolean"));
                    lockComputer = Boolean.parseBoolean(properties.getProperty("lockComputerBoolean"));
                    defaultSound = Boolean.parseBoolean(properties.getProperty("defaultSound"));
                    
                    launchOnStartup = Boolean.parseBoolean(properties.getProperty("launchOnStartup"));
                    startOnLaunch = Boolean.parseBoolean(properties.getProperty("startOnLaunch"));

                    // load previous time used
                } catch (Exception e) {
                    // oh well i guess we will just use the defaults ...
                }
                try {
                    clockHours = Integer.parseInt(properties.getProperty("hoursStored"));
                } catch (Exception e) {
                // oh well i guess we will just use the defaults ...
                }
                try {
                    clockMinutes = Integer.parseInt(properties.getProperty("minutesStored"));
                } catch (Exception e) {
                // oh well i guess we will just use the defaults ...
                }
                try {
                    clockSeconds = Integer.parseInt(properties.getProperty("secondsStored"));
                } catch (Exception e) {
                    // oh well i guess we will just use the defaults ...
                }
                // display them:
                updateFields();

                selectedSoundLocation = properties.getProperty("soundLocation");
            }   
        // ------------- //
            
    
        // EVERYTHING FOR SETTINGS FRAME //
        
            otherFrameCanClose(settingsFrame, false); // call procedure so that the settingsFrame disposes when you hit the X button


            // make the label the width of the hole thing so it is centered
            settingsLabel.setSize(settingsFrame.getWidth(), settingsLabel.getHeight());

            // make all the checkboxes the correct setting
            warningCheckBox.setSelected(warning);
            warningAmountField.setText("" + warningAmount);
            
            //sleepComputerCheckBox.setSelected(sleepComputer); //TEMPORARILY REMOVED
            lockComputerCheckBox.setSelected(lockComputer);
            shutdownComputerCheckBox.setSelected(shutdownComputer);
            
            launchStartupCheckBox.setSelected(launchOnStartup);
            startOnLaunchCheckBox.setSelected(startOnLaunch);

            defaultSoundCheckBox.setSelected(defaultSound);
            // enable/disable the soundLocation options and warning field depending on what is active
            showEnableOptions();

            // set the fileField location to be the last choosen location
            fileLocationField.setText(selectedSoundLocation);
            
            
            documentListeners(); // action listeners for the settings fields in the settings frame
        // ----------------- //
        
        // ----------------- //
        
        // check for auto start
        if (startOnLaunch) {
            // start the timer
            TimerGO.doClick();
        } else {
        // OTHERWISE SHOW THE FRAME LIKE NORMAL
            setVisible(true);
        }
    }
    
    public void resetVars() {
        // kill the clock by disabling the boolean
        going = false;
        
        // reset fields and buttons
        STOP.setEnabled(false);
        Hours.setEditable(true);
        Minutes.setEditable(true);
        Seconds.setEditable(true);
        
        TimerGO.setEnabled(true);
        StopWatchGO.setEnabled(true);
        RESET.setEnabled(true);
        hideUntilAlarmButton.setEnabled(false);
        
        errorLabel.setText(" ");
        
        this.setVisible(true);
    }
    
    
    public void endTimer() {
        
        resetVars();
        
        if (shutdownComputer) {
            try {
                // the integer here (5) gives enough time to play sound
                
                System.out.println("Shutdown " + shutdown(5));
                
               
            } catch (IOException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (sleepComputer) {
            try {
                // the integer here (5) gives enough time to play sound
                
                // we execute the sleep command and print whether it went or not
                System.out.println("Sleep " + sleep(5));
                
                
            } catch (IOException ex) {
                // ... not enough permissions
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (lockComputer) {
            try {
                
                // we execute the lock command and print whether it went or not
                System.out.println("Lock" + lock(5));
                
            } catch (IOException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    public static boolean lock (int time) throws IOException, InterruptedException {
        String sleepCommand = null;
        
        // the integer here would play the alarm out for 5 secs before the computer locks
        TimeUnit.SECONDS.sleep(time);
        
        if (SystemUtils.IS_OS_WINDOWS) 
            sleepCommand = "rundll32 user32.dll,LockWorkStation";
        else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) 
            sleepCommand = "/System/Library/CoreServices/Menu\\ Extras/User.menu/Contents/Resources/CGSession -suspend";
        else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_NET_BSD || SystemUtils.IS_OS_OPEN_BSD || SystemUtils.IS_OS_UNIX) 
            sleepCommand = "gnome-screensaver-command -l";
        else 
            return false;
        
        Runtime.getRuntime().exec(sleepCommand);
        return true;
    }
    
    public boolean sleep(int time) throws IOException, InterruptedException {
        String sleepCommand = null;
        
        TimeUnit.SECONDS.sleep(time);
        
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_NET_BSD || SystemUtils.IS_OS_OPEN_BSD || SystemUtils.IS_OS_UNIX)
            sleepCommand = "systemctl suspend";
        else if(SystemUtils.IS_OS_WINDOWS)
            sleepCommand = "rundll32.exe powrprof.dll,SetSuspendState 0,1,0";
        else if (SystemUtils.IS_OS_MAC|| SystemUtils.IS_OS_MAC_OSX)
            sleepCommand = "pmset sleepnow";
        else
            return false;

        Runtime.getRuntime().exec(sleepCommand);
        return true;
    }
    
    // thanks to apache commons this shutdown command will work on all OS's
    public static boolean shutdown(int time) throws IOException {
        String shutdownCommand = null, t = time == 0 ? "now" : String.valueOf(time);

        if(SystemUtils.IS_OS_AIX)
            shutdownCommand = "shutdown -Fh " + t;
        else if(SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC|| SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_NET_BSD || SystemUtils.IS_OS_OPEN_BSD || SystemUtils.IS_OS_UNIX)
            shutdownCommand = "shutdown -h " + t;
        else if(SystemUtils.IS_OS_HP_UX)
            shutdownCommand = "shutdown -hy " + t;
        else if(SystemUtils.IS_OS_IRIX)
            shutdownCommand = "shutdown -y -g " + t;
        else if(SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS)
            shutdownCommand = "shutdown -y -i5 -g" + t;
        else if(SystemUtils.IS_OS_WINDOWS)
            shutdownCommand = "shutdown.exe -s -t " + t;
        else
            return false;

        Runtime.getRuntime().exec(shutdownCommand);
        return true;
    }
    
    
    // call this function and apply a string to load a wav file from the resources folder
    public synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            File yourFile;
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
    
            @Override
          public void run() {
              // if do play sound:
                    
                    System.out.println("Using default sound : " + defaultSound + " , sound loc: " + url);

                    // first kill any previous sounds:
                    try {
                        clip.stop();
                        clip.close();
                    } catch (Exception e) {
                        // if it doesn't work then their is nothing playing so that is fine
                    }


                    try {


                        if (!defaultSound) {
                            // if we are not using the default location then our AudioSystem loads a file
                            yourFile = new File(url);
                            stream = AudioSystem.getAudioInputStream(yourFile);
                        } else {
                            // if we are using the default location then we load a resource for our AudioSystem
                            InputStream bufferedIn = new BufferedInputStream(getClass().getResourceAsStream(defaultLocation));
                            stream = AudioSystem.getAudioInputStream(bufferedIn);
                        }
                        format = stream.getFormat();
                        info = new DataLine.Info(Clip.class, format);
                        clip = (Clip) AudioSystem.getLine(info);
                        clip.open(stream);
                        clip.start();

                    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) { 
                        Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);

                        errorLabelS.setText("Unable to play file");
                    }
                }
            
        }).start();
      } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settingsFrame = new javax.swing.JFrame();
        mainPanel = new javax.swing.JPanel();
        StopWatchGO = new javax.swing.JButton();
        STOP = new javax.swing.JButton();
        hideUntilAlarmButton = new javax.swing.JButton();
        RESET = new javax.swing.JButton();
        Minutes = new javax.swing.JTextField();
        Hours = new javax.swing.JTextField();
        Seconds = new javax.swing.JTextField();
        TimerGO = new javax.swing.JButton();
        ButtonWebsite = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        settingsPanel = new javax.swing.JPanel();
        settingsLabel = new javax.swing.JLabel();
        lockComputerCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        warningCheckBox = new javax.swing.JCheckBox();
        soundFileButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        fileLocationField = new javax.swing.JTextField();
        launchStartupCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        defaultSoundCheckBox = new javax.swing.JCheckBox();
        warningAmountField = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        shutdownComputerCheckBox = new javax.swing.JCheckBox();
        errorLabelS = new javax.swing.JLabel();
        audioButton = new javax.swing.JLabel();
        HomeButton = new javax.swing.JButton();
        startOnLaunchCheckBox = new javax.swing.JCheckBox();

        javax.swing.GroupLayout settingsFrameLayout = new javax.swing.GroupLayout(settingsFrame.getContentPane());
        settingsFrame.getContentPane().setLayout(settingsFrameLayout);
        settingsFrameLayout.setHorizontalGroup(
            settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        settingsFrameLayout.setVerticalGroup(
            settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );

        mainPanel.setPreferredSize(new java.awt.Dimension(480, 350));
        mainPanel.setRequestFocusEnabled(false);

        StopWatchGO.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        StopWatchGO.setText("Stop Watch");
        StopWatchGO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopWatchGOActionPerformed(evt);
            }
        });

        STOP.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        STOP.setText("Stop");
        STOP.setEnabled(false);
        STOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                STOPActionPerformed(evt);
            }
        });

        hideUntilAlarmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hideIcon.png"))); // NOI18N
        hideUntilAlarmButton.setEnabled(false);
        hideUntilAlarmButton.setIconTextGap(-46);
        hideUntilAlarmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hideUntilAlarmButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hideUntilAlarmButtonMouseExited(evt);
            }
        });
        hideUntilAlarmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideUntilAlarmButtonActionPerformed(evt);
            }
        });

        RESET.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        RESET.setText("Reset");
        RESET.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RESETActionPerformed(evt);
            }
        });

        Minutes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Minutes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Minutes.setText("00");

        Hours.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Hours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Hours.setText("00");

        Seconds.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Seconds.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Seconds.setText("00");

        TimerGO.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        TimerGO.setText("Timer");
        TimerGO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimerGOActionPerformed(evt);
            }
        });

        ButtonWebsite.setFont(new java.awt.Font("Arial", 2, 11)); // NOI18N
        ButtonWebsite.setText("" + (char)67 + (char)111 + (char)112 + (char)121 + (char)114 + (char)105 + (char)103 + (char)104 + (char)116 + (char)32 + (char)50 + (char)48 + (char)49 + (char)56 + (char)32 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109);
        ButtonWebsite.setToolTipText("Click to visit the website NathansSoftware.con");
        ButtonWebsite.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ButtonWebsite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonWebsiteMouseExited(evt);
            }
        });
        ButtonWebsite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonWebsiteActionPerformed(evt);
            }
        });

        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SettingsIcon.png"))); // NOI18N
        settingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsButtonMouseExited(evt);
            }
        });
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setText(" ");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Hide Alarm");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(STOP)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(StopWatchGO)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RESET)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(Hours, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Minutes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Seconds, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(171, 171, 171))))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(ButtonWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(hideUntilAlarmButton, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(settingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(127, 127, 127)
                                .addComponent(TimerGO, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsButton)
                .addGap(85, 85, 85)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TimerGO, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StopWatchGO, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Minutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Hours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Seconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(STOP)
                    .addComponent(RESET))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, 0)
                        .addComponent(hideUntilAlarmButton))
                    .addComponent(ButtonWebsite))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        settingsLabel.setFont(new java.awt.Font("Vrinda", 1, 18)); // NOI18N
        settingsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settingsLabel.setText("SETTINGS");

        lockComputerCheckBox.setText("Lock computer");
        lockComputerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockComputerCheckBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("On timer end");

        warningCheckBox.setText("Pre-timer warning (secs)");
        warningCheckBox.setToolTipText("Play alarm when time reaches this point");
        warningCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warningCheckBoxActionPerformed(evt);
            }
        });

        soundFileButton.setText("...");
        soundFileButton.setEnabled(false);
        soundFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundFileButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("sound file (.wav)");
        jLabel4.setEnabled(false);

        fileLocationField.setEnabled(false);

        launchStartupCheckBox.setText("Launch on startup");
        launchStartupCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                launchStartupCheckBoxItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("- Windows Only -");

        defaultSoundCheckBox.setSelected(true);
        defaultSoundCheckBox.setText("Use default alarm sound");
        defaultSoundCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                defaultSoundCheckBoxItemStateChanged(evt);
            }
        });

        warningAmountField.setText("120");
        warningAmountField.setEnabled(false);

        shutdownComputerCheckBox.setText("Shutdown computer");
        shutdownComputerCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shutdownComputerCheckBoxActionPerformed(evt);
            }
        });

        errorLabelS.setForeground(new java.awt.Color(255, 0, 0));
        errorLabelS.setText(" ");

        audioButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sound-icon.png"))); // NOI18N
        audioButton.setText("demo audio");
        audioButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                audioButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                audioButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                audioButtonMouseExited(evt);
            }
        });

        HomeButton.setText("Home");
        HomeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeButtonActionPerformed(evt);
            }
        });

        startOnLaunchCheckBox.setText("Start timer on opening");
        startOnLaunchCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startOnLaunchCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(launchStartupCheckBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addComponent(HomeButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(settingsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(settingsPanelLayout.createSequentialGroup()
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lockComputerCheckBox)
                                    .addComponent(shutdownComputerCheckBox))
                                .addGap(89, 89, 89)
                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(settingsPanelLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(audioButton)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(settingsPanelLayout.createSequentialGroup()
                                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fileLocationField, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                            .addGroup(settingsPanelLayout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(startOnLaunchCheckBox)
                                                    .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(settingsPanelLayout.createSequentialGroup()
                                                            .addGap(8, 8, 8)
                                                            .addComponent(jLabel4)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addComponent(soundFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(defaultSoundCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(warningCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(warningAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(16, 16, 16)))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, settingsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, settingsPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(errorLabelS, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsLabel)
                    .addComponent(HomeButton))
                .addGap(18, 18, 18)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(startOnLaunchCheckBox))
                .addGap(2, 2, 2)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(warningCheckBox)
                    .addComponent(lockComputerCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(warningAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shutdownComputerCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(defaultSoundCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(soundFileButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(audioButton)
                .addGap(5, 5, 5)
                .addComponent(errorLabelS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addComponent(launchStartupCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void defaultSoundCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_defaultSoundCheckBoxItemStateChanged
        
        defaultSound = defaultSoundCheckBox.isSelected();
        
        showEnableOptions();

        properties.setProperty("defaultSound", "" + defaultSoundCheckBox.isSelected());
               
        save();
    }//GEN-LAST:event_defaultSoundCheckBoxItemStateChanged

    private void launchStartupCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_launchStartupCheckBoxItemStateChanged
        errorLabelS.setText(" ");
        
        
        // this only works on windows so if it is another OS don't do it
        if (SystemUtils.IS_OS_WINDOWS) {
            
            // set the variable to be true/false
            launchOnStartup = launchStartupCheckBox.isSelected();
            
            properties.setProperty("launchOnStartup", "" + launchOnStartup); // set the property to equal that
            
            // save the properties
            save();
            
            if (launchStartupCheckBox.isSelected()) {
                // if it is selected go create a file in the windows startup for us

                // create a new file that leads to the current launch location
                // return new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                //then create the file you have just decided to make
                createBatFile();
            } else {
                // if its unselected go delete that file in the windows startup
                deleteBatFile();
            }

        } else {
            errorLabelS.setText("Cannot start app on startup");
            launchStartupCheckBox.setSelected(false); // if they try to do it with another OS then uncheck it to be clear it doesn't work
        }
    }//GEN-LAST:event_launchStartupCheckBoxItemStateChanged

    private void soundFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundFileButtonActionPerformed

        JFileChooser folderChooser = new JFileChooser(selectedSoundLocation);

        folderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this is so you can select a file or directory
        folderChooser.setApproveButtonText("Select");

        int returnValue = folderChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedSoundLocation = folderChooser.getSelectedFile().getAbsolutePath(); //this takes the file you have choosen and gets its full path name
            System.out.println("Folder: " + selectedSoundLocation);

            fileLocationField.setText(selectedSoundLocation);

            properties.setProperty("soundLocation", selectedSoundLocation);
            
            save();
        }
        // BEFORE if the field was blank it would reset to default on Cancel, but not anymore hahaha
        
    }//GEN-LAST:event_soundFileButtonActionPerformed

    private void warningCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warningCheckBoxActionPerformed
        warning = warningCheckBox.isSelected();

        // set the field to be correctly enabled/disabled
        warningAmountField.setEnabled(warning);

        // save the change in properties file
        properties.setProperty("warningBoolean", "" + warning);

        save();
    }//GEN-LAST:event_warningCheckBoxActionPerformed

    private void lockComputerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockComputerCheckBoxActionPerformed
        lockComputer = lockComputerCheckBox.isSelected();

        properties.setProperty("lockComputerBoolean", "" + lockComputer);

        save();
    }//GEN-LAST:event_lockComputerCheckBoxActionPerformed

    private void shutdownComputerCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shutdownComputerCheckBoxActionPerformed
        shutdownComputer = shutdownComputerCheckBox.isSelected();

        properties.setProperty("shutdownComputerBoolean", "" + shutdownComputer);

        save();
    }//GEN-LAST:event_shutdownComputerCheckBoxActionPerformed

    private void audioButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioButtonMouseEntered
        audioButton.setForeground(Color.red);
        audioButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sound-icon-selected.png")));
    }//GEN-LAST:event_audioButtonMouseEntered

    private void audioButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioButtonMouseExited
        audioButton.setForeground(null);
        audioButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sound-icon.png")));
    }//GEN-LAST:event_audioButtonMouseExited

    private void audioButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_audioButtonMouseClicked
        errorLabelS.setText(" ");
        
        playSound(selectedSoundLocation);
    }//GEN-LAST:event_audioButtonMouseClicked

    private void HomeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeButtonActionPerformed

        settingsFrame.dispose();

        // and also show are main frame again
        OurTimeFrame.setVisible(true);        
    }//GEN-LAST:event_HomeButtonActionPerformed

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        this.setVisible(false);

        settingsFrame.setSize(450,350); // this line must come first or it will not put it in the right place
        settingsFrame.setVisible(true);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setResizable(false);

        // setup background for settings frame and load content from settingsPanel
        setBackground(settingsFrame,settingsPanel,"/resources/settingsTex.png");
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void settingsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonMouseExited
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SettingsIcon.png")));
    }//GEN-LAST:event_settingsButtonMouseExited

    private void settingsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsButtonMouseEntered
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SettingsIcon-Selected.png")));
    }//GEN-LAST:event_settingsButtonMouseEntered

    private void ButtonWebsiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonWebsiteActionPerformed
        URI domain = null;
        try {
            domain = new URI("http://nathansoftware.com/wordpress/"); //launches the website
        } catch (URISyntaxException ex) {
            Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        openWebpage(domain);
    }//GEN-LAST:event_ButtonWebsiteActionPerformed

    private void ButtonWebsiteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseExited
        ButtonWebsite.setForeground(null);
    }//GEN-LAST:event_ButtonWebsiteMouseExited

    private void ButtonWebsiteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWebsiteMouseEntered
        ButtonWebsite.setForeground(Color.RED);
    }//GEN-LAST:event_ButtonWebsiteMouseEntered

    private void TimerGOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimerGOActionPerformed
        saveClockVariables();

        STOP.setEnabled(true);

        Hours.setEditable(false);
        Minutes.setEditable(false);
        Seconds.setEditable(false);

        StopWatchGO.setEnabled(false);
        RESET.setEnabled(false);
        hideUntilAlarmButton.setEnabled(true);

        creatNewTimer(-1); // run the timer downwards
    }//GEN-LAST:event_TimerGOActionPerformed

    private void RESETActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RESETActionPerformed
        // reset all the variables and text fields to 0

        STOP.setEnabled(false);

        Hours.setEditable(true);
        Minutes.setEditable(true);
        Seconds.setEditable(true);

        TimerGO.setEnabled(true);
        StopWatchGO.setEnabled(true);
        RESET.setEnabled(true);
        hideUntilAlarmButton.setEnabled(false);

        // set the variables
        clockSeconds = 0;
        clockMinutes = 0;
        clockHours = 0;

        errorLabel.setText(" ");
        errorLabelS.setText(" ");

        // update the fields the user sees
        updateFields();

        // and stop the sound if its playing

        try {
            clip.stop();
        } catch (Exception e) {
            // if its not playing when you press reset than it will throw this exception
            // but it doesn't matter cause its already not playing
        }
    }//GEN-LAST:event_RESETActionPerformed

    private void hideUntilAlarmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideUntilAlarmButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_hideUntilAlarmButtonActionPerformed

    private void hideUntilAlarmButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideUntilAlarmButtonMouseExited
        hideUntilAlarmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hideIcon.png")));
    }//GEN-LAST:event_hideUntilAlarmButtonMouseExited

    private void hideUntilAlarmButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideUntilAlarmButtonMouseEntered
        hideUntilAlarmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hideIcon-Selected.png")));
    }//GEN-LAST:event_hideUntilAlarmButtonMouseEntered

    private void STOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_STOPActionPerformed
        resetVars();
    }//GEN-LAST:event_STOPActionPerformed

    private void StopWatchGOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopWatchGOActionPerformed
        saveClockVariables();

        STOP.setEnabled(true);

        Hours.setEditable(false);
        Minutes.setEditable(false);
        Seconds.setEditable(false);

        TimerGO.setEnabled(false);
        RESET.setEnabled(false);
        hideUntilAlarmButton.setEnabled(false);

        creatNewTimer(1); // run the timer upwards
    }//GEN-LAST:event_StopWatchGOActionPerformed

    private void startOnLaunchCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startOnLaunchCheckBoxItemStateChanged
        // set the variable to be true/false
        startOnLaunch = startOnLaunchCheckBox.isSelected();

        properties.setProperty("startOnLaunch", "" + startOnLaunch); // set the property to equal that

        // save the properties
        save();
    }//GEN-LAST:event_startOnLaunchCheckBoxItemStateChanged

    
    public void showEnableOptions() {
            warningAmountField.setEnabled(warningCheckBox.isSelected());
            jLabel4.setEnabled(!defaultSoundCheckBox.isSelected());
            soundFileButton.setEnabled(!defaultSoundCheckBox.isSelected());
            fileLocationField.setEnabled(!defaultSoundCheckBox.isSelected());

    }
    
    public void setupDefaultSave() {
        properties.setProperty("warningAmount", "" + warningAmount);
        properties.setProperty("warningBoolean", "" + warning);
        properties.setProperty("sleepComputerBoolean", "" + sleepComputer);
        properties.setProperty("lockComputerBoolean", "" + lockComputer);
        properties.setProperty("defaultSound", "" + defaultSound);
        properties.setProperty("hoursStored", "" + clockHours);
        properties.setProperty("minutesStored", "" + clockMinutes);
        properties.setProperty("secondsStored", "" + clockSeconds);
        properties.setProperty("soundLocation", "" + selectedSoundLocation);
        properties.setProperty("launchOnStartup", "" + launchOnStartup);
        properties.setProperty("startOnLaunch", "" + startOnLaunch);
    }
    
    public void save() {
        System.out.println("Saving");
        try {
            properties.store(new FileOutputStream(thePlace + "save.properties"), null);
        } catch (IOException ex) {
            Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            // couldn't find file
        }
    }
    
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
    
    public void createBatFile() {
        String startupLocation = "";
        
        Boolean GoodOS = true;
        
        // if its a new version of windows look in the app data folder
        if (SystemUtils.IS_OS_WINDOWS_8 || SystemUtils.IS_OS_WINDOWS_10) {
            startupLocation = "C:\\Users\\" + SystemUtils.USER_NAME + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Java-StopWatchTimer-Startup.bat";
        }
        // for windows 7 and prior we will put it here
        else if (SystemUtils.IS_OS_WINDOWS) {
            startupLocation = "C:\\users\\All Users\\Start Menu\\Programs\\Startup\\";
        } 
        else {
            errorLabelS.setText("Cannot start app on startup");
			
            GoodOS = false;
        }
        
        // only continue if we have the right OS
        if (GoodOS) {
            // first we creat the new Bat file
            try {
                PrintWriter writer = new PrintWriter("C:\\Users\\" + SystemUtils.USER_NAME + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Java-StopWatchTimer-Startup.bat");

                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }


            String line = "javaw -jar -Xms1024m -Xmx1024m ";


            // try to find out the location being launched
            
            // so we get the location to our file
            String URL;
            try {
                URL = TimeFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

                // CHECK WHETHER THE EXE OR JAR IS INSTALLED
                // note that the word "start" closes the cmd after running it
                if (URL.contains("exe")) {
                    // if exe launch it by calling the pathway
                    line = "start " + URL.substring(1);
                } else {
                    // if jar launch it by calling the line seen above: javaw -jar -Xms1024m -Xmx1024m [PATHWAY]
                    line = "start " + line + URL.substring(1);
                }
                // we tell it to launch our file from the command line (except remove the first character of our file because it is a / which doesn't work)

            } catch (URISyntaxException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            //then write the bat file in the startup location (note: this is only works for windows because other OS's don't have this startup location nor do they have bat files)
            try {
                BufferedWriter outLoad = new BufferedWriter(new FileWriter(startupLocation));

                outLoad.write(line); //write the filename of the default survey as the first line

                System.out.println("Line: " + line);
                
                outLoad.close();
            } catch (IOException ex) {
                Logger.getLogger(TimeFrame.class.getName()).log(Level.SEVERE, null, ex); 
               //if theres an exception log it
            }
        }
    }
    
    public void deleteBatFile() {
        File bat = new File("C:\\Users\\" + SystemUtils.USER_NAME + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Java-StopWatchTimer-Startup.bat");
        
        bat.delete();
    }
    
    public void creatNewTimer(int factor) {
        // if we don't already have a timer going then create one
        if (going != true) {
            
            going = true;

            // create a new timer        
            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {

                    checkForEndGame(factor); // check if it has reached the end of the timer or the max of the stop watch4

                    // WHEN GOING = FALSE then the timer ends and rings
                    if (going == false) {
                        System.out.println("Killing timer");

                        timer.cancel();
                        timer.purge();
                    } else { // if the timer is NOT DONE then increment it

                        // increment the seconds variable every second
                        clockSeconds += factor;

                        checkForOverlap(factor);

                        updateFields();
                    }
                }
            }, 0, 1000); // the 0 is for "0 delay before excecution" the 1000 is for "1000 milleseconds between excecutions"
        }
    }
    
    public void saveClockVariables() {
        // get the stuff from the text fields
        try {
            errorLabel.setText(" ");
            clockMinutes = Integer.parseInt(Minutes.getText());
            clockHours = Integer.parseInt(Hours.getText());
            clockSeconds = Integer.parseInt(Seconds.getText());
        
        
            // and save them to our preferences file:
            properties.setProperty("hoursStored", "" + clockHours);
            properties.setProperty("minutesStored", "" + clockMinutes);
            properties.setProperty("secondsStored", "" + clockSeconds);

            save();
        } catch (Exception e) {
            errorLabel.setText("Invalid time");
        }
    }
    
    public void updateFields() {
        
        // if the number is two digits than show it
        String check = "" + clockHours;
        
        if (check.length() > 1) {
            Hours.setText("" + clockHours);
        }
        else {
            Hours.setText("0" + clockHours);
        }
        
        check = "" + clockMinutes;
        
        if (check.length() > 1) {
            Minutes.setText("" + clockMinutes);
        }
        else {
            Minutes.setText("0" + clockMinutes);
        }
        
        check = "" + clockSeconds;
        
        if (check.length() > 1) {
            Seconds.setText("" + clockSeconds);
        }
        else {
            Seconds.setText("0" + clockSeconds);
        }
    }
    
    public void checkForOverlap(int direction) {
        
        // when you reach 60 of something you go to 0 and when you reach 0 you go to 59 (the same for minutes)
        
        // so basically if you get to 60 seconds (and you are counting upwards)
        // then increment the minutes and reset the seconds
        if (clockSeconds == 60 && direction == 1) {
            clockSeconds = 0; //reset seconds to 0
            clockMinutes++; // a minute has happened upwards
        }
        // if you get to 0 seconds (and you are counting down)
        // then increment the minutes down and reset the seconds
        else if (clockSeconds < 0 && direction == -1) {
            clockSeconds = 59; //reset seconds to 59
            clockMinutes--;
        }
        // if you get to 60 minutes (and you are counting upwards)
        // then increment the hours and reset the minutes
        if (clockMinutes == 60 && direction == 1) {
            clockMinutes = 0; //reset minutes to 0
            clockHours++;
        }
        // if you get to 0 minutes (and you are counting down)
        // then increment the hours down and reset the minutes
        else if (clockMinutes < 0 && direction == -1) {
            clockMinutes = 59; //reset minutes to 59
            clockHours--;
        }
        
    }
    
    public void checkForEndGame(int factor) {
        // if the timer is at its maximum (and its counting up) or minimum (and counting down) it is finished
        if ((clockHours >= 60 & clockMinutes >= 60 & clockSeconds >= 60 & factor > 0) 
          | (clockHours <= 0 & clockMinutes <= 0 & clockSeconds <= 0 & factor < 0)) {
            // yep if you have been timing for 61 hours that is the limit sorry :)

            System.out.println("Reached End Game");
            
            playSound(selectedSoundLocation);
            endTimer(); // call the end function
            
        }
        
        // PRE WARNING
        // we find out the warning stuff
        int warningHours = warningAmount/3600; // basically get all the hours
        int warningMinutes = (warningAmount - warningHours*3600)/60; // and take the total subtract the hours and get all the minutes
        int warningSeconds = (warningAmount - warningHours*3600 - warningMinutes*60); // and take the total subtract the hours and subtract the minutes and get all the seconds
        
        //**System.out.println("H: " + warningHours + " M:" + warningMinutes + " S:" + warningSeconds);
        
        // if we are using the warning and it has reached the warning time
        if (warning && (clockHours == warningHours & clockMinutes == warningMinutes & clockSeconds == warningSeconds)) {
            System.out.println("Reached Warning");
            
            playSound(selectedSoundLocation);
        }
    }
    
    public void documentListeners() {
            // Listen for changes in the text
            warningAmountField.getDocument().addDocumentListener(new DocumentListener() {
              @Override
              public void changedUpdate(DocumentEvent e) {
                update();
              }
              @Override
              public void removeUpdate(DocumentEvent e) {
                update();
              }
              @Override
              public void insertUpdate(DocumentEvent e) {
                update();
              }

              public void update() {
                 try { 
                     
                    errorLabelS.setText(" ");
                     
                    warningAmount = Integer.parseInt(warningAmountField.getText());
                    
                    properties.setProperty("warningAmount", warningAmountField.getText());

                    save();
                 } catch (Exception e) {
                     errorLabelS.setText("Invalid input in warning field");
                 }
              }
            });
            
            
            fileLocationField.getDocument().addDocumentListener(new DocumentListener() {
              @Override
              public void changedUpdate(DocumentEvent e) {
                //updateDoc();
                // i don't think we need this one it doesn't seem to do anything
                
              }
              @Override
              public void removeUpdate(DocumentEvent e) {
                updateDoc();
              }
              @Override
              public void insertUpdate(DocumentEvent e) {
                updateDoc();
              }

              public void updateDoc() {
                                   
                    selectedSoundLocation = fileLocationField.getText();
                                         
                    properties.setProperty("soundLocation", fileLocationField.getText());

                    save();
              }
            });
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TimeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TimeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TimeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TimeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TimeFrame();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonWebsite;
    private javax.swing.JButton HomeButton;
    private javax.swing.JTextField Hours;
    private javax.swing.JTextField Minutes;
    private javax.swing.JButton RESET;
    private javax.swing.JButton STOP;
    private javax.swing.JTextField Seconds;
    private javax.swing.JButton StopWatchGO;
    private javax.swing.JButton TimerGO;
    private javax.swing.JLabel audioButton;
    private javax.swing.JCheckBox defaultSoundCheckBox;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel errorLabelS;
    private javax.swing.JTextField fileLocationField;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton hideUntilAlarmButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JCheckBox launchStartupCheckBox;
    private javax.swing.JCheckBox lockComputerCheckBox;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton settingsButton;
    private javax.swing.JFrame settingsFrame;
    private javax.swing.JLabel settingsLabel;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JCheckBox shutdownComputerCheckBox;
    private javax.swing.JButton soundFileButton;
    private javax.swing.JCheckBox startOnLaunchCheckBox;
    private javax.swing.JTextField warningAmountField;
    private javax.swing.JCheckBox warningCheckBox;
    // End of variables declaration//GEN-END:variables
}
