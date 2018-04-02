package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class IJVMInterfaceImplementor implements IJVMInterface {
    private byte[] textPool;
    private byte[] constantPool;
    private PrintStream out;
    private InputStream in;
    private Frame frame;
    private boolean halt;
    private ArrayList<int[]> listOfArrays;

    public IJVMInterfaceImplementor() {
    	textPool = BinaryLoader.getTextData();
    	constantPool = BinaryLoader.getConstantData();
        out = System.err;
        frame = new Frame();
        halt = false;
        listOfArrays = new ArrayList<>();
    }
    @Override
    public int topOfStack() {
    	int value = 0;
        try { 
        	value = frame.getStack().topOfStack().toInteger();
        } catch(Exception e) {
        	
        }
        return value;
    }
    
    public ArrayList<int[]> getListOfArrays() {
        return listOfArrays;
    }
    

    @Override
    public int[] getStackContents() {
        return frame.getStack().getStackContents();
    }

    @Override
    public byte[] getText() { 
        return textPool;
    }

    public void setText(byte[] data) {
        this.textPool = data;
    }
    
    public void setFrame(Frame frame) {
    	this.frame = frame;
    }
    
    public Frame getFrame() {
    	return frame;
    }

    @Override
    public int getProgramCounter() {
        return frame.getProgramCounter();
    }

    @Override
    public int getLocalVariable(int i) {
        return frame.getLocalVariables().containsKey(i) 
        		? frame.getLocalVariables().get(i) : 0;
    }

    @Override
    public int getConstant(int index) {
        byte[] bytes = new byte[4];
        for(int i = 0; i < 4; i++) {
        	bytes[i] = constantPool[index * 4 + i];
        }
        Word word = new Word(bytes);
        return word.toInteger();
    }

    @Override
    public void step() {
    	if(!isHalt()) {
    		Instruction.executeInstruction(getInstruction(), this);
    	}
    }

    @Override
    public void run() {
        while(!isHalt() && frame.getProgramCounter() < textPool.length) { 
            step();
        }
    }

    @Override
    public byte getInstruction() {
    	byte instruction = BinaryLoader.getNextInstruction(frame);
    	return instruction;
    }

    @Override
    public void setOutput(PrintStream out) {
        this.out = out;
    }
    
    public PrintStream getOutput() {
    	return this.out;
    }

    @Override
    public void setInput(InputStream in) {
    	this.in = in;
    }
    
    public InputStream getInput() {
    	return this.in;
    }
    
	public boolean isHalt() {
		return halt;
	}
	public void setHalt(boolean halt) {
		this.halt = halt;
	}
}
