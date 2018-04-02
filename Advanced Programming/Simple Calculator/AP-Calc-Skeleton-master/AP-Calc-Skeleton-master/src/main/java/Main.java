
import java.util.Scanner;
public class Main implements CalculatorInterface {
	
	private final int NUMBER_TYPE = 1;
	private final int OPERATOR_TYPE = 2;
	private final int PARENTHESIS_TYPE = 3;
	private final int ERROR = -1;
	
    public TokenList readTokens(String input) {
        TokenList tokenList = new TokenListImpl();
        ParseInput inputParser = new ParseInput(input);
        Token token = null;
        while((token = inputParser.getNextToken()) != null) {
        	if(token.getType() == ERROR) {
        		System.out.println(token.getValue() +  " could not be recognized");
        		return null;
        	}
        	tokenList.add(token);
        }
        return tokenList;
    }


    public Double rpn(TokenList tokens) {
    	DoubleStack numberStack = new DoubleStackImpl();
        for(int i = 0; i < tokens.size(); i++) {
        	Token currentToken = tokens.get(i);
        	if(currentToken.getType() == NUMBER_TYPE) {
        		numberStack.push(currentToken.getDoubleValue());
        	} else if(currentToken.getType() == OPERATOR_TYPE) {
        		if(numberStack.size() >= 2) {
        			numberStack.push(currentToken.applyOperatorToken(numberStack.pop(), numberStack.pop()));
        		} else {
        			System.out.println("Insufficient number of operands for " + currentToken.getValue());
        			return null;
        		}
        	}
        }
        if(numberStack.size() == 1) {
        	return numberStack.pop();
        } else {
        	System.out.println("Invalid expression");
        	return null;
        }
    }

    public TokenList shuntingYard(TokenList tokens) {
        TokenStack tokenStack = new TokenStackImpl();
        TokenList tokenListRPN = new TokenListImpl();
        for(int i = 0; i < tokens.size(); i++) {
        	Token currentToken = tokens.get(i);
        	if(currentToken.getType() == NUMBER_TYPE) {
        		tokenListRPN.add(currentToken);
        	} else if(currentToken.getType() == OPERATOR_TYPE) {
        		while(tokenStack.size() > 0 && tokenStack.top().getPrecedence() >= currentToken.getPrecedence()) {
        			tokenListRPN.add(tokenStack.pop());
        		}
        		tokenStack.push(currentToken);
        	} else if(currentToken.getType() == PARENTHESIS_TYPE) {
        		if(currentToken.getValue().equals("(")) {
        			tokenStack.push(currentToken);
        		} else {
        			while(tokenStack.size() > 0 && !tokenStack.top().getValue().equals("(")) {
        				tokenListRPN.add(tokenStack.pop());
        			}
        			if(tokenStack.size() > 0) {
        				tokenStack.pop();
        			} else {
        				System.out.println("Invalid parentheis");
        				return null;
        			}
        		}
        	}
        }
        while(tokenStack.size() > 0) {
        	if(tokenStack.top().getType() != PARENTHESIS_TYPE) {
        		tokenListRPN.add(tokenStack.pop());
        	} else {
        		System.out.println("Invalid parentheis");
				return null;
        	}
        }
        return tokenListRPN;
    }

    private void start() { 
        Scanner input = new Scanner(System.in);
        while(input.hasNext()) {
        	String expressionInput = input.nextLine(); 
        	if(expressionInput.length() > 0) { //parse the input only if it's not empty
	        	TokenList tokenList = readTokens(expressionInput);
	        	if(tokenList == null || tokenList.size() == 0) {
	        		continue;
	        	}
	        	TokenList tokenListRPN = shuntingYard(tokenList);
	        	if(tokenListRPN == null || tokenListRPN.size() == 0) {
	        		continue;
	        	}
	        	Double result = rpn(tokenListRPN);
	        	if(result != null) {
	        		System.out.printf("%.6f\n", result);
	        	}
        	}
        }
        input.close();
    }
    public static void main(String[] argv) {
        new Main().start();
    }
}
