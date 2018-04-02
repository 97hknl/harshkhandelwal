public class APException extends Exception {

    private static final long serialVersionUID = 1L;

    public APException (String s) {
        System.out.println("error " + s);
    }
}