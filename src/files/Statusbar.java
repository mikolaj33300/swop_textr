package files;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Statusbar {

    private final FileBuffer buffer;
    private int column, lines, insertionPoint, scroll;

    public Statusbar(FileBuffer buffer) {
        this.buffer = buffer;
        this.insertionPoint = 0;
        this.scroll = 0;
        this.calculateDimensionsStatus(this.buffer.getContent());
    }

    /**
     * Determines the top visible line in of the buffer
     */
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

    String renderStatus() {
        String statusLine = this.buffer.getFileHolder().getPath();
        statusLine += " ";
        statusLine += String.valueOf(insertionPoint/this.buffer.getContent().length);
        statusLine += "%";
        return statusLine;
    }

    String getScrollbar(int height, int index){
        if (index == height*((float) insertionPoint/this.buffer.getContent().length))
            return "+";
        else
            return "|";
    }

    void moveCursor(char direction) {
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
                calculateMove(-1);
        }

        shouldScroll();
    }

    void scroll(int offset) {
        this.scroll += offset;
    }

    /**
     * Called when cursor is moved.
     */
    private void shouldScroll() {

    }

    /**
     * For vertical cursor movement, next column position is calculated.
     */
    private void calculateMove(int heightOffset) {
        // First calculate on which col we should be after move
        // --> calculate length of line above / below
        List<Integer> separators = FileAnalyser.analyseContents(this.buffer.getContent());

        // Get current amount of characters until the next line separator.
        // -1 because lines go from [1,a]. Separator list from [0,a-1]
        // Remember: separator list holds indices for the whole string:
        // abcd_efgh_ijkl -> 0: 4 ; 1: 8
        // If we are on line 2 == efgh. Then our current line has 8 characters.
        int currentLineIndex = separators.get(this.lines-1);
        int indexOffsetLine = 0;

        // We can already update our line height. This will always change +a or -a
        this.lines = Math.max(this.lines + heightOffset, 1);

        // Case 1: We are at minimum line index.
        if(this.lines == 1) {

            // Length of the string on line 1
            indexOffsetLine = separators.get(0);

        // Case 2: We are trying to go over the max amount of lines
        } else if(this.lines >= separators.size() + 1) {

            // There can be maximally separators.size() + 1 lines.
            this.lines = separators.size() + 1;

            // Get the length of the line where we jump to
            indexOffsetLine = new String(this.buffer.getContent()).length() - currentLineIndex;

        // Generic case: we switch between two lines
        } else {

            // We find the offset of the next line
            indexOffsetLine = separators.get(this.lines-1) - currentLineIndex;

        }

        this.column = Math.min(this.column, indexOffsetLine);

    }

    /**
     * Calculates values of the statusbar: {@link Statusbar#column} and {@link Statusbar#lines}.
     */
    private void calculateDimensionsStatus(byte[] fileContent) {
        List<Integer> list = FileAnalyser.analyseContents(fileContent);
        int lineIndex = -1;

        // Loop over all indices where a line separator is.
        // If insertionPoint is before first enter -> i = 0 -> line 1 = i+1
        for(int i = 0; i < list.size(); i++) {
            // <= because: insertionPoint is always before a character.
            // abcI_ I is between c and _. I is still on line 1.
            if(insertionPoint <= list.get(i)) lineIndex = i+1;
        }
        // Insertion point is after the last line separator
        if(list.size() > 0 && insertionPoint > list.get(list.size()-1)) lineIndex = list.size() + 1;
        // There are no line separators
        if(list.size() == 0) {
            this.lines = 1;
            this.column = insertionPoint;
            return;
        }

        // lineIndex = i+1 --> vorige i = lineIndex - 2 = i - 1
        int previousEnterIndex = lineIndex - 2 < 0 ? 0 : list.get(lineIndex-2);
        previousEnterIndex += buffer.getFileHolder().getLineSeparator().length() / 2;

        int lineLength = insertionPoint - previousEnterIndex;

        this.lines = lineIndex;
        this.column = lineLength;

    }

}
