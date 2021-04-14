/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Nuser
 */
public class ImageManipulator {
    
    //////////////////
    // FUNCTION TO OPERATE THE FRAME AND LABEL BACKGROUNDS
    //////////////////
    
    // # 1. FUNCTION
    // SCALE IMAGE
    // load the image and scale it
    
    public static ImageIcon GetScaledImage(Dimension size, String pathToImg) {
        
        ImageIcon myImage = new ImageIcon();
        
        // load image
            try {
                //** System.out.println("STRING: " + "Resources/" + pathToImg);
                // LOAD:
                Image temp = ImageIO.read(ClassLoader.getSystemResource(pathToImg));
                
                // RESIZE:
                myImage = new ImageIcon(resizeToBig(temp, size.width, size.height, java.awt.Image.SCALE_SMOOTH)); // NOTE: default image = 108
                
            } catch (IOException ex) {
                // ERROR IMAGE NOT FOUND!! 
                Logger.getLogger(ImageManipulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        return myImage;
    }
    
    // same method but it takes a ImageIcon instead of string
    public static ImageIcon GetScaledImage(Dimension size, ImageIcon theImage) {
                
        // load image
        Image temp = theImage.getImage();
        
        // resize image
        ImageIcon myImage = new ImageIcon(resizeToBig(temp, size.width, size.height, java.awt.Image.SCALE_SMOOTH));
                
        return myImage;
    }
    
    
    //////////////////////////////////////////
    public static ImageIcon resizeImageAspect(Dimension frame, ImageIcon img, boolean fillFrame) {
         // generate a buffered image so we can measure its size
            Image myImage = img.getImage();
                    
            
            BufferedImage i = toBufferedImage(myImage);
            
            // the X to Y ratio is the aspect (for example 4:3 or 16:9)
            
            // EITHER FILL THE FRAME
            if (fillFrame) {
                // if the images X to Y is greater than the frames X to Y then scale based upon the Y axis so it will fill the whole thing
                // otherwise scale it the other direction

                if ((double)i.getWidth()/(double)i.getHeight() >= (double)frame.getWidth()/(double)frame.getHeight()) {
                    // assuming that the image is proportionally more widescreen than the 800*600 frame we scale the image to match the Y direction and be a proportional X
                    System.out.println(":::: " + (double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth() + "," + frame.getHeight());

                    myImage = resizeToBig(myImage, (int)((double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth()), (int)frame.getHeight(), java.awt.Image.SCALE_SMOOTH);

                } else {
                    System.out.println(":::: " + frame.getWidth() + "," + (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()));

                    // in the other dimension
                    myImage = resizeToBig(myImage, (int)frame.getWidth(), (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()), java.awt.Image.SCALE_SMOOTH);
                }
            } 
            // OR INSTEAD FIT INSIDE THE FRAME
            else {
                // if the images X to Y is greater than the frames X to Y (that means we should scale based up the X direction so it all fits)
                // otherwise we will scale assuming the image is taller than it is wide and so we scale by the Y direction
                
                if ((double)i.getWidth()/(double)i.getHeight() >= (double)frame.getWidth()/(double)frame.getHeight()) {
                    
                    System.out.println(":::: " + frame.getWidth() + "," + (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()));

                    myImage = resizeToBig(myImage, (int)frame.getWidth(), (int)((double)((double)frame.getWidth()/(double)i.getWidth())*i.getHeight()), java.awt.Image.SCALE_SMOOTH);
                    
                } else {
                    System.out.println(":::: " + (double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth() + "," + frame.getHeight());

                    myImage = resizeToBig(myImage, (int)((double)((double)frame.getHeight()/(double)i.getHeight())*i.getWidth()), (int)frame.getHeight(), java.awt.Image.SCALE_SMOOTH);

                }
            }
            
            return new ImageIcon(myImage);
    }
   
    
    
    /////////////////////////////
    // underlying resizing function
    /////////////////////////////
    
    
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    // FUNCTON THAT RESIZES AN IMAGE
    // take a image and resize it to an X and Y     
    public static Image resizeToBig(Image original, int biggerWidth, int biggerHeight, int type) {
        
        Image resizedImage = original;
        
        // if we are actually resizing something then do so
        // but if it is 1.0 then we don't want to waste our time resizing
        //if (Scale != 1.0) {
            resizedImage = resizedImage.getScaledInstance(biggerWidth, biggerHeight, type);
        //}
        
        return resizedImage;
    }
    
    //////////////////////
    // LOADING AN IMAGE FROM THE CLIPBOARD
    //////////////////////
    
        /**
     * Get an image off the system clipboard.
     * 
     * @return Returns an Image if successful; otherwise returns null.
     */
    public static Image GetImageFromClipboard()
    {
      Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
          
            // TRY TO RETURN THE IMAGE FROM THE CLIPBOARD
            try
            {
              return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            }
            catch (UnsupportedFlavorException | IOException e)
            {
              // handle this as desired
              e.printStackTrace();
            }

      } else {
          
        System.err.println("getImageFromClipboard: That wasn't an image!");
      }
      
      
      return null;
    }
    
    
    /////////////////////
    
    /*
    *  Reload the resized image to fit the new size of the scaled window
    */
    public static void resizeWindowBackground(ImageWindowObject currentWindow) {
        
        //** System.out.println("W SiZE: " + currentWindow.windowSize);
        
        ImageIcon myImage = currentWindow.windowIcon;
            
        if (!currentWindow.maintainAspect) {
            myImage = ImageManipulator.GetScaledImage(currentWindow.windowSize, myImage);
        } else {
            myImage = ImageManipulator.resizeImageAspect(currentWindow.windowSize, myImage, false);
        }

        JLabel j = new JLabel(myImage);
        j.setLayout( null );

        currentWindow.windowFrame.setContentPane(j);

    }
    
    
    //////////////////
    // IMAGE EDITING
    //////////////////
    
    /*
    * FLIP IMAGE
    */
    public static ImageIcon flipImageHorizontally(ImageIcon initial) {
        
        BufferedImage image = toBufferedImage(initial.getImage());
        
        // Flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return new ImageIcon(image);
    }
    
    public static ImageIcon flipImageVertically(ImageIcon initial) {
        
        BufferedImage image = toBufferedImage(initial.getImage());
        
        // Flip the image vertically
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return new ImageIcon(image);
    }
    
    /////////////
    // IMAGE TO BYTES
    /////////////
    
    public static byte[] extractBytes (String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }
    public static byte[] extractBytes (ImageIcon ImageName) throws IOException {
        // open image
        BufferedImage bufferedImage = toBufferedImage(ImageName.getImage());

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }
}
