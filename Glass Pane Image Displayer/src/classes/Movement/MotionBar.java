/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.Movement;

import application.ImageCollector;
import application.MainToolbar;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

/**
 *
 * @author Nuser
 */
public class MotionBar extends JMenuBar {
    private Point initialClick;
    private JFrame parent;
    
    public MotionBar(final MainToolbar parent, Component... components){
        this.parent = parent;

        for (Component obj : components) {

            obj.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                        initialClick = e.getPoint();
                        getComponentAt(initialClick);
                    }
            });

            obj.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {

                        // get location of Window
                        int thisX = parent.getLocation().x;
                        int thisY = parent.getLocation().y;

                        // Determine how much the mouse moved since the initial click
                        int xMoved = e.getX() - initialClick.x;
                        int yMoved = e.getY() - initialClick.y;

                        // Move window to this position
                        int X = thisX + xMoved;
                        int Y = thisY + yMoved;
                        parent.setLocation(X, Y);
                        
                        if (ImageCollector.anchorToolbar) {
                            ImageCollector.anchorToolbar = false;
                            
                            // SAVE TO PROPERTIES
                            ImageCollector.saveProperties.setProperty("anchorToolbar", "" + ImageCollector.anchorToolbar);
                            ImageCollector.save();
                            
                            parent.updateParent();
                        }
                    }
            });
        }
    }
}
