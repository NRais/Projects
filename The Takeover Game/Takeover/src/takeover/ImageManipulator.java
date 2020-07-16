/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Nuser
 */
public class ImageManipulator {
    
    // FUNCTON THAT RESIZES AN IMAGE
    // take a image and resize it to an X and Y     
    public static Image resizeToBig(Image original, int biggerWidth, int biggerHeight) {
        
        Image resizedImage = original;
        resizedImage = resizedImage.getScaledInstance(biggerWidth, biggerHeight, java.awt.Image.SCALE_SMOOTH);
        
        return resizedImage;
    }
    
    // THE FOLLOWING PROCEDURE RESIZES AN IMAGE
    // takes a JLABEL and assigns it
    public static void resizeImage(JLabel I, int x, int y, String img) {
        try {
            Image temp = ImageIO.read(ClassLoader.getSystemResource(img));
            // scale the image so it fits in the space
            temp = temp.getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH);
            
            I.setIcon(new ImageIcon(temp));
        } catch (IOException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    // THE FOLLOWING FUNCTION TAKE A IMAGE AND RESIZES IT TO A JFRAME
    // it then returns the new image
    public Image resizeImageAspect(JFrame frame, String img) {
         // generate a buffered image so we can measure its size
            Image myImage = new ImageIcon(getClass().getResource(img)).getImage();
                    
            
            BufferedImage i = null;
            try {
                i = ImageIO.read(getClass().getResource(img));
            } catch (IOException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // if the images Y is greater than its X then scale based upon that dimension
            // otherwise scale it the other direction
            if (i.getWidth()/i.getHeight() >= frame.getWidth()/frame.getHeight()) {
                // assuming that the image is proportionally more widescreen than the 800*600 frame we scale the image to match the Y direction and be a proportional X
                // ** System.out.println(": " + (double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth() + "," + frame.getHeight());

                myImage = myImage.getScaledInstance((int)((double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth()), frame.getHeight(), java.awt.Image.SCALE_SMOOTH);
            } else {
                // in the other dimension
                myImage = myImage.getScaledInstance(frame.getWidth(), (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()), java.awt.Image.SCALE_SMOOTH);
            }
            
            return myImage;
    }
        
    
    // THE FOLLOWING FRAMRE BACKGROUND PROCEDURE
    // takes a image and sticks it in the background of a frame
    // (ALSO allows for scaling, boolean determines whether it scales the image or not)
    public void frameSetBackground(JFrame frame, String imageLocation) {
        
        // NOTE: it is a little slow loading the images especially if it is scaling them
        
        System.out.println("Paning");
        
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource(imageLocation));
        
        Image myImage = icon.getImage();
                
        JLabel j = new JLabel(new ImageIcon(myImage));
        j.setLayout( null );
        
        frame.setContentPane(j);
    }
    
    public static void frameSetBackground(JFrame frame, Image myImage) {
        JLabel j = new JLabel(new ImageIcon(myImage));
        j.setLayout( null );
        
        frame.setContentPane(j);
    }
}
