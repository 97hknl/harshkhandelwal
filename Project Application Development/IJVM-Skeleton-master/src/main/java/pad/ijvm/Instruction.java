package pad.ijvm;

import java.io.IOException;
import java.util.ArrayList;

public class Instruction {
	
	public static enum instructions {
		NOP((byte)0x00),
		BIPUSH((byte)0x10),
		IADD((byte)0x60),
		OUT((byte)0xFD),
		WIDE((byte)0xC4),
		SWAP((byte)0x5F),
		POP((byte)0x57),
		LDC_W((byte)0x13),
		ISUB((byte)0x64),
		ISTORE((byte)0x36),
		IRETURN((byte)0xAC),
		IOR((byte)0xB0),
		INVOKEVIRTUAL((byte)0xB6),
		IN((byte)0xFC),
		ILOAD((byte)0x15),
		IINC((byte)0x84),
		IF_ICMPEQ((byte)0x9F),
		IFLT((byte)0x9B),
		IFEQ((byte)0x99),
		IAND((byte)0x7E),
		HALT((byte)0xFF),
		GOTO((byte)0xA7),
		ERR((byte)0xA7),
		DUP((byte)0x59),
		NEWARRAY((byte)0xD1),
		IALOAD((byte)0xD2),
		IASTORE((byte)0xD3);
		
		private byte value;
		
		private instructions(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}
	};
	
	public static instructions getInstruction(byte data) {
		for(instructions instruction : Instruction.instructions.values()) {
			if(instruction.value == data) {
				return instruction;
			}
		}
		return null;
	}
	
