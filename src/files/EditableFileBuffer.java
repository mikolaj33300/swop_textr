package files;

import directory.directorytree.FileSystemEntry;
import listeners.DisplayRequestForFileEntryListener;
import util.FileAnalyserUtil;
import util.json.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class acts as a state object depending if the caller has parsed this object.
 */
public class EditableFileBuffer extends FileBuffer {

    /**
     * The amount of subfiles that are opened
     */
    private int openedSubFiles = 0;

    /**
     * Determines if this object has been parsed
     */
    private boolean parsed = false;

    /**
     * List of listeners for directory requests
     */
    private ArrayList<DisplayRequestForFileEntryListener> directoryRequestListeners = new ArrayList<>();

    /**
     * List of listeners for file buffers
     */
    private ArrayList<DisplayRequestForFileBufferListener> bufferRequestListeners;

    /**
     * List of listeners for directories
     */
    private ArrayList<OpenFileOnPathRequestListener> listenersToDirectories = new ArrayList<>();

    /**
     * List of listeners that are to be unsubscribed when this object is closed
     */
    private ArrayList<Runnable> closingListeners = new ArrayList<>();

    /**
     * Creates FileBuffer object from the given path and line separator
     *
     * @param path          the path of the file to be opened
     * @param lineSeparator the separator we use
     */
    public EditableFileBuffer(String path, byte[] lineSeparator) throws IOException {
        super(path, lineSeparator);
        this.bufferRequestListeners = new ArrayList<>(0);
    }

    /**
     * Creates FileBuffer object from a given FileHolder;*
     * @param fh the file holder
     */
    public EditableFileBuffer(FileHolder fh) throws IOException {
        super(fh);
        this.bufferRequestListeners = new ArrayList<>(0);
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
    public FileSystemEntry parseAsJSON() {
        OpenFileOnPathRequestListener listenerToNewDirectory = new OpenFileOnPathRequestListener() {
            @Override
            public void notifyRequestToOpenFile(String pathToOpen) {
                JsonFileHolder newHolder = createFileHolderFromJSONPathOnThis(pathToOpen);
                EditableFileBuffer newBuffer;
                try {
                    newBuffer = new EditableFileBuffer(newHolder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                requestDisplayingNewFileBuffer(newBuffer);
            }
        };
        listenersToDirectories.add(listenerToNewDirectory);
        Runnable newCloseListener = new Runnable() {
            @Override
            public void run() {
                listenersToDirectories.remove(listenerToNewDirectory);
                closingListeners.remove(this);
            }
        };
        closingListeners.add(newCloseListener);


        FileSystemEntry toOpenEntry = JsonUtil.parseDirectory(this, listenerToNewDirectory, newCloseListener);
        if(toOpenEntry != null) {
            this.parsed = true;
            //this.directoryRequestListeners.get(0).notifyRequestToOpen(toOpenEntry); //So that only one is opened.
        } else {
            //TODO: Unsubscribe here, listener has no meaning
            //TODO: Handle returning error location, maybe in a Pair<Location, FileSystemEntry>?
        }
        return toOpenEntry;
    }


    /**
     * Notifies this object that it has been parsed
     */
    private void requestDisplayingNewFileBuffer(EditableFileBuffer newBuffer) {
        this.bufferRequestListeners.get(0).notifyRequestOpening(newBuffer);
    }

    /**
     * Creates a new JsonFileHolder object from a path on this object
     * @param pathToOpen the path to open
     * @return a new JsonFileHolder object
     */
    private JsonFileHolder createFileHolderFromJSONPathOnThis(String pathToOpen) {
        return new JsonFileHolder(this, pathToOpen);
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
            copy = new EditableFileBuffer(this.getFileHolder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        copy.dirty = this.dirty;
        copy.byteContent = this.cloneByteArrList();
        copy.setLinesArrayList(FileAnalyserUtil.getContentLines(this.getBytes(), this.getLineSeparator()));
        copy.parsed = this.parsed;
        copy.openedSubFiles = this.openedSubFiles;
        return copy;
    }

    /**
     * Subscribes this EditableFileBuffer to a request for opening a directory
     * @param listener the listener to be subscribed
     */
    public void subscribeToDirectoryOpenRequests(DisplayRequestForFileEntryListener listener) {
        this.directoryRequestListeners.add(listener);
    }

    /**
     * Subscribes this EditableFileBuffer to a request for opening a file buffer
     * @param listener the listener to be subscribed
     */
    public void subscribeToFileBufferOpenRequests(DisplayRequestForFileBufferListener listener) {
        this.bufferRequestListeners.add(listener);
    }

    /**
     * Subscribes this EditableFileBuffer to a closing event
     * @param closingListener the listener to be subscribed
     */
    public void subscribeToCloseEvents(Runnable closingListener){
        this.closingListeners.add(closingListener);
    }

}
