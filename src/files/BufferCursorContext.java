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
     * Deletes the character before the {@link FileBuffer#insertionPointByteIndex} and updates the cursor position
     */
    public void deleteCharacter() {
        if(this.insertionPointCol > 0 || this.insertionPointLine > 0) {
            this.containedFileBuffer.dirty = true;
        }
        if(insertionPointCol > 0) {
            this.containedFileBuffer.byteContent.remove(insertionPointByteIndex-1);
            moveCursorLeft();
        } else {
            if(insertionPointLine!=0){
                //shift left the amount of bytes that need to be deleted and delete them one by one
                moveCursorLeft();
                for(int i = 0; i< FileHolder.lineSeparator.length ; i++) {
                    this.containedFileBuffer.byteContent.remove(insertionPointByteIndex);
                }
            }
        }
        linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) this.byteContent.clone()));
    }

    /**
     * Makes the calculation to move the cursor down. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    private void moveCursorDown() {
        if (insertionPointLine < linesArrayList.size() - 1) {
            insertionPointLine++;
            insertionPointCol = Math.min(linesArrayList.get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor up. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    private void moveCursorUp() {
        if (insertionPointLine > 0) {
            insertionPointLine--;
            //shift left if the current line is longer than the previous
            insertionPointCol = Math.min(linesArrayList.get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }

    }

    /**
     * Makes the calculation to move the cursor left. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    private void moveCursorLeft() {
        if (insertionPointCol > 0) {
            insertionPointCol--;
            //insertionPointByteIndex--;
        } else {
            if (insertionPointLine != 0) {
                //move one line up, to last character
                insertionPointLine--;
                insertionPointCol = linesArrayList.get(insertionPointLine).size();
            }
            //otherwise do nothing, stay at first byte
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor to the start of the line. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    private void moveCursorToFront() {
        if (insertionPointCol > 0){
            insertionPointCol = 0;
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Makes the calculation to move the cursor right. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    private void moveCursorRight(){
        if(insertionPointCol < linesArrayList.get(insertionPointLine).size()) {
            insertionPointCol++;
        } else {
            //Move cursor one line down, unless already at bottom line
            if (insertionPointLine < linesArrayList.size() - 1) {
                insertionPointLine++;
                insertionPointCol = 0;
            }
            //otherwise do nothing
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * <p>Each array in {@link FileBuffer#linesArrayList} represents a line that is being printed in
     * the render. The class fields {@link FileBuffer#insertionPointLine} represents on which line
     * we are with the cursor, and {@link FileBuffer#insertionPointCol} the position in the list.</p>
     * <p>The {@link FileBuffer#insertionPointByteIndex} is not accurate on the {@link FileBuffer#byteContent},
     * because line separators are in that array. With the given parameters we can retrieve the correct
     * value of the {@link FileBuffer#insertionPointByteIndex} </p>
     */
    private int convertLineAndColToIndex(int line, int col) {
        int byteLengthSeparatorLen = FileHolder.lineSeparator.length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + linesArrayList.get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }
}
