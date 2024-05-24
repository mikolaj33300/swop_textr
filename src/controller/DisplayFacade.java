package controller;

import exception.HashNotMatchingException;
import exception.PathNotFoundException;
import files.OpenFileOnPathRequestListener;
import ioadapter.TermiosTerminalAdapter;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.VerticalLayoutNode;
import util.*;
import window.NormalWindowFactory;
import window.Window;

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
     * The layout structure for this display
     */
    private Layout rootLayout;
    /**
     * The integer representing an entry in {@link Window}
     */
    private int active = 0;
    /**
     * The adapter used for rendering
     */
    private final TermiosTerminalAdapter termiosTerminalAdapter;

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
        ArrayList<Layout> leaves = new ArrayList<>(paths.length);

        if (paths.length == 1) {
            Window toAdd = new NormalWindowFactory().createWindowOnPath(paths[0], lineSeparatorArg, termiosTerminalAdapter);
            //new FileBufferWindow(paths[0], lineSeparatorArg, termiosTerminalAdapter);
            this.rootLayout = new LayoutLeaf(toAdd.getHashCode());
            this.windows.add(toAdd);
            this.subscribeWindowOpeningRequests(toAdd);
        } else {
            int[] hashes = new int[paths.length];
            for (int i = 0; i < paths.length; i++) {
                String checkPath = paths[i];
                Window toAdd = new NormalWindowFactory().createWindowOnPath(checkPath, lineSeparatorArg, termiosTerminalAdapter);
                this.subscribeWindowOpeningRequests(toAdd);
                this.windows.add(toAdd);
                hashes[i] = toAdd.getHashCode();
            }
            this.rootLayout = new VerticalLayoutNode(hashes);
        }
    }

    /**
     * Used for testing
     *
     * @param toOpenWindow
     * @param termiosTerminalAdapter
     * @param lineSeparatorArg
     * @throws IOException
     */
    DisplayFacade(Window toOpenWindow, TermiosTerminalAdapter termiosTerminalAdapter, byte[] lineSeparatorArg) throws IOException {
        this.windows = new ArrayList<>();
        windows.add(toOpenWindow);
        this.subscribeWindowOpeningRequests(toOpenWindow);
        this.displayCoords = termiosTerminalAdapter.getTextAreaSize();

        this.lineSeparatorArg = lineSeparatorArg.clone();
        this.termiosTerminalAdapter = termiosTerminalAdapter;
        rootLayout = (new LayoutLeaf(windows.get(0).getHashCode()));

        toOpenWindow.setTermiosAdapter(termiosTerminalAdapter);

    }

    /**
     * render all windows
     */
    private void renderContent() throws IOException {
        for (Window window : windows) {
            window.render(windows.get(active).getHashCode());
        }
    }

    /**
     * Paints the screen for this display
     *
     * @throws IOException when something goes wrong updating the coordinates
     */
    public void paintScreen() throws IOException {
        if (displayCoords == null) {
            displayCoords = this.termiosTerminalAdapter.getTextAreaSize();
        }
        updateViewCoordinates();
        renderContent();
        renderCursor();
    }

    /**
     * save the active filebuffer
     */
    public void saveActive() {
        windows.get(active).save();
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
        windows.get(active).renderCursor();
    }

    /**
     * Called from {@link ControllerFacade}, used to close a focused {@link Window} in {@link DisplayFacade#windows}.
     * Will ask the {@link Window#getHandler()} if {@link Window#isSafeToClose()} is true.
     *
     * @return A {@link RenderIndicator} combined with an integer determining how the close action happened.
     * 0 if the window is safe to close and closed
     * 1 if it has a dirty buffer
     * 2 if it is not force closable
     */
    public Pair<RenderIndicator, WindowCloseStatus> closeActive() {
        if (windows.get(active).isSafeToClose()) {
            //safe to do a force close since clean buffer
            return forceCloseActive();
        } else {
            return new Pair<>(RenderIndicator.FULL, WindowCloseStatus.UNSAFE_CLOSE);
        }
    }

    /**
     * Called from {@link ControllerFacade}, used to force close a focused {@link Window} in {@link DisplayFacade#windows}.
     *
     * @return A {@link RenderIndicator} combined with an integer determining how the close action happened.
     * 0 if the window is safe to close and closed
     * 1 if it has a dirty buffer
     * 2 if it is not force closable
     */
    public Pair<RenderIndicator, WindowCloseStatus> forceCloseActive() {
        //checks which hash will be the next one after this is closed
        Integer newHashCode = getNewHashCode();
        if (newHashCode == null) return new Pair<>(RenderIndicator.FULL, WindowCloseStatus.LAST_WINDOW_CLOSED);

        //deletes and sets new one as active
        rootLayout = this.rootLayout.delete(windows.get(active).getHashCode());
        Window activeWindow = windows.get(active);
        activeWindow.forcedClose();
        windows.remove(activeWindow); //delete if no close event was triggerd previously that would in turn delete this window


        int newActive = -1;
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).getHashCode() == newHashCode) {
                newActive = i;
            }
        }

        if (newActive == -1) {
            throw new RuntimeException("Layout and collection of views inconsistent!");
        }
        active = newActive;
        return new Pair<>(RenderIndicator.FULL, WindowCloseStatus.CLOSED_SUCCESFULLY);
    }

    public RenderIndicator passToActive(byte b) throws IOException {
        if(!windows.isEmpty()){
            return this.windows.get(active).input(b);
        }
        return RenderIndicator.NONE;
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     *
     * @param dir the direction to move focus to
     */
    public RenderIndicator moveFocus(MoveDirection dir) {
        int newActive = this.rootLayout.getNeighborsContainedHash(dir, this.windows.get(active).getHashCode());
        for (int i = 0; i < this.windows.size(); i++) {
            if (this.windows.get(i).getHashCode() == newActive) {
                this.active = i;
                break;
            }
        }
        return RenderIndicator.CURSOR;
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
        termiosTerminalAdapter.clearScreen();
    }

    /**
     * Rearranges the Layouts, depending on the argument given
     *
     * @param orientation clockwise or counterclockwise
     */
    public RenderIndicator rotateLayout(RotationDirection orientation) {
        rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).getHashCode());
        return RenderIndicator.FULL;
    }

    /**
     * let the active window know that the right arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowRight() {
        return this.windows.get(active).handleArrowRight();
    }

    /**
     * let the active window know that the Left arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowLeft() {
        return this.windows.get(active).handleArrowLeft();
    }

    /**
     * let the active window know that the Down arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowDown() {
        return this.windows.get(active).handleArrowDown();
    }

    /**
     * let the active window know that the Up arrow is pressed
     *
     * @return {@link RenderIndicator} indicating to re-render the cursor
     */
    public RenderIndicator handleArrowUp() {
        return this.windows.get(active).handleArrowUp();
    }

    /**
     * let the active window insert a separator
     *
     * @return {@link RenderIndicator} indicating to re-render the full display
     */
    public RenderIndicator handleSeparator() throws IOException {
        return this.windows.get(active).handleSeparator();
    }

    /**
     * Opens the snake game by doing overwriting the active {@link DisplayFacade#windows}'s
     * active window by a snake game window. We delete the active window and add a new window to the list.
     */
    public RenderIndicator openSnakeGame() throws IOException {
        // Get UI coords of current window to initialize snake view's playfield
        Coords coordsView = this.windows.get(active).getRealCoords();

        // Get the hash of the current active window, we need this to find&replace the layoutleaf's hashcode
        int hashActive = this.windows.get(active).getHashCode();

        // Remove the window & add the snake window.;
        this.windows.remove(this.windows.get(active));


        Window toAdd = (new NormalWindowFactory().createSnakeGameWindow(coordsView.width, coordsView.height, termiosTerminalAdapter));
        this.windows.add(toAdd);

        // Change hash code & update the view coordinates
        rootLayout.changeHash(hashActive, toAdd.getHashCode());
        this.updateViewCoordinates();

        // Set the active view to the snake view
        active = this.windows.size() - 1;

        return RenderIndicator.FULL;
    }

    /**
     * Duplicates the active view by asking the view directly.
     */
    public RenderIndicator duplicateActive() throws IOException {
        Window windowToAdd = windows.get(active).duplicate();
        if (windowToAdd != null) {
            windows.add(windows.size(), windowToAdd);
            rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getHashCode(), windowToAdd.getHashCode());
        }
        return RenderIndicator.FULL;
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
    ArrayList<Window> getWindows() {
        return this.windows;
    }


    /**
     * Returns the active window integer
     *
     * @return integer determining the active window
     */
    int getActive() {
        return this.active;
    }

    /**
     * Returns the active window. For testing
     *
     * @return the active window
     */
    Window getActiveWindow() {
        return this.windows.get(active);
    }

    /**
     * Updates the view coordinates
     *
     * @return a render indicator determining to rerender all
     * @throws IOException when retrieving the get text area size fails
     */
    private RenderIndicator updateViewCoordinates() throws IOException {
        if (displayCoords == null) {
            displayCoords = this.termiosTerminalAdapter.getTextAreaSize();
        }
        HashMap<Integer, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, displayCoords.width, displayCoords.height));
        for (Window w : windows) {
            w.setRealCoords(coordsMap);
        }

        return RenderIndicator.FULL;
    }

    /**
     * Gets the hashcode of the new active node after the current one is closed, returns null if no other node left
     *
     * @return an Integer object representing a new hashcode
     */
    private Integer getNewHashCode() {
        int oldHashCode = windows.get(active).getHashCode();
        int newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.RIGHT, windows.get(active).getHashCode());
        if (newHashCode == oldHashCode) {
            newHashCode = rootLayout.getNeighborsContainedHash(MoveDirection.LEFT, windows.get(active).getHashCode());
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
     *
     * @param newAdapter the adapter that should be used for the new display
     * @throws IOException
     */
    public DisplayFacade requestOpeningNewDisplay(TermiosTerminalAdapter newAdapter) throws IOException {
        DisplayFromWindowVisitor newVisitor = new DisplayFromWindowVisitor(newAdapter, lineSeparatorArg);
        windows.get(active).accept(newVisitor);
        return newVisitor.getNewFacade();
    }

    /**
     * Sets the window size to a new parameter one, and repaints the screen
     *
     * @param newCoords the new size of the display
     * @throws IOException
     */
    public void repaintOnResize(Coords newCoords) throws IOException {
        this.displayCoords = newCoords;
        this.paintScreen();
    }

    public TermiosTerminalAdapter getTermiosTerminalAdapter() {
        return this.termiosTerminalAdapter;
    }

    /**
     * We will subscribe {@link Window} to this class, making it able to send through {@link Window} elements
     * which he may request to open.
     */
    private void subscribeWindowOpeningRequests(Window window) {
        window.subscribeWindow(
                openedWindow -> {
                    assert(openedWindow != null);
                    windows.add(windows.size(), openedWindow);
                    rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getHashCode(), openedWindow.getHashCode());
                    try {
                        updateViewCoordinates();
                    } catch (Exception e) {

                    }
                }
        );
    }

    public RenderIndicator openRealDirectory() {
        Window windowToAdd = null;
        windowToAdd = new NormalWindowFactory().createRealDirectory(new OpenFileOnPathRequestListener() {
            @Override
            public void notifyRequestToOpenFile(String pathToOpen) {
                openFileOnRealPath(pathToOpen);
            }
        }, termiosTerminalAdapter);
        if (windowToAdd != null) {
            Window finalWindowToAdd = windowToAdd;
            windows.add(windows.size(), finalWindowToAdd);
            rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getHashCode(), finalWindowToAdd.getHashCode());
            int finalWindowToAddHash = finalWindowToAdd.getHashCode();
            Layout layoutBefore = rootLayout.clone();
            windowToAdd.subscribeCloseEvents(new Runnable() {
                @Override
                public void run() {
                    try{
                        rootLayout = rootLayout.delete(finalWindowToAddHash);
                    } catch (HashNotMatchingException hh){

                    }

                    windows.remove(finalWindowToAdd);
                }
            });
        }
        return RenderIndicator.FULL;
    }

    private void openFileOnRealPath(String pathToOpen){
        //TODO: CHECK IF WINDOW IS ALREADY OPEN AND WINDOW.DUPLICATE(). WE CAN USE A LINKED LIST OF DISPLAYS FOR
        // THIS TO KEEP CONTROLLERFACADE OUT OF KNOWING ABOUT WINDOWS AND CHECKING PATHS
        Window toAdd = null;
        try {
            toAdd = new NormalWindowFactory().createWindowOnPath(pathToOpen, lineSeparatorArg, termiosTerminalAdapter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.subscribeWindowOpeningRequests(toAdd);
        this.windows.add(toAdd);
        rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getHashCode(), toAdd.getHashCode());
    }
}
