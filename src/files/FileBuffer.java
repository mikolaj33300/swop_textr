package files;

import controller.TextR;
import ui.FileBufferView;


import java.io.IOException;
import java.util.*;

import static files.FileAnalyserUtil.wrapEachByteElem;

public class FileBuffer {

    /**
     * File reference
     */
    private FileHolder file;

    private ArrayList<FileBufferContentChangedListener> listenersArrayList;

    /**
     * Determines if buffer has been modified
     */
    private boolean dirty = false;

    /**
     * Holds the 'in memory' byte data of the file.
     * The amount of bytes equals the amount of characters in ASCII
     */
    private ArrayList<Byte> byteContent;

    /**
     * An array of 'lines' of bytes. Each array in the array is an array separated by a
     * line separator from the other array.
     * Processed version of {@link FileBuffer#byteContent}
     */
    private ArrayList<ArrayList<Byte>> linesArrayList;

    /*
     * a List of listeners that need to be notified for our rendering
     */
    private ArrayList<FileBufferListener> listeners;

    /**
     * Insertion points column & line do not represent printing locations!
     * All will be relative to {@link FileBuffer#linesArrayList} indices.
     * For statusbar, these will need to be increased with 1
     */
    private int insertionPointCol, insertionPointLine, insertionPointByteIndex;

    /**
     * Creates FileBuffer object with given path;
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     */
    public FileBuffer(String path, FileBufferListener listener) throws IOException {
      this.listeners = new ArrayList<FileBufferListener>();
      this.listeners.add(listener);
        this.file = new FileHolder(path);
        this.byteContent = new ArrayList<Byte>(Arrays.<Byte>asList(wrapEachByteElem(this.file.getContent())));
        this.linesArrayList = FileAnalyserUtil.getContentLines(this.file.getContent());
    }

    // Implementation

    /**
     * Inserting a line separator can only be done with bytes.
     */
    public void enterInsertionPoint() throws IOException {
        insert(System.lineSeparator().getBytes());
        moveCursorDown();
        moveCursorToFront();
        for (int i = 0; i < listeners.size(); i++)
          listeners.get(i).render(this, 0, true);
    }

    /**
     * Updates the content of the FileBuffer
     * is this used at all?
     */
    public void write(byte updatedContents) throws IOException {
        insert(updatedContents);
        dirty = true;
        moveCursorRight();
        for (int i = 0; i < listeners.size(); i++)
          listeners.get(i).render(this, 0, true);
    }

    /**
     * Saves the buffer contents to disk
     */
    public final int save() {
        if (!dirty) return 0;
        int result = this.file.save(FileAnalyserUtil.toArray(this.byteContent));
        if(result == 0){
            this.dirty = false;
            return 0;
        } else {
            return result;
        }

    }

    /**
     * Moves the cursor in a given direction.
     * 'C': right
     * 'D': left
     * 'A': up
     * 'B': down
    public void moveCursor(char direction) {
        switch (direction) {
            // Right
            case 'C':
                moveCursorRight();
                break;
            // Left
            case 'D':
                moveCursorLeft();
                break;
            // Up
            case 'A':
                moveCursorUp();
                break;
            // Down
            case 'B':
                moveCursorDown();
                break;
        }
    }*/

    /**
     * Returns the Y coordinate where the insertion point should be rendered.
     */
    public int getInsertionPointLine() {
        return insertionPointLine;
    }

    /**
     * Returns the X coordinate where the insertion point should be rendered.
     */
    public int getInsertionPointCol() {
        return insertionPointCol;
    }

    /**
     * Returns a copy of the byteConent of this FileBuffer
     * TODO: unchecked cast
     */
    public ArrayList<Byte> getByteContent(){
        return (ArrayList<Byte>) this.byteContent.clone();
    }

