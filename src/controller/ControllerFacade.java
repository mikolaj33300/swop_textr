package controller;

import controller.adapter.SwingTerminalAdapter;
import controller.adapter.TermiosTerminalAdapter;
import files.FileAnalyserUtil;
import exception.PathNotFoundException;
import layouttree.*;
import util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class ControllerFacade {
    private final byte[] lineSeparatorArg;
    private ArrayList<DisplayFacade> displays = new ArrayList<DisplayFacade>(1);
    private int active = 0;
    private TermiosTerminalAdapter initialTermiosAdapter;

    private ArrayList<ASCIIKeyEventListener> listenersToThisEvents;

    private HashMap<DisplayFacade, DisplayFacadeResizeListener> displayFacadeResizeListenerHashMap= new HashMap<DisplayFacade, DisplayFacadeResizeListener>();
    private HashMap<DisplayFacade, ASCIIKeyEventListener> displayFacadeAsciiListenerHashMap= new HashMap<DisplayFacade, ASCIIKeyEventListener>();

    private ArrayList<DisplayOpeningRequestListener> openingRequestListeners = new ArrayList<>(0);

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     * @throws IOException when the path is invalid
     */
    public ControllerFacade(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {

        this.lineSeparatorArg = FileAnalyserUtil.setLineSeparatorFromArgs(args[0]);
        this.initialTermiosAdapter = termiosTerminalAdapter;
        String[] paths;

        if (FileAnalyserUtil.isValidLineSeparatorString(args[0])) {
            paths = Arrays.copyOfRange(args, 1, args.length);
        } else {
            paths = args;
        }

        DisplayFacade initialDisplay = new DisplayFacade(paths, initialTermiosAdapter, lineSeparatorArg);
        this.displays.add(initialDisplay);
    }

    /**
     * save the active filebuffer
     */
    public void saveActive() {
        displays.get(active).saveActive();
    }

    /**
     * closes the active window
     *
     * @return 0 if the window is safe to close and closed 1 if it has a dirty buffer 2 if it is not force closable
     */
    public Pair<RenderIndicator, Integer> closeActive() {
        return displays.get(active).closeActive();
    }

    /**
     * Delegates a force close to the active {@link DisplayFacade}
     * @return 0 if we closed the active window 2 if we can't close it
     */
    public Pair<RenderIndicator, Integer> forceCloseActive() {
        return displays.get(active).forceCloseActive();
    }

    public RenderIndicator passToActive(byte b) throws IOException {
        return displays.get(active).passToActive(b);
    }

    /**
     * Changes the focused {@link DisplayFacade} to another.
     *
     * @param dir the direction to move focus to
     */
    public RenderIndicator moveFocus(MoveDirection dir) {
        displays.get(active).moveFocus(dir);
        return RenderIndicator.FULL;
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
     * @return
     */
    public RenderIndicator rotateLayout(RotationDirection orientation) throws IOException {
        displays.get(active).rotateLayout(orientation);
        return RenderIndicator.FULL;
    }

    /**
     * let the active window know that the right arrow is pressed
     *
     * @return
     */
    public RenderIndicator handleArrowRight() {
        return displays.get(active).handleArrowRight();
    }

    /**
     * let the active window know that the Left arrow is pressed
     */
    public RenderIndicator handleArrowLeft() {
        return displays.get(active).handleArrowLeft();
    }

    /**
     * let the active window know that the Down arrow is pressed
     */
    public RenderIndicator handleArrowDown() {
        return displays.get(active).handleArrowDown();
    }

    /**
     * let the active window know that the Up arrow is pressed
     */
    public RenderIndicator handleArrowUp() {
        return displays.get(active).handleArrowUp();
    }

    /**
     * let the active window insert a separator
     */
    public RenderIndicator handleSeparator() throws IOException {
        return displays.get(active).handleSeparator();
    }

    /**
     * Opens the snake game on the active display
     */
    public RenderIndicator openSnakeGame() throws IOException {
        return displays.get(active).openSnakeGame();
    }

    /**
     * Duplicates the active view by
     */
    public RenderIndicator duplicateActive() throws IOException {
        return displays.get(active).duplicateActive();
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
    ArrayList<DisplayFacade> getDisplays() {
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

/*    public void addDisplay(TermiosTerminalAdapter adapter) {
        String[] test = new String[1];
        test[0] = "long.txt";
        try {
            this.displays.add(new DisplayFacade(test, adapter, this.lineSeparatorArg));
            active = displays.size()-1;
            paintScreen();
        } catch (Exception e){
            System.out.println("adding/rendering display failed");
            System.out.println(e);
            System.exit(1);
        }
    }*/

    public void setActive(int a) {
        this.active = a;
    }

    public ArrayList<Window> getWindows() {
        //TODO: Should we clone here? Lets test it later and see if it breaks
        ArrayList<Window> toReturn = new ArrayList<>(0);
        for(DisplayFacade d : displays){
            toReturn.addAll(d.getWindows());
        }
        return toReturn;
    }

    public RenderIndicator openNewSwingFromActiveWindow() throws IOException {
        SwingTerminalAdapter newAdapter = new SwingTerminalAdapter();
        DisplayFacade newFacade = this.displays.get(active).requestOpeningNewDisplay(newAdapter);
        if(newFacade != null){
            this.displays.add(displays.size(), newFacade);


            ASCIIKeyEventListener newAsciiListener = new ASCIIKeyEventListener() {
                @Override
                public void notifyNormalKey(int byteInt) {
                    for(ASCIIKeyEventListener l : listenersToThisEvents){
                        l.notifyNormalKey(byteInt);
                    }
                }

                @Override
                public void notifySurrogateKeys(int first, int second) {
                    for(ASCIIKeyEventListener l : listenersToThisEvents){
                        l.notifySurrogateKeys(first, second);
                    }
                }
            };
            DisplayFacadeResizeListener newResizeListener = new DisplayFacadeResizeListener(newFacade);

            newAdapter.subscribeToResizeTextArea(newResizeListener);
            newAdapter.subscribeToKeyPresses(newAsciiListener);
            displayFacadeResizeListenerHashMap.put(newFacade, newResizeListener);
            displayFacadeAsciiListenerHashMap.put(newFacade, newAsciiListener);
            newFacade.paintScreen();
        }


         return RenderIndicator.FULL;
    }

    public void paintScreen() throws IOException {
        this.displays.get(active).paintScreen();
    }
}


