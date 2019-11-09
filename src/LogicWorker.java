import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class LogicWorker extends SwingWorker {
    private ClassController cc;
    private Method method;
    private boolean setUp;
    private boolean tearDown;


    public LogicWorker (ClassController cc, Method method){
        this.cc = cc;
        this.method = method;
        ArrayList<String> methodNames = cc.methodsToString();

        if (methodNames.contains("setUp")){
            setUp = true;
        }
        if (methodNames.contains("tearDown")){
            tearDown = true;
        }
    }


    @Override
    protected Integer doInBackground(){
        if (cc.isValidTestClass()) {
            int success = 0, failed = 0, except = 0;
            boolean result = false;

            if (setUp){
                cc.setUpTearDown("setUp");
            }

            try{
                result = cc.invokeMethodByName(method.getName());
            } catch (NoSuchMethodException |
                    IllegalAccessException |
                    InvocationTargetException e)
            {
                e.printStackTrace();
                System.out.println(method.getName() + "-method threw a " + e.getCause() + ".");

                if (tearDown){
                    cc.setUpTearDown("tearDown");
                }
                except++;
            }

            if (tearDown){
                cc.setUpTearDown("tearDown");
            }

            if (result){
                success++;
            } else {
                failed++;
            }
            System.out.println("\n\nTHIS IS THE RESULT: " + "\nSuccess: " + success +
                    "\nFailed: " + failed + "\nException: " + except + "\n");
        }
        return 0;
    }



    protected void done(){
        // TODO: Update the UI elements
        //  output.append(the result)
    }

    // TODO: Do I need an execute method as well?   -- no, but .execute() is called when
    //       I want to call doInBackground and when that is finished, done() will be called
    //       automaticatlly.
    // Check lecture 3 example.
}
