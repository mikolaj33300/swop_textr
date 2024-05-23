package files;

import directory.directorytree.FileSystemEntry;
import listeners.DisplayRequestForFileEntryListener;
import util.json.JsonUtil;

import java.io.IOException;

/**
 * This class acts as a state object depending if the caller has parsed this object.
 */
public class EditableFileBuffer extends FileBuffer {

    private int openedSubFiles = 0;
    private boolean parsed = false;
    private DisplayRequestForFileEntryListener listener;

    /**
     * Creates FileBuffer object with given path;
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     *
     * @param path          the path of the file to be opened
     * @param lineSeparator the separator we use
     */
    public EditableFileBuffer(String path, byte[] lineSeparator) throws IOException {
        super(path, lineSeparator);
    }

    /**
     * Determines what state this object is in. If it is parsed
     * @return a boolean specifying the state of this buffer.
     */
    public boolean isParsed() {
        return this.parsed;
    }

    /**
     * Notifies this object that it has been parsed
     */
    public boolean parseAsJSON() {
        FileSystemEntry toOpenEntry = JsonUtil.parseDirectory(this, new OpenFileOnPathRequestListener() {
            @Override
            public void notifyRequestToOpenFile(String pathToOpen) {
                //Create the holder here
            }
        });
        if(toOpenEntry != null) {
            this.parsed = true;
            this.listener.notifyRequestToOpen(toOpenEntry);
        }
        return false;
    }

    /**
     * Notifies this object that text in this buffer is being edited from another place.
     */
    public void openFile() {
        if(!isParsed()) return;
        openedSubFiles++;
    }

    /**
     * Notifies this object that an editor has finished writing to this file.
     */
    public void closeFile() {
        if(!isParsed()) return;
        openedSubFiles = openedSubFiles - 1 == 0 ? 0 : openedSubFiles--;
    }

    /**
     * Allows the caller to write to this buffer incase there are no open files
     * @param updatedContents the contents to be written
     * @param insertionPointLine the insertion line
     * @param insertionPointCol the insertion column
     */
    @Override
    public void writeCmd(byte updatedContents, int insertionPointLine, int insertionPointCol) {
        if(openedSubFiles == 0)
            super.writeCmd(updatedContents, insertionPointLine, insertionPointCol);
    }

    /**
     * Clones this object
     */
    @Override
    public EditableFileBuffer clone() {
        EditableFileBuffer copy = null;
        try {
            copy = new EditableFileBuffer(this.getFileHolder().getPath(), getFileHolder().getLineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        copy.dirty = this.dirty;
        copy.byteContent = this.cloneByteArrList();
        copy.parsed = this.parsed;
        copy.openedSubFiles = this.openedSubFiles;
        return copy;
    }

    public void subscribeEditableBuffer(DisplayRequestForFileEntryListener listener) {
        this.listener = listener;
    }

}
