package inputhandler;

import directory.directorytree.FileSystemEntry;
import files.DisplayRequestForBufferContextListener;
import files.BufferCursorContext;
import listeners.DisplayRequestForFileEntryListener;
import util.RenderIndicator;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {

    BufferCursorContext bufferCursorContext;
    boolean surrogate;
/*    private DisplayRequestForFileEntryListener requestForFileEntryListener;*/

    private DisplayRequestForBufferContextListener requestForFileBufferListenerUnderlying;

    private DisplayRequestForInputHandlersListener requestForInputHandlersListener;

    /**
     * constructor
     *
     * @param path          the path of our file
     * @param lineSeparator the separator to insert for newlines (dos/linux)
     */
    public FileBufferInputHandler(String path, byte[] lineSeparator) throws IOException {
        this.bufferCursorContext = new BufferCursorContext(path, lineSeparator);
        this.subscribeBufferContextOpenRequests();
    }

    /**
     * constructor
     *
     * @param dupedContext the cursorContext to use for this inputhandler
     */
    public FileBufferInputHandler(BufferCursorContext dupedContext) {
        this.bufferCursorContext = dupedContext;
        this.subscribeBufferContextOpenRequests();
    }

    /**
     * Returns the Buffer Cursor Context: this contains the {@link files.FileBuffer} and the information
     * about cursor positioning
     *
     * @return the current buffercontext
     */
    public BufferCursorContext getFileBufferContextTransparent() {
        return bufferCursorContext;
    }

    /**
     * Handles the input for the {@link files.FileBuffer}
     *
     * @param b the input
     */
    public RenderIndicator input(byte b) {
        switch (b) {
            case -3:
                // Idle
                return RenderIndicator.NONE;
            case 27:
                this.surrogate = true;
                return RenderIndicator.NONE;
            default:
                if (this.surrogate) {
                    surrogateKeysInput(b);
                    this.surrogate = false;
                } else {
                    asciiInput(b);
                }
                return RenderIndicator.WINDOW;
        }
    }

    /**
     * @return if the buffer is dirty
     */
    @Override
    public boolean isSafeToClose() {
        return !bufferCursorContext.getDirty();
    }

    /**
     * handles normal input
     *
     * @param b the input byte
     */
    public void asciiInput(byte b) {
        switch (b) {

            case 8, 127, 62:
                this.bufferCursorContext.deleteCharacter();
                break;

            //TODO: MAKE TERMINAL USE REAL CTRL Z CODE
            // Control + Z or Control + A
            case 26, 1, -1:
                bufferCursorContext.undo();
                break;
            //Control + U
            case 21:
                bufferCursorContext.redo();
                break;
            // Control + S
            case 19:
                this.bufferCursorContext.save();
                break;
            // Control + J = parse
            case 10:
                FileSystemEntry entryToOpen = this.bufferCursorContext.parse();
                if (entryToOpen != null) {
                    this.requestForInputHandlersListener.notifyRequestOpening(
                            new InputhandlersFromModelObjectsFactory().createDirectoryInputHandler(entryToOpen)
                    );
                }
                break;
            //Control + U
            case 25:
                bufferCursorContext.redo();
                break;
            // Line separator
            case 13:
                // Character input
            default:
                this.bufferCursorContext.write(b);
                break;
        }
    }

    /**
     * handles surrogate input
     *
     * @param b the input byte
     */
    public void surrogateKeysInput(byte b) {
        switch (b) {
            // Right
            case 'C':
                bufferCursorContext.moveCursorRight();
                break;
            // Left
            case 'D':
                bufferCursorContext.moveCursorLeft();
                break;
            // Up
            case 'A':
                bufferCursorContext.moveCursorUp();
                break;
            // Down
            case 'B':
                bufferCursorContext.moveCursorDown();
                break;
        }
    }

    /**
     * handle right arrow input
     */
    @Override
    public RenderIndicator handleArrowRight() {
        bufferCursorContext.moveCursorRight();
        return RenderIndicator.CURSOR;
    }

    /**
     * handle left arrow input
     */
    @Override
    public RenderIndicator handleArrowLeft() {
        bufferCursorContext.moveCursorLeft();
        return RenderIndicator.CURSOR;
    }

    /**
     * handle down arrow input
     */
    @Override
    public RenderIndicator handleArrowDown() {
        bufferCursorContext.moveCursorDown();
        return RenderIndicator.CURSOR;
    }

    /**
     * handle up arrow input
     */
    @Override
    public RenderIndicator handleArrowUp() {
        bufferCursorContext.moveCursorUp();
        return RenderIndicator.CURSOR;
    }

    /**
     * enter a separator in the buffer
     */
    @Override
    public RenderIndicator handleSeparator() throws IOException {
        bufferCursorContext.enterSeparator();
        bufferCursorContext.moveCursorRight();
        return RenderIndicator.WINDOW;
    }

    /**
     * close the filebuffer
     *
     * @return if the filebuffer was closed
     */
    @Override
    public int forcedClose() {
        return bufferCursorContext.forcedClose();
    }

    /**
     * save the filebuffer
     */
    @Override
    public void save() {
        this.bufferCursorContext.save();
    }

    public String getPath() {
        return bufferCursorContext.getPath();
    }

/*    *//**
     * This method will pass down a class that allows {@link BufferCursorContext} to call these methods
     * and these methods will be handled here.
     *//*
    private void subscribeFileEntryOpenRequests() {
        this.bufferCursorContext.subscribeFileEntryDisplayRequests(
                entry -> requestForInputHandlersListener.notifyRequestOpening(new InputhandlersFromModelObjectsFactory().createDirectoryInputHandler(entry)));
    }*/

    /**
     * This method will pass down a class that allows {@link BufferCursorContext} to call these methods
     * and these methods will be handled here.
     */
    private void subscribeBufferContextOpenRequests() {
        requestForFileBufferListenerUnderlying =
                ctx -> {assert(ctx != null);requestForInputHandlersListener.notifyRequestOpening(new FileBufferInputHandler(ctx));};
        this.bufferCursorContext.subscribeBufferContextDisplayRequests(requestForFileBufferListenerUnderlying);
    }

    /**
     * Allows the {@link window.FileBufferWindow} to receive requests made from here
     */
    public void subscribeInputHandler(DisplayRequestForInputHandlersListener listener) {
        //this.requestForFileEntryListener = listener;
        //this.subscribeFileEntryOpenRequests();
        this.requestForInputHandlersListener = listener;
    }

    public void accept(InputHandlerVisitor v){
        v.visitFileInputHandler(this);
    }

}
