package util;

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
     * @param lineSeparator the line separator which decides where we split the byte[]
     * @param byteContents the array of byte values we want to transform in a {@link ArrayList} in a {@link ArrayList}
     * @return a list of lists where each entry in the list represents a line of bytes separated by the parameter line separator
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
                linesArrList.add(toArray(Arrays.copyOfRange(byteContents, startOfCurrentLine, i)));
                i += lineSepLength;
                startOfCurrentLine = i;
            } else {
                i = i+1;
            }
        }
        if(startOfCurrentLine<byteContents.length){
            linesArrList.add(toArray(Arrays.copyOfRange(byteContents, startOfCurrentLine, byteContents.length)));
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
     * @param byteArr the byte[] primitive which we want as a {@link Byte} array
     * @return the {@link Byte} array
     */
    public static Byte[] toPrimitive(byte[] byteArr){
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
     * Transforms the given byte[] object to an {@link ArrayList} object
     * @param bArr an array of bytes
     * @return make arrayList from byte array
     */
    public static ArrayList<Byte> toArray(byte[] bArr){
        return new ArrayList<>(Arrays.<Byte>asList(toPrimitive(bArr)));
    }


    /**
     * Puts all elements from a Byte ArrayList in a byte[]
     * @param arrList the {@link ArrayList} object which we want to transform to an array
     * @return a byte[] object with all elements from the given list
     */
    public static byte[] toArray(ArrayList<Byte> arrList) {
        byte[] resultArray = new byte[arrList.size()];
        for(int i = 0; i < arrList.size() ; i++){
            resultArray[i] = arrList.get(i).byteValue();
        }
        return resultArray;
    }


    /**
     * Splices the given array
     * @param array the array that should be spliced
     * @param start the start position (inclusive)
     * @param end the end position (inclusive)
     * @return a byte[] object that contains the range of bytes specified by the parameters
     */
    public static byte[] spliceArray(ArrayList<Byte> array, int start, int end) {
        byte[] splicedLine = new byte[end - start];
        int counter = 0;
        for(int i = start; i < end; i++) {
            splicedLine[counter] = array.get(i);
            counter++;
        }
        return splicedLine;
    }

    /**
     * Inserts the given insertArray into the resultArray
     * @param resultArray the array that is put as start
     * @param insertArray the array that is appended
     * @return a byte[] containing both arrays
     */
    public static byte[] copyArray(byte[] resultArray, byte[] insertArray) {

        byte[] result = new byte[resultArray.length + insertArray.length];

        for(int i = 0; i < result.length; i++) {

            if(i < resultArray.length)
                result[i] = resultArray[i];
            else
                result[i] = insertArray[i - resultArray.length];

        }

        return result;

    }

    /**
     * Gets the line separator used
     * @param contents the contents that will be checked for a line separator
     */
    public static byte[] getLineSeparator(byte[] contents) {
        if(formatBytes(contents).contains("0a") && !formatBytes(contents).contains("0d0a"))
            return "0a".getBytes();
        if(formatBytes(contents).contains("0d0a"))
            return "0d0a".getBytes();
        return System.lineSeparator().getBytes();
    }

}
