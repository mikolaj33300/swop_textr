package controller;

import ioadapter.ResizeListener;
import util.Coords;

import java.io.IOException;

public class DisplayFacadeResizeListener implements ResizeListener {

    /**
     * The display that this listener listens for resize events
     */
    private final DisplayFacade listeningDisplay;


    /**
     * Constructor for the DisplayFacadeResizeListener
     * @param listeningDisplay the display that this listener listens for resize events
     */
    DisplayFacadeResizeListener(DisplayFacade listeningDisplay){
        this.listeningDisplay = listeningDisplay;
    }

    /**
     * Notify the display of new coordinates
     * @param newCoords the new coordinates
     */
    @Override
    public void notifyNewCoords(Coords newCoords) throws IOException {
        listeningDisplay.repaintOnResize(newCoords);
    }
}
