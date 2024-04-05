package ui;

import files.FileAnalyserUtil;
import files.FileBuffer;
import inputhandler.FileBufferInputHandler;
import io.github.btj.termios.Terminal;

import java.io.IOException;

public class FileBufferView extends View {

    /**
     * Reference to the {@link FileBuffer} to retrieve display information
     */
    private final FileBuffer containedFileBuffer;

    public FileBufferView(FileBuffer openedFile) {
        this.containedFileBuffer=openedFile;
    }

    /**
     * Handles the rendering of the whole {@link FileBuffer}
     */
    @Override
    public void render() throws IOException {

        //height-1 to make space for status bar, rounds to select the area from the nearest multiple of height-1
        int renderStartingLineIndex = (containedFileBuffer.getInsertionPointLine() / (coords.height - 1)) * (coords.height - 1);
        //Renders either all the lines until the end, or the next height-2 lines
        for (int i = 0; i < Math.min(coords.height - 1, containedFileBuffer.getLines().size() - renderStartingLineIndex); i++) {
            String lineString = new String(FileAnalyserUtil.toArray(containedFileBuffer.getLines().get(renderStartingLineIndex + i)));
            //For each line, renders between the closest multiples of width-1, or starts at the closest multiple and ends at the end of file
            int renderLineStartIndex = (containedFileBuffer.getInsertionPointCol() / (coords.width - 1)) * (coords.width - 1);
            int renderLineEndIndex = Math.min(renderLineStartIndex + coords.width - 1, lineString.length());
            //endindex -1 to make space for vertical bar
            if (renderLineStartIndex < lineString.length()) {
                assert renderLineEndIndex <= lineString.length() && renderLineStartIndex <= lineString.length();
                if(renderLineStartIndex <= lineString.length() || renderLineEndIndex <= lineString.length()){
                    String toRenderString = lineString.substring(renderLineStartIndex, renderLineEndIndex);
                    if(renderLineEndIndex-renderLineStartIndex+1< coords.width){
                        toRenderString = toRenderString.concat(" ".repeat(coords.width-(renderLineEndIndex-renderLineStartIndex+1)));
                    }
                    Terminal.printText(1 + coords.startY + i, 1 + coords.startX, toRenderString);
                }

            }
        }
        //Some checks to handle very fast input the program cannot handle, does not affect logic and would not be strictly necessary
        assert coords.height > 0 && coords.startY > 0;
        if(coords.startY + coords.height > 0)
            Terminal.printText(coords.startY + coords.height, coords.startX + 1, this.getStatusbarString());
        renderScrollbar(coords.startX+coords.width-1, coords.startY, containedFileBuffer.getInsertionPointLine(), containedFileBuffer.getLines().size());

    }

    /**
     * Generates the string that is used to display the statusbar.
     */
    public String getStatusbarString() {
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

        if(super.getContainsActiveView())
            statusLine += "Active";
        else
            statusLine += "Not Active";
        return statusLine;
    }

    private void renderScrollbar(int scrollStartX, int startY, int focusedLine, int fileBufferTotalHeight){
        for(int i = 0; i<(super.coords.height-1); i++){
            double visibleStartPercent = ((focusedLine/(super.coords.height-1))*(super.coords.height-1))*1.0/containedFileBuffer.getLines().size();
            double visibleEndPercent = ((1+focusedLine/(super.coords.height-1))*(super.coords.height-1))*1.0/containedFileBuffer.getLines().size();

            if((i*1.0/(coords.height-1)>=visibleStartPercent) && (i*1.0/coords.height <= visibleEndPercent)){
                Terminal.printText(1+startY+i, 1+scrollStartX, "+");
            } else {
                Terminal.printText(1+startY+i, 1+scrollStartX, "-");
            }
        }
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
            int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (coords.width-1);
            int cursorYoffset = containedFileBuffer.getInsertionPointLine() % (coords.height-1);
            Terminal.moveCursor(1 + coords.startY + cursorYoffset, 1 + coords.startX + cursorXoffset);
        }
    }
}
