/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import classes.LayerObject;
import classes.PowerPointFiling;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author Nuser
 */
public class ImageCollector {
    
    
    // Initialize save/properties file
    public static Properties saveProperties = new Properties();
    
    // SAVE THE SETTINGS TO THE SETTINGS FILE!
    public static void save() {
        try {
            saveProperties.store(new FileOutputStream("settings.properties"), null);
        } catch (Exception e) {
            // ... oops oh well
        }
    }
    
    
    public static void loadSettings() {
        recentPathToSaveCollection = saveProperties.getProperty("recentPathToSaveCollection");
        recentPathToLoadCollection = saveProperties.getProperty("recentPathToLoadCollection");
        recentPathToOpen = saveProperties.getProperty("recentPathToOpen");
        zoomFactor = Double.parseDouble(saveProperties.getProperty("zoomFactor"));
        toolbarOpacity = Double.parseDouble(saveProperties.getProperty("toolbarOpacity"));
        anchorToolbar = Boolean.parseBoolean(saveProperties.getProperty("anchorToolbar"));
        showToolbarOnTop = Boolean.parseBoolean(saveProperties.getProperty("showToolbarOnTop"));
        preserveAspect = Boolean.parseBoolean(saveProperties.getProperty("preserveAspect"));
    }
    
    public static void saveSettings() {
        saveProperties.setProperty("recentPathToSaveCollection", "" + recentPathToSaveCollection);
        saveProperties.setProperty("recentPathToLoadCollection", "" + recentPathToLoadCollection);
        saveProperties.setProperty("recentPathToOpen", "" + recentPathToOpen);
        saveProperties.setProperty("zoomFactor", "" + zoomFactor);
        saveProperties.setProperty("toolbarOpacity", "" + toolbarOpacity);
        saveProperties.setProperty("anchorToolbar", "" + anchorToolbar);
        saveProperties.setProperty("showToolbarOnTop", "" + showToolbarOnTop);
        saveProperties.setProperty("preserveAspect", "" + preserveAspect);
    }
    ///////////////////
    
    public static boolean barHorizontal = true;     // toolbar either horiztonal or vertical
    public static boolean showToolbarOnTop = false; // the main menu toolbar stays above other windows
    public static String recentPathToOpen = "/";    // the folder to look in to load images
    public static String recentPathToLoadCollection = "/"; // the folder to look in to load collections
    public static String recentPathToSaveCollection = "/"; // the folder to look in to save collections
    
    public static double zoomFactor = (double) 2 / (double) 3; // the portion of the screen filled when you zoom in
    public static double toolbarOpacity = 0.8;                  // the opacity of the main toolbar (kindof pointless TODO)
    
    public static boolean preserveAspect = false; // wether you preserve the aspect ration of the image windows
    public static boolean anchorToolbar = false;
    
    public static Dimension windowDefaultSize = new Dimension(200,150);  // the default dimension of the windows

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            
            // load the settings into the program
            saveProperties.load(new FileInputStream("settings.properties"));
            loadSettings();
        } catch (Exception e) {
            // no properties file
            System.out.println("LOADING DEFAULT SAVE");
            saveSettings();
            
        }   
                
        MainToolbar app = new MainToolbar();
                                
        
        // SETUP INTERFACE WITH SETTINGS
        app.MenuShowToolbarOnTop.setSelected(showToolbarOnTop);
        app.MenuAnchorToolbar.setSelected(anchorToolbar);
        app.MenuPreserveAspectSetting.setSelected(preserveAspect);
        
        app.setVisible(true);
        app.setSize(app.getPreferredSize());
        app.setResizable(false);
        //app.setOpacity((float) toolbarOpacity);
        app.setBackground(new Color(240,240,240));//, (int)(ImageCollector.toolbarOpacity*255)));
        app.setLocationRelativeTo(null);
        app.setAlwaysOnTop(showToolbarOnTop);
        
        // this performs updates to the anchoredness of toolbar based upon the value we got from the save file
        app.updateParent();
        
        
        ///////////////////////////////////////
        // handle args
        // LOAD FILES INTO THE IMAGE COLLECTOR
        ///////////////////////////////////////
        for (String argument : args) {
            System.out.println("arg found: " + argument);
            File file = new File(argument);
        
            // if we loaded something
            if (file.exists()) {
                System.out.println("LOADING SOMETHING");
                XMLSlideShow ppt = null;
                try {
                    ppt = new XMLSlideShow(new FileInputStream(file));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                }

                List<LayerObject> newCollection = PowerPointFiling.load(ppt, app);
                
                app.addCollection(newCollection);

                app.showToolbar();

            }
        
        }
        
    }
    
    
    
}
