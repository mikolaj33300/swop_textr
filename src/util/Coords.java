package util;

public class Coords {
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
    public Coords(int startX, int startY, int width, int height){
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    /**
     * @return a clone fo this object
     */
    public Coords clone(){
        return new Coords(startX, startY, width, height);
    }

    /**
     * @return if this object equals  o
     * @param o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return startX == coords.startX && startY == coords.startY && width == coords.width && height == coords.height;
    }
}
