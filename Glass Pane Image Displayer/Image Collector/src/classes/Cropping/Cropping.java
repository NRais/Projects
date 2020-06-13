/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.Cropping;

import application.MainToolbar;
import classes.ImageManipulator;
import classes.ImageWindowObject;
import classes.Movement.MotionLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Nuser
 */
  
public class Cropping extends JPanel
{
    ImageIcon formerIcon; // variable to store the original icon so it can be undone
    BufferedImage image;
    Dimension size;
    public JLabel clip;
    
    ImageWindowObject original;
        
    MainToolbar theToolbar;
  
    public Cropping(ImageWindowObject original, MainToolbar obj)
    {
        theToolbar = obj;
        
        this.setLayout(null);
        this.formerIcon = original.windowIcon;
        this.image = ImageManipulator.toBufferedImage(original.windowIcon.getImage());
        this.original = original;
        size = new Dimension(image.getWidth(), image.getHeight());
    }
  
    @Override
    protected void paintComponent(Graphics g)
    {
        Dimension l = clip.getSize();
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int x = (getWidth() - size.width)/2;
        int y = (getHeight() - size.height)/2;
        g2.drawImage(image, x, y, this);

        System.out.println("PAinting");
                    
        clip.setSize(l);
        clip.setVisible(true);
        
        // TODO make it so red dot doesn't resize by itself
        // TODO make it so red dot starts larger
    }
  
    @Override
    public Dimension getPreferredSize()
    {
        return size;
    }
  
    public void createClip()
    {
        System.out.println("Creating");
        clip = new MotionLabel();
        clip.setBorder(BorderFactory.createLineBorder(Color.red));

        this.add(clip);
        clip.setVisible(true);

        clip.setBounds(10, 100, image.getWidth()/4, image.getWidth()/4);
    }
  
    // UNDO CROPPED IMAGE
    private void undoClip() 
    {
        // RELOAD ORIGINAL ONTO FRAME
        // get icon from the clip label
                
        original.windowIcon = formerIcon;
        
        original.regenerate();
    }
    
    // SAVE CROPPED IMAGE
    private void clipImage(Boolean createNewImage)
    {
        BufferedImage clipped = null;
        try
        {
            int w = clip.getWidth();
            int h = clip.getHeight();
            int x0 = (getWidth()  - size.width)/2;
            int y0 = (getHeight() - size.height)/2;
            int x = clip.getX() - x0;
            int y = clip.getY() - y0;
            
            // IF THEY HAVE COOSEN SOMETHING OUTSIDE OF THE WINDOW
            // fix any negative values in x or y
            System.out.println("ERROR :" + w + "," + h + " | " + x + "," + y);
            if (x < 0) {
                w = w + x; // fix width to account for the out of bounds x
                x = 0;     // put x inbounds
            }
            if (y < 0) {
                h = h + y; // fix height to account for the out of bounds y
                y = 0;     // put y inbounds
            }
            // fix any overflow in width or hieght
            if (x + w > image.getWidth()) {
                w = image.getWidth() - x;       //reduce width so it doesn't exceed imaeg
            }
            if (y + h > image.getHeight()) {
                h = image.getHeight() - y;      //reduce height so it doesn't exceed imaeg
            }
            System.out.println("ERROR :" + w + "," + h + " | " + x + "," + y);
            
            clipped = image.getSubimage(x, y, w, h);
        }
        catch(RasterFormatException rfe)
        {
            System.out.println("raster format error: " + rfe.getMessage());
            return;
        }
        
        
        if (createNewImage) {
            
            System.out.println("NEW");
            
            theToolbar.addImageToCollection(clipped);
        
        } else {
            
            System.out.println("UPDATE");
            
            // RELOAD IMAGE ONTO WINDOW STORED IMAGE           
            original.windowIcon = new ImageIcon(clipped);

            // resize the frame to fit properly, just fit the current width and then redraw the height to match
            original.regenerate();
        }
    }
  
    public JPanel getUIPanel()
    {
        JButton undo = new JButton("revert crop");
        undo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                undoClip();
            }
        });
        
        JButton cropper = new JButton("crop image");
        cropper.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                clipImage(false);
            }
        });
        
        JButton split = new JButton("split image");
        split.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                clipImage(true);
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(undo);
        panel.add(cropper);
        panel.add(split);
        return panel;
    }

}