	public static void executeInstruction(byte data, IJVMInterfaceImplementor ijvm) { 
		instructions instruction = getInstruction(data);
		int result; 
		int address; 
		Short argument;
		Frame frame = ijvm.getFrame();
		Stack stack = frame.getStack();
		ArrayList<int[]> listOfArrays = ijvm.getListOfArrays();
		frame.increaseProgramCounter(1); 
		switch (instruction) {
		
	    	case NOP:
	    		break;
	    		
		    case BIPUSH:
		    	Byte newByte = BinaryLoader.getNextByte(frame);
		        stack.push(new Word(newByte.intValue()));
		        break;
		        
		    case IADD:
		    	result = stack.pop().toInteger() + stack.pop().toInteger();
		        stack.push(new Word(result));
		        break;
		        
		    case OUT:
		        Word topOfStack = stack.pop();
		        if(topOfStack != null) {
		            ijvm.getOutput().print(topOfStack);
		        }
		        break;
		        
		    case WIDE:
		    	frame.setWide(true);
		    	ijvm.step(); 
		    	break;
		    	
		    case SWAP:
		    	topOfStack = stack.pop();
		    	Word newTopOfStack = stack.pop();
		        stack.push(topOfStack);
		        stack.push(newTopOfStack);
		        break;
		        
		    case POP:
		        stack.pop();
		        break;
		        
		    case LDC_W:
		    	address = BinaryLoader.getNextShortInInteger(frame);
	    		int constant = ijvm.getConstant(address);
	    		stack.push(new Word(constant));
		    	break;
		    	
		    case ISUB:
		    	topOfStack = stack.pop();
		        result = stack.pop().toInteger() - topOfStack.toInteger();
		        stack.push(new Word(result));
		        break;
		        
		    case ISTORE:
		    	if(frame.isWide()) {
		    		address = BinaryLoader.getNextShortInInteger(frame);
		    		frame.setWide(false);
		    	} else {
		    		address = BinaryLoader.getNextByte(frame);
		    	}
	    		frame.getLocalVariables().put(address, stack.pop().toInteger());
		    	break;
		    	
		    case IRETURN:
		    	Word returnedValue = stack.pop();
		    	ijvm.setFrame(frame.getPreviousFrame());
		    	ijvm.getFrame().getStack().pop();
		    	ijvm.getFrame().getStack().push(returnedValue); 
		    	break;
		    	
		    case IOR:
		    	result = stack.pop().toInteger() | stack.pop().toInteger();
		        stack.push(new Word(result));
		        break;
		        
		    case INVOKEVIRTUAL: 
		    	Frame newFrame = new Frame();
		    	int constantAddress = ijvm.getConstant(BinaryLoader.getNextShortInShort(frame));
		    	newFrame.setProgramCounter(constantAddress);
		    	newFrame.setPreviousFrame(frame);
		    	ijvm.setFrame(newFrame); 
		    	newFrame.setNumberOfArguments(BinaryLoader.getNextShortInShort(newFrame));
		    	newFrame.setLocalVariableAreaSize(BinaryLoader.getNextShortInShort(newFrame));
				for(int i = newFrame.getNumberOfArguments() - 1; i >=1; i--) { //the 1 as the lower bound is here due to observation of it as a lower bound while debugging
					newFrame.getLocalVariables().put(i, newFrame.getPreviousFrame().getStack().pop().toInteger());
				}
		    	break;
		    	
		    case IN:
		    	int input = 0;
				try {
					input = ijvm.getInput().read();
				} catch (IOException e) {
					input = 0;
				}
				if(input == -1) {
					stack.push(new Word(0));
				}
				else {
					stack.push(new Word(input));
				}
		    	break;
		    	
		    case ILOAD:
		    	if(frame.isWide()) {
		    		address = BinaryLoader.getNextShortInInteger(frame);
		    		frame.setWide(false);
		    	} else {
		    		address = BinaryLoader.getNextByte(frame);
		    	}
	    		int element = frame.getLocalVariables().get((int)address);
				stack.push(new Word(element));
		    	break;
		    	
		    case IINC: 
		    	if(frame.isWide()) {
		    		address = BinaryLoader.getNextShortInInteger(frame);
		    		frame.setWide(false);
		    	} else {
		    		address = BinaryLoader.getNextByte(frame);
		    	}
		    	byte value = BinaryLoader.getNextByte(frame);
		    	frame.getLocalVariables().put(address, frame.getLocalVariables().get(address) + value);
		    	break;
		    	
		    case IF_ICMPEQ:
		        Word firstWord = stack.pop();
		        Word secondWord = stack.pop();
		        argument = BinaryLoader.getNextShortInShort(frame);
		        if(firstWord.toInteger() == secondWord.toInteger()) {
		        	frame.increaseProgramCounter(argument - 3); //argument -3 because 1 for instruction and 2 for the 2 bytes that make the short need to be subtracted.
		        }
		        break;
		        
		    case IFLT:
		        topOfStack = stack.pop();
		        argument = BinaryLoader.getNextShortInShort(frame);
		        if(topOfStack.toInteger() < 0) {
		        	frame.increaseProgramCounter(argument - 3);
		        }
		        break;
		        
		    case IFEQ:
		        topOfStack = stack.pop();
		        argument = BinaryLoader.getNextShortInShort(frame);
		        if(topOfStack.toInteger() == 0) {
		        	frame.increaseProgramCounter(argument - 3);
		        }
		        break;
		        
		    case IAND:
		    	result = stack.pop().toInteger() & stack.pop().toInteger();
		        stack.push(new Word(result));
		        break;
		        
		    case HALT:
		    	ijvm.setHalt(true);
		    	break;
		    	
		    case GOTO:
		        argument = BinaryLoader.getNextShortInShort(frame); 
		        frame.increaseProgramCounter(argument - 3);
		        break;
		        
		    case ERR:
		    	System.err.println("Error");
		    	ijvm.setHalt(true);
		    	break;
		    	
		    case DUP: 
		    	stack.push(stack.topOfStack());
		    	break;
		    	
		    case NEWARRAY:
		    	int count = stack.pop().toInteger();
		    	int newArray[] = new int[count];
		    	listOfArrays.add(newArray);
		   		frame.getStack().push(new Word(listOfArrays.size() - 1));
		    	break;
		    	
		    case IALOAD: 
		    	int arrayRef = stack.pop().toInteger();
		    	int index = stack.pop().toInteger();
		    	int array[] = listOfArrays.get(arrayRef); 
				stack.push(new Word(array[index]));
		    	break;
		    	
		    case IASTORE: 
		    	arrayRef = stack.pop().toInteger();
		    	index = stack.pop().toInteger();
		    	int valueToInsert = stack.pop().toInteger();
		    	array = listOfArrays.get(arrayRef);
		    	array[index] = valueToInsert;
		    	break;
		    	
		    default:
		    	break;
	}
}
	
	
}
