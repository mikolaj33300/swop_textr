package util;

public enum MoveDirection {

    UP, DOWN, RIGHT, LEFT;

    /**
     * The opposite direction of a {@link MoveDirection} direction
     */
    private MoveDirection opposite;

    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }

    /**
     * @return the opposite direction
     */
    public MoveDirection getOpposite() {
        return opposite;
    }

}
