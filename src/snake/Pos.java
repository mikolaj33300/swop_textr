package snake;

public class Pos {

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
     * @return the x value
     */
    public int x() {
        return this.x;
    }

    /**
     * @return the y value
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
     * Determines if the test position is inbetween the start & end positions.
     * If start & end are not on the same axis {@link Pos#x()} or {@link Pos#y()} are not equal,
     * then the method will always fail.
     * @param start start position
     * @param end end position
     * @param test the position that should be tested
     * @return a boolean determining if the test position is inbetween start & end
     */
    public static boolean isBetween1D(Pos start, Pos end, Pos test) {
        // Start & End is horizontal
        if(start.x() == end.x() && start.x() == test.x()) {
            /*SnakeHead.log("X Start = " + start.getPrint() + " - End: " + end.getPrint() + " -> test: " + test.getPrint()
            + " -> bool: " + (start.y <= test.y && end.y >= test.y));*/
            return start.y <= test.y && end.y >= test.y;
            // Start & End is vertical
        }
        else if(start.y() == end.y() && start.y() == test.y()) {
            /*SnakeHead.log("Y Start = " + start.getPrint() + " - End: " + end.getPrint() + " -> test: " +
                    test.getPrint() + " return: " + (start.x <= test.x && end.x >= test.x));*/
            return start.x <= test.x && end.x >= test.x;
        }
        return false;
    }

}
