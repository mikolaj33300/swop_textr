package files;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Statusbar {

    private int column, lines, insertionPoint;

    public Statusbar(byte[] fileContent) {
        analyseContents(fileContent);
    }

    public void moveCursor(int newLocation) {
        this.insertionPoint = newLocation;
    }

    public void update(byte[] fileContent) {
        calculateDimensionsStatus(fileContent);
    }

    /**
     * Calculates values of the statusbar
     */
    private void calculateDimensionsStatus(byte[] fileContent) {
        List<Integer> list = analyseContents(fileContent);
        int[] dimensions = new int[2];
        int lineIndex = -1;

        // Loop over all indices where a line separator is.
        // If insertionPoint is before first enter -> i = 0 -> line 1 = i+1
        for(int i = 0; i < list.size(); i++) {
            if(insertionPoint <= list.get(i)) lineIndex = i+1;
        }
        if(lineIndex == -1) throw new RuntimeException("Invalid insertionpoint");

        // lineIndex = i+1 --> vorige i = lineIndex - 2 = i - 1
        int previousEnterIndex = lineIndex - 2 == 0 ? 0 : list.get(lineIndex-2);
        int lineLength = list.get(lineIndex-1) - previousEnterIndex;

        this.lines = lineIndex;
        this.column = lineLength;

    }

    /**
     * Returns a Map<Integer, Boolean> which indicates at which integer a line separator should be printed.
     * The byte map contains "0d0a" or "0a" as line separators. Other line separations are found by checking
     * the dimension of the window.
     * Used for rendering.
     */
    List<Integer> analyseContents(byte[] fileContent) {

        String fileContentFormatted = formatBytes(fileContent);
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
    private String formatBytes(byte[] bytes) {
        Formatter formatter = new Formatter();
        for(byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();
    }

}
