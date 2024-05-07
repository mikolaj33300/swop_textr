package util;

public class ByteHelper {

    public static byte[] appendBytes(byte[] startArray, byte[] toAppend) {

        byte[] full = new byte[startArray.length + toAppend.length];

        for(int i = 0; i < full.length; i++) {

            full[i] = i >= startArray.length ? startArray[i] : toAppend[i-startArray.length];

        }

        return full;

    }

}
