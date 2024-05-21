package inputhandler;

import java.io.IOException;
import util.RenderIndicator;

/*
 * commands next
 */
abstract public class InputHandlingElement {

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
    abstract public RenderIndicator input(byte b) throws IOException;

    /**
     * @return if the enclosed object is safe to close
     */
    public abstract boolean isSafeToClose();

    /**
     * pass a arrow right to the enclosed object
     */
    public abstract RenderIndicator handleArrowRight();

    /**
     * pass a arrow left to the enclosed object
     */
    public abstract RenderIndicator handleArrowLeft();

    /**
     * pass a arrow down to the enclosed object
     */
    public abstract RenderIndicator handleArrowDown();

    /**
     * pass a arrow up to the enclosed object
     */
    public abstract RenderIndicator handleArrowUp();

    /**
     * pass a separator to the enclosed object
     */
    public abstract RenderIndicator handleSeparator() throws IOException;

}
