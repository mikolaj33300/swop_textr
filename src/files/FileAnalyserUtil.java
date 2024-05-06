package files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class FileAnalyserUtil {

    /**
     * @param arg a string containing the separator argument
     * @return the lineseparator (\n or \n\r)
     */
    public static byte[] setLineSeparatorFromArgs(String arg) {
        if(arg.equals("--lf"))
            return new byte[]{0x0a};
        else if(arg.equals("--crlf"))
            return new byte[]{0x0d, 0x0a};
        else return System.lineSeparator().getBytes();
    }

    /**
     * @param arg a string containing the separator argument
     * @return if the argument is correctly formed
     */
    public static boolean isValidLineSeparatorString(String arg){
        return arg.equals("--lf") || arg.equals("--crlf");
    }


    /**
     * Subdivides the given byte array into lines
     * @param byteContents
     * @return
     */
    public static ArrayList<ArrayList<Byte>> getContentLines(byte[] byteContents, byte[] lineSeparator) {
        int lineSepLength = lineSeparator.length;
        int startOfCurrentLine = 0;
        ArrayList<ArrayList<Byte>> linesArrList = new ArrayList<>();

        // Loop over bytes in String form.
        int i = 0;
        while (i < byteContents.length) {
            //if separator encountered
            if(Arrays.equals(Arrays.copyOfRange(byteContents, i, i+lineSepLength), lineSeparator)){
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
        if(byteContents.length==0){
            linesArrList = new ArrayList<>(1);
            linesArrList.add(new ArrayList<Byte>(0));
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

    /**
     * @param bArr an array of bytes
     * @return make arrayList from byte array
     */
    public static ArrayList<Byte> createByteWrapArrayList(byte[] bArr){
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

    /**
     * @param arr1 array 2 to compare
     * @param arr2 array 2 to compare
     * @return if given arrays are equal
     */
    public static boolean areByteArrayContentsEqual(byte[] arr1, byte[] arr2) {
        if(arr1.length != arr2.length) return false;
        for(int i = 0; i < arr1.length; i++)
            if(arr1[i] != arr2[i]) return false;
        return true;
    }
}
