public abstract class Methods {

    public static Double operate(double first, double second, int whichCase) {
        // int = 1 +
        // int = 2 *
        // int = 3 -
        // int = 4 /
        // int = 5 exponentiation
        switch (whichCase) {
            case 1:
                //Sum is first + second = result
                return first + second;
            case 2:
                //Moltiplication first * second = result
                return first * second;
            case 3:
                //Subtraction is first - second = result
                return first - second;
            case 4:
                //Division is  first : second = result
                return first / second;
            case 5:
                //Exponentiation is  first^second = result
                return Math.pow(first, second);
            default:
                return null;  //I hope this will never happen!
        }
    }
    
    public static String whichOperation(int wichCase) {
        // int = 1 +
        // int = 2 *
        // int = 3 -
        // int = 4 /
        // int = 5 exponentiation
        switch (wichCase) {
            case 1:
                return "+";
            case 2:
                return "*";
            case 3:
                return "-";
            case 4:
                return "/";
            case 5:
                return "y^x";
            default:
                return null;
        }
    }
    
    //The following recursive method is written basing on the method described in the lecture slides on recursive methods.
    public static double factorial(int a) {
        if (a == 1 || a == 0) {
            return 1;
        } else {
            return a * factorial(a - 1);
        }
    }
}