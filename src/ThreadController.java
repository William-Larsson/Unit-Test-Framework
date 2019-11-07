import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

// TODO: For the report, mention that Java 9 (or higher) or later is needed for compilation

public class ThreadController {
    private Thread logicThread = null;
    private Thread GUIThread   = null;

    /**
     * This thread handles the main logic of the system.
     * Input is received from the user regarding which Java-class the system should
     * perform a unit-test on. If the given class is a valid test class, the system
     * will invoke all the test-methods available and return the result.
     */
    private Thread logic = new Thread(() -> {

        Scanner scan   = new Scanner(System.in);
        ClassController cc = new ClassController(scan.next());
        ArrayList<String> methodNames;

        if (cc.isValidTestClass()) {
            methodNames = cc.methodsToString();
            Boolean result;
            int success = 0, failed = 0, except = 0;

            for (Method m : cc.getTestMethods()) {
                if (m.getName().startsWith("test")){
                    if (methodNames.contains("setUp")){
                        cc.setUpTearDown("setUp");
                    }

                    try{
                        result = cc.invokeMethodByName(m.getName());
                    } catch (NoSuchMethodException |
                            IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                        System.out.println(m.getName() + "-method threw a " + e.getCause() + ".");

                        if (methodNames.contains("tearDown")){
                            cc.setUpTearDown("tearDown");
                        }
                        except++;
                        continue;
                    }

                    if (methodNames.contains("tearDown")){
                        cc.setUpTearDown("tearDown");
                    }

                    if (result){
                        success++;
                    } else {
                        failed++;
                    }
                }
            }
            System.out.println("\n\nTHIS IS THE RESULT: " + "\nSuccess: " + success +
                    "\nFailed: " + failed + "\nException: " + except + "\n");
        }
    });



    /*  TODO: Make function for starting the EDT-thread, or do I need some other solution?
        // Start EDT thread.
        SwingUtilities.invokeLater(() -> {
            UserInferface gui = new UserInferface();
        });
    */



    /**
     * A function that will make sure that all needed thread to get the system
     * started gets launched.
     */
    public void startThreads(){
        logic.start();
        logicThread = logic;
    }
}
