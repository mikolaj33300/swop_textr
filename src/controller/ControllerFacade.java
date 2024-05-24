package controller;

import ioadapter.ASCIIKeyEventListener;
import ioadapter.SwingTerminalAdapter;
import ioadapter.TermiosTerminalAdapter;
import files.FileAnalyserUtil;
import exception.PathNotFoundException;
import ui.UserPopupBox;
import util.*;
import window.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ControllerFacade {

    /**
     * The line separator of the system
     */
    private final byte[] lineSeparatorArg;

    /**
     * The list of displays on which TextR is running. Every display is opened in a separate terminal
     */
    private ArrayList<DisplayFacade> displays = new ArrayList<DisplayFacade>(0);

    private boolean terminalDisplayStillContainsViews;

    /**
     * The index of the active display in the displays list
     */
    private int active = 0;

    private TermiosTerminalAdapter initialTermiosAdapter;

    /**
     * The listeners to the key events
     */
    private ArrayList<ASCIIKeyEventListener> listenersToThisEvents;

    /**
     * The listeners to the resize events
     */
    private HashMap<DisplayFacade, DisplayFacadeResizeListener> displayFacadeResizeListenerHashMap= new HashMap<DisplayFacade, DisplayFacadeResizeListener>();

    private HashMap<DisplayFacade, ASCIIKeyEventListener> displayFacadeAsciiListenerHashMap= new HashMap<DisplayFacade, ASCIIKeyEventListener>();


    /**
     * Creates a ControllerFacade object.
     * Creates a {@link DisplayFacade} object which represents the first display opened by textr.
     * @throws IOException when the path provided to it is invalid
     */
    public ControllerFacade(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {
        terminalDisplayStillContainsViews = true;
        this.listenersToThisEvents = new ArrayList<>(0);
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
    public Pair<RenderIndicator, GlobalCloseStatus> closeActive() {
        WindowCloseStatus closeStatusResult = displays.get(active).closeActive().b;
        if(active == 0){
            if(closeStatusResult == WindowCloseStatus.LAST_WINDOW_CLOSED){
                if(displays.size()>1){
                    terminalDisplayStillContainsViews = false;
                    return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_TERMINAL_SWING_REMAINS);
                } else {
                    return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_ALL_DISPLAYS);
                }

            } else if(closeStatusResult == WindowCloseStatus.UNSAFE_CLOSE){
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.DIRTY_CLOSE_PROMPT);
            } else {
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_SUCCESFULLY);
            }
        } else {
            if(closeStatusResult == WindowCloseStatus.LAST_WINDOW_CLOSED){
                unsubscribeListenersDueToCloseActive();
                displays.remove(active);
                active = 0;
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_LAST_VIEW_ON_SWING);
            } else if(closeStatusResult == WindowCloseStatus.UNSAFE_CLOSE){
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.DIRTY_CLOSE_PROMPT);
            } else {
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_SUCCESFULLY);
            }
        }
    }

    /**
     * Delegates a force close to the active {@link DisplayFacade}
     * @return 0 if we closed the active window 2 if we can't close it
     */
    public Pair<RenderIndicator, GlobalCloseStatus> forceCloseActive() {
        WindowCloseStatus closeStatusResult = displays.get(active).forceCloseActive().b;
        if(active == 0){
            if(closeStatusResult == WindowCloseStatus.LAST_WINDOW_CLOSED){
                if(displays.size()>1){
                    terminalDisplayStillContainsViews = false;
                    return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_TERMINAL_SWING_REMAINS);
                } else {
                    return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_ALL_DISPLAYS);
                }
            } else {
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_SUCCESFULLY);
            }
        } else {
            if(closeStatusResult == WindowCloseStatus.LAST_WINDOW_CLOSED){
                unsubscribeListenersDueToCloseActive();
                displays.remove(active);
                active = 0;
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_LAST_VIEW_ON_SWING);
            } else {
                return new Pair<>(RenderIndicator.FULL, GlobalCloseStatus.CLOSED_SUCCESFULLY);
            }
        }
    }

    private void unsubscribeListenersDueToCloseActive() {
        displays.get(active).getTermiosTerminalAdapter().unsubscribeFromKeyPresses(displayFacadeAsciiListenerHashMap.get(displays.get(active)));
        displays.get(active).getTermiosTerminalAdapter().unsubscribeFromResizeTextArea(displayFacadeResizeListenerHashMap.get(displays.get(active)));
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


            //Will notify controller above it, the keypresses end up at the usecasecontroller to be converted to a system call
            ASCIIKeyEventListener newAsciiListener = new ASCIIKeyEventListener() {
                @Override
                public void notifyNormalKey(int byteInt) {
                    active = displays.indexOf(newFacade);

                    for(ASCIIKeyEventListener l : listenersToThisEvents){
                        l.notifyNormalKey(byteInt);
                    }
                }

                @Override
                public void notifySurrogateKeys(int first, int second) {
                    active = displays.indexOf(newFacade);

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

    /**
     * Renders every element on all displays (a change on one can be reflected on others)
     */
    public void paintScreen() throws IOException {
        if(terminalDisplayStillContainsViews==true){
            for(DisplayFacade f : displays){
                f.paintScreen();
            }
        } else {
            
            new UserPopupBox((displays.size()-1)+" windows open; please close them to quit Textr.", initialTermiosAdapter).render();
            for(int i = 1; i<displays.size(); i++){
                displays.get(i).paintScreen();
            }
        }

    }

    /**
     * Subscribes a listener to key presses
     * @param l the listener to subscribe
     */
    public void subscribeToKeyPresses(ASCIIKeyEventListener l) {
        this.listenersToThisEvents.add(l);
    }

    /**
     * Unsubscribes a listener from key presses
     * @param asciiEventListener the listener to unsubscribe
     */
    public void unsubscribeFromKeyPresses(ASCIIKeyEventListener asciiEventListener) {
        this.listenersToThisEvents.remove(asciiEventListener);
    }

    public DisplayFacade getActiveDisplay() {
        return this.displays.get(active);
    }
}


