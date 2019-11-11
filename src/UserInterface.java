import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class UserInterface extends JFrame implements ActionListener {
    private JPanel inputPanel;
    private JPanel contentPanel;
    private JPanel resultPanel; // TODO: use boxes instead?
    private JTextField textInput;
    private JButton button;
    private JTextArea textArea;
    private JOptionPane popup;
    private Score score;


    /**
     *
     * @param title
     */
    public UserInterface(String title) {
        super(title);

        inputPanel   = new JPanel(new FlowLayout(FlowLayout.LEADING));
        contentPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textInput    = new JTextField(25);
        button       = new JButton("Run tests");
        textArea = new JTextArea();

        textInput.setFont(textInput.getFont().deriveFont(18f));
        contentPanel.setMinimumSize(new Dimension(400, 400));

        add(inputPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.SOUTH);
        inputPanel.add(textInput);
        inputPanel.add(button);
        contentPanel.add(textArea);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Listen for Enter-key input from the text field or "Run Tests" button press.
     * When that occurs, .actionPerformed() will be executed.
     */
    public void listen(){
        button.addActionListener(this);
        textInput.addActionListener(this);
    }


    /**
     * When an action has occurred, this method will run. If the action is caused by
     * a button-press or from pressing Enter in the text field, then the method will
     * loop through all the method in the given class and test them an a separate
     * SwingWorker thread.
     * @param e = The origin of the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button || e.getSource() == textInput){
            try {
                String className = textInput.getText();
                Class testClass = Class.forName(className);

                score = new Score();

                for (Method m : testClass.getMethods()) {
                    if (m.getName().startsWith("test")){
                        // TODO: Doing this to make it thread-safe. Is there any better way?
                        new LogicWorker(textArea, new ClassController(
                                className, Class.forName(className)), m, score).execute();
                    }
                }
            } catch (ClassNotFoundException ex){
                //errorMessage(ex, "Could not find any class named " + textInput.getText());
                JOptionPane.showMessageDialog(null, "Could not find any class named " +
                                textInput.getText(),"An error occurred.", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            System.out.println("Error: Action did not originate from Button or TextField");
        }
    }


    /**
     *
     * @param e
     * @param message
     */
    public void errorMessage(Exception e, String message){
        JOptionPane.showMessageDialog(null, message + "\nCaused by "
                + e, "An error occurred.", JOptionPane.ERROR_MESSAGE);
    }
}