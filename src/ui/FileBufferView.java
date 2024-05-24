package ui;

import ioadapter.TermiosTerminalAdapter;
import files.BufferCursorContext;
import util.FileAnalyserUtil;
import files.FileBuffer;
import util.Coords;

import java.io.IOException;

public class FileBufferView extends View {
    //TODO: Make it not crash when the allocated dimensions are smaller than can fit, this is for some render functions

    /**
     * Reference to the {@link FileBuffer} to retrieve display information
     */
    private final BufferCursorContext containedFileBuffer;

    /**
     * The constructor for this view.
     * @param openedFile the cursor context for the opened file
     * @param termiosTerminalAdapter the assigned adapter for this view, used to interact with termios
     */
    public FileBufferView(BufferCursorContext openedFile,TermiosTerminalAdapter termiosTerminalAdapter) {
        super(termiosTerminalAdapter);
        this.containedFileBuffer=openedFile;
    }

    /**
     * @return the column our cursor is in
     */
    @Override
    public int getFocusedCol() {
        return containedFileBuffer.getInsertionPointCol();
    }

    /**
     * @return the line our cursor is in
     */
    @Override
    public int getFocusedLine() {
        return containedFileBuffer.getInsertionPointLine();
    }

    @Override
    public int getTotalContentHeight() {
        return containedFileBuffer.getLines().size();
    }

    /**
     * Handles the rendering {@link FileBuffer}
     */
    //TODO: remove any remnants of logic that still contains scrolling
    @Override
    public void render(int activeHash) throws IOException {
        Coords coords = super.uiCoordsReal;
        int height = coords.height;
        int width = coords.width;
        int startY = coords.startY;
        int startX = coords.startX;
        super.fill(1 + startX, 1 + startY, width, height, " ");

        //height-1 to make space for status bar, rounds to select the area from the nearest multiple of height-1
        int renderStartingLineIndex = containedFileBuffer.getInsertionPointLine();
        //Renders either all the lines until the end, or the next height-1 lines
        for (int i = 0; i < Math.min(height - 1, containedFileBuffer.getLines().size() - renderStartingLineIndex); i++) {
            String lineString = new String(FileAnalyserUtil.toArray(containedFileBuffer.getLines().get(renderStartingLineIndex + i)));
            //For each line, renders between the closest multiples of width-1, or starts at the closest multiple and ends at the end of file
            int renderLineStartIndex = (containedFileBuffer.getInsertionPointCol() / (width)) * (width);
            int renderLineEndIndex = Math.min(renderLineStartIndex + width, lineString.length());
            //endindex -1 to make space for vertical bar
            if (renderLineStartIndex < lineString.length()) {
                assert renderLineEndIndex <= lineString.length() && renderLineStartIndex <= lineString.length();
                if(renderLineStartIndex <= lineString.length() || renderLineEndIndex <= lineString.length())
                    //System.out.println("end: " + renderLineEndIndex + ", start: " + renderLineStartIndex);
                    termiosTerminalAdapter.printText(1 + startY + i, 1 + startX, lineString.substring(renderLineStartIndex, renderLineEndIndex));
            }
        }
        //Some checks to handle very fast input the program cannot handle, does not affect logic and would not be strictly necessary
        //assert height > 0 && startY > 0;
        if(startY + height > 0) //System.out.println("Given row is smaller than one: " + (startY + height) + " -> startY: " + startY + ", height: " + height);
            termiosTerminalAdapter.printText(startY + height, startX + 1, this.getStatusbarString(activeHash).substring(0, Math.min(width, this.getStatusbarString(activeHash).length())));
    }

    /**
     * Generates the string that is used to display the statusbar.
     */
    public String getStatusbarString(int activeHash) {
        String statusLine = containedFileBuffer.getFileBuffer().getPath();
        statusLine += " #Lines:";
        statusLine += String.valueOf(containedFileBuffer.getLines().size());
        statusLine += " #Chars:";
        statusLine += String.valueOf(containedFileBuffer.getByteContent().size());
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
        if(this.hashCode() == activeHash)
            statusLine += "Active";
        else
            statusLine += "Not Active";
        return statusLine;
    }

    /**
     * @param obj the object to compare this to
     * Determines if the given object is 'equal' to this object.
     * @return true if the {@link FileBufferView#containedFileBuffer} is to parameter {@link FileBuffer}
     */
    public boolean equals(Object obj) {
        if (obj instanceof FileBufferView fBufView) {
            return fBufView.containedFileBuffer.equals(this.containedFileBuffer);
        } else {
            return false;
        }
    }

    /**
     * @return the lenght of the line focussed
     */
    @Override
    protected int getLineLength(int focusedLine) {
        return  containedFileBuffer.getLineLength(focusedLine);
    }

    /**
     * Handles printing the cursor in this view.
     */
    @Override
    public void renderCursor() throws IOException {
        Coords coords = super.uiCoordsReal;
        int width = coords.width;
        int height = coords.height;
        int startY = coords.startY;
        int startX = coords.startX;

        int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (width-1);
        int cursorYoffset = 0;//containedFileBuffer.getInsertionPointLine() % (height-1);
        termiosTerminalAdapter.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
    }

    /**
     * Returns the {@link BufferCursorContext} object of this view
     * @return buffer cursor context
     */
    public BufferCursorContext getBufferCursorContext() {
        return this.containedFileBuffer.deepClone();
    }

}
