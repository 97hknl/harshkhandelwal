import java.util.Scanner;

public class Main {
	
    private void start() {
        Scanner in = new Scanner(System.in);
        String input = null;
        Parser parser = new Parser();
        while(in.hasNextLine()) {
        	input = in.nextLine();
        	parser.changeInput(input);
        	try {
				parser.statement();
			} catch (APException e) {
			}
        }
        in.close();
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
