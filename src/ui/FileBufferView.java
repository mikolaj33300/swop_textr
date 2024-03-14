package ui;

import files.FileAnalyserUtil;
import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.LayoutLeaf;

import java.io.IOException;

public class FileBufferView extends View{
    private FileBuffer containedFileBuffer;

    public FileBufferView(String path, LayoutLeaf parent) {
        super.parent = parent;
        containedFileBuffer = new FileBuffer(path);
    }

    public String getContainedPath(){
        return containedFileBuffer.getPath();
    }

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
        Terminal.printText(startY + height, startX + 1, this.renderStatusbar());
    }

    private String renderStatusbar() {
        String statusLine = containedFileBuffer.getPath();
        statusLine += " #Lines:";
        statusLine += String.valueOf(containedFileBuffer.getLines().size());
        statusLine += " #Chars:";
        String contents = new String(containedFileBuffer.getFileHolder().getContent());
        statusLine += contents.length();
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

    public void moveCursor(char c) {
        containedFileBuffer.moveCursor(c);
    }

    public void write(byte b) {
        containedFileBuffer.write(b);
    }

    public void save() {
        containedFileBuffer.save();
    }

    public void enterInsertionPoint() {
        containedFileBuffer.enterInsertionPoint();
    }

    public void close() {
        containedFileBuffer.close();
    }

    public boolean equals(Object obj) {
        if (obj instanceof FileBufferView fBufView) {
            return fBufView.containedFileBuffer.equals(this.containedFileBuffer);
        } else {
            return false;
        }
    }

    public void deleteCharacter() {
        containedFileBuffer.deleteCharacter();
    }

    public FileBuffer getContainedFileBuffer() {
        return containedFileBuffer.clone();
    }

    public void renderCursor() throws IOException {
        super.setCorrectCoords();
        if (parent.getContainsActiveView()) {
            int cursorXoffset = containedFileBuffer.getInsertionPointCol() % (width-1);
            int cursorYoffset = containedFileBuffer.getInsertionPointLine() % (height-1);
            Terminal.moveCursor(1 + startY + cursorYoffset, 1 + startX + cursorXoffset);
        }
    }
}
