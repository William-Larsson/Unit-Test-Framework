import javax.swing.*;

/**
 * Main file for the Unit-test system. Initializes and runs the GUI so that
 * its ready for user input.
 *
 * Author: William Larsson
 * Date: 2019-11-10
 */

public class MyUnitTester {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UserInterface("Unit test framework").listen());
    }
}

// TODO: For the report, mention that Java 9 (or higher) or later is needed for compilation