package pad.ijvm;

import pad.ijvm.interfaces.IJVMInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class MachineFactory {

    public static IJVMInterface createIJVMInstance(File binary) throws IOException {
        MachineFactory machineFactory = new MachineFactory();
        byte file[] = machineFactory.readFileInByteArray(binary);
        new BinaryLoader(file);
        IJVMInterface ijvmInterface = new IJVMInterfaceImplementor();
        return ijvmInterface;
    }

    public byte[] readFileInByteArray(File file) {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.err.printf("%s", e.getMessage());
        } catch (IOException e) {
            System.err.printf("%s", e.getMessage());
        }
        return bytes;
    }
}
