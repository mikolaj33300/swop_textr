package window;

import ioadapter.TermiosTerminalAdapter;
import inputhandler.InputHandlingElement;
import listeners.OpenWindowRequestListener;
import ui.View;
import util.Coords;
import util.Rectangle;
import util.RenderIndicator;

import java.io.IOException;
import java.util.HashMap;

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
     * Gives in input to the intputhandler of this window
     * @param b the byte input
     * @return the render indicator
     * @throws IOException
     */
    public RenderIndicator input(byte b) throws IOException{
        return getHandler().input(b);
    }

    /**
     * Closes the window
     */
    public void forcedClose() {
        getHandler().forcedClose();
    }

    /**
     * Returns the hashCode of the view linked to this window
     * @return the hashCode of the view linked to this window
     */
    public int getHashCode(){
        return getView().hashCode();
    }

    /**
     * let the active window know that the right arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowRight() {
        return getHandler().handleArrowRight();
    }


    /**
     * let the active window know that the left arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowLeft() {
        return getHandler().handleArrowLeft();
    }


    /**
     * let the active window know that the up arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowUp() {
        return getHandler().handleArrowUp();
    }

    /**
     * let the active window know that the down arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowDown() {
        return getHandler().handleArrowDown();
    }


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

    /**
     * Subscribes a open window reuquest listener to this window
     * @param openWindowRequestListener the window to subscribe
     */
    public abstract void subscribeWindow(OpenWindowRequestListener openWindowRequestListener);

    /**
    * Subscribes a close event listener to this window
    */
    public abstract void subscribeCloseEvents(Runnable closeEventListener);

        /**
     * Checks if it is safe to close this window
     * @return true if it is safe to close, false otherwise
     */
    public boolean isSafeToClose(){
        return getHandler().isSafeToClose();
    }

    /**
     * Renders the window
     * @param activeHash the hashcode of the active window in the above DisplayFactory
     */
    public void render(int activeHash) throws IOException {
        getView().render(getHashCode());
    }

    /**
     * Renders the cursor
     */
    public void renderCursor() throws IOException {
        getView().renderCursor();
    }

    /**
     * Returns the real coordinates of the view
     * @return the real coordinates of the view
     */
    public Coords getRealCoords() {
        return getView().getRealCoords();
    }

    /**
     * Passes a handleSeparator request to the handler
     * @return the render indicator
     */
    public RenderIndicator handleSeparator() throws IOException {
        return getHandler().handleSeparator();
    }

    /**
     * Passes a save request to the handler
-     */
    public void save() {
        getHandler().save();
    }

    /**
     * Changes the realCoords of the contained View
     */
    public void setRealCoords( HashMap<Integer, Rectangle> coordsMap) {
        getView().setRealCoords(coordsMap.get(getHashCode()));
    }

    /**
     * Returns path for the open resource in this window or null if irrelevant. For windows with files it delegates
     * the call to get path to the file on lowest level. A virtual file built on a JSON will return null. A real file
     * the path that is currently open.
     */
    public abstract String getPath();
}
