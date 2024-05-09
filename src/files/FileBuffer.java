package files;

import listeners.DeletedCharListener;
import listeners.DeletedInsertionPointListener;
import listeners.EnteredInsertionPointListener;
import ui.FileBufferView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static files.FileAnalyserUtil.toPrimitive;

public class FileBuffer {

    /**
     * File reference
     */
    private FileHolder file;

    /**
     * array of listeners for rendering
     */
    private ArrayList<EnteredInsertionPointListener> enteredInsertionPointListeners;
    private ArrayList<DeletedInsertionPointListener> deletedInsertionPointListeners;

    private ArrayList<DeletedCharListener> deletedCharListeners;

    /**
     * Undo stack
     */
    private ArrayList<Command> undoStack = new ArrayList<Command>();

    /**
     * the amount of commands undone
     * the end of the command stack - this is the command to be redone
     */
    private int nbUndone;

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
     * Creates FileBuffer object with given path;
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     *
     * @param path          the path of the file to be opened
     * @param lineSeparator the separator we use
     */
    public FileBuffer(String path, byte[] lineSeparator) throws IOException {
        this.file = new FileHolder(path, lineSeparator);
        this.byteContent = new ArrayList<Byte>(Arrays.<Byte>asList(toPrimitive(this.file.getContent())));
        this.linesArrayList = FileAnalyserUtil.getContentLines(this.file.getContent(), this.getLineSeparator());
        this.deletedInsertionPointListeners = new ArrayList<DeletedInsertionPointListener>();
        this.enteredInsertionPointListeners = new ArrayList<EnteredInsertionPointListener>();
        this.deletedCharListeners = new ArrayList<DeletedCharListener>();
    }

    // Implementation

    public void enterInsertionCmd(int iLine, int iCol){
        execute(new BufferEnterInsertionCommand(iCol, iLine, this));
    }

    /**
     * Inserting a line separator can only be done with bytes.
     *
     * @param iLine the line where the enter character needs to go
     * @param iCol the column where the enter character needs to go
     */
    protected void enterInsertionPoint(int iLine, int iCol) {
        int byteArrIndex = convertLineAndColToIndex(iLine, iCol);
        insert(byteArrIndex, getLineSeparator());

        //TODO: Make safer according to PDF on toledo
        for (int i = 0; i < enteredInsertionPointListeners.size(); i++){
            enteredInsertionPointListeners.get(i).handleEnteredInsertionPoint(iLine, iCol);
        }
    }

    /**
     * undo the command nbUndone points to
     */
    public void undo() {
        if (undoStack.size() > nbUndone)
            undoStack.get(undoStack.size() - ++nbUndone).undo();
    }

    /**
     * redo the command nbUndone points to
     */
    public void redo() {
        if (nbUndone > 0)
            undoStack.get(undoStack.size() - nbUndone--).execute();
    }

    /**
     * add a command to the stack and execute it
     * clears the command stack from nbUndone
     *
     * @param command the command to add and execute
     * @return void
     */
    private void execute(Command command) {
        for (; nbUndone > 0; nbUndone--)
            undoStack.remove(undoStack.size() - 1);
        undoStack.add(command);
        command.execute();
    }

    public void subscribeToDeletionChar(DeletedCharListener deletedCharListener){
        deletedCharListeners.add(deletedCharListener);
    }

    public void subscribeToEnterInsertion(EnteredInsertionPointListener enteredInsertionPointListener){
        enteredInsertionPointListeners.add(enteredInsertionPointListener);
    }

    public void subscribeToDeletionInsertion(DeletedInsertionPointListener deletedInsertionPointListener){
        deletedInsertionPointListeners.add(deletedInsertionPointListener);
    }

    public void unsubscribeFromDeletionChar(DeletedCharListener d){
        deletedCharListeners.remove(d);
    }

    public void unsubscribeFromEnterInsertion(EnteredInsertionPointListener e){
        enteredInsertionPointListeners.remove(e);
    }

    public void unsubscribeFromDeletionInsertion(DeletedInsertionPointListener d){
        deletedInsertionPointListeners.remove(d);
    }

    /**
     * delete the character at the column and row and pushes it undo stack
     *
     * @param insertionPointCol  the column of the deleted character
     * @param insertionPointLine the row of the deleted character
     */
    public void deleteCharacterCmd(int insertionPointCol, int insertionPointLine) {
        execute(new BufferDeleteCharacterCommand(insertionPointCol, insertionPointLine, this));
    }

    /**
     * delete the character at the column and row
     *
     * @param insertionPointCol  the column of the deleted character
     * @param insertionPointLine the row of the deleted character
     */
    protected void deleteCharacter(int insertionPointCol, int insertionPointLine) {
        int insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);

