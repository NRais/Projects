/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.Movement;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;

/**
 *
 * @author Nuser
 */
public class MotionLabel extends JLabel {
    private Point initialClick;
    private JLabel entity;
    
    private boolean dragging = false; // BOOLEAN TO DETERMINE IF THE DRAGGING PROCESS HAS ALREADY BEGUN (if so let it continue until the end)
    
    private Insets dragInsets = new Insets(15,15,15,15);

    public MotionLabel(){
    this.entity = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                // if you clicked in a valid spot (not on the resizable insets)
                if (e.getPoint().getX() > dragInsets.left &&
                    e.getPoint().getY() > dragInsets.top &&
                    e.getPoint().getY() < - dragInsets.bottom + (entity.getHeight()) &&
                    e.getPoint().getX() < - dragInsets.right + (entity.getWidth())) {
                    
                    //* System.out.println("PRESS");
                    
                    initialClick = e.getPoint();
                    getComponentAt(initialClick);
                    
                    // note that you have already been dragging
                    // until you let go it will override other aspect and let you keep dragging
                    dragging = true;
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                // now you have stopped dragging
                dragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                
                
                // IF you have clicked not on the resizable inset area
                // & IF you have already been dragging (i.e you clicked properly inside the thing)
                if (initialClick != null &&
                        ((e.getPoint().getX() > dragInsets.left &&
                    e.getPoint().getY() > dragInsets.top &&
                    e.getPoint().getY() < - dragInsets.bottom + (entity.getHeight()) &&
                    e.getPoint().getX() < - dragInsets.right + (entity.getWidth())) 
                        && dragging == true)) {
                    
                    //* System.out.println("DRAGGING: " + e.getPoint().getX() + ", " + e.getPoint().getY() + " - " + entity.getWidth() + "," + entity.getHeight());
                    
                    // get location of Window
                    int thisX = entity.getLocation().x;
                    int thisY = entity.getLocation().y;

                    // Determine how much the mouse moved since the initial click
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;

                    // Move window to this position
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    entity.setLocation(X, Y);
                    
                }
            }
        });
    }
}
