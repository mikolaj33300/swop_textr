package files;

import controller.TextR;
import layouttree.LayoutLeaf;
import ui.FileBufferView;
import command.Command;

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
     * Undo stack
     */
    private ArrayList<Command> undoStack = new ArrayList<Command>();
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
     */
    public FileBuffer(String path, byte[] lineSeparator) throws IOException {
        this.file = new FileHolder(path, lineSeparator);
        this.byteContent = new ArrayList<Byte>(Arrays.<Byte>asList(wrapEachByteElem(this.file.getContent())));
        this.linesArrayList = FileAnalyserUtil.getContentLines(this.file.getContent(), this.getLineSeparator());
        this.listenersArrayList = new ArrayList<>();
    }

    // Implementation

    /**
     * Inserting a line separator can only be done with bytes.
     */
    public void enterInsertionPoint(int byteArrIndex) throws IOException {
        insert(byteArrIndex, System.lineSeparator().getBytes());
        for (int i = 0; i < listenersArrayList.size(); i++)
            listenersArrayList.get(i).contentsChanged();
    }

    public void undo() {
      if (undoStack.size() > nbUndone)
        undoStack.get(undoStack.size() - ++nbUndone).undo();
    }

    public void redo() {
      if (nbUndone > 0)
        undoStack.get(undoStack.size() - nbUndone--).execute();
    }

    private void execute(Command command) {
      for (; nbUndone > 0; nbUndone--)
        undoStack.remove(undoStack.size() - 1);
      undoStack.add(command);
      command.execute();
    }

    public void deleteCharacterCmd(int insertionPointCol, int insertionPointLine){
      execute(new Command() {
        private int iCol = insertionPointCol;
        private int iLine = insertionPointLine;
        private byte deleted;

        public void execute() {
          deleted = deleteCharacter(iCol, iLine);
        }
        public void undo() {
          insert(convertLineAndColToIndex(iCol, iLine), deleted);
        }
      });
    }

    /**
     * Deletes the character before the insertion pt and updates the cursor position, given coords of cursor
     * when character is to be deleted.
     */
    public byte deleteCharacter(int insertionPointCol, int insertionPointLine) {
        int insertionPointByteIndex = convertLineAndColToIndex(insertionPointLine, insertionPointCol);
        byte res = 0;

        if(insertionPointCol > 0 || insertionPointLine > 0) {
            this.dirty = true;
        }
        if(insertionPointCol > 0) {
            res = this.byteContent.remove(insertionPointByteIndex-1);
        } else {
            if(insertionPointLine!=0){
                //shift left the amount of bytes that need to be deleted and delete them one by one
                for(int i = 0; i< file.getLineSeparator().length ; i++) {
                    this.byteContent.remove(insertionPointByteIndex-getLineSeparator().length);
                }
            }
        }
        ArrayList<Byte> tmp = new ArrayList<Byte>(this.byteContent);
        linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) tmp), this.getLineSeparator());
        return res;
    }

    public byte deleteCharacterWithIndex(int insertionPointByteIndex) {
        byte res = 0;

        if(insertionPointCol > 0 || insertionPointLine > 0) {
            this.dirty = true;
        }
        if(insertionPointCol > 0) {
            res = this.byteContent.remove(insertionPointByteIndex-1);
        } else {
            if(insertionPointLine!=0){
                //shift left the amount of bytes that need to be deleted and delete them one by one
                for(int i = 0; i< FileHolder.lineSeparator.length ; i++) {
                    res = this.byteContent.remove(insertionPointByteIndex);
                }
            }
        }
        linesArrayList = FileAnalyserUtil.getContentLines(toArray((ArrayList<Byte>) this.byteContent.clone()));
        return res;
    }

    public void writeCmd(byte updatedContents, int byteArrIndex) {
      private byte uC = updatedContents;
      private int bArrIndex = byteArrIndex;

      void execute() { write(updatedContents, byteArrIndex); }
      void undo() { deleteCharacterWithIndex(byteArrIndex); }

    }

    /**
     * Updates the content of the FileBuffer
     */
    public void write(byte updatedContents, int byteArrIndex) {
        insert(byteArrIndex, updatedContents);
        dirty = true;
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
     * Returns a copy of the byteConent of this FileBuffer
     * TODO: unchecked cast
     */
    public ArrayList<Byte> getByteContent(){
      ArrayList<Byte> res = new ArrayList<Byte>(this.byteContent);
        return res;
    }

    /**
     * Returns an array of byte arrays. Each array represents an array of bytes which is separated by
     * another array by a line separator specified.
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
    public byte[] getBytes() {
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
    void deleteLine(int insertionPointLine) {
        linesArrayList.remove(insertionPointLine);
        this.linesArrayList = FileAnalyserUtil.getContentLines(FileAnalyserUtil.toArray(this.byteContent), this.getLineSeparator());
        // TODO column verplaatsen wanneer verwijderde lijn meer columns had dan de vorige
    }

    // Private implementations

    /**
     * Inserts the byte values.
     */
    private void insert(int byteArrayIndex, byte... data) {
        byteContent.addAll(byteArrayIndex,
                Arrays.<Byte>asList(wrapEachByteElem(data)));

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
        if(dirty) {
            return 1;
        } else {
            return 0;
        }
    }

    private int convertLineAndColToIndex(int line, int col) {
        int byteLengthSeparatorLen = file.getLineSeparator().length;
        int byteArrIndex = 0;
        for (int i = 0; i < line; i++) {
            byteArrIndex = byteArrIndex + linesArrayList.get(i).size() + byteLengthSeparatorLen;
        }
        byteArrIndex = byteArrIndex + col;
        return byteArrIndex;
    }

    public byte[] getLineSeparator() {
        return file.getLineSeparator();
    }

    public String getPath() {
        return file.getPath();
    }
}
