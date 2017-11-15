import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main program to run a competition
 * @author bpt
 */
public class CompetitionManager {
    /** The manager in use */
    IManager manager;
    
    /** Constructor for a CompetitionManager
     * 
     * @param manager the manager decides the rules of the competition (playing order)
     */
    public CompetitionManager(IManager manager) {
        this.manager = manager;
    }
    
    /** Runs the competition, pairs of teams are presented and the system waits for input of 
     * the results between each pair. Accepts undo and redo commands from the user.
     * 
     * @param listFileName the name of the file contain the list of teams
     * @throws FileNotFoundException thrown if the list of players can't be found
     */
    public void runCompetition(String listFileName) throws FileNotFoundException {
        ArrayList<String> competitors = readPlayers(listFileName);

        Scanner in = new Scanner(System.in);
      
        manager.setPlayers(competitors);
        Match match = manager.nextMatch();
        do {
             
            System.out.println("Player 1: " + match.getPlayer1());
            System.out.println("Player 2: " + match.getPlayer2());
            System.out.println("Please enter scores, redo or undo");
            boolean notValidInput = true;
            boolean draw=true;
            int p1score=0, p2score=0;
            String op = "";
            while (draw) {
                while (notValidInput) {
                    if (in.hasNextInt()) {
                        p1score = in.nextInt();
                        notValidInput = false;
                    } else if (in.hasNext()) {
                        String str = in.next();
                        if (str.equalsIgnoreCase("undo") || str.equalsIgnoreCase("redo")) {
                            notValidInput=false;
                            op = str;
                        }
                        else System.out.println(str + " is not a valid input, please enter a number, redo or undo");
                    }
                }
                
                if (op.equalsIgnoreCase("undo")) {
                    if (manager.canUndo()) {
                        manager.undo(); 
                        if (manager.hasNextMatch()) match = manager.nextMatch();
                    } else {
                        System.out.println("Can't undo, first match.");
                    }
                    break;
                }
                if(op.equalsIgnoreCase("redo")) {
                    if (manager.canRedo()) {
                        manager.redo();
                        if (manager.hasNextMatch()) match = manager.nextMatch();
                    }  else {
                        System.out.println("Can't redo, last match.");
                    }
                    break;
                }
                notValidInput = true;
                while (notValidInput) {
                    if (in.hasNextInt()) {
                        notValidInput = false;
                    } else if (in.hasNext()) {
                        String str = in.next();
                        System.out.println(str + " is not a valid input, please enter a number");
                    }
                }
                p2score = in.nextInt();
                if (p1score == p2score) {
                    System.out.println("We need a result, not a draw!  Please have a rematch!");
                } else {
                    draw = false;
                }
                if (!draw) {
                    manager.setMatchWinner(p1score > p2score);
                    if (manager.hasNextMatch()) match = manager.nextMatch();
                    else {
                         System.out.println("Winner is: " + manager.getWinner());
                        System.out.println("Please enter quit or undo:");
                        notValidInput = true;
                        while (notValidInput) {
                            notValidInput = false;
                        
                            if (in.hasNext()) {
                                String str = in.next();
                                if (str.equalsIgnoreCase("quit")) break;
                                else if (str.equalsIgnoreCase("undo")) {
                                    if (manager.canUndo()) {
                                        manager.undo();
                                        if (manager.hasNextMatch()) {
                                            match = manager.nextMatch();
                                        }
                                    } else {
                                        System.out.println("Can't undo, first match.");
                                    }
                                    break;
                                } else {
                                    System.out.println(str + " is not a valid input, please enter quit or undo");
                                    notValidInput = true;
                                }
                            }
                        }
                    }
                }
            }
            
        } while(manager.hasNextMatch());
    }
    
    /** Main method
     * 
     * @param args args[0] should be the name of the IManager class and args[1] the name of a file containing the list of players or teams, one per line
     * 
     * @throws FileNotFoundException thrown if the list file isn't found
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length!=2) {
            System.out.println("Usage: CompetitionManager <IManager class name> <list file> ");
            System.exit(0);
        }
        IManager manager = IManagerFactory.getManager(args[0]);
        if (manager==null) {
            System.out.println("Can't find IManager class name: " + args[0]);
            System.exit(0);
        }
        
        CompetitionManager cm = new CompetitionManager(manager);
        cm.runCompetition(args[1]);
    }
    
    public static ArrayList<String> readPlayers(String filename) throws FileNotFoundException {
        FileReader fr = new FileReader(filename);
        Scanner sc = new Scanner(fr);
        ArrayList<String> players = new ArrayList<>();
        while (sc.hasNextLine()) {
            players.add(sc.nextLine());
        }
        return players;
    }
    
}
