package pad.ijvm;

import java.util.Hashtable;

public class Frame { 
	private Stack stack;
	private int programCounter;
	private Hashtable<Integer, Integer> localVariables; 
	private short numberOfArguments;
	private short localVariableAreaSize; 
	private Frame previousFrame; 
	private boolean wide;
	
	public Frame() {
		stack = new Stack();
		programCounter = 0;
		localVariables = new Hashtable<>(); 
	}
	
	public int getProgramCounter() {
		return programCounter;
	}
	public void setProgramCounter(int programCounter) {
		this.programCounter = programCounter;
	}
	public Stack getStack() {
		return stack;
	}
	public void setStack(Stack stack) {
		this.stack = stack;
	}
	public Hashtable<Integer, Integer> getLocalVariables() {
		return localVariables;
	}
	public void setLocalVariables(Hashtable<Integer, Integer> localVariables) {
		this.localVariables = localVariables;
	}

	public short getNumberOfArguments() {
		return numberOfArguments;
	}

	public void setNumberOfArguments(short numberOfArguments) {
		this.numberOfArguments = numberOfArguments;
	}

	public short getLocalVariableAreaSize() {
		return localVariableAreaSize;
	}

	public void setLocalVariableAreaSize(short localVariableAreaSize) {
		this.localVariableAreaSize = localVariableAreaSize;
	}

	public Frame getPreviousFrame() {
		return previousFrame;
	}

	public void setPreviousFrame(Frame previousFrame) {
		this.previousFrame = previousFrame;
	}
	
    public void increaseProgramCounter(int value) {
    	setProgramCounter(getProgramCounter() + value); 
    }

	public boolean isWide() {
		return wide;
	}

	public void setWide(boolean wide) {
		this.wide = wide;
	}

}
