import java.awt.Color;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class About extends JPanel {

    public About() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        File file = new File("about.txt");

        Scanner scan;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            //Application will crash before this happens!
            scan = null;
        }

        String s = "<html>";
        while (scan.hasNextLine()) {
            s += scan.nextLine() + "<br>";
        }

        s += "</html>";

        JLabel label = new JLabel(s);
        JScrollPane scroll = new JScrollPane(label);
        add(scroll);
    }
}