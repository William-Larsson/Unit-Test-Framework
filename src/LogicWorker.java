/**
 * SwingWorker class. Used to invoke all the methods that we want tested
 * from a given class.
 */
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LogicWorker extends SwingWorker<ArrayList<String>, Object> {
    private JTextArea textArea;
    private ClassController cc;
    private Method[] methods;
    private Score score;
    private boolean setUp;
    private boolean tearDown;
    private boolean result;

    /**
     * Gets access to all vital areas of the system an set up the ground
     * rules for the given test class.
     * @param textArea = the GUI component where the result will be written
     * @param cc = class controller with functionality for the methods.
     * @param methods = the methods to test
     * @param score = the resulting score.
     */
    public LogicWorker (JTextArea textArea, ClassController cc, Method[] methods, Score score){
        this.textArea = textArea;
        this.cc       = cc;
        this.methods  = methods;
        this.score    = score;
        this.result   = false;

        ArrayList<String> methodNames = cc.methodsToString();

        if (methodNames.contains("setUp")){
            setUp = true;
        }
        if (methodNames.contains("tearDown")){
            tearDown = true;
        }
    }


    /**
     * SwingWorker thread that executes in the background. Calls all
     * the methods available and returns the answers given from them.
     * @return = the result from the methods as a sentence.
     */
    @Override
    protected ArrayList<String> doInBackground() {
        ArrayList<String> output = new ArrayList<>();;
        if (cc.isValidTestClass()) {
            for (Method m : methods) {
                evalClassMethod(m, output);
            }
        } else {
            output.add("Invalid test class.\n" + cc.getInvalidCause());
        }
        return output;
    }


    /**
     * When doInBackground is done, print the result to the textArea.
     */
    protected void done(){
        try {
            ArrayList<String> returnValue = get();

            if (!returnValue.get(0).startsWith("Invalid test class.")){
                for (String str : returnValue) {
                    textArea.append(str + "\n");
                }
                textArea.append("\n" + score.getSuccess() + " tests succeeded.\n");
                textArea.append(score.getFail() + " tests failed.\n");
                textArea.append(score.getException() + " tests failed with an exception.\n\n");
            } else{
                JOptionPane.showMessageDialog(null, returnValue.get(0),
                        "An error occurred.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Thread was interrupted.");
        }
    }


    /**
     * If a method is a method that should be tested, run the setUp/tearDown
     * if required and test if a method is a success, fail or exception.
     * @param m = the method to test.
     * @param output = array containing all the method test results.
     */
    private void evalClassMethod(Method m, ArrayList<String> output){
        if (m.getName().startsWith("test")) {
            if (setUp) cc.setUpTearDown("setUp");

            try {
                result = cc.invokeMethodByName(m.getName());

                if (tearDown) cc.setUpTearDown("tearDown");

                if (result) {
                    score.setSuccess(score.getSuccess() + 1);
                    output.add(m.getName()+ ": Success.");
                } else {
                    score.setFail(score.getFail() + 1);
                    output.add(m.getName() + ": Fail.");
                }
            } catch (NoSuchMethodException |
                    IllegalAccessException |
                    InvocationTargetException e) {
                score.setException(score.getException() + 1);
                if (tearDown) cc.setUpTearDown("tearDown");
                output.add(m.getName() + ": Fail. Generated a " + e.getCause());
            }
        }
    }
}
