package inputhandler;

import ui.Rectangle;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandlingElement {

    public int getHash() {
        return this.hashCode();
    }

    public abstract int close();

    public abstract void save();

    /*
     * Handles input of specific characters
     */
    abstract public void input(byte b) throws IOException;

    public abstract boolean isSafeToClose();

    public abstract void handleArrowRight();

    public abstract void handleArrowLeft();

    public abstract void handleArrowDown();

    public abstract void handleArrowUp();

    public abstract void handleSeparator() throws IOException;

}
