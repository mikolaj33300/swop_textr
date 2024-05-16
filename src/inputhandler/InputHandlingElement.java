package inputhandler;

import java.io.IOException;
import ui.renderIndicator;

/*
 * commands next
 */
abstract public class InputHandlingElement {

    boolean contentsChangedSinceRender = false;

    public int getHash() {
        return this.hashCode();
    }

    public abstract int forcedClose();

    /**
     * pass a save to the enclosed object
     */
    public abstract void save();

    /**
     * Handles input of specific characters
     * @param b the input byte
     */
    abstract public renderIndicator input(byte b) throws IOException;

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

    /**
     * Determines if this element needs to be rerendered
     * @return boolean
     */
    public boolean needsRerender() {
        return this.contentsChangedSinceRender;
    }

    /**
     * Turns the boolean for rerender to false. Use after rendering
     */
    public void setContentsChangedSinceLastRenderFalse() {
        contentsChangedSinceRender = false;
    }

}
