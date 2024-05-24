package util;

/**
 * Enum for the direction of a move (eg. moving cursor in a FileBuffer ormoving a snake in a game of snake)
 */
public enum MoveDirection {

    UP, DOWN, RIGHT, LEFT;

    /**
     * The opposite direction of a {@link MoveDirection} direction
     */
    private MoveDirection opposite;

    /**
     * Defines the opposite direction for each direction
     */
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
