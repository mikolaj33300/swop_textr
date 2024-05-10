package util;

public class Coords {

    /**
     * The X start position of our coordinate section (top, left)
     */
    public final int startX;
    /**
     * The Y start position of our coordinate section (top, left)
     */
    public final int startY;
    /**
     * Width of this coordinate section
     */
    public final int width;
    /**
     * Height of this coordinate section
     */
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
     * @return a clone of this object, copying the class fields
     */
    @Override
    public Coords clone(){
        return new Coords(startX, startY, width, height);
    }

    /**
     * @return boolean determining if the given object is equal to this object
     * @param o the object to be compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return startX == coords.startX && startY == coords.startY && width == coords.width && height == coords.height;
    }
}
