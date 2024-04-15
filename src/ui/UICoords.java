package ui;

import java.util.Objects;

public class UICoords {
    public final int startX;
    public final int startY;
    public final int width;
    public final int height;

    public UICoords(int startX, int startY, int width, int height){
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public UICoords clone(){
        return new UICoords(startX, startY, width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UICoords uiCoords = (UICoords) o;
        return startX == uiCoords.startX && startY == uiCoords.startY && width == uiCoords.width && height == uiCoords.height;
    }
}
