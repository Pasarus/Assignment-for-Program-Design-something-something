/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * Represents a single Match in a competition
 * @author bpt
 */
public class Match implements Cloneable {
    String player1, player2;
    /**
     * Construct a match from the two players
     * @param player1 the first player
     * @param player2 the second player
     */
    public Match(String player1, String player2){
        this.player1 = player1;
        this.player2 = player2;
    }
    
    /**
     * Get the first player
     * @return returns player1
     */
    public String getPlayer1() {
        return player1;
    }

    /**
     * Get the second player
     * @return returns player2
     */
    public String getPlayer2(){
        return player2;
    }

    @Override
    public Object clone() {
        Match m = new Match(player1, player2);
        return m;
    }
}
