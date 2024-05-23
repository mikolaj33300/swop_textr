package window;

import ioadapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import inputhandler.InputHandlingElement;
import listeners.OpenWindowRequestListener;
import ui.FileBufferView;
import ui.ScrollbarDecorator;
import ui.View;

import java.io.IOException;

/**
 * Tasks of this class to implement specific methods from superclass:
 *  - duplicate: accepts a {@link WindowVisitor}
 *  - returning the inputhandler, defined in the constructor
 *  - returning the view, defined in the constructor
 * (These things used to be handled by the layer above, now done by this object, hence we need the abstract methods)
 */
public class FileBufferWindow extends Window {

    /**
     * The input handler of this window, handles the input of the fileBufferWindow and links to the internal workings of the fileBufferWindow
     */
    private FileBufferInputHandler fileBufferInputHandler;

    /**
     * The view of this window, visual representation of the fileBufferWindow
     */
    private View view;
    /**
     * The listener that can open files
     */
    private OpenWindowRequestListener listener;

    /**
     * Constructor of this FileBufferWindow
     * Creates a new handler and a new view based on the given path, lineSeparatorArg and terminal adapter
     * @param path the path of the file
     * @param lineSeparatorArg the line separator of the file
     * @param adapter the terminal adapter of the fileBufferWindo
     */
    public FileBufferWindow(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws PathNotFoundException, IOException {
        super();
        FileBufferInputHandler openedFileHandler;
        openedFileHandler = new FileBufferInputHandler(path, lineSeparatorArg);

        this.view = (new ScrollbarDecorator(new FileBufferView(openedFileHandler.getFileBufferContextTransparent(), adapter)));
        this.fileBufferInputHandler = (openedFileHandler);
    }

    /**
     * Constructor of this FileBufferWindow
     * Initializes this FileBufferWindow with the given FileBufferView and FileBufferInputHandler
     * No new objects will be created
     * @param newView the view of the fileBufferWindow
     * @param fileBufferInputHandler the handler of the fileBufferWindow
     */
    public FileBufferWindow(FileBufferView newView, FileBufferInputHandler fileBufferInputHandler) {
        this.view = newView;
        this.fileBufferInputHandler = fileBufferInputHandler;

    }


    /**
     * Returns the view of the FileBufferWindow, a FileBufferView
     * @return the view of the FileBufferWindow, a FileBufferView
     */
    @Override
    public View getView() {
        return this.view;
    }

    /**
     * Returns the handler of the FileBufferWindow, a FileBufferInputHandler
     * @return the handler of the FileBufferWindow, a FileBufferInputHandler
     */
    @Override
    public InputHandlingElement getHandler() {
        return this.fileBufferInputHandler;
    }

    /**
     * Duplicates this FileBufferWindow
     * @return a new FileBufferWindow with the same contents
     */
    @Override
    public FileBufferWindow duplicate() {
        BufferCursorContext dupedContext = new BufferCursorContext(this.fileBufferInputHandler.getFileBufferContextTransparent());
        FileBufferView newView = new FileBufferView(dupedContext, this.view.getTermiosTerminalAdapter());

        FileBufferWindow windowToAdd = new FileBufferWindow(newView, new FileBufferInputHandler(dupedContext));
        windowToAdd.view = new ScrollbarDecorator(windowToAdd.view);
        return windowToAdd;
    }

    /**
     * Allows a layer above to call upon this object using a {@link WindowVisitor} implementation.
     * @param v the visitor object
     * @throws IOException when reading files goes wrong.
     */
    @Override
    public void accept(WindowVisitor v) throws IOException {
        v.visitFileWindow(this);
    }

    /**
     * Changes the termios adapter of the FileBufferWindow
     * @param newAdapter the new adapter
     */
    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {
        this.view.setTermiosTerminalAdapter(newAdapter);
    }

    /**
     * The input handler {@link FileBufferInputHandler} is able to request to open a new window.
     * We receive a new window here.
     */
    private void subscribeInputHandler() {
        this.fileBufferInputHandler.subscribeInputHandler(
                window -> {
                    this.listener.openWindow(new NormalWindowFactory().createDirectoryOnFileStructure(window, this.view.getTermiosTerminalAdapter()));
                }
        );
    }

    @Override
    public void subscribeWindow(OpenWindowRequestListener listener) {
        this.listener = listener;
        this.subscribeInputHandler();
    }

    @Override
    public String getPath() {
        return fileBufferInputHandler.getPath();
    }

}
