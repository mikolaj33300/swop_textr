package files;

import core.TextR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class FileAnalyserUtil {


    /**
     * Subdivides the given byte array into lines
     * @param byteContents
     * @return
     */
    public static ArrayList<ArrayList<Byte>> getContentLines(byte[] byteContents) {
        int lineSepLength = FileHolder.lineSeparator.length;
        int startOfCurrentLine = 0;
        ArrayList<ArrayList<Byte>> linesArrList = new ArrayList<>();

        // Loop over bytes in String form.
        int i = 0;
        while (i < byteContents.length) {
            //if separator encountered
            if(Arrays.equals(Arrays.copyOfRange(byteContents, i, i+lineSepLength), FileHolder.lineSeparator)){
                linesArrList.add(createByteWrapArrayList(Arrays.copyOfRange(byteContents, startOfCurrentLine, i)));
                i += lineSepLength;
                startOfCurrentLine = i;
            } else {
                i = i+1;
            }
        }
        if(startOfCurrentLine<byteContents.length){
            linesArrList.add(createByteWrapArrayList(Arrays.copyOfRange(byteContents, startOfCurrentLine, byteContents.length)));
        }
        return linesArrList;
    }
    /**
     * Creates the string representation of the byte[].
     * Used for finding line separators: they will be formatted as 0d0a or 0a
     * Note: every byte will be formatted to 2 string characters in ASCII.
     */
    public static String formatBytes(byte[] bytes) {
        Formatter formatter = new Formatter();
        for(byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }

    /**
     * Returns an array of Byte wrappers for given array of bytes
     * @param byteArr
     * @return
     */
    public static Byte[] wrapEachByteElem(byte[] byteArr){
        Byte[] wrapperArray = new Byte[byteArr.length];
        for (int i = 0; i < byteArr.length; i++) {
            wrapperArray[i] = byteArr[i];
        }
        return wrapperArray;
    }

    /*    private static byte[] formatStringAsHexBytes(String s){
            byte[] byteArr = new byte[s.length()/2];
            for(int i=0; i<s.length(); i= i+2){
                byteArr[i/2] = Byte.parseByte(s.substring(i, i+2), 16);
            }
            return byteArr;
        }*/
    private static ArrayList<Byte> createByteWrapArrayList(byte[] bArr){
        return new ArrayList<>(Arrays.<Byte>asList(wrapEachByteElem(bArr)));
    }


    /**
     * Puts all elements from a Byte ArrayList in a byte[]
     * @param arrList
     * @return
     */
    public static byte[] toArray(ArrayList<Byte> arrList) {
        byte[] resultArray = new byte[arrList.size()];
        for(int i = 0; i < arrList.size() ; i++){
            resultArray[i] = arrList.get(i).byteValue();
        }
        return resultArray;
    }
}
