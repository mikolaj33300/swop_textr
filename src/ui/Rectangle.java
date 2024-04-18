package ui;

import java.util.Objects;

public class Rectangle {
    public final double startX;
    public final double startY;
    public final double width;
    public final double height;
    public Rectangle(double startX, double startY, double width, double height){
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public Rectangle clone(){
        return new Rectangle(startX, startY, width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(startX, rectangle.startX) == 0 && Double.compare(startY, rectangle.startY) == 0 && Double.compare(width, rectangle.width) == 0 && Double.compare(height, rectangle.height) == 0;
    }

    public String getPrint() {
        return "[" + startX + ", " + startY + "] -> [" + (startX+width) + ", " + (startY+height) + "]";
    }

}
