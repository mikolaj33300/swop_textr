package files;

import listeners.DeletedCharListener;
import listeners.DeletedInsertionPointListener;
import listeners.EnteredInsertionPointListener;
import listeners.OpenParsedDirectoryListener;
import ui.View;

import java.io.IOException;
import java.util.ArrayList;

public class BufferCursorContext {

    /**
     * Insertion points column & line do not represent printing locations!
     * All will be relative to {link FileBuffer linesArrayList} indices.
     * For statusbar, these will need to be increased with 1
     */
    private int insertionPointCol, insertionPointLine, insertionPointByteIndex;

    private EnteredInsertionPointListener el;
    private DeletedInsertionPointListener dil;
    private DeletedCharListener dcl;
    EditableFileBuffer containedFileBuffer;
    private OpenParsedDirectoryListener openParsedDirectoryListener;

    public BufferCursorContext(String path, byte[] lineSeparator) throws IOException {
        this.containedFileBuffer = new EditableFileBuffer(path, lineSeparator);
        this.insertionPointCol=0;
        this.insertionPointLine=0;
        this.insertionPointByteIndex=0;
        subscribeToEnterInsertionFb();
        subscribeToDeletionCharFb();
        subscribeToDeletionInsertionFb();
        View.write("test2.txt", "\n>> cursor context created");
    }

    private void subscribeToDeletionInsertionFb() {
        dil = new DeletedInsertionPointListener() {
            @Override
            public void handleDeletedInsertionPoint(int deletedLine, int deletedCol) {
                if(deletedLine<insertionPointLine-1){
                    insertionPointLine--;
                } else if(deletedLine == insertionPointLine){
                   /* When the line you are on gets merged with the previous one, your cursor jumps to the end of the new merged line.
                        This has to do with the order in which operations on fb are executed (first contents are changed, then subscribers are
                    notified so the previous contents are lost. This choice is arbitrary and does not affect fulfilling the assignment
                            requirements.*/
                    insertionPointLine--;
                    insertionPointCol = containedFileBuffer.getLines().get(insertionPointLine).size();
                }
            }
        };
        containedFileBuffer.subscribeToDeletionInsertion(dil);
    }

    private void subscribeToDeletionCharFb() {
        dcl = new DeletedCharListener() {
            @Override
            public void handleDeletedChar(int deletedLine, int deletedCol) {
                if(getInsertionPointLine()==deletedLine && getInsertionPointCol()>=deletedCol){
                    insertionPointCol--;
                }
            }
        };
        containedFileBuffer.subscribeToDeletionChar(dcl);
    }

    private void subscribeToEnterInsertionFb() {
        el = new EnteredInsertionPointListener() {
            @Override
            public void handleEnteredInsertionPoint(int enteredLine, int enteredCol) {
                if(enteredLine<insertionPointLine){
                    insertionPointLine++;
                } else if (enteredLine==insertionPointLine && insertionPointCol<enteredCol){
                    insertionPointLine++;
                    insertionPointCol=0;
                }
            }
        };
        containedFileBuffer.subscribeToEnterInsertion(el);
    }

    private BufferCursorContext(EditableFileBuffer fb, int insertionPointCol, int insertionPointLine) {
        this.containedFileBuffer = fb.clone();
        this.insertionPointCol=insertionPointCol;
        this.insertionPointLine=insertionPointLine;
        this.insertionPointByteIndex=convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }

    /**
     * Shallow copy constructor
     * @param bfc
     */
    public BufferCursorContext(BufferCursorContext bfc){
        this.containedFileBuffer = bfc.containedFileBuffer;
        this.insertionPointByteIndex = bfc.insertionPointByteIndex;
        this.insertionPointLine = bfc.insertionPointLine;
        this.insertionPointCol = bfc.insertionPointCol;

        subscribeToEnterInsertionFb();
        subscribeToDeletionCharFb();
        subscribeToDeletionInsertionFb();
    }

