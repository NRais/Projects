/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Nuser
 */
public class MapObject {
    
    // THE MAP OBJECT CLASS
    // contains 3 important things
    // - NUMBER OF PLAYERS the map supports
    // - BOARD of the current map (2d array)
    // - IMAGE the maps little picture
    
    // ALSO NAME of the map (but that is stored adjacent in the hash table not in this object)
    
    public int[][] mapBoard = null;
    public int numberOfPlayers = 0;
    public ImageIcon MapPicture = null;
    
    
    public MapObject(String MapName) {
        
        System.out.println("SETTING UP :" + MapName);
        
        // SETUP OUR MAPS IMAGE
        // load our maps picture
        MapPicture = new ImageIcon("maps/" + MapName + ".png");

        // LOAD MAP PICTURE FROM MAP FOLDER
        // IF there is NOT a picture show DEFAULT
        File f = new File("maps/" + MapName + ".png"); // load the file to check if it exists
        if (!f.exists()) {

            System.out.println("ERROR: map image not found");
            // NOTE: this is no big deal, we expect maps not to have images and so have a default
            
            MapPicture = new ImageIcon(getClass().getResource("/resources/menu/default-map.png"));
        }
        
        
        
        // SETUP OUR MAPS BOARD

        // File Reader
        BufferedReader reader = null;

        try {
            
            // read the file from the maps folder
            reader = new BufferedReader(new FileReader("maps/" + MapName + ".map"));

            String headerLine = reader.readLine();
            // READ header (line 1) = number of players,X,Y
            numberOfPlayers = Integer.parseInt(headerLine.split(",")[0]);

            int X = Integer.parseInt(headerLine.split(",")[1]);
            int Y = Integer.parseInt(headerLine.split(",")[2]);

            // ** System.out.println("MAP: " + X + "," + Y);
            
            mapBoard = new int[X][Y];

            boolean EOF = false;
            // rest of the file is array of spaces (UNTIL EOF)
            // iterate through the lines
            int y = 0; 
            while (!EOF) {
                //** System.out.println(" LINE " + y);

                try {
                    String nextLine = reader.readLine();

                    String[] items = nextLine.split(",");

                    for (int x = 0; x < items.length; x++) {
                        
                        mapBoard[x][y] = Integer.parseInt(items[x]);
                        
                        System.out.print("," + mapBoard[x][y]);
                    }

                    y++; // increment counter To assign next line

                } catch (IOException | NumberFormatException | NullPointerException e) {
                    // if break End Of File
                    // ** Logger.getLogger(MapObject.class.getName()).log(Level.SEVERE, null, e);
                    
                    // ** System.out.println("END!");
                    EOF = true;
                }
            }
            
            // NOW WE HAVE A FULL tempBoard array
            //** System.out.println(Arrays.deepToString(mapBoard));
            

        } catch (Exception ex) {
            Logger.getLogger(MapObject.class.getName()).log(Level.SEVERE, null, ex);
            // NO SUCH FILE EXISTS OR DATA IS CORRUPT
            System.out.println("ERROR - MAP could not be read");
        }
        
    }
    
}
