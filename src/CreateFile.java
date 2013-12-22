import java.io.*;

abstract class CreateFile {

    public static void create() throws FileNotFoundException {
        FileNotFoundException exc1 = new FileNotFoundException("Impossible to create Files!");
        FileNotFoundException exc2 = new FileNotFoundException("Impossible to write on log.txt");
        File logFile = new File("log.txt");
        if (!logFile.exists()) {     //Create the Log file.
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                throw exc1;
            }
        }
        try {  //Empty the Log file + check if you can write on it.
            FileWriter fstream = new FileWriter(logFile);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("");
            out.close();
        } catch (IOException ex) {
            throw exc2;
        }
        File aboutFile = new File("about.txt");
        aboutFile.delete();     //Delete About file on hard drive, because the About has to be exactly what's written below.
        try {
            aboutFile.createNewFile();
            FileWriter fstream = new FileWriter(aboutFile);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("--------------------------------------------\n"
                    + "This application was developed by\n"
                    + "Christian Dallago\n"
                    + "Student at LUB.\n"
                    + "\n"
                    + "This simple calculator is attempting to be as\n"
                    + "precise as possible, signaling every possible\n"
                    + "user error in different ways, in order to give\n"
                    + "to the user the possibility to understand and learn.\n"
                    + "\n"
                    + "This application was written in Java using\n"
                    + "NetBeans IDE 7.0.1 on a Mac 10.7.2 .\n"
                    + "\n"
                    + "The main method is located in the 'Calculator'\n"
                    + "class. There are 6 classes of which the heaviest\n"
                    + "is 'CalculatorPanel'. There are aproximately\n"
                    + "758 lines of code and 18 methods (including main,\n"
                    + "constructors and action listeners).\n");
            out.close();
        } catch (IOException e) {
            throw exc1;
        }
        aboutFile.setReadOnly();       //Set about to read-only. Is a LITTLE security.
    }
}