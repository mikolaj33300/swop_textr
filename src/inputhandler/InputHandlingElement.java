package inputhandler;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandlingElement {

    boolean needsRerender = false;

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

    /**
     * Determines if this element needs to be rerendered
     * @return boolean
     */
    public boolean needsRerender() {
        return this.needsRerender;
    }

    /**
     * Turns the boolean for rerender to false. Use after rendering
     */
    public void toggleRerender() {
        needsRerender = false;
    }

}
