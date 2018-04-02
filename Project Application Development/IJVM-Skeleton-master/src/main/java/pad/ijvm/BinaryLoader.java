package pad.ijvm;

import java.util.Arrays;

public class BinaryLoader {
    private final static int MAGIC_NUMBER = 0x1DEADFAD;
    private static byte byteFile[];
    private static int currentIndex;
    private static byte[] constantData;
    private static byte[] textData;
    
    public BinaryLoader(byte[] File) {
        BinaryLoader.byteFile = File;
        currentIndex = 0;
        load();
    }

    public static byte[] getConstantData() {
        return constantData;
    }

    public static byte[] getTextData() {
        return textData;
    }

    public static void load() {
        magicNumberCheck();
        Word[] constants = getNextBlock();
        constantData = new byte[constants[1].toInteger()];
        for(int i = 0; i < constantData.length; i++) {
            constantData[i] = byteFile[currentIndex++];
        }
        Word[] text = getNextBlock();
        textData = new byte[text[1].toInteger()];
        for(int i = 0; i < textData.length; i++) {
            textData[i] = byteFile[currentIndex++];
        }
    }

    private static void magicNumberCheck() {
        Word magic = extractNextWord();
        if(magic.toInteger() != MAGIC_NUMBER) { 
            try {
                throw new Exception("Magic number does not match");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Word extractNextWord() {
        if(currentIndex + 4 >= byteFile.length) {
            return null;
        }
        Word word = new Word(Arrays.copyOfRange(byteFile, currentIndex, currentIndex + 4));
        currentIndex += 4;
        return word;
    }
    /*
        Each block is defined as:
        <4 bytes of origin> <4 bytes of size><DATA>
        This method returns the first 2 32-bits of the block
     */
    public static Word[] getNextBlock() {
        if(currentIndex + 8 >= byteFile.length) {
            return null;
        }
        Word blockDetails[] = new Word[2];
        blockDetails[0] = extractNextWord();
        blockDetails[1] = extractNextWord();
        return blockDetails;
    }
    
    public static Integer getNextShortInInteger(Frame frame) {
        if(frame.getProgramCounter() + 1 >= textData.length) {
            return null;
        }
        byte first = textData[frame.getProgramCounter()];
        byte second = textData[frame.getProgramCounter() + 1];
        int result = ((first & 0xFF) << 8) | (second & 0xFF);
        frame.increaseProgramCounter(2);
        return result;
    }
    
    public static Short getNextShortInShort(Frame frame) {
        if(frame.getProgramCounter() + 1 >= textData.length) {
            return null;
        }
        byte first = textData[frame.getProgramCounter()];
        byte second = textData[frame.getProgramCounter() + 1];
        int result = ((first & 0xFF) << 8) | (second & 0xFF);
        frame.increaseProgramCounter(2);
        return (short)result;
    }
    
    public static Byte getNextByte(Frame frame) {
    	if(frame.getProgramCounter() >= textData.length) {
    		return null;
    	}
    	byte value = textData[frame.getProgramCounter()];
        frame.increaseProgramCounter(1);
        return value;
    }
    
    public static Byte getNextInstruction(Frame frame) {
    	if(frame.getProgramCounter() >= textData.length) {
    		return null;
    	}
    	byte value = textData[frame.getProgramCounter()]; 
        return value;
    }
}
