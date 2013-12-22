import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class CalculatorPanel extends JPanel {

    private static double flo, MR = 0.0;
    private static int comma = 0, operation = 0;
    private static String str = "", log = "";
    private static JButton[] list = {new JButton("MC"), new JButton("M+"), new JButton("M-"), new JButton("MR"), new JButton("="),
        new JButton("x^3"), new JButton("x^2"), new JButton("1/x"), new JButton("/"), new JButton("*"),
        new JButton("+/-"), new JButton("7"), new JButton("8"), new JButton("9"), new JButton("-"),
        new JButton("x!"), new JButton("4"), new JButton("5"), new JButton("6"), new JButton("+"),
        new JButton("log2(x)"), new JButton("1"), new JButton("2"), new JButton("3"), new JButton("Dec>Bin"),
        new JButton("log10(x)"), new JButton("y^x"), new JButton("0"), new JButton("."), new JButton("Bin>Dec")
    };
    private static JButton enter, reset;
    private static JLabel down, up, message;

    public CalculatorPanel() {
        list[4].setFont(new Font("", Font.PLAIN, 22));  // Here I'm setting the font of the "=" sign. I want it just a bit bigger than default.

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        /*1*/ JPanel upper = new JPanel();  // Panel which will contain the "up" and "down" label.
        upper.setLayout(new GridLayout(2, 1));
        /*2*/ JPanel under = new JPanel();  // Panel which will contain most of the buttons.
        under.setLayout(new GridLayout(6, 5));
        /*3*/ JPanel bottom = new JPanel();  // Panel which will contain the del and reset buttons.
        /*4*/ JPanel messagePanel = new JPanel();  // Panel which will contain the message label.

        down = new JLabel("0", SwingConstants.RIGHT);
        down.setFont(new Font("", Font.BOLD, 20));
        up = new JLabel("", SwingConstants.RIGHT);
        upper.add(up);
        upper.add(down);
        upper.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        upper.setBackground(Color.white);

        Maths maths = new Maths(); // Action listener
        for (JButton a : list) {
            under.add(a);
            a.addActionListener(maths);
        }

        enter = new JButton("DEL");
        enter.addActionListener(maths);
        bottom.add(enter);
        reset = new JButton("RESET");
        reset.addActionListener(maths);
        bottom.add(reset);

        message = new JLabel("", SwingConstants.RIGHT);
        message.setForeground(Color.red);
        messagePanel.setPreferredSize(new Dimension(5, 20));
        messagePanel.setBackground(Color.LIGHT_GRAY);
        messagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        messagePanel.add(message);

        add(upper);
        add(under);
        add(bottom);
        add(messagePanel);
    }

    private static class Maths extends Methods implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            message.setText(""); // Whatever button is pressed the warnings on the message panel get deleted.
            String s = ((JButton) ae.getSource()).getText(); // VERY IMPORTANT: from this "s" depends every if below.
            DecimalFormat nf = new DecimalFormat("#.#######");

            // Get event from number buttons only and add the number to str which serves as container for all operations.
            // Display is limited to 31 entries (maximum for binary digits and enugh for simple two operation computations.
            Integer a = 0;
            while (a < 10 && str.length() < 31) {
                if (a.toString().equals(s)) {
                    if (str.equals("") && s.equals("0")) {
                    } else {
                        str += s;
                        down.setText(str);
                    }
                }
                a++;
            }
            // Add comma JUST ONCE (use of 'comma' counter). Case in which display shows '0' (str = ""), put str = "0."
            if (s.equals(".") && comma == 0) {
                if (str.equals("")) {
                    str = "0.";
                } else {
                    str += s;
                }
                down.setText(str);
                comma = 1;
            }
            if (s.equals("M+")) {
                MR += Double.parseDouble(down.getText());
                Log.writeOnLog(down.getText() + " has been added to the memory.");
                str = "";
                comma = 0;
            }
            if (s.equals("M-")) {
                MR -= Double.parseDouble(down.getText());
                Log.writeOnLog(down.getText() + " has been removed from the memory.");
                str = "";
                comma = 0;
            }
            if (s.equals("MR")) {
                down.setText(nf.format(MR));
                str = "";
                comma = 0;
            }
            if (s.equals("MC")) {
                MR = 0.0;
                Log.writeOnLog("Memory has been cleared.");
            }
            // Change sign just in case the number is != 0 and NOT a result. This algorythm changes the first char to -, or takes the substr from what's after the - to the end.
            if (s.equals("+/-")) {
                if (str.equals("") || str.equals("0.")) {
                } else {
                    if (str.charAt(0) == '-') {
                        str = str.substring(1, str.length());
                    } else {
                        str = "-" + str;
                    }
                    down.setText(str);
                }
            }
            // DEL. 1) If str = "", display '0' -useful for results and when trying to delete a zero string-
            // 2) If you delete the comma '.', put the comma counter to 0, so that you can reuse the comma.
            // 3) If you delete EVERYTHING till the last number, put str = "" and display 0
            // 4) If the number is >1, simply delete the last char.
            // 5) If (suppose) you have -2 and you press delete, str will be = "", and display = 0 (i.e. NO SIGN).
            if (s.equals("DEL")) {
                down.setText("0");
                if (str.length() == 2 && str.charAt(0) == '-') {
                    str = "";
                }
                if (str.length() > 0) {
                    if (str.charAt(str.length() - 1) == '.') {
                        comma = 0;
                    }
                    if (str.length() == 1) {
                        str = "";
                        down.setText("0");
                    } else {
                        str = str.substring(0, str.length() - 1);
                        down.setText(str);
                    }
                }
            }
            // Very simple way to compute square. Takes number from "down" JLabel and computes the square.
            if (s.equals("x^2")) {
                flo = Double.parseDouble(down.getText());
                /**/ log = down.getText() + " ~ " + s + " ~ ";
                flo *= flo;
                /**/ log += (nf.format(flo));
                down.setText((nf.format(flo)));
                resetDisplay();  //  Explained below
                // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                Log.writeOnLog(log);
                log = "";
            }
            // Very simple way to compute cube.  Takes number from "down" JLabel and computes the cube.
            if (s.equals("x^3")) {
                flo = Double.parseDouble(down.getText());
                /**/ log = down.getText() + " ~ " + s + " ~ ";
                flo *= flo * flo;
                /**/ log += (nf.format(flo));
                down.setText(nf.format(flo));
                resetDisplay();  //  Explained below
                // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                Log.writeOnLog(log);
                log = "";
            }
            // Very simple way to compute 1/x. Shows dialogue if ur trying to divide by zero or '0.'.  Takes number from "down" JLabel and computes op.
            if (s.equals("1/x")) {
                if (down.getText().equals("0") || down.getText().equals("0.")) {
                    message.setText("Impossible! Dividing by 0!");
                    Log.writeOnLog("Tried to divide by zero.");
                } else {
                    flo = Double.parseDouble(down.getText());
                    /**/ log = down.getText() + " ~ " + s + " ~ ";
                    flo = 1 / flo;
                    /**/ log += (nf.format(flo));
                    down.setText(nf.format(flo));
                    // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                    Log.writeOnLog(log);
                    log = "";
                    resetDisplay();  //  Explained below
                }
            }
            // Factorial of INTEGERS, uses a method of the extended class "Methods".
            if (s.equals("x!")) {
                String advice = "*Float values were ignored.*";
                flo = Double.parseDouble(down.getText());
                // Factorials of numbers below 0 don't make any sense! Get message + write on log.
                if (flo < 0.00000000) {
                    message.setText("Factorial of negative number doesn't have significate.");
                    Log.writeOnLog("Trying to compute factorial of negative number: " + flo);
                }
                // Case 0, convention is 1. Write on log.
                if (flo == 0.00000000) {
                    down.setText("1");
                    Log.writeOnLog("0 ~ n! ~ 1");
                }
                // if it's a float value between 1 and 0. Writes on log + writes that u were a bad boy ;)
                if (flo > 0 && flo < 0.999999999999) {
                    message.setText("<html>Reminder: This calculator only computes factorials<br>of integers. The float values were ignored.</html>");
                    down.setText("1");
                    Log.writeOnLog(nf.format(flo) + " ~ n! ~ 1  " + advice);
                }
                // Factorials of numbers > 1 and <= 700. Message box + entry in log if the numer to factor is a float.
                // Everything written on log.
                if (flo > 1 && flo <= 170) {
                    double h = 0.000000001, k;
                    int g = (int) flo;
                    k = flo - g;
                    if (k > h) {
                        message.setText("<html>Reminder: This calculator only computes factorials<br>of integers. The float values were ignored.</html>");
                        Log.writeOnLog("*Tried factorial of float*");
                    }
                    down.setText(nf.format(factorial(g)));
                    Log.writeOnLog(nf.format(flo) + " ~ n! ~ " + nf.format(factorial(g)));
                }
                // If number to factorize is above 700 the message box returns infinite.
                if (flo > 170) {
                    down.setText("���");
                    Log.writeOnLog(nf.format(flo) + " ~ n! ~ ���");
                }
                resetDisplay();  //  Explained below
            }
            // Log2 of number is computed using Math class and the substituion rule.
            if (s.equals("log2(x)")) {
                flo = Double.parseDouble(down.getText());
                // Log2 of negative number is IMPOSSIBLE. Returns message + writes on log.
                if (flo < 0.00000000) {
                    message.setText("Impossible to compute log of numbers less than 0!");
                    Log.writeOnLog("Tried to compute log2 of number below 0: " + flo);
                } else {
                    // Log2 of zero is MINUS-INFINITE. Returns message + writes on log.
                    if (flo == 0.00000) {
                        message.setText("Log of 0 is -infinite!");
                        Log.writeOnLog("Computed log2 of 0, which is -infinite.");
                        // Log2 of number. Returns result + writes on log.
                    } else {
                        /**/ log = nf.format(flo) + " ~ " + s + " ~ ";
                        flo = (Math.log(flo) / Math.log(2));
                        down.setText(nf.format(flo));
                        /**/ log += down.getText();
                        resetDisplay();  //  Explained below
                        // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                        Log.writeOnLog(log);
                        log = "";
                    }
                }
            }
            //LOOK ABOVE!
            if (s.equals("log10(x)")) {
                flo = Double.parseDouble(down.getText());
                if (flo < 0.00000000) {
                    message.setText("Impossible to compute log of numbers less than 0!");
                    Log.writeOnLog("Tried to compute log10 of number below 0: " + flo);
                } else {
                    if (flo == 0.00000) {
                        message.setText("Log of 0 is -infinite!");
                        Log.writeOnLog("Computed log10 of 0, which is -infinite.");
                    } else {
                        /**/ log = nf.format(flo) + " ~ " + s + " ~ ";
                        flo = (Math.log10(flo));
                        down.setText(nf.format(flo));
                        /**/ log += down.getText();
                        resetDisplay();
                        Log.writeOnLog(log);
                        log = "";
                    }
                }
            }
            //Simple transformation form Decimal value (lower or equal 2147483647) to Binary value + writing operation on log.
            if (s.equals("Dec>Bin")) {
                flo = Double.parseDouble(down.getText());
                if (flo > 2147483647) {
                    message.setText("Number too big! Should be less or equal 2147483647.");
                } else {
                    /**/ log = down.getText() + " ~ " + s + " ~ ";
                    log += Integer.toBinaryString(((int) flo));
                    down.setText(Integer.toBinaryString((int) flo));
                    resetDisplay();  //  Explained below
                    // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                    Log.writeOnLog(log);
                    log = "";
                }
            }
            if (s.equals("Bin>Dec")) {
                int temp = 0;
                boolean shallI = true;
                //Check if number entered is a binary digit and >0. If NOT, next 'if' (operation) will NOT be performed.
                //If NOT message appears and log is updated with an error.
                if ((down.getText()).charAt(0) == '-') {
                    message.setText("The binary digit should be positive.");
                    shallI = false;
                    Log.writeOnLog("Tried to convert negative binary.");
                } else {
                    // Control if the entered digit is really a binary digit
                    while (temp < (down.getText()).length() && shallI) {
                        if ((down.getText()).charAt(temp) != '0' && (down.getText()).charAt(temp) != '1') {
                            message.setText("This is not a binary digit!");
                            shallI = false;
                            Log.writeOnLog("Tried to convert binary which wasn't a binary: " + down.getText());
                        } else {
                            temp++;
                        }
                    }
                }
                if (shallI) {
                    /**/ log = down.getText() + " ~ " + s + " ~ ";
                    log += Integer.parseInt(down.getText(), 2);
                    down.setText(Integer.toString(Integer.parseInt(down.getText(), 2)));
                    str = "";
                    flo = 0.0;
                    // Saves operation in log file!!!!!! In the following way: NUM ~ OPERATION ~ RESULT
                    Log.writeOnLog(log);
                    log = "";
                }
            }
            if (s.equals("+")) {
                if ("".equals(up.getText())) {
                    simpleOp(1);  // Method descrived below.
                }
            }
            if (s.equals("*")) {
                if ("".equals(up.getText())) {
                    simpleOp(2);  // Method descrived below.
                }
            }
            if (s.equals("-")) {
                if ("".equals(up.getText())) {
                    simpleOp(3);  // Method descrived below.
                }
            }
            if (s.equals("/")) {
                if ("".equals(up.getText())) {
                    simpleOp(4);  // Method descrived below.
                }
            }
            if (s.equals("y^x")) {
                if ("".equals(up.getText())) {
                    simpleOp(5);  // Method descrived below.
                }
            }
            if (s.equals("=")) {
                if ("".equals(up.getText()) || operation == 0) {
                } else {
                    if (operation == 4 && (down.getText().equals("0") || down.getText().equals("0."))) {
                        message.setText("Impossible! Dividing by 0!");
                        {
                            Log.writeOnLog("Tried to divide by zero.");
                        }
                    } else {
                        // Operate is a method of 'Methods' class.
                        flo = operate(Double.parseDouble(up.getText()), Double.parseDouble(down.getText()), operation);
                        /**/ log = up.getText() + " " + whichOperation(operation) + " " + down.getText() + " = " + nf.format(flo);
                        up.setText("");
                        down.setText(nf.format(flo));
                        resetDisplay();
                        operation = 0;
                        // Saves operation in log file!!!!!! In the following way: NUM + NUM = RESULT
                        Log.writeOnLog(log);
                        log = "";
                    }
                }
            }
            if (s.equals("RESET")) {
                up.setText("");
                down.setText("0");
                operation = 0;
                resetDisplay();
                Log.writeOnLog("Calculator was RESET.");
            }
            // Method to prevent the "comma problem", i.e. the ',' symbol used to separate integral part from floating part.
            // Method to prevent display from showing numbers bigger than 50 entries or NaN's, i.e. infinite...
            // NEEDS TO BE IN LAST POSITION OF FORKFLOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            commaProblem();
            displayProblem();
        }
    }

    // SOME METHODS  -------------------------------------------------------------------------------------------------------
    private static void simpleOp(int op) {
        // This method is used to determine which operation with two operands is going to be performed. A constand "operation" is modified suiting the operation.
        operation = op;
        // In addition this method puts the number in the "down" label to the "up" label and resets the down label.
        up.setText(down.getText());
        down.setText("0");
        resetDisplay();
    }

    private static void commaProblem() {
        // The problem is the following: if I compute an op and the result is a float, normally this float is signed with the "," symbol.
        // Unfortunately if I want to do another operation on this result I can't because that "," symbol should be a "."; therefore this method changes that "," to a "."!
        int a = 0;
        while (a < (down.getText()).length()) {
            if ((down.getText()).charAt(a) != ',') {
                a++;
            } else {
                str = (down.getText()).substring(0, a) + "." + (down.getText()).substring(a + 1, (down.getText()).length());
                down.setText(str);
                str = "";
            }
        }
    }

    private static void displayProblem() {
        // Too big numbers are no help. This calculator is a simple calculator, such big numbers don't have revelance in this case.
        if (down.getText().length() > 50) {
            String temp;
            temp = (down.getText()).charAt(0) + "." + down.getText().substring(1, 4) + "*10^" + (down.getText().length() - 1) + "</html>";
            message.setText("<html>Number too large to be displayed:<br>" + temp);
            down.setText("0");
            resetDisplay();
        }
        // If result is NaN (Not a Number), this NaN is reported in the message box below and the display reset so you can go on computing.
        try {
            Double.parseDouble(down.getText());
        } catch (NumberFormatException e) {
            message.setText("<html>Not a Number!<br>" + down.getText() + "</html>");
            down.setText("0");
            resetDisplay();
        }
    }

    private static void resetDisplay() {
        str = "";
        flo = 0.0;
        comma = 0;
    }
}