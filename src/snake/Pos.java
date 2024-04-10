package snake;

public class Pos {

    private final int x, y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int distanceX(Pos pos) {
        return Math.abs(x - pos.x);
    }

    public int distanceY(Pos pos) {
        return Math.abs(y - pos.y);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Pos p)
            return p.x == x && p.y == y;
        return false;
    }

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
        if(start.x() == end.x() && start.x() == test.x())
            return Math.abs(start.y() - test.y()) <= Math.abs(start.y() - end.y());
            // Start & End is vertical
        else if(start.y() == end.y() && start.y() == test.y())
            return Math.abs(start.x() - test.x()) <= Math.abs(start.x() - end.x());
        return false;
    }

}
