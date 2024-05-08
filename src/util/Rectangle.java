package util;

/**
 * This class is used to calculate the dimensions of each opened {@link ui.View} in the terminal.
 */
public class Rectangle {

    /**
     * The start X value for this rectangle
     */
    public final double startX;
    /**
     * The start Y value for this rectangle
     */
    public final double startY;
    /**
     * The width of the rectangle
     */
    public final double width;
    /**
     * The height of the rectangle
     */
    public final double height;

    /**
     * the constructor for the rectangle
     * @param startX the x starting crd
     * @param startY the y starting crd
     * @param width the width of our rectangle
     * @param height the height of our rectangle
     */
    public Rectangle(double startX, double startY, double width, double height){
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    /**
     * return a clone of this
     */
    public Rectangle clone(){
        return new Rectangle(startX, startY, width, height);
    }

    /***
     * @param o the object to compare againts
     * @return if this equals object o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(startX, rectangle.startX) == 0 && Double.compare(startY, rectangle.startY) == 0 && Double.compare(width, rectangle.width) == 0 && Double.compare(height, rectangle.height) == 0;
    }

    /**
     * Returns a string representation of this object. Used for testing & debugging
     * @return string version of this object
     */
    public String getPrint() {
        return "[" + startX + ", " + startY + "] -> [" + (startX+width) + ", " + (startY+height) + "]";
    }

}
