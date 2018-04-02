
public class TokenStackImpl implements TokenStack {
	
	private final int INITIAL_SIZE = 10;
	private int numTokens;
	private Token[] tokenStack;
	
	public TokenStackImpl() {
		tokenStack = new Token[INITIAL_SIZE];
		numTokens = 0;
	}
	
	private boolean isFull() {
		return numTokens == tokenStack.length;
	}
	
	private boolean isEmpty() {
		if(numTokens == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void doubleCapacity() {
		Token[] newTokenStack = new Token[tokenStack.length * 2];
		for(int i=0; i<tokenStack.length; i++) {
			newTokenStack[i] = tokenStack[i];
		}
		this.tokenStack = newTokenStack;
	}

	@Override
	public void push(Token token) {
		if(isFull()) { 
			doubleCapacity();
		}
		tokenStack[numTokens++] = token;
	}

	@Override
	public Token pop() {
		if(isEmpty()) { 
			return null;
		} else {
			numTokens -= 1;
			return tokenStack[numTokens];
		}
	}

	@Override
	public Token top() {
		if(isEmpty()) { 
			return null;
		} else {
			return tokenStack[numTokens - 1];
		}
	}

	@Override
	public int size() {
		return numTokens;
	}

}
