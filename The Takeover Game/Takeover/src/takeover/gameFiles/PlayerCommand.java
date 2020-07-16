/*
 * Here comes the text of your license
 *  * Copyright 2018 Nathan Rais 
 *  * 
 *  *      This file is part of The Easy Survey Creator.
 *  *
 *  *   The Easy Survey Creator by Nathan Rais is free software 
 *  *   but is licensed under the terms of the Creative Commons 
 *  *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *  *   you may redistribute this as long as you give attribution and do not 
 *  *   modify any part of this software in any way.
 *  *
 *  *   The Easy Survey Creator is distributed in the hope that it will be useful,
 *  *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  *   CC BY-ND license for more details.
 *  *
 *  *   You should have received a copy of the CC BY-ND license along with 
 *  *   The Easy Survey Creator. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 * 
 */
package takeover.gameFiles;

/**
 *
 * @author Nuser
 */
public class PlayerCommand {
    // package command for the controls hash table

    private final int player;
    private final int unit;
    private final String command;
    
    public PlayerCommand(int newPlayer, int newUnit, String newCommand) {
        this.player = newPlayer;
        this.unit = newUnit;
        this.command = newCommand;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerCommand)) return false;
        PlayerCommand key = (PlayerCommand) o;
        return player == key.player && unit == key.unit && command == key.command;
    }

    // THIS MUST BE A UNIQUE CODE FOR EACH OBJECT (in order for the hashmap to work)
    @Override
    public int hashCode() {
        // NOTE: there are only 4 players and only 2 units that can be controlled ever
        // there are a fair amount of commands however so that is the most important
        return (player << 4) + (unit << 4) + command.hashCode();
    }
    
    public int getPlayer() {
        return player;
    }
    public int getUnit() {
        return unit;
    }
    public String getCommand() {
        return command;
    }
}
