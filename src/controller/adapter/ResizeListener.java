package controller.adapter;

import util.Coords;

import java.io.IOException;

public interface ResizeListener {
    public void notifyNewCoords(Coords newCoords) throws IOException;
}
