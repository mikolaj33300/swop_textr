package ui;

import java.util.Objects;

public class Rectangle {
    public final double startX;
    public final double startY;
    public final double width;
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
}
