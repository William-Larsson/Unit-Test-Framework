/**
 *
 * Author: William Larsson
 * Date: 2019-11-XX
 */
import se.umu.cs.unittester.TestClass;
import java.lang.reflect.*;
import java.util.ArrayList;


public class ClassController{
    private String className;
    private Class<?> cl = null;
    private TestClass classInstance;
    private Method[] methods;

    /**
     * Create an instance of the given class, save its name and all of its methods.
     * @param className = the name of the class.
     */
    public ClassController(String className){
        this.className = className;
        try {
            cl = Class.forName(className);
            methods = cl.getMethods();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class can not be found.");
        }
    }


    /**
     * A method for validating if the class is a valid test class and not an interface or GUI and makes
     * sure it implements the TestClass interface.
     * @return True if the class is a valid test class, otherwise false.
     */
    public boolean isValidTestClass (){
        if (cl.isInterface()) {
            System.out.println(className + " is an interface and cannot be instantiated.");
            return false;
        } else if (!TestClass.class.isAssignableFrom(cl)){
            System.out.println(className + "does not implement the TestClass interface.");
            return false;
        } else {
            try {
                Constructor<?> construct = cl.getConstructor();
                classInstance = (TestClass) construct.newInstance();

                if (classInstance instanceof java.awt.Component) {
                    System.out.println(className + " is a GUI component.");
                    return false;
                } else {
                    System.out.println(cl + " is NOT a GUI component.");
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
}
