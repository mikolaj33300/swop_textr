package files;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Statusbar {

    private FileBuffer buffer;
    private int column, lines, insertionPoint, scroll;

    public Statusbar(FileBuffer buffer) {
        this.buffer = buffer;
        analyseContents(buffer.getContent());
        this.insertionPoint = 0;
        this.scroll = 0;
    }

    int getScroll() {
        return this.scroll;
    }

    int getColumn() {
        return this.column;
    }

    int getLines() {
        return this.lines;
    }

    int getInsertionPoint() {
        return this.insertionPoint;
    }

    public void moveCursor(char direction) {
        switch(direction) {
            // Right
            case 'C':
                insertionPoint++;
                break;
            // Left
            case 'D':
                insertionPoint--;
                break;
            // Up
            case 'A':
                calculateMove(1);
                // Down
            case 'B':
                //insertionPoint += width;
        }

        shouldScroll();
    }

    public void scroll(int offset) {
        this.scroll += offset;
    }

    /**
     * Called when cursor is moved.
     */
    private void shouldScroll() {

    }

    private void calculateMove(int heightOffset) {
        // First calculate on which col we should be after move
        // --> calculate length of line above / below
        List<Integer> separators = analyseContents(this.buffer.getContent());

        // Get current amount of characters until the next line separator.
        // -1 because lines go from [1,a]. Separator list from [0,a-1]
        // Remember: separator list holds indices for the whole string:
        // abcd_efgh_ijkl -> 0: 4 ; 1: 8
        // If we are on line 2 == efgh. Then our current line has 8 characters.
        int currentLineIndex = separators.get(this.lines-1);

        // We can already update our line height. This will always change +a or -a
        this.lines = Math.max(this.lines + heightOffset, 1);

        // Case 1: We are at minimum line index.
        if(this.lines == 1) {

            // Length of the string on line 1
            int indexFirstSep = separators.get(0);
            this.column = Math.min(this.column, indexFirstSep);

        // Case 2: We are trying to go over the max amount of lines
        } else if(this.lines >= separators.size() + 1) {

            // There can be maximally separators.size() + 1 lines.
            this.lines = separators.size() + 1;

            // Get the length of the line where we jump to
            int indexOffsetLine = new String(this.buffer.getContent()).length();
            this.column = Math.min(this.column, indexOffsetLine);

        // Generic case: we switch between two lines
        } else {

            // We find the offset of the next line
            //int indexOffsetLine =

        }
    }

    /**
     * Calculates values of the statusbar: {@link Statusbar#column} and {@link Statusbar#lines}.
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
