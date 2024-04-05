package ui;

import files.FileAnalyserUtil;
import files.FileBuffer;
import ui.InsertionPoint;
import observer.FileBufferListener;
import layouttree.LayoutLeaf;

import io.github.btj.termios.Terminal;
import java.io.IOException;

public class FileBufferView extends View implements FileBufferListener {

  /*
   * the hashcode of the rendered object
   */
    private final int hashCode;
    Terminal Terminal;

    /**
     * Constructor for FileBufferView
     */
    public FileBufferView(Terminal Terminal, int hashCode) throws IOException {
      this.Terminal = Terminal;
      this.hashCode = hashCode;
    }

    /**
     * Handles the rendering of the whole {@link FileBuffer}
     */
    @Override
    public void render(FileBuffer containedFileBuffer, int hashCode, boolean active) throws IOException {
        // super.setCorrectCoords(hashCode); //TODO => update from facade when rotating everything

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
                    this.Terminal.printText(1 + startY + i, 1 + startX, lineString.substring(renderLineStartIndex, renderLineEndIndex));
            }
        }
        //Some checks to handle very fast input the program cannot handle, does not affect logic and would not be strictly necessary
        assert height > 0 && startY > 0;
        if(startY + height > 0) //System.out.println("Given row is smaller than one: " + (startY + height) + " -> startY: " + startY + ", height: " + height);
            this.Terminal.printText(startY + height, startX + 1, this.getStatusbarString(containedFileBuffer, active));
        renderScrollbar(super.startX+super.width-1, super.startY, containedFileBuffer.getInsertionPointLine(), containedFileBuffer.getLines().size(), containedFileBuffer);

    }

    /**
     * Generates the string that is used to display the statusbar.
     */
    public String getStatusbarString(FileBuffer containedFileBuffer, boolean active) {
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

        if(active)
            statusLine += "Active";
        else
            statusLine += "Not Active";
        return statusLine;
    }

    private void renderScrollbar(int scrollStartX, int startY, int focusedLine, int fileBufferTotalHeight, FileBuffer containedFileBuffer){
        for(int i = 0; i<(super.height-1); i++){
            double visibleStartPercent = ((focusedLine/(super.height-1))*(super.height-1))*1.0/containedFileBuffer.getLines().size();
            double visibleEndPercent = ((1+focusedLine/(super.height-1))*(super.height-1))*1.0/containedFileBuffer.getLines().size();

            if((i*1.0/(height-1)>=visibleStartPercent) && (i*1.0/height <= visibleEndPercent)){
                this.Terminal.printText(1+startY+i, 1+scrollStartX, "+");
            } else {
                this.Terminal.printText(1+startY+i, 1+scrollStartX, "-");
            }
        }
    }

    /**
     * Handles printing the cursor in this view. Upon calling {@link View#setCorrectCoords()} is called to
     * retrieve the {@link View#startX}, {@link View#startY}, {@link View#width} and {@link View#height}
     */
    public void renderCursor(InsertionPoint pt, int hashCode, boolean active) throws IOException {
        //super.setCorrectCoords(hashCode); // TODO
        if (active) {
            int cursorXoffset = pt.col % (width-1);
            int cursorYoffset = pt.row % (height-1);
            this.Terminal.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
        }
    }
}
