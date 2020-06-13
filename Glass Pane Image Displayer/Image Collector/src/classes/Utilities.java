/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import application.ImageCollector;
import application.MainToolbar;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Nathan
 */
public class Utilities {
        
    public static boolean popupAreYouSure(MainToolbar frame, String AYS) {
        
        /*int selectedOption = JOptionPane.showConfirmDialog(frame, 
                                  AYS, 
                                  "Choose", 
                                  JOptionPane.YES_NO_OPTION
                                  ); 
        
        return selectedOption == JOptionPane.YES_OPTION;*/
        return true;
    }
    
    // works badly if you don't pass the toolbar 
    public static File popupSelectFile(MainToolbar parent, String approve, String property) {
        return popupSelectFile(parent, approve, property, null);
    }
    
    public static File popupSelectFile(MainToolbar parent, String approve, String property, FileNameExtensionFilter filter) {

        File selected;
        
        // --- FILE CHOOSER --- //
        JFileChooser chooser = new JFileChooser(ImageCollector.saveProperties.getProperty(property));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //this makes it so you can only select a file
        chooser.setFileFilter(filter);
        
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        
        int retrival = chooser.showDialog(parent, approve); // note: chooser shows on top of this (i.e. on top of the MainToolbar
        
        
        // IF YOU SELECT THE APPROVE OPTION IT DOES ALL THIS::
        if (retrival == JFileChooser.APPROVE_OPTION) {
            
            // figure out what they choose
            selected = chooser.getSelectedFile();
            
            // store the defaults
            ImageCollector.saveProperties.setProperty(property, "" + selected.getPath());
            ImageCollector.save();
                    
            
            return selected;
        } else {
            return null;
        }
        
    }
}
