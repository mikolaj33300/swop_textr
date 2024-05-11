package controller;

import controller.adapter.TermiosTerminalAdapter;
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
    private byte[] lineSeparatorArg;
    private Window windows = null;
    private Layout rootLayout;
    private int active;

    private boolean contentsChangedSinceLastRender;
    private TermiosTerminalAdapter termiosTerminalAdapter;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     *
     * @throws IOException when the path is invalid
     */
    public ControllerFacade(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {
        this.contentsChangedSinceLastRender = true;
        this.termiosTerminalAdapter = termiosTerminalAdapter;
        this.lineSeparatorArg = FileAnalyserUtil.setLineSeparatorFromArgs(args[0]);
        String[] paths;
        if (FileAnalyserUtil.isValidLineSeparatorString(args[0])) {
            paths = Arrays.copyOfRange(args, 1, args.length);
        } else {
            paths = args;
        }
        ArrayList<Layout> leaves = new ArrayList<Layout>(paths.length);
        for (int i = 0; i < paths.length; i++) {
          int hash;
            if (this.windows == null){
              this.windows = new Window(paths[i], this.lineSeparatorArg, termiosTerminalAdapter);
              hash = this.windows.hashCode();
              active = hash;
            } else {
              hash = this.windows.createFileBuffer(paths[i], this.lineSeparatorArg, termiosTerminalAdapter);
            }
            leaves.add(new LayoutLeaf(hash));
        }

        if (leaves.size() == 1)
            this.rootLayout = leaves.get(0);
        else
            this.rootLayout = new VerticalLayoutNode(leaves);
        this.updateViewCoordinates();
    }

    /**
     * render the active window
     */
    public void renderContent() throws IOException {
        this.contentsChangedSinceLastRender = false;
        windows.renderAll();
    }

    /**
     * save the active filebuffer
     */
    public void saveActive() {
      windows.save(active);
    }

    /**
     * render the cursor in the active view
     */
    public void renderCursor() throws IOException {
        windows.renderCursor(active);
    }

    /**
     * closes the active window
     *
     * @return 0 if the window is safe to close and closed 1 if it has a dirty buffer 2 if it is not force closable
     */
    public int closeActive() {
        contentsChangedSinceLastRender = true;
        if (windows.close(active)) {
            //safe to do a force close since clean buffer
            contentsChangedSinceLastRender = true;
            return forceCloseActive();
        } else {
            return 1;
        }
    }

    /**
     *
     * @return 0 if we closed the active window 2 if we can't close it
     */
    public int forceCloseActive() {
        this.contentsChangedSinceLastRender = true;
        //checks which hash will be the next one after this is closed
        Integer newHashCode = getNewHashCode();
        if (newHashCode == null) return 2;

        //deletes and sets new one as active
        rootLayout = this.rootLayout.delete(active);
        active = windows.forceClose(active);
        updateViewCoordinates();
        return 0;
    }

    public void passToActive(byte b) throws IOException {
        windows.passToActive(active, b);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     *
     * @param dir the direction to move focus to
     */
    public void moveFocus(MoveDirection dir) {
        contentsChangedSinceLastRender = true;
        active = this.rootLayout.getNeighborsContainedHash(dir, active);
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
        return;
    }

    /**
     * Rearranges the Layouts, depending on the argument given
     *
     * @param orientation clockwise or counterclockwise
     */
    public void rotateLayout(RotationDirection orientation) throws IOException {
        contentsChangedSinceLastRender = true;
        rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, active);
        updateViewCoordinates();
    }

    /**
     * let the active window know that the right arrow is pressed
     */
    public void handleArrowRight() {
      windows.handleArrowRight(active);
    }

    /**
     * let the active window know that the Left arrow is pressed
     */
    public void handleArrowLeft() {
      windows.handleArrowLeft(active);
    }

    /**
     * let the active window know that the Down arrow is pressed
     */
    public void handleArrowDown() {
      windows.handleArrowDown(active);
    }

    /**
     * let the active window know that the Up arrow is pressed
     */
    public void handleArrowUp() {
      windows.handleArrowUp(active);
    }

    /**
     * let the active window insert a separator
     */
    public void handleSeparator() throws IOException {
        this.windows.handleSeparator(active);
    }

    /**
     * Opens the snake game by doing overwriting the active {@link ControllerFacade#windows}'s
     * {@link inputhandler.InputHandlingElement} to {@link SnakeInputHandler} and {@link View}
     * to {@link SnakeView}. We delete the active window and add a new window to the list.
     */
    public void openSnakeGame() throws IOException {
        this.contentsChangedSinceLastRender = true;
        // Get UI coords of current window to initialize snake view's playfield
        Coords coordsView = windows.getRealUICoordsFromScaled(active, termiosTerminalAdapter);
        int newActive = this.windows.createSnakeGame(coordsView, termiosTerminalAdapter);

        // Get the hash of the current active window, we need this to find&replace the layoutleaf's hashcode

        // Remove the window & add the snake window.
        // this.windows.remove(this.windows.get(active)); // TODO

        // Change hash code & update the view coordinates
        rootLayout.changeHash(active, newActive);
        this.updateViewCoordinates();
        active = newActive;

        // Set the active view to the snake view
    }

    /**
     * Duplicates the active view by
     */
    public void duplicateActive() throws IOException {
      this.contentsChangedSinceLastRender = true;

      int oldActive = active;
      active = windows.duplicateActive(termiosTerminalAdapter, active);
      rootLayout = rootLayout.insertRightOfSpecified(oldActive, active);
      updateViewCoordinates();
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
    Window getWindows() {
        return windows;
    }

    /**
     * Returns the active window integer
     *
     * @return integer determining the active window
     */
    int getActive() {
        return active;
    }

    private void updateViewCoordinates() {
        this.contentsChangedSinceLastRender = true;
        HashMap<Integer, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, 1, 1));
        windows.updateAllViewCords(coordsMap);
    }

    /**
     * Gets the hashcode of the new active node after the current one is closed, returns null if no other node left
     *
     * @return
     */
    private Integer getNewHashCode() {
        int oldHashCode = windows.hashCode();
        int newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.RIGHT, active);
        if (newHashCode == oldHashCode) {
            newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.LEFT, active);
            if (oldHashCode == newHashCode) {
                //no left or right neighbor to focus
                rootLayout = null;
                return null;
            }
        }
        return newHashCode;
    }

    public boolean getContentsChangedSinceLastRender() {
        return this.contentsChangedSinceLastRender;
    }
}
