/**
 * Main file for the Unit-test system. Starts up the main thread used for the
 * logic- and GUI-parts of the program.
 *
 * Author: William Larsson
 * Date: 2019-11-07
 */

public class MyUnitTester {
    public static void main(String[] args) {
        ThreadController tc = new ThreadController();
        tc.startThreads();
    }
}
