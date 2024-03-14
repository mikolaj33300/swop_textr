package ui;

import files.FileAnalyserUtil;
import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;

import java.io.IOException;

public class FileBufferView extends View{
    /**
     * The FileBuffer linked to this FileBufferView
     */
    private FileBuffer containedFileBuffer;

    /**
     * Constructor for FileBufferView that makes copies of its arguments to prevent representation exposure
     */
    public FileBufferView(String path, LayoutLeaf parent) {
        super.parent = parent.clone();
        containedFileBuffer = new FileBuffer(path);
    }

    /**
     * Render all the elements on the this FileBufferView
     */
    @Override
    public void render() throws IOException {
        super.setCorrectCoords();

        //height-1 to make space for status bar, rounds to select the area from the nearest multiple of height-1
        int renderStartingLineIndex = (containedFileBuffer.getInsertionPointLine() / (height - 1)) * (height - 1);
        //Renders either all the lines until the end, or the next height-2 lines
        for (int i = 0; i < Math.min(height - 1, containedFileBuffer.getLines().size() - renderStartingLineIndex); i++) {
            String lineString = new String(FileAnalyserUtil.toArray(containedFileBuffer.getLines().get(renderStartingLineIndex + i)));
            //For each line, renders between the closest multiples of width-1, or starts at the closest multiple and ends at the end of file
            int renderLineStartIndex = (containedFileBuffer.getInsertionPointCol() / (width - 1)) * (width - 1);
            int renderLineEndIndex = Math.min(renderLineStartIndex + width - 1, lineString.length());
            //endindex -1 to make space for vertical bar
            if (renderLineStartIndex < lineString.length()) {
                Terminal.printText(1 + startY + i, 1 + startX, lineString.substring(renderLineStartIndex, renderLineEndIndex));
            }
        }
        Terminal.printText(startY + height, startX + 1, this.generateStatusbar());
    }

    /**
     * Renders the cursor on the current view
     */
    public void renderCursor() throws IOException {
        super.setCorrectCoords();
        if (parent.getContainsActiveView()) {
            int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (width-1);
            int cursorYoffset = containedFileBuffer.getInsertionPointLine() % (height-1);
            Terminal.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
        }
    }

    /**
     * Returns a String to be used as Statusbar
     * Contains the Views path, amount of lines, amount of characters, row and column of insertionpoint,
     * whether the current view is active, and whether FileBuffer is dirty
     */
    private String generateStatusbar() {
        String statusLine = containedFileBuffer.getPath();
        statusLine += " #Lines:";
        statusLine += String.valueOf(containedFileBuffer.getLines().size());
        statusLine += " #Chars:";
        statusLine += String.valueOf(containedFileBuffer.getAmountChars());
        statusLine += " Insert:[";
        statusLine += containedFileBuffer.getInsertionPointLine();
        statusLine += ";";
        statusLine += containedFileBuffer.getInsertionPointCol();
        statusLine += "] ";
        if (containedFileBuffer.getDirty())
            statusLine += "Dirty";
        else
            statusLine += "Clean";
        statusLine += " ";

        if(super.parent.getContainsActiveView())

            statusLine += "Active";
        else
            statusLine += "Not Active";
        return statusLine;
    }

    /**
     * Moves the cursor of the FileBuffer linked to this FileBufferView up, down, left or right
     * The direction of the movement depends on the given argument
     */
    public void moveCursor(char c) {
        containedFileBuffer.moveCursor(c);
    }

    /**
     * Writes a byte on the FileBuffer linked to this FileBufferView
     */
    public void write(byte b) {
        containedFileBuffer.write(b);
    }

    /**
     * Saves the FileBuffer linked to this FileBufferView to the file linked to its FileHolder
     */
    public void save() {
        containedFileBuffer.save();
    }

    /**
     * Inserts a lineseperator on the current insertionpoint in the FileBuffer linked to this FileBufferView
     */
    public void enterInsertionPoint() {
        containedFileBuffer.enterInsertionPoint();
    }

    /**
     * Closes the current buffer and recalculates the Layout-tree
     * If //TODO: kijk of dit specifiek programma sluiot, of een andere test -> zie na implementatie
     */
    public void close() {
        containedFileBuffer.close();
    }

    /**
     * Checks whether this FileBuffer and the given object are the same type and have the same contents
     */
    public boolean equals(Object obj) {
        if (obj instanceof FileBufferView fBufView) {
            return fBufView.containedFileBuffer.equals(this.containedFileBuffer);
        } else {
            return false;
        }
    }

    /**
     * Deletes the character at the current insertionpoint of the FileBuffer linked to this FileBufferView
     */
    public void deleteCharacter() {
        containedFileBuffer.deleteCharacter();
    }

    /**
     * Returns a copy of the FileBuffer linked to this FileBufferView
     */
    public FileBuffer getContainedFileBuffer() {
        return containedFileBuffer.clone();
    }

}
