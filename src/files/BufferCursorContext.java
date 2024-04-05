package files;

import java.util.ArrayList;

public class BufferCursorContext {

    /**
     * Insertion points column & line do not represent printing locations!
     * All will be relative to {link FileBuffer linesArrayList} indices.
     * For statusbar, these will need to be increased with 1
     */
    private int insertionPointCol, insertionPointLine, insertionPointByteIndex;
    FileBuffer containedFileBuffer;
    public BufferCursorContext(String path){

    }

    public BufferCursorContext(FileBuffer referredFileBuffer){

    }

    /**
     * Deletes the character before the insertion pt and updates the cursor position
     */
    public void deleteCharacter() {
        moveCursorLeft();
        containedFileBuffer.deleteCharacter(insertionPointCol, insertionPointLine);
    }

    /**
     * Makes the calculation to move the cursor down. Modifies the {@link BufferCursorContext#insertionPointCol} and {@link BufferCursorContext#insertionPointLine} accordingly.
     */
    public void moveCursorDown() {
        if (insertionPointLine < containedFileBuffer.getLines().size() - 1) {
            insertionPointLine++;
            insertionPointCol = Math.min(containedFileBuffer.getLines().get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor up. Modifies the {@link BufferCursorContext#insertionPointCol} and {@link BufferCursorContext#insertionPointLine} accordingly.
     */
    public void moveCursorUp() {
        if (insertionPointLine > 0) {
            insertionPointLine--;
            //shift left if the current line is longer than the previous
            insertionPointCol = Math.min(containedFileBuffer.getLines().get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }

    }

    /**
     * Makes the calculation to move the cursor left. Modifies the {@link BufferCursorContext#insertionPointCol} and {@link BufferCursorContext#insertionPointLine} accordingly.
     */
    public void moveCursorLeft() {
        if (insertionPointCol > 0) {
            insertionPointCol--;
            //insertionPointByteIndex--;
        } else {
            if (insertionPointLine != 0) {
                //move one line up, to last character
                insertionPointLine--;
                insertionPointCol = containedFileBuffer.getLines().get(insertionPointLine).size();
            }
            //otherwise do nothing, stay at first byte
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor to the start of the line. Modifies the {@link BufferCursorContext#insertionPointCol} and {@link BufferCursorContext#insertionPointLine} accordingly.
     */
    private void moveCursorToFront() {
        if (insertionPointCol > 0){
            insertionPointCol = 0;
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor right. Modifies the {@link BufferCursorContext#insertionPointCol} and {@link BufferCursorContext#insertionPointLine} accordingly.
     */
    public void moveCursorRight(){
        if(insertionPointCol < containedFileBuffer.getLines().get(insertionPointLine).size()) {
            insertionPointCol++;
        } else {
            //Move cursor one line down, unless already at bottom line
            if (insertionPointLine < containedFileBuffer.getLines().size() - 1) {
                insertionPointLine++;
                insertionPointCol = 0;
            }
            //otherwise do nothing
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * <p>Each array in {@link FileBuffer#getLines()} represents a line that is being printed in
     * the render. The class fields insertionPointLine represents on which line
     * we are with the cursor, and insertionPointCol the position in the list.</p>
     * <p>The insertionPointByteIndex is not accurate on the byteContent,
     * because line separators are in that array. With the given parameters we can retrieve the correct
     * value of the insertionPointByteIndex </p>
     */
    private int convertLineAndColToIndex(int line, int col) {
        int byteLengthSeparatorLen = FileHolder.lineSeparator.length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + containedFileBuffer.getLines().get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }

    public void save() {
        containedFileBuffer.save();
    }

    public void write(byte b) {
        containedFileBuffer.write(b, insertionPointByteIndex);
        moveCursorRight();
    }

    public int getInsertionPointLine() {
        return insertionPointLine;
    }

    public ArrayList<ArrayList<Byte>> getLines() {
        return containedFileBuffer.getLines();
    }

    public int getInsertionPointCol() {
        return insertionPointCol;
    }

    public ArrayList<Byte> getByteContent() {
        return containedFileBuffer.getByteContent();
    }

    public FileHolder getFileHolder() {
        return containedFileBuffer.getFileHolder();
    }

    public boolean getDirty() {
        return containedFileBuffer.getDirty();
    }
}
