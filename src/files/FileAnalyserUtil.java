package files;

import core.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class FileAnalyserUtil {

    /**
     * Returns a Map<Integer, Boolean> which indicates at which integer a line separator should be printed.
     * The byte map contains "0d0a" or "0a" as line separators. Other line separations are found by checking
     * the dimension of the window.
     * Used for rendering.
     */
    public static ArrayList<ArrayList<byte>> getContentLines(byte[] byteContents) {
        String fileContentFormatted = formatBytes(byteContents);
        String usedLineSeparatorFormatted = Controller.getLineSeparator();
        int startOfCurrentLine = 0;
        ArrayList<ArrayList<byte>> linesArrList = new ArrayList<>();

        // Loop over bytes in String form.
        for(int i = 0; i < fileContentFormatted.length()-1;) {
            if(fileContentFormatted.substring(i, i+usedLineSeparatorFormatted.length()-1).equals(usedLineSeparatorFormatted)){
                linesArrList.add(toArrayList(fileContentFormatted.substring(startOfCurrentLine, i)));
                i = i+usedLineSeparatorFormatted.length();
                startOfCurrentLine = i;
            } else {
                i = i+2;
            }
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

    private static ArrayList<byte> toArrayList(String s){
        return new ArrayList<byte>(Arrays.<byte>asList(s.getBytes()));
    }
}
