/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover.gameFiles;

import javax.swing.JLabel;

/**
 *
 * @author Nuser
 */
public class UnitObject extends JLabel {
    
    String myDirection = ""; // LEFT,RIGHT,UP,DOWN (ANYTHING ELSE WILL CAUSE IT NOT TO MOVE)
    
    Boolean aiControlled;
    
    int[] myLocation = new int[2]; // X,Y coordinates of location
    
    int myTimerCount = 0; // a count down until 0 when it reaches 0 the unit can move (differnet units can have different speeds)
    int myTime = 0; // speed of the unit (how much it has to countdown each time)
    
    int[] myStartSpace = new int[2];
    
    
    
    public UnitObject(Boolean AI, int speedTime) {
        
        // start out as X,Y = 0,0
        myStartSpace[0] = 0;
        myStartSpace[1] = 0;
                
        myTime = speedTime;
        
        aiControlled = AI;
    }
    
}
