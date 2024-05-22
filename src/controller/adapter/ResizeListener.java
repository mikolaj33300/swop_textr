package controller.adapter;

import util.Coords;

import java.io.IOException;

public interface ResizeListener {

    /**
     * Listener to notify a change in coordinates
     * @param newCoords the new coordinates
     */
    public void notifyNewCoords(Coords newCoords) throws IOException;
}
