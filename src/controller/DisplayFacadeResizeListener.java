package controller;

import ioadapter.ResizeListener;
import util.Coords;

import java.io.IOException;

public class DisplayFacadeResizeListener implements ResizeListener {
    private final DisplayFacade listeningDisplay;

    DisplayFacadeResizeListener(DisplayFacade listeningDisplay){
        this.listeningDisplay = listeningDisplay;
    }

    @Override
    public void notifyNewCoords(Coords newCoords) throws IOException {
        listeningDisplay.repaintOnResize(newCoords);
    }
}
