package JUnit.tests;
import se.umu.cs.unittester.TestClass;
import java.lang.reflect.Method;

/**
 * JUnit tests for the ClassController class. Tests are divided into two separate sup-classes
 * because of different needs from the setUp- and tearDown-methods that are executed before/after
 * each of the method tests.
 *
 * Author: William Larsson
 * Date: 2019-11-20
 */
class ClassControllerTest {
    private static class testing1 implements se.umu.cs.unittester.TestClass{}
    private static class testing2{}

    private String className;
    private Class<?> cl;
    private TestClass classInstance;
    private Method[] methods;
    private String invalidCause;

    /**
     * Tests that doesn't require an actual instance of the class we want to test.
     * (or they are created internally, so an instance wont need to be made in advance)
     */
    class noClassInstance{

        @org.junit.jupiter.api.BeforeEach
        void setUp(){
            try {
                cl = Class.forName("testing1");
                className = cl.getName();
                methods = cl.getMethods();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @org.junit.jupiter.api.AfterEach
        void tearDown() {
        }

        @org.junit.jupiter.api.Test
        void isValidTestClass() {
            System.out.println(cl.getMethods());
        }

        @org.junit.jupiter.api.Test
        void methodsToString() {
        }

        @org.junit.jupiter.api.Test
        void getInvalidCause() {
        }
    }


    /**
     * Tests that require an instance of the test class we want to test to be made in advanced.
     */
    class classInstanceNeeded {

        @org.junit.jupiter.api.BeforeEach
        void setUp(){
            try {
                cl = Class.forName("testing1");
                className = cl.getName();
                methods = cl.getMethods();
                //TODO: make a class instance
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @org.junit.jupiter.api.AfterEach
        void tearDown() {
        }

        @org.junit.jupiter.api.Test
        void invokeMethodByName() {
        }

        @org.junit.jupiter.api.Test
        void setUpTearDown() {
        }
    }
}