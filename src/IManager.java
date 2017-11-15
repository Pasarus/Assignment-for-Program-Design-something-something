/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 * Interface that defines the behaviour of a competition
 * @author bpt
 */
public interface IManager {
    /**
     * Set the players or teams to use in the competition
     * @param players the players or teams
     */
    public void setPlayers(ArrayList<String> players);
    
    /**
     * Return true if there is another match in the competition that can be fetched using nextMatch
     * @return returns true if the competition is still going
     */
    public boolean hasNextMatch();
    
    /**
     * Returns the nextMatch to play
     * @return returns the next match
     * @throws NoNextMatchException if the competition is over and no more matches
     */
    public Match nextMatch() throws NoNextMatchException;
    
    /** Sets the winner for the last retrieved Match
     * 
     * @param player1 true indicates the first player wins, otherwise player 2
     */
    public void setMatchWinner(boolean player1);
    
    /** 
     * Get the name of the player/team that finished in first place. 
     * @return returns the name of the team/player, or null if competition still running or n too large
     */
    public String getWinner();
    
    /** 
     * Undo the last match score entered
     */
    public void undo();
    
    /**
     * Redo a previously undone match score entry
     */
    public void redo();
    
    /**
     * Check if it is possible to undo the last match entered
     * @return true if undo is possible or false otherwise
     */
    public boolean canUndo();
    
    /**
     * Check if it is possible to redo the last undone match entry
     * @return true if redo is possible or false otherwise
     */
    public boolean canRedo();
    
}
