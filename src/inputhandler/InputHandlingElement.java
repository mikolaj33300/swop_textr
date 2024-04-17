package inputhandler;

import ui.Rectangle;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandlingElement {

    /**
     * pass a close to the enclosed object
     */
    public abstract int close();

    /**
     * pass a save to the enclosed object
     */
    public abstract void save();

    /**
     * Handles input of specific characters
     * @param b the input byte
     */
    abstract public void input(byte b) throws IOException;

    /**
     * @return if the enclosed object is safe to close
     */
    public abstract boolean isSafeToClose();

    /**
     * pass a arrow right to the enclosed object
     */
    public abstract void handleArrowRight();

    /**
     * pass a arrow left to the enclosed object
     */
    public abstract void handleArrowLeft();

    /**
     * pass a arrow down to the enclosed object
     */
    public abstract void handleArrowDown();

    /**
     * pass a arrow up to the enclosed object
     */
    public abstract void handleArrowUp();

    /**
     * pass a separator to the enclosed object
     */
    public abstract void handleSeparator() throws IOException;

}
