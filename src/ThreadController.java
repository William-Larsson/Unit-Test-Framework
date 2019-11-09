import javax.swing.*;
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
        Scanner scan       = new Scanner(System.in);
        ClassController cc = new ClassController(scan.next());
        ArrayList<String> methodNames;

        if (cc.isValidTestClass()) {
            int success = 0, failed = 0, except = 0;
            boolean result, setUp = false, tearDown = false;

            methodNames = cc.methodsToString();
            if (methodNames.contains("setUp")){
                setUp = true;
            }
            if (methodNames.contains("tearDown")){
                tearDown = true;
            }

            for (Method m : cc.getTestMethods()) {
                if (m.getName().startsWith("test")){
                    if (setUp){
                        cc.setUpTearDown("setUp");
                    }

                    try{
                        result = cc.invokeMethodByName(m.getName());
                    } catch (NoSuchMethodException |
                            IllegalAccessException |
                            InvocationTargetException e)
                    {
                        e.printStackTrace();
                        System.out.println(m.getName() + "-method threw a " + e.getCause() + ".");

                        if (tearDown){
                            cc.setUpTearDown("tearDown");
                        }
                        except++;
                        continue;
                    }

                    if (tearDown){
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

}