    /**
     * Returns an array of byte arrays. Each array represents an array of bytes which is separated by
     * another array by a line separator specified in {@link TextR#getLineSeparatorArg()}.
     */
    public ArrayList<ArrayList<Byte>> getLines() {
        ArrayList<ArrayList<Byte>> clonedLinesList = new ArrayList<ArrayList<Byte>>();
        for(int i = 0; i<linesArrayList.size(); i++){
            ArrayList<Byte> clonedLine = new ArrayList<Byte>();
            for(int j = 0; j<linesArrayList.get(i).size(); j++){
                clonedLine.add(linesArrayList.get(i).get(j));
            }
            clonedLinesList.add(clonedLine);
        }
        return clonedLinesList;
    }



    // Default methods

    /**
     * Returns the FileHolder object file (for testing purposes)
     */
    public FileHolder getFileHolder() {
        return this.file.clone();
    }

    /**
     * Returns copy of this buffers' content.
     */
    byte[] getBytes() {
        return FileAnalyserUtil.toArray(byteContent);
    }

    /**
     *  Returns the amount of chars in the buffercontent
     */
    public int getAmountChars(){
        return this.byteContent.size();
    }

    /**
     * Determines if a given byte[] is the same as this buffer's {@link FileBuffer#byteContent}
     */
    boolean contentsEqual(ArrayList<Byte> compare) {
        if (compare.size() != byteContent.size()) return false;
        for (int i = 0; i < compare.size(); i++)
            if (compare.get(i) != byteContent.get(i).byteValue()) return false;
        return true;
    }

    /**
     * Clones the byte array
     */
    ArrayList<Byte> cloneByteArrList() {
        ArrayList<Byte> copy = new ArrayList<>(byteContent);
        return copy;
    }

    /**
     * Returns the insertion point.
     */
    public int getInsertionPoint() {
        return insertionPointByteIndex;
    }

    /**
     * Puts all elements from {@link FileBuffer#byteContent} in a byte[]
     */
    byte[] toArray(ArrayList<Byte> arrList) {
        byte[] resultArray = new byte[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            resultArray[i] = arrList.get(i).byteValue();
        }
        return resultArray;
    }

    /**
     * Determines if the buffer has been modified.
     */
    public boolean getDirty() {
        return this.dirty;
    }

    // Private implementations

    /**
     * Inserts the byte values.
     */
    private void insert(byte... data) {
        byteContent.addAll(convertLineAndColToIndex(this.insertionPointLine, this.insertionPointCol),
                Arrays.<Byte>asList(wrapEachByteElem(data)));

        linesArrayList = FileAnalyserUtil.getContentLines(this.getBytes());
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

    /**
     * Makes the calculation to move the cursor down. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    public void moveCursorDown() throws IOException {
        if (insertionPointLine < linesArrayList.size() - 1) {
            insertionPointLine++;
            insertionPointCol = Math.min(linesArrayList.get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        for (int i = 0; i < listeners.size(); i++)
          listeners.get(i).render(this, 0, true);
    }

    /**
     * Makes the calculation to move the cursor up. Modifies the {@link FileBuffer#insertionPointCol} and {@link FileBuffer#insertionPointLine} accordingly.
     */
    public void moveCursorUp() {
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
    public void moveCursorLeft() {
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
    public void moveCursorRight(){
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

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = null;
        try {
            copy = new FileBuffer(this.file.getPath(), this.listeners.get(0));// TODO pass all listeners
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        copy.dirty = this.dirty;
        copy.byteContent = this.cloneByteArrList();
        return copy;
    }

    /**
     * Checks if the given {@link FileBuffer} references the same {@link FileHolder}
     * and temporarily, if the content, and dirty boolean match.
     */
    public boolean equals(FileBuffer buffer) {
        return this.dirty == buffer.dirty && this.contentsEqual(buffer.byteContent) && this.file.getPath().equals(buffer.file.getPath());
    }

    /**
     * Warns the user if the {@link FileBuffer#dirty} is set to true and prompts the user to save the
     * {@link FileBuffer}. When done, it will remove the {@link FileBufferView} linked to this object.
     *
     * @return
     */
    public int close() {
        if(dirty) {
            return 1;
        } else {
            return 0;
        }
    }
}
