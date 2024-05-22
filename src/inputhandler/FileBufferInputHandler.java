package inputhandler;

import files.BufferCursorContext;
import listeners.OpenParsedDirectoryListener;
import ui.View;
import util.RenderIndicator;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {

    BufferCursorContext fb;
    boolean surrogate;
    private OpenParsedDirectoryListener listener;

    /**
     * constructor
     *
     * @param path          the path of our file
     * @param lineSeparator the separator to insert for newlines (dos/linux)
     */
    public FileBufferInputHandler(String path, byte[] lineSeparator) throws IOException {
        this.fb = new BufferCursorContext(path, lineSeparator);
        View.write("test2.txt", ">> handler created");
    }

    /**
     * constructor
     *
     * @param dupedContext the cursorContext to use for this inputhandler
     */
    public FileBufferInputHandler(BufferCursorContext dupedContext) {
        this.fb = dupedContext;
    }

    /**
     * Returns the Buffer Cursor Context: this contains the {@link files.FileBuffer} and the information
     * about cursor positioning
     *
     * @return the current buffercontext
     */
    public BufferCursorContext getFileBufferContextTransparent() {
        return fb;
    }

    /**
     * Handles the input for the {@link files.FileBuffer}
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
        return !fb.getDirty();
    }

    /**
     * handles normal input
     *
     * @param b the input byte
     */
    public void asciiInput(byte b) {
        View.write("test2.txt", "byte = " + (int) b);
        switch (b) {

            case 8, 127, 62:
                this.fb.deleteCharacter();
                break;

            //TODO: MAKE TERMINAL USE REAL CTRL Z CODE
            // Control + Z or Control + A
            case 26, 1, -1:
                fb.undo();
                break;
            //Control + U
            case 21:
                fb.redo();
                break;
            // Control + S
            case 19:
                this.fb.save();
                break;
            // Control + J = parse
            case 10:
                View.write("test2.txt", "called");
                this.fb.parse();
                break;
            //Control + U
            case 25:
                fb.redo();
                break;
            // Line separator
            case 13:
                // Character input
            default:
                this.fb.write(b);
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
                fb.moveCursorRight();
                break;
            // Left
            case 'D':
                fb.moveCursorLeft();
                break;
            // Up
            case 'A':
                fb.moveCursorUp();
                break;
            // Down
            case 'B':
                fb.moveCursorDown();
                break;
        }
    }

    /**
     * handle right arrow input
     */
    @Override
    public RenderIndicator handleArrowRight() {
        fb.moveCursorRight();
	return RenderIndicator.CURSOR;
    }

    /**
     * handle left arrow input
     */
    @Override
    public RenderIndicator handleArrowLeft() {
        fb.moveCursorLeft();
	return RenderIndicator.CURSOR;
    }

    /**
     * handle down arrow input
     */
    @Override
    public RenderIndicator handleArrowDown() {
        fb.moveCursorDown();
	return RenderIndicator.CURSOR;
    }

    /**
     * handle up arrow input
     */
    @Override
    public RenderIndicator handleArrowUp() {
        fb.moveCursorUp();
	return RenderIndicator.CURSOR;
    }

    /**
     * enter a separator in the buffer
     */
    @Override
    public RenderIndicator handleSeparator() throws IOException {
        fb.enterSeparator();
        fb.moveCursorRight();
	return RenderIndicator.WINDOW;
    }

    /**
     * close the filebuffer
     *
     * @return if the filebuffer was closed
     */
    @Override
    public int forcedClose() {
        return fb.forcedClose();
    }

    /**
     * save the filebuffer
     */
    @Override
    public void save() {
        this.fb.save();
    }

    public String getPath() {
	return fb.getPath();
    }

    /**
     * This method will pass down a class that allows {@link BufferCursorContext} to call these methods
     * and these methods will be handled here.
     */
    private void subscribeFileBufferContext() {
        View.write("test2.txt", "\n<Handler> Subscribing to context in" + fb.hashCode());
        this.fb.subscribe(
                window -> {
                    View.write("test2.txt", "in buffer cursor context");
                    this.listener.openWindow(window);
                }
        );
    }

    /**
     * Allows the {@link window.FileBufferWindow} to receive requests made from here
     */
    public void subscribeInputHandler(OpenParsedDirectoryListener listener) {
        View.write("test2.txt", "\n<Handler> Received request for subscribing in inputhandler" + this.hashCode());
        this.listener = listener;
        this.subscribeFileBufferContext();
    }

}
