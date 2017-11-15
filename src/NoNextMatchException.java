/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Exception thrown if trying to get a Match when the competition is over
 * @author bpt
 */
public class NoNextMatchException extends RuntimeException {
    /** Constructor for the exception
     * 
     * @param str An exception message of your choice
     */
    public NoNextMatchException(String str) {
        super(str);
    }
}
