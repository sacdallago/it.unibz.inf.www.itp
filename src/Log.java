import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class Log extends JPanel {

    private static File file = new File("log.txt");
    private static JLabel label;
    private static BufferedWriter out;
    private static FileWriter fstream;

    public Log() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        Scanner scan;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            scan = null;
        }

        String s = "<html>";
        while (scan.hasNextLine()) {
            s += scan.nextLine() + "<br>";
        }

        s += "</html>";

        label = new JLabel(s);
        JButton button = new JButton("Empty Log");
        button.addActionListener(new Listener());

        //I used JScrollPane as described in this webpage: http://www.java2s.com/Tutorial/Java/0240__Swing/Createascrollablelist.htm
        JScrollPane scroll = new JScrollPane(label);

        add(scroll);
        add(button);
    }

    private static class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            emptyLog();
        }
    }

// SOME METHODS  -------------------------------------------------------------------------------------------------------
    public static void writeOnLog(String s) {
        try {
            fstream = new FileWriter(file, true);
            out = new BufferedWriter(fstream);
            out.write(s + "\n");
            out.close();
        } catch (IOException ex) {
        }
    }

    public static void emptyLog() {
        try {
            fstream = new FileWriter(file);
            out = new BufferedWriter(fstream);
            out.write("");
            out.close();
        } catch (IOException ex) {
        }
        Calculator.updateUI("log");
    }
}