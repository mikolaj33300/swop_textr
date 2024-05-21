package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import inputhandler.InputHandlingElement;
import inputhandler.SnakeInputHandler;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.VerticalLayoutNode;
import ui.SnakeView;
import ui.View;
import util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class DisplayFacade {
    /**
     * The line separator for this display
     */
    private final byte[] lineSeparatorArg;
    /**
     * The {@link Window} objects within this display
     */
    private ArrayList<Window> windows;
    /**
     * The list of {@link FileBufferWindow}s for this display
     */
    private ArrayList<FileBufferWindow> fileBufferWindows;
    /**
     * The layout structure for this display
     */
    private Layout rootLayout;
    /**
     * The integer representing an entry in {@link FileBufferWindow}
     */
    private int active;
    /**
     * The adapter used for rendering
     */
    private final TermiosTerminalAdapter termiosTerminalAdapter;
    private final ArrayList<DisplayOpeningRequestListener> displayRequestListeners = new ArrayList<>(0);

    /**
     * The listener that will catch any
     */
    private final DisplayFacadeResizeListener displayFacadeResizeListener;

    /**
     * The {@link Coords} indicating the size of the display
     */
    private Coords displayCoords;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     *
     * @throws IOException when the path is invalid
     */
    public DisplayFacade(String[] paths, TermiosTerminalAdapter termiosTerminalAdapter, byte[] lineSeparatorArg) throws PathNotFoundException, IOException {
        this.lineSeparatorArg = lineSeparatorArg;
        this.termiosTerminalAdapter = termiosTerminalAdapter;
        this.displayCoords = null;

        this.windows = new ArrayList<>();
        this.fileBufferWindows = new ArrayList<>();
        ArrayList<Layout> leaves = new ArrayList<>(paths.length);

        for (int i = 0; i < paths.length; i++) {
            String checkPath = paths[i];
            FileBufferWindow toAdd;

            toAdd = new FileBufferWindow(checkPath, lineSeparatorArg, termiosTerminalAdapter);

            this.fileBufferWindows.add(toAdd);
            this.windows.add(toAdd);
            leaves.add(new LayoutLeaf(windows.get(i).getView().hashCode()));
        }

        if (leaves.size() == 1)
            this.rootLayout = leaves.get(0);
        else
            this.rootLayout = new VerticalLayoutNode(leaves);

        DisplayFacadeResizeListener listenerToAdd = new DisplayFacadeResizeListener(this);
        this.displayFacadeResizeListener = listenerToAdd;
        termiosTerminalAdapter.subscribeToResizeTextArea(listenerToAdd);

    }

    /**
     * Used for testing
     * @param toOpenWindow
     * @param termiosTerminalAdapter
     * @param lineSeparatorArg
     * @throws IOException
     */
    DisplayFacade(FileBufferWindow toOpenWindow, TermiosTerminalAdapter termiosTerminalAdapter, byte[] lineSeparatorArg) throws IOException {
        this.windows = new ArrayList<>();
        this.fileBufferWindows = new ArrayList<>();
        windows.add(toOpenWindow);
        fileBufferWindows.add(toOpenWindow);
        this.displayCoords = termiosTerminalAdapter.getTextAreaSize();

        this.lineSeparatorArg = lineSeparatorArg.clone();
        this.termiosTerminalAdapter = termiosTerminalAdapter;
        rootLayout = (new LayoutLeaf(windows.get(0).getView().hashCode()));

        toOpenWindow.setTermiosAdapter(termiosTerminalAdapter);

        this.updateViewCoordinates();
        this.paintScreen();

        DisplayFacadeResizeListener listenerToAdd = new DisplayFacadeResizeListener(this);
        this.displayFacadeResizeListener = listenerToAdd;
        termiosTerminalAdapter.subscribeToResizeTextArea(listenerToAdd);
    }

    /**
     * render all windows
     */
    private void renderContent() throws IOException {
        for (Window window : windows) {
            window.getView().render(windows.get(active).getView().hashCode());
        }
    }

    /**
     * Paints the screen for this display
     * @throws IOException when something goes wrong updating the coordinates
     */
    public void paintScreen() throws IOException {
        if (displayCoords == null) {
            displayCoords = this.termiosTerminalAdapter.getTextAreaSize();
        }
        updateViewCoordinates();
        clearContent();
        renderContent();
        renderCursor();
    }

    /**
     * save the active filebuffer
     */
    public void saveActive() {
        windows.get(active).getHandler().save();
        try {
            renderContent();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Render the cursor in the active view
     */
    private void renderCursor() throws IOException {
        windows.get(active).getView().renderCursor();
    }

    /**
     * Called from {@link ControllerFacade}, used to close a focused {@link Window} in {@link DisplayFacade#windows}.
     * Will ask the {@link Window#getHandler()} if {@link InputHandlingElement#isSafeToClose()} is true.
     * @return  A {@link RenderIndicator} combined with an integer determining how the close action happened.
     *          0 if the window is safe to close and closed
     *          1 if it has a dirty buffer
     *          2 if it is not force closable
     */
    public Pair<RenderIndicator, Integer> closeActive() {
        if (windows.get(active).getHandler().isSafeToClose()) {
            //safe to do a force close since clean buffer
            return forceCloseActive();
        } else {
            return new Pair<>(RenderIndicator.FULL, 1);
        }
    }

    /**
     * Called from {@link ControllerFacade}, used to force close a focused {@link Window} in {@link DisplayFacade#windows}.
     * @return  A {@link RenderIndicator} combined with an integer determining how the close action happened.
     *          0 if the window is safe to close and closed
     *          1 if it has a dirty buffer
     *          2 if it is not force closable
     */
    public Pair<RenderIndicator, Integer> forceCloseActive() {
        //checks which hash will be the next one after this is closed
        Integer newHashCode = getNewHashCode();
        if (newHashCode == null) return new Pair<>(RenderIndicator.FULL, 2);

        //deletes and sets new one as active
        rootLayout = this.rootLayout.delete(windows.get(active).getView().hashCode());
        windows.get(active).getHandler().forcedClose();
        fileBufferWindows.remove(windows.get(active));
        windows.remove(active);


        int newActive = -1;
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getView().hashCode() == newHashCode) {
                newActive = i;
            }
        }

        if (newActive == -1) {
            throw new RuntimeException("Layout and collection of views inconsistent!");
        }
        active = newActive;
        return new Pair<>(RenderIndicator.FULL, 0);
    }

    public RenderIndicator passToActive(byte b) throws IOException {
        return this.windows.get(active).getHandler().input(b);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     * @param dir the direction to move focus to
     */
    public RenderIndicator moveFocus(MoveDirection dir) {
        int newActive = this.rootLayout.getNeighborsContainedHash(dir, this.windows.get(active).getView().hashCode());
        for (int i = 0; i < this.windows.size(); i++) {
            if (this.windows.get(i).getView().hashCode() == newActive) {
                this.active = i;
                break;
            }
        }
        return RenderIndicator.FULL;
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
        termiosTerminalAdapter.clearScreen();
    }

    /**
     * Rearranges the Layouts, depending on the argument given
     * @param orientation clockwise or counterclockwise
     */
    public RenderIndicator rotateLayout(RotationDirection orientation) {
        rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).getView().hashCode());
        return RenderIndicator.FULL;
    }

    /**
     * let the active window know that the right arrow is pressed
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowRight() {
        return this.windows.get(active).getHandler().handleArrowRight();
    }

    /**
     * let the active window know that the Left arrow is pressed
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowLeft() {
        return this.windows.get(active).getHandler().handleArrowLeft();
    }

    /**
     * let the active window know that the Down arrow is pressed
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowDown() {
        return this.windows.get(active).getHandler().handleArrowDown();
    }

    /**
     * let the active window know that the Up arrow is pressed
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowUp() {
        return this.windows.get(active).getHandler().handleArrowUp();
    }

    /**
     * let the active window insert a separator
     * @return {@link RenderIndicator} indicating to re-render the full display
     */
    public RenderIndicator handleSeparator() throws IOException {
        return this.windows.get(active).getHandler().handleSeparator();
    }

    /**
     * Opens the snake game by doing overwriting the active {@link DisplayFacade#windows}'s
     * {@link inputhandler.InputHandlingElement} to {@link SnakeInputHandler} and {@link View}
     * to {@link SnakeView}. We delete the active window and add a new window to the list.
     */
    public RenderIndicator openSnakeGame() throws IOException {
        // Get UI coords of current window to initialize snake view's playfield
        Coords coordsView = this.windows.get(active).getView().getRealCoords();

        // Get the hash of the current active window, we need this to find&replace the layoutleaf's hashcode
        int hashActive = this.windows.get(active).getView().hashCode();

        // Remove the window & add the snake window.
        this.fileBufferWindows.remove(this.windows.get(active));
        this.windows.remove(this.windows.get(active));


        SnakeWindow toAdd = new SnakeWindow(coordsView.width, coordsView.height, termiosTerminalAdapter);
        this.windows.add(toAdd);

        // Change hash code & update the view coordinates
        rootLayout.changeHash(hashActive, toAdd.getView().hashCode());
        this.updateViewCoordinates();

        // Set the active view to the snake view
        active = this.windows.size() - 1;

        return RenderIndicator.FULL;
    }

    /**
     * Duplicates the active view by asking the view directly.
     */
    public RenderIndicator duplicateActive() throws IOException {
        windows.get(active).accept(new DuplicateWindowVisitor());
        return RenderIndicator.FULL;
    }

    /**
     * Returns the line separator
     * @return byte[] containing the line separator
     */
    byte[] getLineSeparatorArg() {
        return this.lineSeparatorArg;
    }

    /**
     * Returns the array of windows
     * @return list of window objects
     */
    ArrayList<Window> getWindows() {
        return this.windows;
    }

    /**
     * Returns the list of active {@link FileBufferWindow} objects. For testing
     * @return the list of file buffer windows
     */
    ArrayList<FileBufferWindow> getFileBufferWindows() {
        return this.fileBufferWindows;
    }

    /**
     * Returns the active window integer
     * @return integer determining the active window
     */
    int getActive() {
        return this.active;
    }

    /**
     * Returns the active window. For testing
     * @return the active window
     */
    Window getActiveWindow() {
        return this.windows.get(active);
    }

    /**
     * Updates the view coordinates
     * @return a render indicator determining to rerender all
     * @throws IOException when retrieving the get text area size fails
     */
    private RenderIndicator updateViewCoordinates() throws IOException {
        if (displayCoords == null) {
            displayCoords = this.termiosTerminalAdapter.getTextAreaSize();
        }
        HashMap<Integer, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, displayCoords.width, displayCoords.height));
        for (Window w : windows) {
            w.getView().setRealCoords(coordsMap.get(w.getView().hashCode()));
        }

        return RenderIndicator.FULL;
    }

    /**
     * Gets the hashcode of the new active node after the current one is closed, returns null if no other node left
     * @return an Integer object representing a new hashcode
     */
    private Integer getNewHashCode() {
        int oldHashCode = windows.get(active).getView().hashCode();
        int newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.RIGHT, windows.get(active).getView().hashCode());
        if (newHashCode == oldHashCode) {
            newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.LEFT, windows.get(active).getView().hashCode());
            if (oldHashCode == newHashCode) {
                //no left or right neighbor to focus
                rootLayout = null;
                return null;
            }
        }
        return newHashCode;
    }

    /**
     * Called when a new display should be opened. This is passed down from {@link ControllerFacade} on the active
     * display. The display will then do correct operations by calling upon the active {@link Window}
     * @param newAdapter the adapter that should be used for the new display
     * @throws IOException
     */
    public void requestOpeningNewDisplay(TermiosTerminalAdapter newAdapter) throws IOException {
        windows.get(active).accept(new newDisplayFromWindowVisitor(newAdapter));
    }

    /**
     * Sets the window size to a new parameter one, and repaints the screen
     * @param newCoords the new size of the display
     * @throws IOException
     */
    public void repaintOnResize(Coords newCoords) throws IOException {
        this.displayCoords = newCoords;
        this.paintScreen();
    }

    /**
     * This visitor is used so {@link Window} instances can be called by {@link Window#accept(WindowVisitor)}.
     * Depending on which action, in this case duplication, we can let the window double-dispatch to here, where
     * {@link DisplayFacade.DuplicateWindowVisitor} can access {@link DisplayFacade#windows}.
     */
    public class DuplicateWindowVisitor implements WindowVisitor {

        /**
         * Initializes a new {@link Window} object and adds it to {@link DisplayFacade#windows}.
         * @param fbw the window that double dispatched to this location.
         */
        @Override
        public void visitFileWindow(FileBufferWindow fbw) {
            FileBufferWindow windowToAdd = fbw.duplicate();
            if (windowToAdd != null) {
                windows.add(windows.size(), windowToAdd);
                fileBufferWindows.add(fileBufferWindows.size(), windowToAdd);
                rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getView().hashCode(), windowToAdd.getView().hashCode());
            }
        }

        @Override
        public void visitSnakeWindow(SnakeWindow sw) {
            // No duplicating snake
        }

    }

    /**
     * This visitor is used so {@link Window} instances can be called by {@link Window#accept(WindowVisitor)}.
     * Depending on which action, in this case opening a new display, we can let the window double-dispatch to here, where
     * {@link DisplayFacade.DuplicateWindowVisitor} can access {@link DisplayFacade#windows}.
     */
    public class newDisplayFromWindowVisitor implements WindowVisitor {
        private TermiosTerminalAdapter newAdapter;

        /**
         * Upon creation of this object, {@link DisplayFacade} gives a {@link TermiosTerminalAdapter} which will be used
         * for the new {@link DisplayFacade}.
         * @param newAdapter the termios adapter for the new display facade
         */
        public newDisplayFromWindowVisitor(TermiosTerminalAdapter newAdapter) {
            this.newAdapter = newAdapter;
        }

        /**
         * Dispatches a request to {@link DisplayFacade#displayRequestListeners}, which will notify a method in {@link ControllerFacade} that
         * adds a newly created {@link DisplayFacade} object
         * @param fbw the {@link FileBufferWindow} where the 'open new display' command is triggered
         * @throws IOException ?
         */
        @Override
        public void visitFileWindow(FileBufferWindow fbw) throws IOException {
            FileBufferWindow windowToAdd = fbw.duplicate();
            windowToAdd.setTermiosAdapter(newAdapter);
            if (windowToAdd != null) {
                DisplayFacade displayToAdd = new DisplayFacade(windowToAdd, newAdapter, lineSeparatorArg);
                for (DisplayOpeningRequestListener dl : displayRequestListeners) {
                    dl.notifyRequestOpenDisplay(displayToAdd);
                }
            }
        }

        @Override
        public void visitSnakeWindow(SnakeWindow sw) {
            // No putting snake on other window
        }

    }

    /**
     * Subscribes to a listener
     * @param listener
     */
    public void subscribeToRequestsOpeningDisplay(DisplayOpeningRequestListener listener) {
        this.displayRequestListeners.add(listener);
    }

    public void unsubscribeFromRequestsOpeningDisplay(DisplayOpeningRequestListener listener) {
        this.displayRequestListeners.remove(listener);
    }

}
