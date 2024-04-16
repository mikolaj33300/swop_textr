package snake;

public enum MoveDirection {

    UP, DOWN, RIGHT, LEFT;

    private MoveDirection opposite;

    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }

    public MoveDirection getOpposite() {
        return opposite;
    }

}
