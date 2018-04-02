import java.util.*;

public class ParseInput {
	private StringTokenizer tokenizer;
	private final String DELIMITERS = "()+-/*^ ";
	public ParseInput(String input) {
		tokenizer = new StringTokenizer(input, DELIMITERS, true);
	}
	public Token getNextToken() {
		while(tokenizer != null && tokenizer.hasMoreTokens()) {
			String nextElement = tokenizer.nextToken().trim();
			if(nextElement.equals("")) {
				continue;
			}
			Token token = new TokenImpl(nextElement);
			return token;
		}
		return null;
	}
}
