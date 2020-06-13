/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes.Movement;

import application.ImageCollector;
import application.MainToolbar;
import classes.ContextMenuMouseListener;
import classes.ImageWindowObject;
import static application.MainToolbar.cropImage;
import static application.MainToolbar.killImage;
import static application.MainToolbar.zoomImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nathan
 */
public class CustomFrame extends JFrame {
    
    public ContextMenuMouseListener click;
    public ComponentResizer cr;
    
    private Point initialClick;
    private JFrame parent;          // so that our motion frame can do some editing
    
    private boolean dragging = false; // BOOLEAN TO DETERMINE IF THE DRAGGING PROCESS HAS ALREADY BEGUN (if so let it continue until the end)
    
    private Insets dragInsets = new Insets(15,15,15,15);

    
    public CustomFrame(MainToolbar mainToolbar, ImageWindowObject window) {
        
        /// MOTION FRAME
        this.parent = this;
        
        click = new ContextMenuMouseListener(mainToolbar, window); //initialize the edit menu
        
        this.setUndecorated(true);
        
        //** window.windowFrame.setOpacity((float) 0.5);
        this.setBackground(new Color(0, 0, 0, (int)(ImageCollector.toolbarOpacity*255)));
        this.setLayout(new BorderLayout()); // card layout to fit a picture in the middle
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        
        // COMPONENT RESIZER
        // call the component resizer class to allow the user to resize the undecorated frame
        cr = new ComponentResizer(window);
        cr.registerComponent(this);
        cr.setSnapSize(new Dimension(10, 10));
        cr.setMinimumSize(new Dimension(30,30));
        cr.setMaximumSize(new Dimension(1000,1000));
        cr.setDragInsets(new Insets(10,10,10,10));
                  
        // ---- WINDOW RIGHT CLICK CONTEXT MENU ----
        addMouseListener(new MouseAdapter() { //add a mouse listener to that newly added object
            
            @Override
            public void mouseClicked(MouseEvent evt) { 
                // ** Object item = evt.getSource(); // figure out which button was just clicked
                                
                // ** collection.get(collection.indexOf(window))
                
                // WHEN THIS BUTTON IS CLICKED we determine if it is a left or right click
                    if(SwingUtilities.isRightMouseButton(evt)){
                        
                        click.mouseClicked(mainToolbar, evt, window);
                    }
                    
                
            }
        });
        
        // ---- WINDOW RIGHT CLICK KEY SHORTCUTS ----
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                                
                // KEY EVENT TO FIRE ONE OF THE BUTTONS
                // if you press Z it zooms, if you hit DELETE it closes
                switch (e.getKeyChar()) {
                    case 'Z': // NOTE: capital Z (SHIFT+Z) = zoom
                        zoomImage(mainToolbar, window);
                        break;
                    case 'X': // NOTE: capital X (SHIFT+X) = crop
                        cropImage(mainToolbar, window);
                        break;
                    case KeyEvent.VK_BACK_SPACE: // = delete
                        killImage(mainToolbar, window); 
                        break;
                    case 'B': // NOTE: capital B (SHIFT+B) = send to back 
                        window.windowFrame.toBack();
                        break;
                    case 'A': // NOTE: capital A (SHIFT+A) = on/off aspect ratio
                        window.maintainAspect = !window.maintainAspect;
                        break;
                    case 'H': // NOTE: capital H (SHIFT+H) = hide
                        window.windowFrame.setState(JFrame.ICONIFIED);
                        break;
                        
                    default:
                        break;
                }
            } 

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                
                // if you clicked in a valid spot (not on the resizable insets)
                if (e.getPoint().getX() > dragInsets.left &&
                    e.getPoint().getY() > dragInsets.top &&
                    e.getPoint().getY() < - dragInsets.bottom + (parent.getHeight()) &&
                    e.getPoint().getX() < - dragInsets.right + (parent.getWidth())) {
                    
                    //** System.out.println("PRESS");
                    
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
                    e.getPoint().getY() < - dragInsets.bottom + (parent.getHeight()) &&
                    e.getPoint().getX() < - dragInsets.right + (parent.getWidth())) 
                        && dragging == true)) {
                    
                    //** System.out.println("DRAGGING: " + e.getPoint().getX() + ", " + e.getPoint().getY() + " - " + parent.getWidth() + "," + parent.getHeight());
                    
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
                    
                }
            }
        });
    }
    
}
