package window;

import ioadapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import inputhandler.InputHandlingElement;
import inputhandler.SnakeInputHandler;
import listeners.OpenWindowRequestListener;
import ui.SnakeView;
import ui.View;

import java.io.IOException;

public class SnakeWindow extends Window {

    /**
     * The snake view of this window, visual representation of the snakeWindow
     */
    private SnakeView snakeView;

    /**
     * The snake input handler of this window, handles the input of the snakeWindow and links to the internal workings of the snakeWindow
     */
    private SnakeInputHandler snakeInputHandler;

    /**
     * Constructor of this SnakeWindow
     * Creates a new handler and a new view based on the given width, height and terminal adapter
     * @param width the width of the snakeWindow
     * @param height the height of the snakeWindow
     * @param termiosTerminalAdapter the terminal adapter of the snakeWindow
     */
    public SnakeWindow(int width, int height, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {
        SnakeInputHandler handler = new SnakeInputHandler(width, height);
        SnakeView view = new SnakeView(handler.getSnakeGame(), termiosTerminalAdapter);
        this.snakeView = (view);
        this.snakeInputHandler = (handler);
    }

    /**
     * Returns the view of the SnakeWindow, a snakeView
     * @return the view of the SnakeWindow, a snakeView
     */
    @Override
    public View getView() {
        return this.snakeView;
    }

    /**
     * Returns the handler of the SnakeWindow, a snakeInputHandler
     * @return the handler of the SnakeWindow, a snakeInputHandler
     */
    @Override
    public InputHandlingElement getHandler() {
        return this.snakeInputHandler;
    }

    /**
     * Duplicates this SnakeWindow
     * @return a new SnakeWindow with the same contents
     */
    @Override
    public SnakeWindow duplicate() {
        return null;
    }

    /**
     * Accepts a visitor for this SnakeWindow
     * @param v the visitor
     */
    @Override
    public void accept(WindowVisitor v){
        v.visitSnakeWindow(this);
    }

    /**
     * Changes the termios adapter of the SnakeWindow
     * @param newAdapter the new adapter
     */
    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {
        snakeView.setTermiosTerminalAdapter(newAdapter);
    }

    /**
     * Subscribes this SnakeWindow to a listener
     * @param openWindowRequestListener listener to be subscribed to
     */
    @Override
    public void subscribeWindow(OpenWindowRequestListener openWindowRequestListener) {
        /*Snake normally won't request to open windows but this can be changed in
        the future*/
    }

    /**
     * A SnakeWindow does not have a path, thus it returns null
     * @return null
     */
    @Override
    public void subscribeCloseEvents(Runnable closeEventListener) {
        //No functionality to close needed, but this could be interesting in the case where a snake game would close
        //itself on win. So it kindof makes sense to make this method available to all windows in general.
    }

    @Override
    public String getPath() {
        return null;
    }
}
