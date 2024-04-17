package ui;

import java.util.Objects;

public class UICoords {
    public final int startX;
    public final int startY;
    public final int width;
    public final int height;

    /**
     * constructor
     * @param startX the x start cord
     * @param startY the y start cord
     * @param width the ui width
     * @param height the ui height
     */
    public UICoords(int startX, int startY, int width, int height){
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    /**
     * @return a clone fo this object
     */
    public UICoords clone(){
        return new UICoords(startX, startY, width, height);
    }

    /**
     * @return if this object equals  o
     * @param object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UICoords uiCoords = (UICoords) o;
        return startX == uiCoords.startX && startY == uiCoords.startY && width == uiCoords.width && height == uiCoords.height;
    }
}
