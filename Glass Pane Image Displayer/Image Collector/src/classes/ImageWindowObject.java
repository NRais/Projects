/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import classes.Movement.CustomFrame;
import application.ImageCollector;
import application.MainToolbar;
import java.awt.Dimension;
import javax.swing.ImageIcon;

/**
 *
 * @author Nuser
 */
public class ImageWindowObject {
    
    public CustomFrame windowFrame;
    public ImageIcon windowIcon; //this is the ORIGINAL image - not resized
        
    public Dimension windowSize;
    
    public boolean maintainAspect = ImageCollector.preserveAspect; //start out with the default
    
    public int currentLayer;
    
    public ImageWindowObject(MainToolbar parent, ImageIcon picture, Dimension size, int layer) {
        
        windowFrame = new CustomFrame(parent, this);
        windowIcon = picture;
        windowSize = size;
        
        currentLayer = layer;
    }
    
    
    // return a ratio of X to Y
    // (ex. 3/2 which can be used to properly scale this frame)
    public double getAspectRatioX() {
        double XtoY = (double) windowIcon.getIconWidth() / (double) windowIcon.getIconHeight();
        
        return XtoY;
    }
    
    // return a ratio of Y to X
    // (ex. 2/3 which can be used to properly scale this frame)
    public double getAspectRatioY() {
        double YtoX = (double) windowIcon.getIconHeight() / (double) windowIcon.getIconWidth();
        
        return YtoX;
    }
    
    public void setSize(Dimension size) {
        windowSize = size;
        windowFrame.setSize(size);
    }
    
    public void regenerate() {
        // resize the frame to fit properly, just fit the current width and then redraw the height to match

        this.setSize(new Dimension(windowSize.width, (int) (windowSize.width*getAspectRatioY()) ));

        // reload the image to fit properly
        ImageManipulator.resizeWindowBackground(this);
        
        windowFrame.setVisible(true);
    }
}
