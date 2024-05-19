package controller;

import controller.adapter.TermiosTerminalAdapter;
import files.BufferCursorContext;
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
import java.util.HashMap;

    class DisplayFacade {
        private final byte[] lineSeparatorArg;
        private ArrayList<Window> windows;

        private ArrayList<FileBufferWindow> fileBufferWindows;
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
        public DisplayFacade(String[] paths, TermiosTerminalAdapter termiosTerminalAdapter, byte[] lineSeparatorArg) throws PathNotFoundException, IOException {
            this.lineSeparatorArg = lineSeparatorArg;
            this.contentsChangedSinceLastRender = true;
            this.termiosTerminalAdapter = termiosTerminalAdapter;


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

            this.updateViewCoordinates();
        }

        /**
         * render all windows
         */
        public void renderContent() throws IOException {
            for (Window window : windows) {
                window.getView().render(windows.get(active).getView().hashCode());
                window.getHandler().setContentsChangedSinceLastRenderFalse();
            }
        }

        public void paintScreen() throws IOException {
            clearContent();
            renderContent();
            renderCursor();
            this.contentsChangedSinceLastRender = false;
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
         * render the cursor in the active view
         */
        public void renderCursor() throws IOException {
            windows.get(active).getView().renderCursor();
        }

        /**
         * closes the active window
         *
         * @return 0 if the window is safe to close and closed 1 if it has a dirty buffer 2 if it is not force closable
         */
        public int closeActive() {
            contentsChangedSinceLastRender = true;
            if (windows.get(active).getHandler().isSafeToClose()) {
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
            rootLayout = this.rootLayout.delete(windows.get(active).getView().hashCode());
            windows.get(active).getHandler().forcedClose();
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
            updateViewCoordinates();
            return 0;
        }

        public void passToActive(byte b) throws IOException {
            this.windows.get(active).getHandler().input(b);
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * Changes the focused {@link LayoutLeaf} to another.
         *
         * @param dir the direction to move focus to
         */
        public void moveFocus(MoveDirection dir) {
            contentsChangedSinceLastRender = true;
            int newActive = this.rootLayout.getNeighborsContainedHash(dir, this.windows.get(active).getView().hashCode());
            for (int i = 0; i < this.windows.size(); i++) {
                if (this.windows.get(i).getView().hashCode() == newActive) {
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
         * Rearranges the Layouts, depending on the argument given
         *
         * @param orientation clockwise or counterclockwise
         */
        public void rotateLayout(RotationDirection orientation) throws IOException {
            contentsChangedSinceLastRender = true;
            rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).getView().hashCode());
            updateViewCoordinates();
        }

        /**
         * let the active window know that the right arrow is pressed
         */
        public void handleArrowRight() {
            this.windows.get(active).getHandler().handleArrowRight();
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * let the active window know that the Left arrow is pressed
         */
        public void handleArrowLeft() {
            this.windows.get(active).getHandler().handleArrowLeft();
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * let the active window know that the Down arrow is pressed
         */
        public void handleArrowDown() {
            this.windows.get(active).getHandler().handleArrowDown();
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * let the active window know that the Up arrow is pressed
         */
        public void handleArrowUp() {
            this.windows.get(active).getHandler().handleArrowUp();
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * let the active window insert a separator
         */
        public void handleSeparator() throws IOException {
            this.windows.get(active).getHandler().handleSeparator();
            this.contentsChangedSinceLastRender = windows.get(active).getHandler().needsRerender();
        }

        /**
         * Opens the snake game by doing overwriting the active {@link DisplayFacade#windows}'s
         * {@link inputhandler.InputHandlingElement} to {@link SnakeInputHandler} and {@link View}
         * to {@link SnakeView}. We delete the active window and add a new window to the list.
         */
        public void openSnakeGame() throws IOException {
            this.contentsChangedSinceLastRender = true;
            // Get UI coords of current window to initialize snake view's playfield
            Coords coordsView = this.windows.get(active).getView().getRealUICoordsFromScaled(termiosTerminalAdapter);

            // Get the hash of the current active window, we need this to find&replace the layoutleaf's hashcode
            int hashActive = this.windows.get(active).getView().hashCode();

            // Remove the window & add the snake window.
            this.windows.remove(this.windows.get(active));

            SnakeWindow toAdd = new SnakeWindow(coordsView.width, coordsView.height, termiosTerminalAdapter);
            this.windows.add(toAdd);

            // Change hash code & update the view coordinates
            rootLayout.changeHash(hashActive, toAdd.getView().hashCode());
            this.updateViewCoordinates();

            // Set the active view to the snake view
            active = this.windows.size() - 1;
        }

        /**
         * Duplicates the active view by
         */
        public void duplicateActive() throws IOException {
            this.contentsChangedSinceLastRender = true;

            Window windowToAdd = windows.get(active).duplicate();
            windows.add(windows.size(), windowToAdd);

            rootLayout = rootLayout.insertRightOfSpecified(windows.get(active).getView().hashCode(), windowToAdd.getView().hashCode());
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

        Window getActiveWindow(){
            return this.windows.get(active);
        }

        private void updateViewCoordinates() {
            this.contentsChangedSinceLastRender = true;
            HashMap<Integer, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, 1, 1));
            for (Window w : windows) {
                w.getView().setScaledCoords(coordsMap.get(w.getView().hashCode()));
            }
        }

        /**
         * Gets the hashcode of the new active node after the current one is closed, returns null if no other node left
         *
         * @return
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

        public boolean getContentsChangedSinceLastRender() {
            return this.contentsChangedSinceLastRender;
        }
    }
