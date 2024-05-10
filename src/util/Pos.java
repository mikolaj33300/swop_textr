package util;

public class Pos {

    /**
     * The x, y coordinates of this position
     */
    private final int x, y;

    /**
     * @param x the x position
     * @param y the y position
     */
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x value of this position
     */
    public int x() {
        return this.x;
    }

    /**
     * @return the y value of this position
     */
    public int y() {
        return this.y;
    }

    /**
     * @param pos the other point
     * @return the x distance between the two points
     */
    public int distanceX(Pos pos) {
        return Math.abs(x - pos.x);
    }

    /**
     * @param pos the other point
     * @return the y distance between the two points
     */
    public int distanceY(Pos pos) {
        return Math.abs(y - pos.y);
    }

    /**
     * @param o the object to compare to
     * @return if this equals the given object
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Pos p)
            return p.x == x && p.y == y;
        return false;
    }

    /**
     * @return a clone of this object
     */
    @Override
    public Pos clone() {
        return new Pos(x, y);
    }

    /**
     * Returns a string version of the position in [X,Y] format
     * @return string representation
     */
    public String getPrint() {
        return "[" + x + ", " + y + "]";
    }

    /**
     * Determines if the test position is inbetween the start and end positions.
     * If start and end are not on the same axis PoxX or PoxY are not equal,
     * then the method will always fail.
     * @param start start position
     * @param end end position
     * @param test the position that should be tested
     * @return a boolean determining if the test position is inbetween start and end
     */
    public static boolean isBetween1D(Pos start, Pos end, Pos test) {
        // Start & End is horizontal
        if(start.x() == end.x() && start.x() == test.x())
            return start.y <= test.y && end.y >= test.y;
        else if(start.y() == end.y() && start.y() == test.y())
            return start.x <= test.x && end.x >= test.x;
        return false;
    }

}
