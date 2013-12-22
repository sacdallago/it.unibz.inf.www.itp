import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

public class Calculator {

    private static JMenuItem calculator, about, exit, log;
    private static JPanel panel, calculatorPanel;
    private static JFrame frame;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //Creates Log.txt and About.txt files + clear the Log and add text to About.
        CreateFile.create();
        //-----------------------------------

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuListener listen = new MenuListener();

        //I used JMenuBar, JMenuItem JMenu and the "hot keys" as described in the Java TUTORIAL (http://docs.oracle.com/javase/tutorial/uiswing/components/menu.html)
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        bar.add(menu);

        calculator = new JMenuItem("Calculator");
        calculator.setMnemonic(KeyEvent.VK_N);
        menu.add(calculator);
        calculator.addActionListener(listen);

        about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_A);
        menu.add(about);
        about.addActionListener(listen);

        log = new JMenuItem("Log");
        log.setMnemonic(KeyEvent.VK_L);
        menu.add(log);
        log.addActionListener(listen);

        exit = new JMenuItem("Exit (ALT+F4)");
        exit.setMnemonic(KeyEvent.VK_E);
        menu.add(exit);
        exit.addActionListener(listen);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        calculatorPanel = new CalculatorPanel();
        panel.add(calculatorPanel);

        frame.setJMenuBar(bar);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setBounds(500, 200, 430, 450);
        frame.setVisible(true);
    }

    private static class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (about.isArmed()) {
                updateUI("about");
            }
            if (calculator.isArmed()) {
                updateUI("calculator");
            }
            if (exit.isArmed()) {
                System.exit(0);
            }
            if (log.isArmed()) {
                updateUI("log");
            }
        }
    }

// SOME METHODS  -------------------------------------------------------------------------------------------------------
    public static void updateUI(String a) {
        panel.removeAll();
        a = a.toLowerCase();
        if (a.equals("log")) {
            panel.add(new Log());
        }
        if (a.equals("about")) {
            panel.add(new About());
        }
        if (a.equals("calculator")) {
            panel.add(calculatorPanel);
        }
        panel.updateUI();
    }
}