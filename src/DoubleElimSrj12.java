import java.util.*;

/**
 * Created by Pasarus on 12/11/2017.
 */

public class DoubleElimSrj12 implements IManager {

    //Using ArrayDeque as it is a more efficient implementation of the Deque interface than a LinkedList
    private Deque<String> winnersQueue = new ArrayDeque<>();
    private Deque<String> losersQueue = new ArrayDeque<>();

    //A stack thats actually a Deque, Used for the undo and redo.
    private Deque<StoreMatch> undo = new ArrayDeque<>();
    private Deque<StoreMatch> redo = new ArrayDeque<>();

    //A stack that's actually a Deque for what match type it was.

    //Used for keeping track of who won the final game
    private String lastMatchWinner = "";

    //Used for keeping track between games of the first and second players.
    private String firstPlayer = "";
    private String secondPlayer = "";

    //Defines which type of match is currently being played.
    //0 = Winner\Winner, 1 = Winner\Looser, 2 = Looser\Looser
    private int matchType = 0;

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
        if (canUndo()){
            undo.push(new StoreMatch(returnMatch.getPlayer1(), returnMatch.getPlayer2(), matchType));
        }
        return returnMatch;
    }

    public void setMatchWinner(boolean player1){
        if (player1){
            //Player 1 won
            switch (matchType){
                case 0:
                    //Win/Win game where player1 won
                    winnersQueue.addLast(firstPlayer);
                    losersQueue.addLast(secondPlayer);
                    break;
                case 1:
                    //Winner of the whole thing - player1
                    lastMatchWinner = firstPlayer;
                    break;
                case 2:
                    //Loose/Loose game where player1 won
                    losersQueue.addLast(firstPlayer);
                    break;
            }
        } else {
            //Player2 won
            switch (matchType){
                case 0:
                    //Win/Win game where player2 won
                    winnersQueue.addLast(secondPlayer);
                    losersQueue.addLast(firstPlayer);
                    break;
                case 1:
                    //Winner of the whole thing - player2
                    winnersQueue.addLast(secondPlayer);
                    break;
                case 2:
                    //Loose/Loose game where player2 won
                    losersQueue.addLast(secondPlayer);
                    break;
            }
        }
    }

    public String getWinner(){
        return lastMatchWinner;
    }

    public void undo(){
        StoreMatch undone = undo.pop();
        redo.push(undone);

        String player1 = undone.getPlayer1();
        String player2 = undone.getPlayer2();

        int newMatchType = undone.getMatchType();
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

    }

    public boolean canUndo(){
        //replace with true when implemented.
        return true;
    }

    public boolean canRedo() {
        //Replace with true when implemented.
        return false;
    }
}

//This new class has been made so that I can store in the undo/redo stacks matchType along with the other data,
//That way I don't need an extra stack for match type.
class StoreMatch extends Match {

    private int matchType;

    public StoreMatch(){
        super("","");
    }
    StoreMatch(String player1, String player2, int matchType){
        super (player1, player2);
        this.matchType = matchType;
    }

    public int getMatchType(){
        return matchType;
    }

    public String getPlayer1(){
        return super.player1;
    }

    public String getPlayer2(){
        return super.player2;
    }
}