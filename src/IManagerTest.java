/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bpt
 */
public class IManagerTest {
    IManager manager;

    public IManagerTest() {
    }



    @Before
    public void setUp() {
        manager = new DoubleElimSrj12();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setPlayers method 
     */
    @Test
    public void testSetPlayers() {
        System.out.println("setPlayers");
        ArrayList<String> players = new ArrayList<>();
        players.add("A");
        players.add("B");
        players.add("C");
        players.add("D");
        players.add("E");


        manager.setPlayers(players);
        assertTrue(manager.hasNextMatch());

    }

    /**
     * Test of hasNextMatch method, of class IManager.
     */
    @Test
    public void testHasNextMatch() {
        System.out.println("hasNextMatch");
        ArrayList<String> players = new ArrayList<>();
        players.add("A");
        players.add("B");


        manager.setPlayers(players);
        assertTrue(manager.hasNextMatch());
        Match m = manager.nextMatch();  // A vs B
        manager.setMatchWinner(true); // WQ: A, LQ: B
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch(); // A vs B
        manager.setMatchWinner(true); // Winner A
        assertFalse(manager.hasNextMatch());
    }

    /**
     * Test of nextMatch method, of class IManager.
     */
    @Test
    public void testNextMatch() {
        System.out.println("nextMatch");
        ArrayList<String> players = new ArrayList<>();
        players.add("A");
        players.add("B");
        players.add("C");

        manager.setPlayers(players);
        assertTrue(manager.hasNextMatch());
        Match m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "A");
        assertEquals(m.getPlayer2(), "B");
        manager.setMatchWinner(true);  //WQ: C, A ; LQ: B
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);  //WQ: C ; LQ: B, A
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "B");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);  //WQ: C ; LQ: B
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "B");
        manager.setMatchWinner(true);  // Final complete
        assertFalse(manager.hasNextMatch());
        String winner = manager.getWinner();
        assertEquals(winner, "C");
    }





    /**
     * Test of undo method, of class IManager.
     */
    @Test
    public void testUndo() {
        System.out.println("undo");
        ArrayList<String> players = new ArrayList<>();
        players.add("A");
        players.add("B");
        players.add("C");

        manager.setPlayers(players);
        assertFalse(manager.canUndo());
        Match m = manager.nextMatch();
        assertFalse(manager.canUndo());
        assertEquals(m.getPlayer1(), "A");
        assertEquals(m.getPlayer2(), "B");
        manager.setMatchWinner(true);  //WQ: C, A ; LQ: B
        assertTrue(manager.canUndo());
        manager.undo();
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "A");
        assertEquals(m.getPlayer2(), "B");
        assertFalse(manager.canUndo());
        manager.setMatchWinner(true);  //WQ: C, A ; LQ: B
        assertTrue(manager.canUndo());
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);  //WQ: C ; LQ: B, A
        assertTrue(manager.canUndo());
        manager.undo();
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        assertTrue(manager.canUndo());
        manager.undo();
        assertTrue(manager.hasNextMatch());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "A");
        assertEquals(m.getPlayer2(), "B");
        assertFalse(manager.canUndo());
    }

    /**
     * Test of redo method, of class IManager.
     */
    @Test
    public void testRedo() {
        System.out.println("redo");
        ArrayList<String> players = new ArrayList<>();
        players.add("A");
        players.add("B");
        players.add("C");

        manager.setPlayers(players); //WQ: A,B, C ; LQ:
        Match m = manager.nextMatch(); //A, B
        assertFalse(manager.canRedo());
        manager.setMatchWinner(true);  //WQ: C, A ; LQ: B
        m = manager.nextMatch();
        manager.undo(); //WQ: A,B, C ; LQ:
        assertTrue(manager.canRedo());
        manager.redo(); //WQ: C, A ; LQ: B
        assertFalse(manager.canRedo());
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);  //WQ: C ; LQ: B, A
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "B");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);  //WQ: C ; LQ: B
        assertFalse(manager.canRedo());
        manager.undo(); //WQ: C ; LQ: B, A
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "B");
        assertEquals(m.getPlayer2(), "A");
        assertTrue(manager.canRedo());
        manager.undo(); //WQ: C, A ; LQ: B
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        assertTrue(manager.canRedo());
        manager.undo(); //WQ: A,B, C ; LQ:
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "A");
        assertEquals(m.getPlayer2(), "B");
        assertTrue(manager.canRedo());
        manager.redo(); //WQ: C, A ; LQ: B
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "C");
        assertEquals(m.getPlayer2(), "A");
        assertTrue(manager.canRedo());
        manager.redo(); //WQ: C ; LQ: B, A
        m = manager.nextMatch();
        assertEquals(m.getPlayer1(), "B");
        assertEquals(m.getPlayer2(), "A");
        manager.setMatchWinner(true);
        assertFalse(manager.canRedo());
    }



    /**
     * Test with a set of random competitions.
     */
    @Test
    public void testRandomCompetions() {
        System.out.println("random competition");
        Random rand = new Random();

        int count = 2;
        for (int k = 0; k < 10; k++) {
            ArrayList<String> players = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                players.add("" + i);
            }
            int targetWinner = rand.nextInt(players.size());
            String targetWinnerStr = "" + targetWinner;
            manager.setPlayers(players);

            while (manager.hasNextMatch()) {
                Match m = manager.nextMatch();
                String p1 = m.getPlayer1();
                String p2 = m.getPlayer2();
                if (p1.equalsIgnoreCase(targetWinnerStr)) manager.setMatchWinner(true);
                else if (p2.equalsIgnoreCase(targetWinnerStr)) manager.setMatchWinner(false);
                else {
                    boolean b = rand.nextBoolean();
                    if (b) manager.setMatchWinner(true);
                    else manager.setMatchWinner(false);
                }
            }
            String winner = manager.getWinner();
            assertTrue(winner.equalsIgnoreCase(targetWinnerStr));
            count*=2;
        }
    }


}
