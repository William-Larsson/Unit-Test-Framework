import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LogicWorker extends SwingWorker<String, Object> {
    private JTextArea textArea;
    private ClassController cc;
    private Method method;
    private Score score;
    private boolean setUp;
    private boolean tearDown;
    private boolean result;


    /**
     *
     * @param cc
     * @param method
     * @param score
     */
    public LogicWorker (JTextArea textArea, ClassController cc, Method method, Score score){
        this.textArea = textArea;
        this.cc       = cc;
        this.method   = method;
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
     *
     * @return
     */
    @Override
    protected String doInBackground(){
        if (cc.isValidTestClass()) {
            if (setUp){
                cc.setUpTearDown("setUp");
            }

            try{
                result = cc.invokeMethodByName(method.getName());

                if (tearDown){
                    cc.setUpTearDown("tearDown");
                }

                if (result){
                    score.setSuccess(score.getSuccess() + 1);
                    return method.getName() + ": Success.";
                } else {
                    score.setFail(score.getFail() + 1);
                    return method.getName() + ": Fail.";
                }
            } catch (NoSuchMethodException |
                    IllegalAccessException |
                    InvocationTargetException e)
            {
                score.setException(score.getException() + 1);
                if (tearDown){
                    cc.setUpTearDown("tearDown");
                }
                return method.getName() + ": Fail. Generated a " + e.getCause();
            }
        }
        return "Invalid test class.\n" + cc.getInvalidCause();
    }


    /**
     *
     */
    protected void done(){
        try {
            String returnValue = get();

            if (!returnValue.startsWith("Invalid test class")){

                textArea.append(returnValue + "\n");

            } else{
                JOptionPane.showMessageDialog(null, returnValue,
                        "An error occurred.", JOptionPane.ERROR_MESSAGE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Thread was interrupted.");
        }
    }





    // TODO: Do I need an execute method as well?   -- no, but .execute() is called when
    //       I want to call doInBackground and when that is finished, done() will be called
    //       automaticatlly.
    // Check lecture 3 example.
}
