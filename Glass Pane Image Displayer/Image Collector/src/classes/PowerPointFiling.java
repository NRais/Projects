/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import application.MainToolbar;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author Nathan
 */
public class PowerPointFiling {
    
    public static List<LayerObject> load(XMLSlideShow ppt, MainToolbar parent) {
        // form a collection from the powerpoint file
        List<LayerObject> collection = new ArrayList<>();
        
        CoreProperties props = ppt.getProperties().getCoreProperties();
        String title = props.getTitle();
        
        System.out.println("Title: " + title);
        
        int layer = 0;
        
        // for each slide we want to create a layer
        for (XSLFSlide slide: ppt.getSlides()) {
            
            String slideName = slide.getXmlObject().getCSld().getName(); // load the slides name for our layer
            
            if (slideName.length() == 0) {
                slideName = "Slide " + slide.getSlideNumber(); // if we have a blank slide name assign a default
            }
            
            // check for duplicate slide names
            boolean repeat = true;
            while (repeat) {
                repeat = false;
                for (LayerObject l : parent.collection) {
                    if (l.name.equals(slideName)) {
                        slideName = slideName + "(2)"; // modify the slide name
                        repeat = true; // now we should search all of them again
                    }
                }
            }
            
            LayerObject l = new LayerObject(slideName);
            
            System.out.println("Starting slide...");

            List<XSLFShape> shapeList = slide.getShapes();

            for (XSLFShape shape: shapeList) {
                
                System.out.println("Type: " + shape.getClass());
                
                // for text shapes
                if (shape instanceof XSLFTextBox) {

                    XSLFTextShape textShape = (XSLFTextShape)shape;
                    String text = textShape.getText();

                    System.out.println("Text: " + text);
                }
                // for picture shapes
                else if (shape instanceof XSLFPictureShape) {
                    System.out.println("Picture: ");
                    
                    XSLFPictureShape pictureShape = (XSLFPictureShape)shape;
                    
                    byte[] bytes = pictureShape.getPictureData().getData();
                    
                    try {
                        // create image out of the picture data
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));

                        Rectangle2D anchor = pictureShape.getAnchor();
                        
                        ImageWindowObject window = new ImageWindowObject(parent, new ImageIcon(img), new Dimension((int)anchor.getWidth(), (int)anchor.getHeight()), layer);

                        window.windowFrame.setLocation((int)anchor.getX(), (int)anchor.getY());
                        
                        // TODO: if the object is out of bounds of the screen, let them know
                        if (anchor.getX() < 0 | anchor.getX() > Toolkit.getDefaultToolkit().getScreenSize().width | anchor.getY() < 0 | anchor.getY() > Toolkit.getDefaultToolkit().getScreenSize().height) {
                            System.out.println("Object out of the screen");
                        }
                                               
                        
                        l.layer.add(window); //store this object in our current layer
                        
                    } catch (IOException ex) {
                        Logger.getLogger(PowerPointFiling.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    System.out.println("Shape: ");
                    
                    // SET SCALING FACTOR (so they look good)
                    double scale = 2.0;
                    
                    // we figure out the size of the shape
                    Rectangle2D anchor = shape.getAnchor();
                    // calculate scaled up values so we draw the image bigger than it is for quality purposes
                    int x = (int)(anchor.getWidth()*scale);
                    int y = (int)(anchor.getHeight()*scale);
                    
                    BufferedImage img = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();
                    graphics.setComposite(AlphaComposite.Clear);
                    graphics.fillRect(0, 0, x, y);

                    // clear the drawing area
                    //graphics.setColor(new Color(255,255,255,0));
                    //graphics.fillRect(0, 0, (int)anchor.getWidth(), (int)anchor.getHeight());
                    graphics.setComposite(AlphaComposite.Src);
                    
                    // default rendering options
                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    graphics.scale(scale, scale);
                    
                    shape.draw(graphics, new Rectangle(0, 0,(int)anchor.getWidth(),(int)anchor.getHeight()));
                    
                    // CREATE THE WINDOW
                    ImageWindowObject window = new ImageWindowObject(parent, new ImageIcon(img), new Dimension((int)anchor.getWidth(), (int)anchor.getHeight()), layer);

                    window.windowFrame.setLocation((int)anchor.getX(), (int)anchor.getY());

                    // TODO: if the object is out of bounds of the screen, let them know
                    if (anchor.getX() < 0 | anchor.getX() > Toolkit.getDefaultToolkit().getScreenSize().width | anchor.getY() < 0 | anchor.getY() > Toolkit.getDefaultToolkit().getScreenSize().height) {
                        System.out.println("Object out of the screen");
                    }

                    l.layer.add(window); //store this object in our current layer
                    
                    graphics.dispose();
                    img.flush();
                }
            }
            
            // add the layer object
            collection.add(l);
            layer++;
        }	 
        
        return collection;
    }
    
    //////////////////////////////
    // SAVE 
    //////////////////////////////
    public static void save (List<LayerObject> collection, File outputFile) throws FileNotFoundException, IOException {

        XMLSlideShow ppt = new XMLSlideShow();
        ppt.setPageSize(Toolkit.getDefaultToolkit().getScreenSize()); // set the page size to that of the screen



        //XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);

        /*XSLFSlideLayout layout 
            = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);*/ // SETTING SLIDE LAYOUT


        /*XSLFTextShape titleShape = slide.getPlaceholder(0);
        XSLFTextShape contentShape = slide.getPlaceholder(1);

        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFAutoShape) {
                // this is a template placeholder
            }
        }*/

        // check each layer object in the collection
        for (LayerObject layerObject : collection) {

            // each layer is stored on a slide
            XSLFSlide slide = ppt.createSlide();
            slide.getXmlObject().getCSld().setName(layerObject.name); // set the slides name

            // check each image in the layer object
            for (ImageWindowObject image : layerObject.layer) {

                // get the bytes
                byte[] pictureData = IOUtils.toByteArray(getInputStream(image.windowIcon.getImage()));

                // CREATE PICTURE
                XSLFPictureData pd = ppt.addPicture(pictureData, PictureData.PictureType.PNG);
                XSLFPictureShape picture = slide.createPicture(pd);

                picture.setAnchor(new Rectangle(image.windowFrame.getX(), image.windowFrame.getY(), image.windowSize.width, image.windowSize.height));
            }
        }

        //byte[] pictureData = IOUtils.toByteArray(new FileInputStream("test.png"));

        FileOutputStream out = new FileOutputStream(outputFile);
        ppt.write(out);
        out.close(); 
        
    }
    
    public static InputStream getInputStream(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),    BufferedImage.TYPE_INT_ARGB);
        //bufferedImage is the RenderedImage to be written
        Graphics2D g2 = bufferedImage.createGraphics();
        
        g2.drawImage(image, null, null);
        
        File outputfile = new File("saved.png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(PowerPointFiling.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try { 
            ImageIO.write(bufferedImage, "png", outStream);
        } catch (IOException ex) {
            Logger.getLogger(PowerPointFiling.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ByteArrayInputStream(outStream.toByteArray());
    }
}
