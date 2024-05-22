package files;

import ioadapter.RealTermiosTerminalAdapter;
import listeners.OpenParsedDirectoryListener;
import ui.View;
import util.json.JsonUtil;
import window.DirectoryWindow;

import java.io.IOException;

/**
 * This class acts as a state object depending if the caller has parsed this object.
 */
public class EditableFileBuffer extends FileBuffer {

    private int openedSubFiles = 0;
    private boolean parsed = false;
    private OpenParsedDirectoryListener listener;

    /**
     * Creates FileBuffer object with given path;
     * Initializes {@link FileHolder} object and retrieves its {@link FileHolder#getContent()}
     *
     * @param path          the path of the file to be opened
     * @param lineSeparator the separator we use
     */
    public EditableFileBuffer(String path, byte[] lineSeparator) throws IOException {
        super(path, lineSeparator);
        View.write("test2.txt", ">> buffer created " + this.hashCode());
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
    public boolean parse() {
        if(JsonUtil.parseDirectory(this) != null) {
            this.parsed = true;
            View.write("test2.txt", "\nparse correct" + (this.hashCode()));
            this.listener.openWindow(new DirectoryWindow(JsonUtil.parseDirectory(this), new RealTermiosTerminalAdapter()));
        }
        View.write("test2.txt", "parsing failed at location" + JsonUtil.getErrorLocation(new String(this.getBytes())));
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

    public void subscribeEditableBuffer(OpenParsedDirectoryListener listener) {
        View.write("test2.txt", "\n<Buffer> Subscribed: " + this.hashCode());
        this.listener = listener;
    }

}
