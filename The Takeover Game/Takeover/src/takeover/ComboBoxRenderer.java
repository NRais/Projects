/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Nuser
 */
class ComboBoxRenderer extends JLabel implements 
        ListCellRenderer {
    
    public ComboBoxRenderer() {
        setOpaque(false);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }
    
     /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    @Override
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        // setup what happens when we hover over an item
        if (isSelected) {
            setBackground(Color.red);
            setForeground(Color.red);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        //Set the icon and text.  If icon was null, say so.
        ImageIcon icon = null;
        // if random map
        if ("random".equals((String)value)) {
            icon = resizeImage(15, 15, "/resources/menu/icons/random.png");
        }  // if default map    
        else if (Takeover.defaultMaps.contains((String)value)) {
            icon = resizeImage(15, 15, "/resources/menu/icons/default.png");
        } // else custom map         
        else if ("custom".contains((String)value)) {
            icon = resizeImage(15, 15, "/resources/menu/icons/custom.png");
        } // else user made map 
        else {
            icon = resizeImage(15, 15, "/resources/menu/icons/user.png");
        }
        
        setIcon(icon);
        if (icon != null) {
            setText((String)value);
            setFont(list.getFont());
        } else {
           System.out.println("ERROR NULL ICON IN DROPDOWN BOX");
        }

        return this;
    }
    
    private ImageIcon resizeImage(int x, int y, String img) {
        
        Image temp = new ImageIcon(getClass().getResource(img)).getImage();
        
        temp = temp.getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH);
        
        return new ImageIcon(temp);
    }
}
