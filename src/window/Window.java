package window;

import ioadapter.TermiosTerminalAdapter;
import inputhandler.InputHandlingElement;
import ui.View;

import java.io.IOException;

public abstract class Window {
    //TODO turn this into a decorator of view and handler: so this also gets handle/updateCoords methods and delegates it to the view/handler
    Window() {

    }
    /**
     * Returns the view of the window
     * @return the view of the window
     */
    public abstract View getView();

    /**
     * Returns the handler of the window
     * @return the handler of the window
     */
    public abstract InputHandlingElement getHandler();

    /**
     * Duplicates this window
     * @return a new window with the same contents
     * @throws IOException
     */
    public abstract Window duplicate() throws IOException;

    /**
     * Accepts a visitor for this window
     * @param wv the visitor
     */
    public abstract void accept(WindowVisitor wv) throws IOException;

    /**
     * Changes the termios adapter of the window
     * @param newAdapter the new adapter
     */
    public abstract void setTermiosAdapter(TermiosTerminalAdapter newAdapter);
}
