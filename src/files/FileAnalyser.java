package files;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class FileAnalyser {

    /**
     * Returns a Map<Integer, Boolean> which indicates at which integer a line separator should be printed.
     * The byte map contains "0d0a" or "0a" as line separators. Other line separations are found by checking
     * the dimension of the window.
     * Used for rendering.
     */
    public static List<Integer> analyseContents(byte[] byteContents) {

        String fileContentFormatted = formatBytes(byteContents);
        List<Integer> newLinesOccurrence = new ArrayList<>();
        boolean found0d = false;

        // Loop over bytes in String form.
        for(int i = 0; i < fileContentFormatted.length()-1; i += 2) {

            // We take 2 characters at space i.
            String part = fileContentFormatted.substring(i, i+2);

            // If we have 0a, check if we had 0d before. We have a line separation eitherway.
            if(part.equals("0a")) {
                if(found0d) {
                    found0d = false;
                    newLinesOccurrence.add((i / 2) - 1);
                }
                else
                    newLinesOccurrence.add(i/2);

            } else
                found0d = false;


            // If this part is 0d, we have possibly found 0d0a. Set found0d true;
            if(part.equals("0d"))                 // Set counter to 0 again ; we are on a new line
                found0d = true;
            //System.out.println("! found 0d");


        }

        // Debug
        //for(Map.Entry<Integer, Boolean> e : newLinesOccurrence.entrySet())
        //    System.out.println("< i = " + e.getKey() + " --> l = " + e.getValue());

        return newLinesOccurrence;
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

}