    /**
     * Deletes the character before the insertion pt and updates the cursor position
     */
    public void deleteCharacter() {
        int previousLine = insertionPointLine;
        int previousCol = insertionPointCol;
        moveCursorLeft();
        containedFileBuffer.deleteCharacterCmd(previousCol, previousLine);
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
     *//*
    private void moveCursorToFront() {
        if (insertionPointCol > 0){
            insertionPointCol = 0;
        }
        insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
    }*/

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
        int byteLengthSeparatorLen = containedFileBuffer.getLineSeparator().length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + containedFileBuffer.getLines().get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }

    /**
     * close the filebuffer
     */
    public int forcedClose(){
        containedFileBuffer.unsubscribeFromEnterInsertion(el);
        containedFileBuffer.unsubscribeFromDeletionInsertion(dil);
        containedFileBuffer.unsubscribeFromDeletionChar(dcl);
        return containedFileBuffer.close();
    }

    /**
     * save the buffer to disk
     */
    public void save() {
        containedFileBuffer.save();
    }

    /**
     * write b at the cursor position
     * @param b 
     */
    public void write(byte b) {
        containedFileBuffer.writeCmd(b, insertionPointLine, insertionPointCol);
        moveCursorRight();
    }

    /**
     * undo in the file buffer
     */
    public void undo() {
      containedFileBuffer.undo();
    }

    /**
     * redo in the filebuffer
     */
    public void redo() {
      containedFileBuffer.redo();
    }

    /**
     * @return the line of the insertionPoint
     */
    public int getInsertionPointLine() {
        return this.insertionPointLine;
    }

    /**
     * @return the number of lines in our filebuffer
     */
    public ArrayList<ArrayList<Byte>> getLines() {
        return containedFileBuffer.getLines();
    }

    /**
     * @return the column of the insertionPoint
     */
    public int getInsertionPointCol() {
        return this.insertionPointCol;
    }

    /**
     * @return the content of the filebuffer as byte array
     */
    public ArrayList<Byte> getByteContent() {
        return containedFileBuffer.getByteContent();
    }

    /**
     * @return the contained filebuffer
     */
    public EditableFileBuffer getFileBuffer() {
	return containedFileBuffer.clone();
    }

    /**
     * @return if the filebuffer is dirty
     */
    public boolean getDirty() {
        return containedFileBuffer.getDirty();
    }

    /**
     * @return deep clone of this buffercontext
     */
    public BufferCursorContext deepClone() {
        return new BufferCursorContext(this.containedFileBuffer, this.insertionPointCol, this.insertionPointLine);
    }

    /**
     * enter a separator at the insertionPoint
     */
    public void enterSeparator() throws IOException {
        containedFileBuffer.enterInsertionCmd(insertionPointLine, insertionPointCol);
    }

    /**
     * @param line the line to get length of
     * @return the length of line
     */
    public int getLineLength(int line) {
      return containedFileBuffer.getLineLength(line);
    }

    /**
     * Returns the path of the {@link FileHolder}
     * @return the path in string format
     */
    public String getPath() {
	return containedFileBuffer.getPath();
    }

    /**
     * Will attempt to parse the buffers contents (temp?)
     */
    public void parse() {
        this.containedFileBuffer.parse();
    }

    /**
     * Subscribes this method to a listener in {@link inputhandler.FileBufferInputHandler}
     * @param listener the listener that passes a window
     */
    public void subscribe(OpenParsedDirectoryListener listener) {
        View.write("test2.txt", "\n<CursorContext> Received request for subscribing in" + this.hashCode());
        this.openParsedDirectoryListener = listener;
        this.subscribeEditableBuffer();
    }

    /**
     * This will subscribe the {@link EditableFileBuffer} to this class.
     */
    private void subscribeEditableBuffer() {
        View.write("test2.txt", "\n<CursorContext> Subscribing to editable buffer" + this.containedFileBuffer.hashCode());
        this.containedFileBuffer.subscribeEditableBuffer(
                window -> {
                    View.write("test2.txt", "in context");
                    this.openParsedDirectoryListener.openWindow(window);
                }
        );
    }

}
