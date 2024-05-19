package inputhandler;

import files.BufferCursorContext;
import util.RenderIndicator;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {

    BufferCursorContext fb;
    boolean surrogate;

    /**
     * constructor
     *
     * @param path          the path of our file
     * @param lineSeparator the separator to insert for newlines (dos/linux)
     */
    public FileBufferInputHandler(String path, byte[] lineSeparator) throws IOException {
        this.fb = new BufferCursorContext(path, lineSeparator);
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
        switch (b) {
            case 8, 127, 10, 62:
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
            // Control + T
            case 20:
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
        contentsChangedSinceRender = true;
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
        contentsChangedSinceRender = true;
    }

    /**
     * handle right arrow input
     */
    @Override
    public void handleArrowRight() {
        fb.moveCursorRight();
        contentsChangedSinceRender = true;
    }

    /**
     * handle left arrow input
     */
    @Override
    public void handleArrowLeft() {
        fb.moveCursorLeft();
        contentsChangedSinceRender = true;
    }

    /**
     * handle down arrow input
     */
    @Override
    public void handleArrowDown() {
        fb.moveCursorDown();
        contentsChangedSinceRender = true;
    }

    /**
     * handle up arrow input
     */
    @Override
    public void handleArrowUp() {
        fb.moveCursorUp();
        contentsChangedSinceRender = true;
    }

    /**
     * enter a separator in the buffer
     */
    @Override
    public void handleSeparator() throws IOException {
        fb.enterSeparator();
        fb.moveCursorRight();
        contentsChangedSinceRender = true;
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
        contentsChangedSinceRender = true;
    }

    public String getPath() {
	return fb.getPath();
    }
}
