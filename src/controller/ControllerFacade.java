package controller;

import files.FileAnalyserUtil;
import files.PathNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import inputhandler.FileBufferInputHandler;
import inputhandler.SnakeInputHandler;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.ROT_DIRECTION;
import layouttree.MOVE_DIRECTION;
import layouttree.VerticalLayoutNode;
import ui.*;
import ui.FileBufferView;

class ControllerFacade {
    private byte[] lineSeparatorArg;
    private ArrayList<Window> windows;
    private Layout rootLayout;
    private int active;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     *
     * @throws IOException when the path is invalid
     */
    public ControllerFacade(String[] args) throws PathNotFoundException, IOException {
        this.lineSeparatorArg = FileAnalyserUtil.setLineSeparatorFromArgs(args[0]);
        String[] paths;
        if (FileAnalyserUtil.isValidLineSeparatorString(args[0])) {
            paths = Arrays.copyOfRange(args, 1, args.length);
        } else {
            paths = args;
        }

        this.windows = new ArrayList<Window>();
        ArrayList<Layout> leaves = new ArrayList<Layout>(paths.length);
        for (int i = 0; i < paths.length; i++) {
            String checkPath = paths[i];
            FileBufferInputHandler openedFileHandler;
            try {
                openedFileHandler = new FileBufferInputHandler(checkPath, lineSeparatorArg);
            } catch (PathNotFoundException e) {
                throw e;
            }
            this.windows.add(new Window(new FileBufferView(openedFileHandler.getFileBufferContextTransparent()), openedFileHandler));
            leaves.add(new LayoutLeaf(windows.get(i).view.hashCode()));
        }

        if (leaves.size() == 1)
            this.rootLayout = leaves.get(0);
        else
            this.rootLayout = new VerticalLayoutNode(leaves);

        this.updateViewCoordinates();
    }

  public void renderContent() throws IOException {
      for (Window window : windows) {
          window.view.render(windows.get(active).view.hashCode());
      }
  }

    public void saveActive() {
        windows.get(active).handler.save();
    }

    public void renderCursor() throws IOException {
        windows.get(active).view.renderCursor();
    }

    public int closeActive() {
        if (windows.get(active).handler.isSafeToClose()) {
            //safe to do a force close since clean buffer
            return forceCloseActive();
        } else {
            return 1;
        }
    }

    public int forceCloseActive() {
        //checks which hash will be the next one after this is closed
        Integer newHashCode = getNewHashCode();
        if (newHashCode == null) return 2;

        //deletes and sets new one as active
        rootLayout = this.rootLayout.delete(windows.get(active).view.hashCode());
        windows.remove(active);
        int newActive = -1;
        for (int i = 0; i < windows.size(); i++) {
            if (windows.get(i).view.hashCode() == newHashCode) {
                newActive = i;
            }
        }
        active = newActive;
        if (active == -1) {
            throw new RuntimeException("Layout and collection of views inconsistent!");
        }

        updateViewCoordinates();
        updateViewCoordinates();
        return 0;
    }

    /**
     * Gets the hashcode of the new active node after the current one is closed, returns null if no other node left
     *
     * @return
     */
    private Integer getNewHashCode() {
        int oldHashCode = windows.get(active).view.hashCode();
        int newHashCode = rootLayout.getNeighborsContainedHash(MOVE_DIRECTION.RIGHT, windows.get(active).view.hashCode());
        if (newHashCode == oldHashCode) {
            newHashCode = rootLayout.getNeighborsContainedHash(MOVE_DIRECTION.LEFT, windows.get(active).view.hashCode());
            if (oldHashCode == newHashCode) {
                //no left or right neighbor to focus
                rootLayout = null;
                return null;
            }
        }
        return newHashCode;
    }

    public void passToActive(byte b) throws IOException {
        this.windows.get(active).handler.input(b);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    public void moveFocus(MOVE_DIRECTION dir) {

        int newActive = this.rootLayout.getNeighborsContainedHash(dir, this.windows.get(active).view.hashCode());
        for (int i = 0; i < this.windows.size(); i++) {
            if (this.windows.get(i).view.hashCode() == newActive) {
                this.active = i;
                break;
            }
        }

    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
        return;
    }

    /**
     * Rearranges the Layouts clockwise or counterclockwise, depending on the argument given
     */
    public void rotateLayout(ROT_DIRECTION orientation) throws IOException {
        rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).view.hashCode());
        updateViewCoordinates();
    }

    private void updateViewCoordinates() {
        HashMap<Integer, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, 1, 1));
        for (Window w : windows) {
            w.view.setScaledCoords(coordsMap.get(w.view.hashCode()));
        }
    }

    public void handleArrowRight() {
        this.windows.get(active).handler.handleArrowRight();
    }

    public void handleArrowLeft() {
        this.windows.get(active).handler.handleArrowLeft();
    }

    public void handleArrowDown() {
        this.windows.get(active).handler.handleArrowDown();
    }

    public void handleArrowUp() {
        this.windows.get(active).handler.handleArrowUp();
    }

    public void handleSeparator() throws IOException {
        this.windows.get(active).handler.handleSeparator();
    }

    /**
     * Opens the snake game by doing overwriting the active {@link ControllerFacade#windows}'s
     * {@link inputhandler.InputHandlingElement} to {@link SnakeInputHandler} and {@link View}
     * to {@link SnakeView}. We delete the active window and add a new window to the list.
     */
    public void openSnakeGame() throws IOException {
        UICoords coordsView = this.windows.get(active).view.getRealUICoordsFromScaled();
        SnakeInputHandler handler = new SnakeInputHandler(coordsView.width, coordsView.height);
        this.windows.remove(this.windows.get(active));
        this.windows.add(
                new Window(
                        new SnakeView(handler.getSnakeGame()),
                        handler
                )
        );
        this.updateViewCoordinates();
        active = this.windows.size() - 1;
    }

}