        if (insertionPointCol > 0 || insertionPointLine > 0) {
            this.dirty = true;
        }
        if (insertionPointCol > 0) {
            this.byteContent.remove(insertionPointByteIndex - 1);
            ArrayList<Byte> tmp = new ArrayList<Byte>(this.byteContent);
            linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) tmp), this.getLineSeparator());
            //TODO: Make safer according to PDF on toledo
            for (int i = 0; i < deletedCharListeners.size(); i++){
                deletedCharListeners.get(i).handleDeletedChar(insertionPointLine, insertionPointCol);
            }
        } else {
            if (insertionPointLine != 0) {
                //shift left the amount of bytes that need to be deleted and delete them one by one
                for (int i = 0; i < file.getLineSeparator().length; i++) {
                    this.byteContent.remove(insertionPointByteIndex - getLineSeparator().length);
                    ArrayList<Byte> tmp = new ArrayList<Byte>(this.byteContent);
                    linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) tmp), this.getLineSeparator());
                }
                //TODO: Make safer according to PDF on toledo
                for (int i = 0; i < deletedInsertionPointListeners.size(); i++){
                    deletedInsertionPointListeners.get(i).handleDeletedInsertionPoint(insertionPointLine, insertionPointCol);
                }

            }
        }
    }

    public void writeCmd(byte updatedContents, int insertionPointLine, int insertionPointCol) {
        execute(new BufferWriteCommand(updatedContents, insertionPointCol, insertionPointLine, this));

    }

    /**
     * Updates the content of the FileBuffer
     *
     * @param updatedContents the byte to insert
     * @param insertionPointLine    the line of that byte
     * @param insertionPointCol    the line of that byte
     */
    protected void write(byte updatedContents, int insertionPointLine, int insertionPointCol) {
        insert(convertLineAndColToIndex(insertionPointLine, insertionPointCol), updatedContents);
        dirty = true;
    }

    /**
     * Saves the buffer contents to disk
     *
     * @return if the save was successful
     */
    public final int save() {
        if (!dirty) return 0;
        int result = this.file.save(FileAnalyserUtil.toArray(this.byteContent));
        if (result == 0) {
            this.dirty = false;
            return 0;
        } else {
            return result;
        }

    }

    /**
     * @return a copy of the byteContent of this FileBuffer
     */
    public ArrayList<Byte> getByteContent() {
        ArrayList<Byte> res = new ArrayList<Byte>(this.byteContent);
        return res;
    }

    /**
     * @return an array of byte arrays. Each array represents an array of bytes which is separated by
     * another array by a line separator specified.
     */
    public ArrayList<ArrayList<Byte>> getLines() {
        ArrayList<ArrayList<Byte>> clonedLinesList = new ArrayList<ArrayList<Byte>>();
        for (int i = 0; i < linesArrayList.size(); i++) {
            ArrayList<Byte> clonedLine = new ArrayList<Byte>();
            for (int j = 0; j < linesArrayList.get(i).size(); j++) {
                clonedLine.add(linesArrayList.get(i).get(j));
            }
            clonedLinesList.add(clonedLine);
        }
        return clonedLinesList;
    }


    // Default methods

    /**
     * @return the FileHolder object file (for testing purposes)
     */
    public FileHolder getFileHolder() {
        return this.file.clone();
    }

    /**
     * @return copy of this buffers' content.
     */
    public byte[] getBytes() {
        return FileAnalyserUtil.toArray(byteContent);
    }

    /**
     * @return the amount of chars in the buffercontent
     */
    public int getAmountChars() {
        return this.byteContent.size();
    }

    /**
     * Determines if a given byte[] is the same as this buffer's {@link FileBuffer#byteContent}
     *
     * @param compare the byte list to compare to
     * @return if this byte list is equal to the compare list
     */
    boolean contentsEqual(ArrayList<Byte> compare) {
        if (compare.size() != byteContent.size()) return false;
        for (int i = 0; i < compare.size(); i++)
            if (compare.get(i) != byteContent.get(i).byteValue()) return false;
        return true;
    }

    /**
     * @return Clone of the byte array
     */
    ArrayList<Byte> cloneByteArrList() {
        ArrayList<Byte> copy = new ArrayList<>(byteContent);
        return copy;
    }

    /**
     * Puts all elements from {@link FileBuffer#byteContent} in a byte[]
     *
     * @param arrList the ArrayList<Byte> to convert
     * @return array of our ArrayList<Byte>
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
     *
     * @return if this FileBuffer is dirty
     */
    public boolean getDirty() {
        return this.dirty;
    }

    // Private implementations

    /**
     * Inserts the byte values.
     */
    private void insert(int byteArrayIndex, byte... data) {
        byteContent.addAll(byteArrayIndex,
                Arrays.<Byte>asList(toPrimitive(data)));

        linesArrayList = FileAnalyserUtil.getContentLines(this.getBytes(), this.getLineSeparator());
    }

    // Base methods

    /**
     * Clones this object
     */
    public FileBuffer clone() {
        FileBuffer copy = null;
        try {
            copy = new FileBuffer(this.file.getPath(), file.getLineSeparator());
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
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileBuffer buffer) {
            return this.dirty == buffer.dirty && this.contentsEqual(buffer.byteContent) && this.file.getPath().equals(buffer.file.getPath());
        } else {
            return false;
        }
    }

    /**
     * Warns the user if the {@link FileBuffer#dirty} is set to true and prompts the user to save the
     * {@link FileBuffer}. When done, it will remove the {@link FileBufferView} linked to this object.
     *
     * @return
     */
    public int close() {
        if (dirty) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * converts line and column to position in an array with newlines
     * @param line the line
     * @param col column
     * @return the index in an array
     */
    public int convertLineAndColToIndex(int line, int col) {
        int byteLengthSeparatorLen = file.getLineSeparator().length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + linesArrayList.get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }

    /**
     * @param line the line to get length of
     * @return the length of line
     */
    public int getLineLength(int line) {
      return linesArrayList.get(line).size();
    }

    /**
     * @return lineseparator
     */
    public byte[] getLineSeparator() {
        return file.getLineSeparator();
    }

    /**
     * @return the path of the file 
     */
    public String getPath() {
        return file.getPath();
    }

    /**
     * Sets the buffer content (gebruikt door {@link JsonFileHolder})
     */
    void setLinesArrayList(ArrayList<ArrayList<Byte>> bytes) {
        this.linesArrayList = bytes;
    }

}
