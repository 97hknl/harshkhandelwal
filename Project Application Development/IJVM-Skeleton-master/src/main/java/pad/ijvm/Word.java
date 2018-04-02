package pad.ijvm;

import java.nio.ByteBuffer;

public class Word {
    byte[] bytes;
    public Word() {
        bytes = new byte[4];
    }

    public Word(int integer) {
    	bytes = ByteBuffer.allocate(4).putInt(integer).array();
    	 bytes = new byte[] {
    	            (byte)(integer >> 24),
    	            (byte)(integer >> 16),
    	            (byte)(integer >> 8),
    	            (byte)integer
    	          	};
    }

    public Word(byte[] bytes) {
        this.bytes = bytes;
    }
   
    public int toInteger() {
    	int result = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) <<  16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        return result;
    }
    
    public String toString() {
    	return (char)this.toInteger() + "";
    }
}
