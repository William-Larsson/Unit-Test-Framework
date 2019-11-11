/**
 * A class that that handles all the functionality we need to examine
 * a given class that's implementing the TestClass interface.
 *
 * Author: William Larsson
 * Date: 2019-11-08
 */
import se.umu.cs.unittester.TestClass;
import java.lang.reflect.*;
import java.util.ArrayList;


/**
 *
 */
public class ClassController{
    private String className;
    private Class<?> cl;
    private TestClass classInstance;
    private Method[] methods;
    private String invalidCause;

    /**
     * Create an instance of the given class, save its name and all of its methods.
     * @param className = the name of the class.
     */
    public ClassController(String className, Class cl){
        this.className = className;
        this.cl = cl;
        methods = cl.getMethods();
    }


    /**
     * A method for validating if the class is a valid test class and not an interface or GUI and makes
     * sure it implements the TestClass interface.
     * Also saves the reason why class fails the validation test in a local variable.
     * @return True if the class is a valid test class, otherwise false.
     */
    public boolean isValidTestClass (){
        if (cl.isInterface()) {
            invalidCause = className + " is an interface and cannot be instantiated.";
            return false;
        } else if (!TestClass.class.isAssignableFrom(cl)){
            invalidCause = className + "does not implement the TestClass interface.";
            return false;
        } else {
            try {
                Constructor<?> construct = cl.getConstructor();
                classInstance = (TestClass) construct.newInstance();

                if (classInstance instanceof java.awt.Component) {
                    invalidCause = className + " is a GUI component.";
                    return false;
                }
            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * Return the class object for usage in other parts of the system.
     * @return The TestClass object.
     */
    public TestClass getClassObject(){
        return classInstance;
    }


    /**
     * Return all the methods that the class object has got. This includes methods from
     * superclasses, such as Object.
     * @return = An array of methods.
     */
    public Method[] getTestMethods(){
        return methods;
    }


    /**
     *  Creates and return a list of all the names if methods in the class as am
     *  array of strings.
     * @return = list of all the names of the methods.
     */
    public ArrayList<String> methodsToString(){
        ArrayList<String> names = new ArrayList<>();
        for (Method m : methods) {
            names.add(m.getName());
        }
        return names;
    }


    /**
     * Given a name of a method, invoke that method. Then print to the user what the return value was,
     * unless an exception was thrown.
     * @param name = the name of the method.
     * @return = the return value the invoked method.
     */
    public boolean invokeMethodByName(String name) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException
    {
        System.out.println(classInstance.getClass().getMethod(name));
        boolean result = (boolean) classInstance.getClass().getMethod(name).invoke(classInstance);
        System.out.println(name + "-method returned a " + result);
        return result;
    }


    /**
     * A method variant on the invokeByNameMethod. Specially needed for setUp and tearDown methods, since
     * these do not have nay return value. Invokes the method, aka executes the code in that method.
     * @param name = setUp or tearDown, for each respective method.
     */
    public void setUpTearDown(String name){
        try {
            classInstance.getClass().getMethod(name).invoke(classInstance);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("Method " + name + " threw a " + e.getCause());
        }
    }


    /**
     *
     * @return
     */
    public String getInvalidCause(){
        return invalidCause;
    }
}
