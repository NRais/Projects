/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package takeover.gameFiles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import takeover.Takeover;

/**
 *
 * @author Nuser
 */
public class PlayerObject {
    
    String myName = "";
    
    UnitObject myUnits[];
    
    int myPoints = 0;
    
    List<String> myWeapons = new ArrayList<>();
        
    Color myTeamColor;
    
    
    public PlayerObject(String name, Color color, Boolean AIPlayer, int numberOfUnits, int speed) {
        
        myName = name;
        myTeamColor = color;
        
        // initialize units
        myUnits = new UnitObject[numberOfUnits];
        
        for (int i = 0; i < numberOfUnits; i++) {
            
            // TODO determine if the unit is AI or not
            Boolean isAI = false;
            
            // if it is an AI player or the 3rd/4th/... unit than it is AI
            if (i > 2 | AIPlayer) {
                isAI = true;
            }
            
            // add unit
            myUnits[i] = new UnitObject(isAI, speed);
        }
        
    }
    
    
    
    ///////////////////////////
    // DEFAULT CLASS
    ///////////////////////////
    // USED TO SETUP PLAYERS
    ///////////////////////////
    
    // this class is a bit unlike the rest
    // it is meant to be called from out side the thing in order to allow you to setup the players
    public static PlayerObject[] setupDefaultPlayers() {
        
        // make new player object array
        PlayerObject[] newPlayers = new PlayerObject[Takeover.numberOfPlayers];
        
        // generate players
        for (int i = 0; i < newPlayers.length; i++) {
            
            // setup player characteristics
            String name = Takeover.playerUser[i];
            Color team = Takeover.playerColor[i];
            Boolean ai = Takeover.playerAI[i];
            int units = Takeover.numberOfUnits[i];
            int unitSpeed = Takeover.defaultSpeedTime;
            
            newPlayers[i] = new PlayerObject(name, team, ai, units, unitSpeed);
        }
        
        return newPlayers;
    }
    
}
