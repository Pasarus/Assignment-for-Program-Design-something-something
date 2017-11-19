import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


/**
 * Created by Pasarus on 12/11/2017.
 */

public class DoubleElimSrj12 implements IManager {

    //Using ArrayDeque as it is a more efficient implementation of the Deque interface than a LinkedList
    private Deque<String> winnersQueue = new ArrayDeque<>();
    private Deque<String> losersQueue = new ArrayDeque<>();

    //A stack that's actually a Deque, Used for the undo and redo.
    private Deque<StoreMatch> undo = new ArrayDeque<>();
    private Deque<StoreMatch> redo = new ArrayDeque<>();

    //Used for keeping track of who won the final game
    private String lastMatchWinner = "";

    //Used for keeping track between games of the first and second players.
    private String firstPlayer = "";
    private String secondPlayer = "";

    //Defines which type of match is currently being played.
    //0 = Winner\Winner, 1 = Winner\Looser, 2 = Looser\Looser
    private int matchType = 0;

    //For tracking how many times undo has been called
    private int undoTotal = 0;

    public void setPlayers(ArrayList<String> players){
        //For each member of players add it into the winning queue.
        for (String player : players){
            winnersQueue.add(player);
        }
    }

    public boolean hasNextMatch(){
        //If winners and losers = 1 then true, else if winners or losers > 2 return true, else return false.
        return ((winnersQueue.size() == 1 && losersQueue.size() == 1) || winnersQueue.size() > 1 || losersQueue.size() > 1);
    }

    public Match nextMatch() throws NoNextMatchException{
        //declare match object, then depending on what method of match we are having gather information
        Match returnMatch;
        if (winnersQueue.size() == 1 && losersQueue.size() == 1){
            //If there is only one in either of the queues then do finals
            firstPlayer = winnersQueue.removeFirst();
            secondPlayer = losersQueue.removeFirst();
            matchType = 1;
            returnMatch = new Match(firstPlayer, secondPlayer);
        } else if (winnersQueue.size() > losersQueue.size()){
            //If winners is larger then
            firstPlayer = winnersQueue.removeFirst();
            secondPlayer = winnersQueue.removeFirst();
            matchType = 0;
            returnMatch = new Match(firstPlayer, secondPlayer);
        } else if (losersQueue.size() >= winnersQueue.size()){
            //If winners is not larger than losers do:
            firstPlayer = losersQueue.removeFirst();
            secondPlayer = losersQueue.removeFirst();
            matchType = 2;
            returnMatch = new Match(firstPlayer, secondPlayer);
        } else {
            //Catch if for some reason it isn't already caught even though it should be
            throw new NoNextMatchException("");
        }
        return returnMatch;
    }

    public void setMatchWinner(boolean player1){
                //Handle removing redo stack
        if(redo.size()>0){
            redo.clear();
        }

        //Add this new match result to the stack for undo
        undo.addFirst(new StoreMatch(firstPlayer, secondPlayer, matchType, player1));

        //Actually perform the action
        determineWinner(firstPlayer, secondPlayer, player1, matchType);
    }

    public String getWinner(){
        return lastMatchWinner;
    }

    public void undo(){
        StoreMatch undone = undo.pop();
        undoTotal++;
        redo.push(undone);

        String player1 = undone.getPlayer1();
        String player2 = undone.getPlayer2();

        int newMatchType = undone.getMatchType();

        //To check if nextmatch has been called previously and whether or not the undo is the first undo to happen
        if ((!(player1.equals(firstPlayer)) || !(player2.equals(secondPlayer)))&& (undoTotal==1)){
            //Undo the next match because nextmatch has been called
            switch(matchType){
                case 0:
                    //Winner/Winner reverse nextmatch
                    winnersQueue.addFirst(secondPlayer);
                    winnersQueue.addFirst(firstPlayer);
                    break;
                case 1:
                    //Winner/Loser reverse nextmatch
                    winnersQueue.addFirst(firstPlayer);
                    losersQueue.addFirst(secondPlayer);
                    break;
                case 2:
                    //Loser/Loser reverse nextmatch
                    losersQueue.addFirst(secondPlayer);
                    losersQueue.addFirst(firstPlayer);
                    break;
            }
        }

        //Then actually move onto finishing the undo
        if (newMatchType == 0){
            winnersQueue.addFirst(player2);
            winnersQueue.addFirst(player1);
        } else if (newMatchType == 1){
            //I believe that this can never be accessed but I added functionality anyway
            winnersQueue.addFirst(player1);
            losersQueue.addFirst(player2);
        } else if (newMatchType == 2){
            losersQueue.addFirst(player2);
            losersQueue.addFirst(player1);
        }
    }

    public void redo(){
        StoreMatch redoing = redo.pop();
        undoTotal--;

        //Remove player1 and player2 from the queues.
        winnersQueue.remove(redoing.getPlayer1());
        winnersQueue.remove(redoing.getPlayer2());
        losersQueue.remove(redoing.getPlayer1());
        losersQueue.remove(redoing.getPlayer2());

        //Redo the results of the previous match
        determineWinner(redoing.getPlayer1(), redoing.getPlayer2(), redoing.player1Won(), redoing.getMatchType());
        undo.push(redoing);
    }

    public boolean canUndo(){
        //replace with true when implemented.
        return undo.size() > 0;
    }

    public boolean canRedo() {
        //Replace with true when implemented.
        return redo.size() > 0;
    }

    private void determineWinner(String player1, String player2, Boolean player1Won, int matchType){
        if (player1Won){
            //Player 1 won
            switch (matchType){
                case 0:
                    //Win/Win game where player1 won
                    winnersQueue.addLast(player1);
                    losersQueue.addLast(player2);
                    break;
                case 1:
                    //Winner of the whole thing - player1
                    //Never going to actually access this part of code.
                    lastMatchWinner = player1;
                    break;
                case 2:
                    //Loose/Loose game where player1 won
                    losersQueue.addLast(player1);
                    break;
            }
        } else {
            //Player2 won
            switch (matchType){
                case 0:
                    //Win/Win game where player2 won
                    winnersQueue.addLast(player2);
                    losersQueue.addLast(player1);
                    break;
                case 1:
                    //Winner of the whole thing - player2
                    winnersQueue.addLast(player2);
                    break;
                case 2:
                    //Loose/Loose game where player2 won
                    losersQueue.addLast(player2);
                    break;
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException{
        String iManagerClassName = "DoubleElimSrj12";
        String fileName = "list.txt";

        IManager manager = IManagerFactory.getManager(iManagerClassName);
        CompetitionManager cm = new CompetitionManager(manager);

        cm.runCompetition(fileName);
    }
}


//This new class has been made so that I can store in the undo/redo stacks matchType along with the other data,
//That way I don't need an extra stack for match type.
class StoreMatch extends Match {

    private int matchType;
    private boolean player1Won;

    StoreMatch(String player1, String player2, int matchType, boolean player1Won){
        super (player1, player2);
        this.matchType = matchType;
        this.player1Won = player1Won;
    }

    int getMatchType(){
        return matchType;
    }

    public String getPlayer1(){
        return super.player1;
    }

    public String getPlayer2(){
        return super.player2;
    }

    boolean player1Won(){
        return player1Won;
    }
}