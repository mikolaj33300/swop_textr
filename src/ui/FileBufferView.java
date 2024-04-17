package ui;

import controller.TermiosTerminalAdapter;
import files.BufferCursorContext;
import files.FileAnalyserUtil;
import files.FileBuffer;

import java.io.IOException;

public class FileBufferView extends View{

    /**
     * Reference to the {@link FileBuffer} to retrieve display information
     */
    private final BufferCursorContext containedFileBuffer;

    private TermiosTerminalAdapter termiosTerminalAdapter;

    public FileBufferView(BufferCursorContext openedFile,TermiosTerminalAdapter termiosTerminalAdapter) {
        this.containedFileBuffer=openedFile;
        this.termiosTerminalAdapter = termiosTerminalAdapter;
    }

    /**
     * Handles the rendering of the whole {@link FileBuffer}
     */
    @Override
    public void render(int activeHash) throws IOException {
        UICoords coords = super.getRealUICoordsFromScaled(termiosTerminalAdapter);
        int height = coords.height;
        int width = coords.width;
        int startY = coords.startY;
        int startX = coords.startX;

        //height-1 to make space for status bar, rounds to select the area from the nearest multiple of height-1
        int renderStartingLineIndex = containedFileBuffer.getInsertionPointLine();
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
                    termiosTerminalAdapter.printText(1 + startY + i, 1 + startX, lineString.substring(renderLineStartIndex, renderLineEndIndex));
            }
        }
        //Some checks to handle very fast input the program cannot handle, does not affect logic and would not be strictly necessary
        //assert height > 0 && startY > 0;
        if(startY + height > 0) //System.out.println("Given row is smaller than one: " + (startY + height) + " -> startY: " + startY + ", height: " + height);
            termiosTerminalAdapter.printText(startY + height, startX + 1, this.getStatusbarString(activeHash));
        renderScrollbar(height, startX+width-1, startY, containedFileBuffer.getInsertionPointLine(), containedFileBuffer.getLines().size());
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

    private void renderScrollbar(int height, int scrollStartX, int startY, int focusedLine, int fileBufferTotalHeight){
        for(int i = 0; i<(height-1); i++){
            double visibleStartPercent = ((focusedLine/(height-1))*(height-1))*1.0/containedFileBuffer.getLines().size();
            double visibleEndPercent = ((1+focusedLine/(height-1))*(height-1))*1.0/containedFileBuffer.getLines().size();

            if((i*1.0/(height-1)>=visibleStartPercent) && (i*1.0/height <= visibleEndPercent)){
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "+");
            } else {
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "-");
            }
        }
    }

    /**
     * Determines if the given object is 'equal' to this object.
     * Returns true if the {@link FileBufferView#containedFileBuffer} is to parameter {@link FileBuffer}
     */
    public boolean equals(Object obj) {
        if (obj instanceof FileBufferView fBufView) {
            return fBufView.containedFileBuffer.equals(this.containedFileBuffer);
        } else {
            return false;
        }
    }

    /**
     * Handles printing the cursor in this view.
     */
    public void renderCursor() throws IOException {
        UICoords coords = getRealUICoordsFromScaled(termiosTerminalAdapter);
        int width = coords.width;
        int height = coords.height;
        int startY = coords.startY;
        int startX = coords.startX;

        int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (width-1);
        int cursorYoffset = 0;//containedFileBuffer.getInsertionPointLine() % (height-1);
        termiosTerminalAdapter.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
    }
}
