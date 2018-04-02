
public class DoubleStackImpl implements DoubleStack {

	private final int INITIAL_SIZE = 10;
	private int numTokens;
	private Double[] doubleStack;
	
	public DoubleStackImpl() {
		doubleStack = new Double[INITIAL_SIZE];
		numTokens = 0;
	}
	
	private boolean isFull() {
		return numTokens == doubleStack.length;
	}
	
	private boolean isEmpty() {
		if(numTokens == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void doubleCapacity() {
		Double[] newDoubleStack = new Double[doubleStack.length * 2];
		for(int i=0; i<doubleStack.length; i++) {
			newDoubleStack[i] = doubleStack[i];
		}
		this.doubleStack = newDoubleStack;
	}

	@Override
	public void push(Double number) {
		if(isFull()) { 
			doubleCapacity();
		}
		doubleStack[numTokens++] = number;
	}

	@Override
	public Double pop() {
		if(isEmpty()) { 
			return null;
		} else {
			numTokens -= 1;
			return doubleStack[numTokens];
		}
	}

	@Override
	public Double top() {
		if(isEmpty()) { 
			return null;
		} else {
			return doubleStack[numTokens - 1];
		}
	}

	@Override
	public int size() {
		return numTokens;
	}

}
