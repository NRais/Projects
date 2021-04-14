/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import application.MainToolbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
/**
 *
 * Copyright 2018 Nathan Rais 
 * 
 *      This file is part of The Easy Survey Creator.
 *
 *   The Easy Survey Creator by Nathan Rais is free software 
 *   but is licensed under the terms of the Creative Commons 
 *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *   you may redistribute this as long as you give attribution and do not 
 *   modify any part of this software in any way.
 *
 *   The Easy Survey Creator is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   CC BY-ND license for more details.
 *
 *   You should have received a copy of the CC BY-ND license along with 
 *   The Easy Survey Creator. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 *
 */
public class ContextMenuMouseListener extends MouseAdapter {
    private JPopupMenu popup = new JPopupMenu();
    
    //private ImageWindowObject currentItem;

    private JMenu edit;
    private Action cropAction;
    private Action flipHorizontallyAction;
    private Action flipVerticallyAction;
    
    private Action removeAction;
    private Action zoomAction;
    private Action sendBackAction;
    private Action hideAction;
    
    private JMenuItem layerAction;
    
    
    private JCheckBoxMenuItem preserveAspect;
    
    public ContextMenuMouseListener(MainToolbar thisFrame, ImageWindowObject currentItem) {
        // initializing all the buttons to their correct action
        edit = new JMenu("Edit");
                
        
        hideAction = new AbstractAction("<html>Hide Window   <font color=\"#86aeef\"><i>SHIFT+H</i></font>") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentItem.windowFrame.setState(JFrame.ICONIFIED);
            }
        };
        
        sendBackAction = new AbstractAction("<html>Send to Back   <font color=\"#86aeef\"><i>SHIFT+B</i></font>") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                // send this layer behind all others
                currentItem.windowFrame.toBack();
            }
        };
        
        removeAction = new AbstractAction("<html>Remove Window   <font color=\"#86aeef\"><i>BACK SPACE</i></font>") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (Utilities.popupAreYouSure(thisFrame, "<html>Are you sure you want to continue?<br><center><i> (this cannot be undone)")) {
                    thisFrame.killImage(thisFrame, currentItem);
                }
            }
        };

        zoomAction = new AbstractAction("<html>Zoom Window   <font color=\"#86aeef\"><i>SHIFT+Z</i></font>") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                thisFrame.zoomImage(thisFrame, currentItem);
            }
        };
        
        cropAction = new AbstractAction("<html>Crop Window   <font color=\"#86aeef\"><i>SHIFT+X</i></font>") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                thisFrame.cropImage(thisFrame, currentItem);
            }
        };
        
        preserveAspect = new JCheckBoxMenuItem();
        preserveAspect.setText("<html>Preserve Aspect Ratio   <font color=\"#86aeef\"><i>SHIFT+A</i></font>");
        preserveAspect.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                
                currentItem.maintainAspect = preserveAspect.isSelected();
            }
        });
        
        
        flipHorizontallyAction = new AbstractAction("Flip Horizontally") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentItem.windowIcon = ImageManipulator.flipImageHorizontally(currentItem.windowIcon);

                
                ImageManipulator.resizeWindowBackground(currentItem); // reload window
                
                currentItem.windowFrame.setVisible(true);
            }
        };
        flipVerticallyAction = new AbstractAction("Flip Vertically") {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentItem.windowIcon = ImageManipulator.flipImageVertically(currentItem.windowIcon);
                
                ImageManipulator.resizeWindowBackground(currentItem); //reload window
                
                
                currentItem.windowFrame.setVisible(true);
            }
        };
        
        // assign the layer text to be the current layer
        layerAction = new JMenuItem("<html>Layer:  <i><b>" + thisFrame.collection.get(currentItem.currentLayer).name + "</b></i>");
        
        layerAction.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                // allow them to change the layer
                JComboBox field = new JComboBox();
                // add all layers
                for (LayerObject l : thisFrame.collection) {
                    field.addItem(l.name);
                }
                int selectedOption = JOptionPane.showConfirmDialog(thisFrame, field, "Edit Layer",
                                              JOptionPane.DEFAULT_OPTION);
                
                // if they actually typed a name
                if (selectedOption == JOptionPane.YES_OPTION && !field.getSelectedItem().equals(thisFrame.collection.get(currentItem.currentLayer).name) && !field.getSelectedItem().equals("")) {
                    
                    // get the new layer
                    int i = thisFrame.getLayer((String)field.getSelectedItem()); 
                    
                    // assign the image to that layer instead
                    if (i != -1) {
                        // remove from the old layer
                        thisFrame.collection.get(currentItem.currentLayer).layer.remove(currentItem);
                        // set new layer
                        currentItem.currentLayer = i;
                        // add to new layer
                        thisFrame.collection.get(i).layer.add(currentItem);
                    }
                }
            }
        });
        
        
        popup.add(layerAction);
        
        popup.addSeparator();
        
        popup.add(hideAction);
        popup.add(sendBackAction);
        popup.add(zoomAction);
        
        popup.addSeparator();
            
        popup.add(removeAction);
        popup.add(edit);
        edit.add(cropAction);
        edit.add(flipHorizontallyAction);
        edit.add(flipVerticallyAction);
        
        popup.addSeparator();
        
        popup.add(preserveAspect);
        
    }

    public void mouseClicked(MainToolbar parent, MouseEvent e, ImageWindowObject item) {
        if (item.windowFrame.isFocusable()) {
                zoomAction.setEnabled(true);
                removeAction.setEnabled(true);
                sendBackAction.setEnabled(true);
                cropAction.setEnabled(true);
                layerAction.setEnabled(true);
                preserveAspect.setEnabled(true);
                preserveAspect.setSelected(item.maintainAspect);

                // Show a certain part of the popup
                int nx = e.getX();

                if (nx > 500) {
                    nx = nx - popup.getSize().width;
                }

                popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
                // reload what layer it is part of
                layerAction.setText("<html>Layer:  <i><b>" + parent.collection.get(item.currentLayer).name + "</b></i>");
        }
    }
    
}