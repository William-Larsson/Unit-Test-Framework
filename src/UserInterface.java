import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class UserInterface extends JFrame implements ActionListener {
    private File dir;
    private URLClassLoader classLoader;
    private JPanel inputPanel;
    private JPanel contentPanel;
    private JTextField textInput;
    private JButton button;
    private JTextArea textArea;
    private JScrollPane scrollPane;


    /**
     * Build the GUI and set up the classpath to where the testClass-files are located.
     * @param title = The title written at the top of the GUI-window.
     */
    public UserInterface(String title) {
        super(title);

        dir = new File(".");
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{
                    dir.toURI().toURL()
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        inputPanel   = new JPanel();
        contentPanel = new JPanel();
        textInput    = new JTextField(25);
        button       = new JButton("Run tests");
        textArea     = new JTextArea("");
        scrollPane   = new JScrollPane(textArea);

        textInput.setFont(textInput.getFont().deriveFont(18f));
        textArea.setColumns(41);
        textArea.setRows(20);

        contentPanel.setBorder(BorderFactory.createTitledBorder("Test results."));
        contentPanel.setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.PAGE_START);
        add(contentPanel, BorderLayout.PAGE_END);
        contentPanel.add(scrollPane);
        inputPanel.add(textInput);
        inputPanel.add(button);

        setMinimumSize(new Dimension(500, 400));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
     * create a SwingWorker which will handle the user input.
     * @param e = The origin of the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        textArea.setText(null);
        if (e.getSource() == button || e.getSource() == textInput){
            try {
                String className = textInput.getText();
                Class testClass = Class.forName(className, true, classLoader);

                new LogicWorker(textArea, new ClassController(className, testClass),
                        testClass.getMethods(), new Score()).execute();

            } catch (ClassNotFoundException ex){
                JOptionPane.showMessageDialog(null,
                        "Could not find any class named " +
                                "\"" + textInput.getText() + "\"",
                        "An error occurred.", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Error: Action did not originate from Button or TextField");
        }
    }
}