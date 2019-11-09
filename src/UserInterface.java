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
    private JTextArea output;
    private ClassController cc;


    public UserInterface(String title) {
        super(title);

        inputPanel   = new JPanel(new FlowLayout(FlowLayout.LEADING));
        contentPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        textInput    = new JTextField(25);
        button       = new JButton("Run tests");
        output       = new JTextArea();

        textInput.setFont(textInput.getFont().deriveFont(18f));

        add(inputPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.SOUTH);
        inputPanel.add(textInput);
        inputPanel.add(button);
        contentPanel.add(output);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void listen(){
        button.addActionListener(this);
        textInput.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button || e.getSource() == textInput){
            cc = new ClassController(textInput.getText());

            for (Method m : cc.getTestMethods()) {
                if (m.getName().startsWith("test")){
                    new LogicWorker(cc, m);
                }
            }
        }
        else {
            System.out.println("Error in actionPerformed in UserInterface");
        }
    }
}



/*
        for (Method m : cc.getTestMethods()) {
            contentPanel.add(new JLabel(m.getName()));
        }
 */


// Använd JScrollPane för rutan där vi ska printa alla testresultat.
// JTextField går att låsa så att ingen ny input kan skrivas (typ under körning av föregående input?)
// JOptionPane -- kan användas för att skriva ut errormeddelanden!!

// SwingWorker execute startar automatiskt upp en egen tråd och kör processerna i bakgrunden.
// Detta för att inte låsa upp gränssnittet för användaren medan beräkningar sker.
// VIKTIGT ATT HÅLLA KOLLA PÅ VILKA SAKER SOM SKA KÖRAS PÅ VILKA TRÅDAR!!! KOLLA SLIDES FÖ.L 3

// TODO: Swingworker är engångsklasser!!
// om man vill använda samma swingworker klass igen, då skapar vi en ny SwingWorker och anropar den!
//
