package controller;

import controller.adapter.TermiosTerminalAdapter;
import controller.Display;
import files.BufferCursorContext;
import files.FileAnalyserUtil;
import exception.PathNotFoundException;
import inputhandler.FileBufferInputHandler;
import inputhandler.SnakeInputHandler;
import layouttree.*;
import ui.*;
import util.Coords;
import util.MoveDirection;
import util.RotationDirection;
import util.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class ControllerFacade {
    private final byte[] lineSeparatorArg;
    private ArrayList<Display> displays = new ArrayList<Display>(1);
    private int active = 0;

    private boolean contentsChangedSinceLastRender;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     *
     * @throws IOException when the path is invalid
     */
    public ControllerFacade(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {

      this.lineSeparatorArg = FileAnalyserUtil.setLineSeparatorFromArgs(args[0]);
      this.displays.add(new Display(args, termiosTerminalAdapter, lineSeparatorArg));
      String[] paths;
      if (FileAnalyserUtil.isValidLineSeparatorString(args[0])) {
          paths = Arrays.copyOfRange(args, 1, args.length);
      } else {
          paths = args;
      }
    }

    /**
     * render the active window
     */
    public void renderContent() throws IOException {
      displays.get(active).renderContent();
    }

    /**
     * save the active filebuffer
     */
    public void saveActive() {
      displays.get(active).saveActive();
    }

    /**
     * render the cursor in the active view
     */
    public void renderCursor() throws IOException {
        displays.get(active).renderCursor();
    }

    /**
     * closes the active window
     *
     * @return 0 if the window is safe to close and closed 1 if it has a dirty buffer 2 if it is not force closable
     */
    public int closeActive() {
      return displays.get(active).closeActive();
    }

    /**
     *
     * @return 0 if we closed the active window 2 if we can't close it
     */
    public int forceCloseActive() {
      return displays.get(active).forceCloseActive();
    }

    public void passToActive(byte b) throws IOException {
      displays.get(active).passToActive(b);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     *
     * @param dir the direction to move focus to
     */
    public void moveFocus(MoveDirection dir) {
      displays.get(active).moveFocus(dir);
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
      displays.get(active).clearContent();
    }

    /**
     * Rearranges the Layouts, depending on the argument given
     *
     * @param orientation clockwise or counterclockwise
     */
    public void rotateLayout(RotationDirection orientation) throws IOException {
      displays.get(active).rotateLayout(orientation);
    }

    /**
     * let the active window know that the right arrow is pressed
     */
    public void handleArrowRight() {
      displays.get(active).handleArrowRight();
    }

    /**
     * let the active window know that the Left arrow is pressed
     */
    public void handleArrowLeft() {
      displays.get(active).handleArrowLeft();
    }

    /**
     * let the active window know that the Down arrow is pressed
     */
    public void handleArrowDown() {
      displays.get(active).handleArrowDown();
    }

    /**
     * let the active window know that the Up arrow is pressed
     */
    public void handleArrowUp() {
      displays.get(active).handleArrowUp();
    }

    /**
     * let the active window insert a separator
     */
    public void handleSeparator() throws IOException {
      displays.get(active).handleSeparator();
    }

    /**
     * Opens the snake game by doing overwriting the active {@link ControllerFacade#windows}'s
     * {@link inputhandler.InputHandlingElement} to {@link SnakeInputHandler} and {@link View}
     * to {@link SnakeView}. We delete the active window and add a new window to the list.
     */
    public void openSnakeGame() throws IOException {
      displays.get(active).openSnakeGame();
    }

    /**
     * Duplicates the active view by
     */
    public void duplicateActive() throws IOException {
      displays.get(active).duplicateActive();
    }

    /**
     * Returns the line separator
     *
     * @return byte[] containing the line separator
     */
    byte[] getLineSeparatorArg() {
        return this.lineSeparatorArg;
    }

    /**
     * Returns the array of windows
     *
     * @return list of window objects
     */
    ArrayList<Display> getDisplays() {
        return displays;
    }

    /**
     * Returns the active window integer
     *
     * @return integer determining the active window
     */
    int getActive() {
        return this.active;
    }

    public boolean getContentsChangedSinceLastRender() {
        return this.contentsChangedSinceLastRender;
    }

    public void addDisplay(TermiosTerminalAdapter adapter) {
	String[] test = new String[1];
	test[0] = "long.txt";
	try { 
	    this.displays.add(new Display(test, adapter, this.lineSeparatorArg));
	    active = displays.size()-1;
	    renderContent();
	} catch (Exception e){
	    System.out.println("adding/rendering display failed");
	    System.out.println(e);
	    System.exit(1);
	}
    }

    public void setActive(int a) {
	this.active = a;
    }
}
