package ui;

import files.FileAnalyserUtil;
import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;

import java.io.IOException;

public class FileBufferView extends View {

    /**
     * Reference to the {@link FileBuffer} to retrieve display information
     */
    private final FileBuffer containedFileBuffer;

    //TODO: fix representation exposure
    /**
     * Constructor for FileBufferView
     */
    public FileBufferView(String path, LayoutLeaf parent) throws IOException {
        super.parent = parent;
        containedFileBuffer = new FileBuffer(path);
    }

    /**
     * Handles the rendering of the whole {@link FileBuffer}
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
                assert renderLineEndIndex <= lineString.length() && renderLineStartIndex <= lineString.length();
                if(renderLineStartIndex <= lineString.length() || renderLineEndIndex <= lineString.length())
                    //System.out.println("end: " + renderLineEndIndex + ", start: " + renderLineStartIndex);
                    Terminal.printText(1 + startY + i, 1 + startX, lineString.substring(renderLineStartIndex, renderLineEndIndex));
            }
        }
        assert height > 0 && startY > 0;
        if(startY + height > 0) //System.out.println("Given row is smaller than one: " + (startY + height) + " -> startY: " + startY + ", height: " + height);
            Terminal.printText(startY + height, startX + 1, this.renderStatusbar());
    }

    /**
     * Generates the string that is used to display the statusbar.
     */
    private String renderStatusbar() {
        String statusLine = containedFileBuffer.getFileHolder().getPath();
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

        if(super.parent.getContainsActiveView())

            statusLine += "Active";
        else
            statusLine += "Not Active";
        return statusLine;
    }

    /**
     * Moves the cursor in a direction specified by the parameter
     */
    public void moveCursor(char c) {
        containedFileBuffer.moveCursor(c);
    }

    /**
     * Writes a specific byte to the {@link FileBuffer}
     */
    public void write(byte b) {
        containedFileBuffer.write(b);
    }

    /**
     * Saves the contents of the {@link FileBuffer} to {@link java.io.File}
     */
    public int save() {
        return containedFileBuffer.save();
    }

    /**
     * Enters an insertion point to the byte content. This method is separate from {@link FileBufferView#write(byte)} since of non-ASCII content
     */
    public void enterInsertionPoint() {
        containedFileBuffer.enterInsertionPoint();
    }

    /**
     * Closes this {@link FileBuffer}
     *
     * @return
     */
    public int close() {
        return containedFileBuffer.close();
    }

    /**
     * Determines if the given object is 'equal' to this object.
     * Returns true if the {@link FileBufferView#containedFileBuffer} is {@link FileBuffer#equals(FileBuffer)} to parameter {@link FileBuffer}
     */
    public boolean equals(Object obj) {
        if (obj instanceof FileBufferView fBufView) {
            return fBufView.containedFileBuffer.equals(this.containedFileBuffer);
        } else {
            return false;
        }
    }

    /**
     * Deletes the character before the insertion line in {@link FileBuffer}. Called when {@link Controller}
     */
    public void deleteCharacter() {
        containedFileBuffer.deleteCharacter();
    }

    /**
     * Returns a copy of the FileBuffer of this FileBufferView
     */
    public FileBuffer getContainedFileBuffer() {
        return containedFileBuffer.clone();
    }

    /**
     * Handles printing the cursor in this view. Upon calling {@link View#setCorrectCoords()} is called to
     * retrieve the {@link View#startX}, {@link View#startY}, {@link View#width} and {@link View#height}
     */
    public void renderCursor() throws IOException {
        super.setCorrectCoords();
        if (parent.getContainsActiveView()) {
            int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (width-1);
            int cursorYoffset = containedFileBuffer.getInsertionPointLine() % (height-1);
            Terminal.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
        }
    }
}
