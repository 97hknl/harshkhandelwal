public class TokenListImpl implements TokenList {
	private final int INITIAL_SIZE = 10;
	private int numTokens;
	private Token[] tokenList;
	
	public TokenListImpl() {
		tokenList = new Token[INITIAL_SIZE];
		numTokens = 0;
	}

	@Override
	public void add(Token token) {
		if(numTokens == tokenList.length) {
			doubleCapacity();
		}
		tokenList[numTokens++] = token;
	}

	@Override
	public void remove(int index) {
		if(index >= 0 && index < numTokens) {
			for(int i = index + 1; i < numTokens; i++) {
				tokenList[i - 1] = tokenList[i];
			}
			tokenList[--numTokens] = null;
		}
	}

	@Override
	public void set(int index, Token token) {
		if(index >= 0 && index < numTokens) {
			tokenList[index] = token;
		}
	}

	@Override
	public Token get(int index) {
		if(index >= 0 && index < numTokens) {
			return tokenList[index];
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return numTokens;
	}
	
	private void doubleCapacity() {
		Token[] newTokenList = new Token[tokenList.length * 2];
		for(int i = 0; i < tokenList.length; i++) {
			newTokenList[i] = tokenList[i];
		}
		this.tokenList = newTokenList;
	}
}
