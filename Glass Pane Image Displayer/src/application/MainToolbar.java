/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import classes.PowerPointFiling;
import classes.Movement.ComponentResizer;
import classes.Cropping.Cropping;
import classes.Movement.DummyJCheckBoxMenuItem;
import classes.Movement.DummyJMenuItem;
import classes.ImageManipulator;
import classes.ImageWindowObject;
import classes.LayerObject;
import classes.Movement.MotionBar;
import classes.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author Nuser
 */
public class MainToolbar extends javax.swing.JFrame {

    // this is the collection of images
    // in it is a variety of layers
    // in each layer are a number of ImageWindowObjects (which repersent the windows)
    public List<LayerObject> collection = new ArrayList<>();
        
    JLabel bannerLogo = new JLabel();
        
    
    public static String BACKGROUND_OPACITY = "background opacity";
    public static String ZOOM_SIZE = "Set zoom size %";
    
    /**
     * Creates new form MainToolbar
     */
    public MainToolbar() {	
        //NOTE all saves are loaded in the ImageCollector class
        initComponents();
        
        
        // add a new layer 0 to the collection (for all the items to go into)
        addNewLayer("Default");
        
        
        bannerLogo.setIcon(ImageManipulator.resizeImageAspect(this.getSize() ,new ImageIcon(getClass().getResource("/resources/nathansoftware.png")), false));
        bannerLogo.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                URI domain = null;
                try {
                    domain = new URI("" + (char)104 + (char)116 + (char)116 + (char)112 + (char)58 + (char)47 + (char)47 + (char)119 + (char)119 + (char)119 + (char)46 + (char)78 + (char)97 + (char)116 + (char)104 + (char)97 + (char)110 + (char)115 + (char)111 + (char)102 + (char)116 + (char)119 + (char)97 + (char)114 + (char)101 + (char)46 + (char)99 + (char)111 + (char)109); //launches the website
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                }
                openWebpage(domain);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        this.getJMenuBar().add(bannerLogo);
        
        // setup vertical or horizontal jmenubar
        // TODO alignToolbar();
    }
    
    /*private void alignToolbar() {
        if (ImageCollector.barHorizontal) {
            jMenuBar1.setLayout(new GridLayout(1,0));
            jMenu1.setText("File");
            jMenu2.setText("Edit");
            jMenu3.setText("Layering");
            jMenu5.setText("Settings");
        } else {
            jMenuBar1.setLayout(new GridLayout(0,1));
            jMenu1.setText("");
            jMenu2.setText("");
            jMenu3.setText("");
            jMenu5.setText("");
            
        }
    }*/
    
    public void showToolbar() {
        // if we want it always on top then we do so
        if (ImageCollector.showToolbarOnTop)
            this.requestFocus();
    }
    
    private static void openWebpage(URI uri) {
       Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
       if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
           try {
               desktop.browse(uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupSlider = new javax.swing.JDialog();
        genericSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        genericContinueButton = new javax.swing.JButton();
        zoomFrame1 = new javax.swing.JFrame();
        jLabel2 = new javax.swing.JLabel();
        zoomExit = new javax.swing.JLabel();
        LayerMenuRename = new classes.Movement.DummyJMenuItem();
        LayerMenuHideAll = new classes.Movement.DummyJMenuItem();
        LayerMenuShowAll = new classes.Movement.DummyJMenuItem();
        LayerMenuSendBackAll = new classes.Movement.DummyJMenuItem();
        LayerMenuResetAll = new classes.Movement.DummyJMenuItem();
        LayerMenuFocusAll = new classes.Movement.DummyJCheckBoxMenuItem();
        LayerMenuResizeAll = new classes.Movement.DummyJMenuItem();
        LayerMenuMerge = new classes.Movement.DummyJMenuItem();
        LayerMenuRemoveLayer = new classes.Movement.DummyJMenuItem();
        javax.swing.JMenuBar jMenuBar1 = null;
        jMenu1 = new javax.swing.JMenu();
        MenuOpen = new javax.swing.JMenuItem();
        MenuAdd = new javax.swing.JMenuItem();
        MenuClose = new javax.swing.JMenuItem();
        MenuSave = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        MenuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        MenuOpenFromFolder = new javax.swing.JMenuItem();
        MenuOpenFromCamera = new javax.swing.JMenuItem();
        MenuOpenFromClipboard = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        LayerMenu = new javax.swing.JMenu();
        MenuNewLayer = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        MenuZoomSetting = new javax.swing.JMenuItem();
        MenuWindowSizeSetting = new javax.swing.JMenuItem();
        MenuPreserveAspectSetting = new javax.swing.JCheckBoxMenuItem();
        MenuToolbarSettings = new javax.swing.JMenu();
        MenuAnchorToolbar = new javax.swing.JCheckBoxMenuItem();
        MenuHideToolbar = new javax.swing.JMenuItem();
        MenuShowToolbarOnTop = new javax.swing.JCheckBoxMenuItem();
        MenuOpacity = new javax.swing.JMenuItem();

        popupSlider.setAlwaysOnTop(true);

        genericSlider.setMajorTickSpacing(20);
        genericSlider.setMinorTickSpacing(10);
        genericSlider.setPaintLabels(true);
        genericSlider.setPaintTicks(true);
        genericSlider.setSnapToTicks(true);
        genericSlider.setValue(30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Set toolbar opacity:");

        genericContinueButton.setText("Continue");
        genericContinueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genericContinueButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout popupSliderLayout = new javax.swing.GroupLayout(popupSlider.getContentPane());
        popupSlider.getContentPane().setLayout(popupSliderLayout);
        popupSliderLayout.setHorizontalGroup(
            popupSliderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupSliderLayout.createSequentialGroup()
                .addComponent(genericSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(popupSliderLayout.createSequentialGroup()
                .addGroup(popupSliderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popupSliderLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1))
                    .addGroup(popupSliderLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(genericContinueButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        popupSliderLayout.setVerticalGroup(
            popupSliderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupSliderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(genericSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(genericContinueButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        zoomFrame1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                zoomFrame1FocusLost(evt);
            }
        });
        zoomFrame1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                zoomFrame1KeyPressed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setToolTipText("");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 11, 11, 11));
        zoomFrame1.getContentPane().add(jLabel2, java.awt.BorderLayout.CENTER);

        zoomExit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        zoomExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/close.png"))); // NOI18N
        zoomExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomExitMouseClicked(evt);
            }
        });
        zoomFrame1.getContentPane().add(zoomExit, java.awt.BorderLayout.PAGE_START);

        LayerMenuRename.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/name.png"))); // NOI18N
        LayerMenuRename.setText("Rename layer");
        LayerMenuRename.setEnabled(false);
        LayerMenuRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuRenameActionPerformed(evt);
            }
        });

        LayerMenuHideAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/hide.png"))); // NOI18N
        LayerMenuHideAll.setText("Hide");
        LayerMenuHideAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuHideAllActionPerformed(evt);
            }
        });

        LayerMenuShowAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/show.png"))); // NOI18N
        LayerMenuShowAll.setText("Show");
        LayerMenuShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuShowAllActionPerformed(evt);
            }
        });

        LayerMenuSendBackAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/send_back.png"))); // NOI18N
        LayerMenuSendBackAll.setText("Send to Back");
        LayerMenuSendBackAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuSendBackAllActionPerformed(evt);
            }
        });

        LayerMenuResetAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/reset.png"))); // NOI18N
        LayerMenuResetAll.setText("Reset");
        LayerMenuResetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuResetAllActionPerformed(evt);
            }
        });

        LayerMenuFocusAll.setSelected(true);
        LayerMenuFocusAll.setText("Layer Focusable");
        LayerMenuFocusAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LayerMenuFocusAllItemStateChanged(evt);
            }
        });

        LayerMenuResizeAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/resize.png"))); // NOI18N
        LayerMenuResizeAll.setText("Resize Layer Items");
        LayerMenuResizeAll.setEnabled(false);
        LayerMenuResizeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuResizeAllActionPerformed(evt);
            }
        });

        LayerMenuMerge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/merge.png"))); // NOI18N
        LayerMenuMerge.setText("Merge Layers");
        LayerMenuMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuMergeActionPerformed(evt);
            }
        });

        LayerMenuRemoveLayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete.png"))); // NOI18N
        LayerMenuRemoveLayer.setText("Delete");
        LayerMenuRemoveLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LayerMenuRemoveLayerActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jMenuBar1 = new MotionBar(this, this, bannerLogo, jMenu1, jMenu2, jMenu3, jMenu5, MenuToolbarSettings);
        jMenuBar1.setOpaque(false);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png"))); // NOI18N
        jMenu1.setText("File");
        jMenu1.setMargin(new java.awt.Insets(0, 10, 0, 10));

        MenuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK));
        MenuOpen.setText("Open");
        MenuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenActionPerformed(evt);
            }
        });
        jMenu1.add(MenuOpen);

        MenuAdd.setText("Open (and Add)");
        MenuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAddActionPerformed(evt);
            }
        });
        jMenu1.add(MenuAdd);

        MenuClose.setText("Close");
        MenuClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuCloseActionPerformed(evt);
            }
        });
        jMenu1.add(MenuClose);

        MenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuSave.setText("Save");
        MenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveActionPerformed(evt);
            }
        });
        jMenu1.add(MenuSave);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save as...");
        jMenuItem2.setEnabled(false);
        jMenu1.add(jMenuItem2);

        MenuExit.setText("Exit");
        MenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuExitActionPerformed(evt);
            }
        });
        jMenu1.add(MenuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        jMenu2.setText("Edit");
        jMenu2.setMargin(new java.awt.Insets(0, 10, 0, 10));

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/new-folder.png"))); // NOI18N
        jMenu4.setText("Load New Image");
        jMenu4.setToolTipText("");

        MenuOpenFromFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuOpenFromFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/open.png"))); // NOI18N
        MenuOpenFromFolder.setText("from Folder");
        MenuOpenFromFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFromFolderActionPerformed(evt);
            }
        });
        jMenu4.add(MenuOpenFromFolder);

        MenuOpenFromCamera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/camera.png"))); // NOI18N
        MenuOpenFromCamera.setText("from Camera");
        MenuOpenFromCamera.setEnabled(false);
        MenuOpenFromCamera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFromCameraActionPerformed(evt);
            }
        });
        jMenu4.add(MenuOpenFromCamera);

        MenuOpenFromClipboard.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuOpenFromClipboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/clipboard.png"))); // NOI18N
        MenuOpenFromClipboard.setText("from Clipboard");
        MenuOpenFromClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpenFromClipboardActionPerformed(evt);
            }
        });
        jMenu4.add(MenuOpenFromClipboard);

        jMenu2.add(jMenu4);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/default.png"))); // NOI18N
        jMenu3.setText("Layering");
        jMenu3.setMargin(new java.awt.Insets(0, 10, 0, 10));

        LayerMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/layers.png"))); // NOI18N
        LayerMenu.setText("Layers");
        jMenu3.add(LayerMenu);

        MenuNewLayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons8-save-filled-50.png"))); // NOI18N
        MenuNewLayer.setText("New Layer");
        MenuNewLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuNewLayerActionPerformed(evt);
            }
        });
        jMenu3.add(MenuNewLayer);

        jMenuBar1.add(jMenu3);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/custom.png"))); // NOI18N
        jMenu5.setText("Settings");

        MenuZoomSetting.setText("Set zoom size");
        MenuZoomSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuZoomSettingActionPerformed(evt);
            }
        });
        jMenu5.add(MenuZoomSetting);

        MenuWindowSizeSetting.setText("Set default window size");
        MenuWindowSizeSetting.setEnabled(false);
        MenuWindowSizeSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuWindowSizeSettingActionPerformed(evt);
            }
        });
        jMenu5.add(MenuWindowSizeSetting);

        MenuPreserveAspectSetting.setText("Preserve aspect by default");
        MenuPreserveAspectSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuPreserveAspectSettingActionPerformed(evt);
            }
        });
        jMenu5.add(MenuPreserveAspectSetting);

        jMenuBar1.add(jMenu5);

        MenuToolbarSettings.setText("...");

        MenuAnchorToolbar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK));
        MenuAnchorToolbar.setText("Anchor Toolbar");
        MenuAnchorToolbar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        MenuAnchorToolbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAnchorToolbarActionPerformed(evt);
            }
        });
        MenuToolbarSettings.add(MenuAnchorToolbar);

        MenuHideToolbar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK));
        MenuHideToolbar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        MenuHideToolbar.setText("Hide Toolbar");
        MenuHideToolbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuHideToolbarActionPerformed(evt);
            }
        });
        MenuToolbarSettings.add(MenuHideToolbar);

        MenuShowToolbarOnTop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.SHIFT_MASK));
        MenuShowToolbarOnTop.setText("Show Toolbar on-top");
        MenuShowToolbarOnTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        MenuShowToolbarOnTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuShowToolbarOnTopActionPerformed(evt);
            }
        });
        MenuToolbarSettings.add(MenuShowToolbarOnTop);

        MenuOpacity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png"))); // NOI18N
        MenuOpacity.setText("Set Background Opacity");
        MenuOpacity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOpacityActionPerformed(evt);
            }
        });
        MenuToolbarSettings.add(MenuOpacity);

        jMenuBar1.add(MenuToolbarSettings);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuExitActionPerformed
        if (Utilities.popupAreYouSure(this, "<html>Are you sure you want to Exit?<br><center><i>(progress may be lost)")) {        
                this.dispose();
                System.exit(0);
        }
    }//GEN-LAST:event_MenuExitActionPerformed

    private void MenuHideToolbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuHideToolbarActionPerformed
        
        this.setState(JFrame.ICONIFIED);
        
    }//GEN-LAST:event_MenuHideToolbarActionPerformed

    private void MenuOpacityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpacityActionPerformed
        
        showPopupSlider(BACKGROUND_OPACITY, ImageCollector.toolbarOpacity);
        
    }//GEN-LAST:event_MenuOpacityActionPerformed

    public void showPopupSlider(String type, double value) {
        popupSlider.setVisible(true);
        popupSlider.setSize(popupSlider.getPreferredSize());
        popupSlider.setLocationRelativeTo(null);
        popupSlider.setResizable(false);
        
        genericSlider.setValue((int)(100*value));
        
        jLabel1.setText("Set " + type);
    }
    
    private void genericContinueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genericContinueButtonActionPerformed
        
        if (jLabel1.getText().equals("Set " + BACKGROUND_OPACITY)) {
            
            ImageCollector.toolbarOpacity = (float) genericSlider.getValue()/100;
            
            // SAVE TO PROPERTIES
            ImageCollector.saveProperties.setProperty("toolbarOpacity", "" + ImageCollector.toolbarOpacity);

            // TODO currently doens't change toolbar
            //this.setBackground(new Color(240,240,240, (int)(ImageCollector.toolbarOpacity*255)));
            //this.repaint();
            //this.validate();

            System.out.println("toolbarOpacity " + ImageCollector.toolbarOpacity + " / " + this.getBackground());
            
            // for each of the layers, refresh all the items so they have the new opacity
            for (LayerObject item : collection) {

                // for each window in that layer
                item.layer.stream().forEach((window) -> {

                    window.windowFrame.setBackground(new Color(0, 0, 0, (int)(ImageCollector.toolbarOpacity*255)));
                    window.regenerate();

                });
            }

            
        } else if (jLabel1.getText().equals(ZOOM_SIZE)) {
            
            ImageCollector.zoomFactor = (float) genericSlider.getValue()/100;

            // SAVE TO PROPERTIES
            ImageCollector.saveProperties.setProperty("zoomFactor", "" + ImageCollector.zoomFactor);
            
            System.out.println("zoomFactor " + ImageCollector.zoomFactor);

        } 
        
        ImageCollector.save();
        
        popupSlider.dispose();
    }//GEN-LAST:event_genericContinueButtonActionPerformed

    private void MenuShowToolbarOnTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuShowToolbarOnTopActionPerformed
        
        ImageCollector.showToolbarOnTop = MenuShowToolbarOnTop.getState();
        
        // SAVE TO PROPERTIES
        ImageCollector.saveProperties.setProperty("showToolbarOnTop", "" + ImageCollector.showToolbarOnTop);
        ImageCollector.save();
        
        this.setAlwaysOnTop(ImageCollector.showToolbarOnTop);
    }//GEN-LAST:event_MenuShowToolbarOnTopActionPerformed

    private void MenuOpenFromFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFromFolderActionPerformed
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "ico", "bmp", "gif");
        
        File load = Utilities.popupSelectFile(this, "Load", "recentPathToOpen", filter);
           
        // if we loaded something
        if (load != null) {
            Image fileToLoad = null;
            try {
                fileToLoad = ImageIO.read(load);
            } catch (IOException ex) {
                Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
            }
            // ------------------------
        
            addImageToCollection(fileToLoad);
        }
        
        // potentially we want to restore focus after they load so they can re-access the toolbar
        showToolbar();
        
    }//GEN-LAST:event_MenuOpenFromFolderActionPerformed

    private void MenuOpenFromCameraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFromCameraActionPerformed
        
    }//GEN-LAST:event_MenuOpenFromCameraActionPerformed

    private void MenuOpenFromClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenFromClipboardActionPerformed
        
        // Create a image and try to load into it from the clipboard
        
        if (ImageManipulator.GetImageFromClipboard() != null) {
            Image fileToLoad = ImageManipulator.GetImageFromClipboard();
            
            addImageToCollection(fileToLoad);
        } else {
            System.out.println("Sorry nothing in the clipboard");
        }
        // ------------------------
        
    }//GEN-LAST:event_MenuOpenFromClipboardActionPerformed

    private void zoomFrame1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_zoomFrame1FocusLost
       System.out.println("Focus Lost");
       
       zoomFrame1.dispose();
    }//GEN-LAST:event_zoomFrame1FocusLost

    private void MenuZoomSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuZoomSettingActionPerformed
        showPopupSlider(ZOOM_SIZE, ImageCollector.zoomFactor);
    }//GEN-LAST:event_MenuZoomSettingActionPerformed

    private void MenuWindowSizeSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuWindowSizeSettingActionPerformed
        // set default window size
    }//GEN-LAST:event_MenuWindowSizeSettingActionPerformed

    private void zoomExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomExitMouseClicked
        zoomFrame1.dispose();
    }//GEN-LAST:event_zoomExitMouseClicked

    private void LayerMenuRemoveLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuRemoveLayerActionPerformed
        if (Utilities.popupAreYouSure(this, "<html>Are you sure you want to continue?<br><center><i>(this cannot be undone)")) {
            DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
            // remove the current layer
            List<Integer> layers = new ArrayList<>();
            layers.add(getLayer(j.getId()));
            callRemoveLayers(layers);
        }  
    }//GEN-LAST:event_LayerMenuRemoveLayerActionPerformed

    private void LayerMenuHideAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuHideAllActionPerformed
        DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
        Integer layer = getLayer(j.getId());
        
        // for each window in that layer
        collection.get(layer).layer.stream().forEach((window) -> {

            window.windowFrame.setState(JFrame.ICONIFIED);
        });
        
    }//GEN-LAST:event_LayerMenuHideAllActionPerformed

    private void LayerMenuShowAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuShowAllActionPerformed
        DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
        Integer layer = getLayer(j.getId());
        // for each window in that layer
        collection.get(layer).layer.stream().forEach((window) -> {

            window.windowFrame.setState(JFrame.NORMAL);
        });
    }//GEN-LAST:event_LayerMenuShowAllActionPerformed

    private void zoomFrame1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zoomFrame1KeyPressed
        // if you press escape in the zoom frame it exits
        if (evt.getKeyChar() == KeyEvent.VK_ESCAPE) {
            zoomFrame1.dispose();
        }
    }//GEN-LAST:event_zoomFrame1KeyPressed

    private void LayerMenuResizeAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuResizeAllActionPerformed
        
    }//GEN-LAST:event_LayerMenuResizeAllActionPerformed

    private void LayerMenuResetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuResetAllActionPerformed
        if (Utilities.popupAreYouSure(this, "<html>Are you sure you want to Reset?<br><center><i>(this cannot be undone)")) {
            
            DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
            Integer layer = getLayer(j.getId());

            // for each window in that layer
            collection.get(layer).layer.stream().forEach((window) -> {

                window.windowSize.width = ImageCollector.windowDefaultSize.width;

                window.regenerate();

                window.windowFrame.setLocationRelativeTo(null);
            });
        }
    }//GEN-LAST:event_LayerMenuResetAllActionPerformed

    private void MenuPreserveAspectSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuPreserveAspectSettingActionPerformed
        ImageCollector.preserveAspect = MenuPreserveAspectSetting.isSelected();

        // SAVE TO PROPERTIES
        ImageCollector.saveProperties.setProperty("preserveAspect", "" + ImageCollector.preserveAspect);
        ImageCollector.save();

        System.out.println("preserveAspect " + ImageCollector.preserveAspect);
    }//GEN-LAST:event_MenuPreserveAspectSettingActionPerformed

    private void MenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveActionPerformed
        callSaveCollection();
        
        
    }//GEN-LAST:event_MenuSaveActionPerformed

    private void MenuNewLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuNewLayerActionPerformed
        JTextField label = new JTextField("Type layer name");
        JOptionPane.showMessageDialog(this, label, "New Layer",
                                      JOptionPane.QUESTION_MESSAGE);
        
        // TODO IF YOU HIT X button IT SHOULDN'T CREATE A NEW LAYER
        
        // if they actually typed a name
        if (!label.getText().equals("Type layer name")) {
            addNewLayer(label.getText());
        }
    }//GEN-LAST:event_MenuNewLayerActionPerformed

    private void LayerMenuFocusAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LayerMenuFocusAllItemStateChanged
        // for each of the selected layers, reset all the items
        System.out.println("Doing");
        
        DummyJCheckBoxMenuItem j = (DummyJCheckBoxMenuItem)evt.getSource();
        
        Integer layer = getLayer(j.getId());

        // for each window in that layer
        collection.get(layer).layer.stream().forEach((window) -> {

            // TODODODODODODO DOESN"T WORK
            System.out.println("Doing more ");

            // set the window as focusable
            window.windowFrame.setFocusableWindowState(j.isSelected());
            // NOTE: ComponentResize and ContextMenuMouseListener both look for this variable to see if they should operate
            window.windowFrame.setFocusable(j.isSelected());
            
        });
    }//GEN-LAST:event_LayerMenuFocusAllItemStateChanged

    private void MenuAnchorToolbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAnchorToolbarActionPerformed
        System.out.println("HJE" + ImageCollector.anchorToolbar);
        ImageCollector.anchorToolbar = !ImageCollector.anchorToolbar;
        MenuAnchorToolbar.setSelected(ImageCollector.anchorToolbar);
        
        // SAVE TO PROPERTIES
        ImageCollector.saveProperties.setProperty("anchorToolbar", "" + ImageCollector.anchorToolbar);
        ImageCollector.save();
        
        updateParent();
    }//GEN-LAST:event_MenuAnchorToolbarActionPerformed

    private void MenuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOpenActionPerformed
        List<LayerObject> newCollection = callLoadCollection();
        
        overwriteCollection(newCollection);
        
        showToolbar();
    }//GEN-LAST:event_MenuOpenActionPerformed

    private void MenuCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuCloseActionPerformed
        // clear everything
        if (Utilities.popupAreYouSure(this, "<html>Are you sure you want to close?<br><center><i>(unsaved changes may be lost)")) {
            callRemoveLayers(getAllLayers());

            // add a new layer 0 to the collection
            addNewLayer("Default");
        }
    }//GEN-LAST:event_MenuCloseActionPerformed

    private void MenuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAddActionPerformed
        List<LayerObject> newCollection = callLoadCollection();
        
        addCollection(newCollection);
        
        showToolbar();
    }//GEN-LAST:event_MenuAddActionPerformed

    private void LayerMenuSendBackAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuSendBackAllActionPerformed
        DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
        Integer layer = getLayer(j.getId());

        // for each window in that layer
        collection.get(layer).layer.stream().forEach((window) -> {

            // send it to the back
            window.windowFrame.toBack();
            
        });
    }//GEN-LAST:event_LayerMenuSendBackAllActionPerformed

    private void LayerMenuMergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuMergeActionPerformed
        DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
        // current Layer
        Integer startingL = getLayer(j.getId());
        
        // get new layer to merge into
        JComboBox field = new JComboBox();
        // add all layers
        for (LayerObject l : collection) {
            field.addItem(l.name);
        }
        int selectedOption = JOptionPane.showConfirmDialog(this, field, "Merge into:",
                                      JOptionPane.DEFAULT_OPTION);

        // if they actually choose one that works
        if (selectedOption == JOptionPane.YES_OPTION && !field.getSelectedItem().equals(collection.get(startingL).name)) {

            // get the new layer
            int newI = getLayer((String)field.getSelectedItem()); 
            
            
            // add our current layer to our new layer
            collection.get(newI).layer.addAll(collection.get(startingL).layer);
            
            // re brand their layering (to set the new ones in the right layer)
            collection.get(newI).layer.forEach((window) -> {
                window.currentLayer = newI;
            });
            
            // remove the current layer
            List<Integer> layers = new ArrayList<>();
            layers.add(startingL);
            callRemoveLayers(layers);
            
            // figure out which layer we have just created (it could have changed from the deletion
            int finalL = newI;
            if (newI > startingL) {
                finalL = finalL - 1;
            }
            
            // and regenerate the window so they all show properly
            collection.get(finalL).layer.forEach((window) -> {
                window.regenerate();
            });
        }
        
        
    }//GEN-LAST:event_LayerMenuMergeActionPerformed

    private void LayerMenuRenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LayerMenuRenameActionPerformed
        DummyJMenuItem j = (DummyJMenuItem)evt.getSource();
        
        // current Layer
        Integer layer = getLayer(j.getId());
        
        
        JTextField field = new JTextField(collection.get(layer).name);
        
        int selectedOption = JOptionPane.showConfirmDialog(this, field, "Rename",
                                      JOptionPane.DEFAULT_OPTION);
        
        // IF you chose OK
        // AND you didn't leave blank
        // AND you didn't leave the same
        if (selectedOption == JOptionPane.YES_OPTION && !field.getText().equals(collection.get(layer).name) && !field.getText().equals("")) {
            collection.get(layer).name = field.getText();
            LayerMenu.getItem(layer).setText(field.getText());
            
            // then we have to change all the layers sub-menu to use the new ID
            for (Component c : LayerMenu.getItem(layer).getComponents()) {
                
                ((DummyJMenuItem)c).setId(field.getText());
                
            }
            
        }
    }//GEN-LAST:event_LayerMenuRenameActionPerformed

            
    private void callSaveCollection() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Files", "pptx");
        
        File outputFile = Utilities.popupSelectFile(this, "Save", "recentPathToSaveCollection", filter);
        
        if (outputFile != null) {
            if (!outputFile.exists() || outputFile.exists() && Utilities.popupAreYouSure(this, "<html>Are you sure you want to overwrite this file?")) {
                
                System.out.println("SAVING");

                // make sure the file has an extension
                // if it does not contain one then we will save it as pptx automatically
                if (!outputFile.getName().contains(".")) {
                    outputFile = new File(outputFile.getAbsolutePath() + ".pptx");
                }

                try {
                    PowerPointFiling.save(collection, outputFile);
                } catch (IOException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private List<LayerObject> callLoadCollection() {
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Files", "pptx");
        
        File file = Utilities.popupSelectFile(this, "Load", "recentPathToLoadCollection", filter);
        
        // if we loaded something
        if (file != null) {
            if (file.exists()) {
                XMLSlideShow ppt = null;
                try {
                    ppt = new XMLSlideShow(new FileInputStream(file));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainToolbar.class.getName()).log(Level.SEVERE, null, ex);
                }

                return PowerPointFiling.load(ppt, this);

            } else {
                return null;
            }
        } else {
            return null;
        }
        
    }
    
    private void callRemoveLayers(List<Integer> layers) {
        // Generate an iterator. Start just after the last element.
        ListIterator<Integer> li = layers.listIterator(layers.size());

        // Iterate in reverse so that we don't remove an item and immediately the subsequent item falls into its place
        // go through each of the layers we want to delete
        while(li.hasPrevious()) {
            int item = li.previous(); // go back to the next one
            System.out.println("ITEM 2Delete : " + item + " C" + collection.get(item));
            
            // get rid of all windows in this layer
            collection.get(item).layer.forEach((window) -> {
                window.windowFrame.dispose();
            });
            
            // get rid of the layer's check box
            LayerMenu.remove(item);
            
            // get rid of the layer
            collection.remove(collection.get(item));
            
            // then all subsequent layers need to be bumper downwards (because we just removed this one)
            for (int i = item; i < collection.size(); i++) {
                System.out.println("Starting on " + i + " | trying to update layers up to: " + collection.size());
                
                collection.get(item).layer.forEach((window) -> {
                    System.out.println("An item is now in the correct layer");
                    window.currentLayer = item; // ensure the current layer is the current layer
                });
                
            }
        }
        
    }
    
    public void updateParent() {
        System.out.println("ANCHORED" + ImageCollector.anchorToolbar);
        if (ImageCollector.anchorToolbar) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int width = toolkit.getScreenSize().width;
            
            this.setLocation(0,25); // slightly offset from top or you can't hit the exit button
            this.setSize(width, this.getHeight());
        } else {
            this.setSize(610, this.getHeight());
            this.setLocationRelativeTo(null);
        }
    }
    
    private void showNewLayer (LayerObject l) {
        JMenu cb = new JMenu(l.name);
        cb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/picture.png")));
        
        LayerMenu.add(cb);
        cb.add(new DummyJMenuItem(LayerMenuRename, l.name));
        cb.add(new DummyJMenuItem(LayerMenuHideAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuShowAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuSendBackAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuResetAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuMerge, l.name));
        cb.add(new DummyJCheckBoxMenuItem(LayerMenuFocusAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuResizeAll, l.name));
        cb.add(new DummyJMenuItem(LayerMenuRemoveLayer, l.name));
        cb.setSelected(true);
    }
    
    
    
    private void addNewLayer(String name) {
        LayerObject l = new LayerObject(name);
        collection.add(l);
        
        showNewLayer(l);
    }
    
    public Integer getLayer(String name) {        
        // for each layer
        for (int i = 0; i < collection.size(); i++) {
            // if that layer is the one we are looking for
            if (collection.get(i).name.equals(name)) {
                return i;
            }
        }
        
        // if none are fonud we throw error
        return -1;
    }
    
    
    private List<Integer> getAllLayers() {
        List<Integer> tempCurrentLayers = new ArrayList<>();
        
        // for each layer
        for (int i = 0; i < collection.size(); i++) {
            tempCurrentLayers.add(i);
        }
        
        return tempCurrentLayers;
    }
    
    // search through until you find a layer to use
    // this method is used to determine where to stick new windows
    private int getCurrentActiveLayer() {
        
        int layer = 0;
        
        // keep going until we find one that is active
        //while (collection.get(layer).layer.) {
            // ... TODO
        //}
        
        // IF NONE ARE SELECTED then return the 0th
        return layer;
    }
    
    public void addImageToCollection(Image f) {
        //try {
            
            ImageWindowObject window = new ImageWindowObject(this, new ImageIcon(f), ImageCollector.windowDefaultSize, getCurrentActiveLayer());
        
            collection.get(getCurrentActiveLayer()).layer.add(window); //store this object in our array in the active layer

            generateWindow(window);
        /*} catch(NullPointerException e) {
            System.out.println("Not valid image");
        }*/
    }
    
    public void addCollection(List<LayerObject> collection) {
        if (collection != null) {
            // generate their windows
            for (LayerObject layer : collection) {

                // set the layer visible on the menu bar
                System.out.println("Adding LAYER");
                this.collection.add(layer);
                showNewLayer(layer);

                for (ImageWindowObject image : layer.layer) {

                    // regenerate the image so it is visible
                    System.out.println("Adding IMAGE");
                    
                    image.regenerate();

                }

            }
        }
    }
    
    public void overwriteCollection(List<LayerObject> collection) {
        if (collection != null) {
            // first clear everything
            callRemoveLayers(getAllLayers());

            // then add new ones
            System.out.println("Adding COLLECTION");
            this.collection = collection;

            // then generate their windows
            for (LayerObject layer : collection) {

                // set the layer visible on the menu bar
                System.out.println("Adding LAYER");
                showNewLayer(layer);

                for (ImageWindowObject image : layer.layer) {

                    // regenerate the image so it is visible
                    System.out.println("Adding IMAGE");
                    image.regenerate();

                }

            }
        }
    }
    
    public static void generateWindow(ImageWindowObject window) {
        
        
        // CHECK IF WE SHOULD GENERATE A ASPECT PRESERVED WINDOW OR A NORMAL
        if (ImageCollector.preserveAspect) {
            // assign the frame to the default X
            // assign the Y proportionally to that X so the window fits correctly
            window.setSize(new Dimension(ImageCollector.windowDefaultSize.width, (int) (ImageCollector.windowDefaultSize.width * window.getAspectRatioY())));
        } else {
            window.setSize(ImageCollector.windowDefaultSize);
            
        }
        
        /// LOAD THE IMAGE ONTO THE FRAME
        ImageManipulator.resizeWindowBackground(window);
        /////////////////
        
        window.windowFrame.setVisible(true);
        
        
        //window.windowFrame.add(window.motionPanel, BorderLayout.CENTER);
    }
    
    //////////////////////////
    // FUNCTIONS
    //////////////////////////
    
    public static void killImage(MainToolbar main, ImageWindowObject whichImage) {
        //dipose the frame
        System.out.println("S: " + whichImage);
                
        whichImage.windowFrame.dispose();
        
        //remove it from its layer
        main.collection.get(whichImage.currentLayer).layer.remove(whichImage);
    }
    
    public static void cropImage(MainToolbar main, ImageWindowObject whichImage) {
        
        Cropping test = new Cropping(whichImage, main);
        
        test.createClip();
        
        // call the component resizer class to allow the user to resize the cropper
        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(test.clip);
        cr.setSnapSize(new Dimension(1, 1));
        cr.setMinimumSize(new Dimension(30,30));
        cr.setMaximumSize(new Dimension(1000,1000));
        cr.setDragInsets(new Insets(10,10,10,10));
        
        
        JFrame f = new JFrame();
        f.setAlwaysOnTop(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new JScrollPane(test));
        f.getContentPane().add(test.getUIPanel(), "South");
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
    
    public static void zoomImage(MainToolbar main, ImageWindowObject whichImage) {
        
        double zoomScreenFraction = ImageCollector.zoomFactor;
        double zoomScreenMargin = (double) ((1 - zoomScreenFraction) / 2); // Everything that is not filled, half is put as the margin for each side
        
        int border = 22; // the image is this much smaller than the zoomFrame
        
        System.out.println("S: " + zoomScreenFraction + " . " + zoomScreenMargin);
        
        // increase the zoom frames size to correct fullscreen
        int screenH = main.getToolkit().getScreenSize().height;
        int screenW = main.getToolkit().getScreenSize().width;
        
        JLabel jLabel2 = new JLabel();
        JFrame zoomFrame = new JFrame();
        JLabel zoomExit = new JLabel();
        
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setToolTipText("");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 11, 11, 11));
        zoomFrame.getContentPane().add(jLabel2, java.awt.BorderLayout.CENTER);

        zoomExit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        zoomExit.setIcon(new javax.swing.ImageIcon(main.getClass().getResource("/resources/close.png"))); // NOI18N
        zoomExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                zoomFrame.dispose();
            }
        });
        zoomFrame.getContentPane().add(zoomExit, java.awt.BorderLayout.PAGE_START);
        
        zoomFrame.setUndecorated(true);
        zoomFrame.setBackground(new Color(0,0,0,30));
        zoomFrame.setResizable(false);
        zoomFrame.setLocation((int) (screenW * zoomScreenMargin), (int) (screenH * zoomScreenMargin));
        zoomFrame.setSize((int) (screenW * zoomScreenFraction), (int) (screenH * zoomScreenFraction));
        zoomFrame.setAlwaysOnTop(true);
        
        // increase the image size inside the frame (and scale it to the frame)
        jLabel2.setIcon(ImageManipulator.resizeImageAspect(new Dimension(zoomFrame.getWidth() - border, zoomFrame.getHeight() - border*2 - zoomExit.getHeight()), whichImage.windowIcon, false));

        zoomFrame.setVisible(true);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainToolbar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainToolbar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainToolbar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainToolbar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainToolbar().setVisible(true);
            }
        });
    }
	
    private classes.Movement.MotionBar jMenuBar1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu LayerMenu;
    private classes.Movement.DummyJCheckBoxMenuItem LayerMenuFocusAll;
    private classes.Movement.DummyJMenuItem LayerMenuHideAll;
    private classes.Movement.DummyJMenuItem LayerMenuMerge;
    private classes.Movement.DummyJMenuItem LayerMenuRemoveLayer;
    private classes.Movement.DummyJMenuItem LayerMenuRename;
    private classes.Movement.DummyJMenuItem LayerMenuResetAll;
    private classes.Movement.DummyJMenuItem LayerMenuResizeAll;
    private classes.Movement.DummyJMenuItem LayerMenuSendBackAll;
    private classes.Movement.DummyJMenuItem LayerMenuShowAll;
    private javax.swing.JMenuItem MenuAdd;
    public javax.swing.JCheckBoxMenuItem MenuAnchorToolbar;
    private javax.swing.JMenuItem MenuClose;
    private javax.swing.JMenuItem MenuExit;
    private javax.swing.JMenuItem MenuHideToolbar;
    private javax.swing.JMenuItem MenuNewLayer;
    private javax.swing.JMenuItem MenuOpacity;
    private javax.swing.JMenuItem MenuOpen;
    private javax.swing.JMenuItem MenuOpenFromCamera;
    private javax.swing.JMenuItem MenuOpenFromClipboard;
    private javax.swing.JMenuItem MenuOpenFromFolder;
    public javax.swing.JCheckBoxMenuItem MenuPreserveAspectSetting;
    private javax.swing.JMenuItem MenuSave;
    public javax.swing.JCheckBoxMenuItem MenuShowToolbarOnTop;
    private javax.swing.JMenu MenuToolbarSettings;
    private javax.swing.JMenuItem MenuWindowSizeSetting;
    private javax.swing.JMenuItem MenuZoomSetting;
    private javax.swing.JButton genericContinueButton;
    private javax.swing.JSlider genericSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JDialog popupSlider;
    private javax.swing.JLabel zoomExit;
    private javax.swing.JFrame zoomFrame1;
    // End of variables declaration//GEN-END:variables
}
