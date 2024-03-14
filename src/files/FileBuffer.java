package files;

import core.Controller;
import io.github.btj.termios.Terminal;


import java.util.*;

import static files.FileAnalyserUtil.toArray;
import static files.FileAnalyserUtil.wrapEachByteElem;

public class FileBuffer {

    /**
     * File reference
     */
    private FileHolder file;

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
    public FileBuffer(String path) {
        this.file = new FileHolder(path);
        this.byteContent = new ArrayList<Byte>(Arrays.<Byte>asList(wrapEachByteElem(this.file.getContent())));
        this.linesArrayList = FileAnalyserUtil.getContentLines(this.file.getContent());
        this.insertionPointCol = 0;
        this.insertionPointLine = 0;
        this.insertionPointByteIndex = 0;
    }

    // Implementation

    /**
     * Inserting a line separator can only be done with bytes.
     */
    public void enterInsertionPoint() {
        insert(System.lineSeparator().getBytes());
        moveCursorDown();
        moveCursorToFront();
    }

    /**
     * Updates the content of the FileBuffer
     */
    public void write(byte updatedContents) {
        insert(updatedContents);
        dirty = true;
        moveCursorRight();
    }

    /**
     * Saves the buffer contents to disk
     */
    public final void save() {
        if (!dirty) return;
        this.file.save(FileAnalyserUtil.toArray(this.byteContent));
        this.dirty = false;
    }

    /**
     * Moves the cursor in a given direction.
     * 'C': right
     * 'D': left
     * 'A': up
     * 'B': down
     */
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
    }

    /**
     * Returns a string with relevant information for the statusbar
     */
    public String getRender() {
        String statusLine = this.getFileHolder().getPath();
        statusLine += " #Lines:";
        statusLine += String.valueOf(this.getLines().size());
        statusLine += " #Chars:";
        String contents = new String(this.getFileHolder().getContent());
        statusLine += contents.length();
        statusLine += " Insert:[";
        statusLine += this.getInsertionPointLine();
        statusLine += ";";
        statusLine += this.getInsertionPointCol();
        statusLine += "] ";
        if(this.getDirty())
            statusLine += "Dirty";
        else
            statusLine += "Clean";
        statusLine += " ";
        return statusLine;
    }

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
     * Returns an array of byte arrays. Each array represents an array of bytes which is separated by
     * another array by a line separator specified in {@link Controller#getLineSeparatorArg()}.
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

    /**
     * Deletes the full array
     */
    void deleteLine() {
        linesArrayList.remove(insertionPointLine);
        this.linesArrayList = FileAnalyserUtil.getContentLines(FileAnalyserUtil.toArray(this.byteContent));
        // TODO column verplaatsen wanneer verwijderde lijn meer columns had dan de vorige
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
        int byteLengthSeparatorLen = Controller.getLineSeparatorArg().length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + linesArrayList.get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }

    private void moveCursorDown() {
        if (insertionPointLine < linesArrayList.size() - 1) {
            insertionPointLine++;
            insertionPointCol = Math.min(linesArrayList.get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    private void moveCursorUp() {
        if (insertionPointLine > 0) {
            insertionPointLine--;
            //shift left if the current line is longer than the previous
            insertionPointCol = Math.min(linesArrayList.get(insertionPointLine).size(), insertionPointCol);
            insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

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

    private void moveCursorToFront() {
        if (insertionPointCol > 0){
            insertionPointCol = 0;
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

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

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = new FileBuffer(this.file.getPath());
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

    public void deleteCharacter() {
        if(insertionPointCol > 0) {
            this.byteContent.remove(insertionPointByteIndex-1);
            moveCursorLeft();
        } else {
            if(insertionPointLine!=0){
                //shift left the amount of bytes that need to be deleted and delete them one by one
                moveCursorLeft();
                for(int i = 0; i< Controller.getLineSeparatorArg().length; i++) {
                    this.byteContent.remove(insertionPointByteIndex);
                }
            }
        }
        linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) this.byteContent.clone()));
    }

    public void close() {

    }
}
