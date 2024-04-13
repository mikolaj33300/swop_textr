package snake;

/**
 * Information expert: this class holds all the information about positioning and movement
 * Cohesion: subclasses can reuse this information without worrying about details
 * Creator: n/a this class holds information only
 * Low coupling: this class holds information about positions, subclasses can indirectly call upon this class without worrying about position updating
 * Indirection:
 * Polymorphism: yes, this is an abstract class
 */
public abstract class Snake {

    /**
     * The direction of the snake
     */
    private MoveDirection direction;
    /**
     * The start position represents from where the snake moves FROM
     * The end position is either the head or the 'knikpunt'
     */
    private Pos start, end;

    public Snake(MoveDirection direction, Pos start, Pos end) {
        this.direction = direction;
        this.start = start;
        this.end = end;
    }

    /**
     * Function that implements the behaviour that the snake should execute every 'tick'
     */
    protected abstract void tick();

    /**
     * Updates the start position of the snake, used for moving the snake.
     * @param pos the new start position
     */
    protected void updateStart(Pos pos) {
        this.start = pos;
    }

    /**
     * Updates the end position of snake. Used for moving the snake.
     * @param pos the new end position
     */
    protected void updateEnd(Pos pos) {
        this.end = pos;
    }

    protected void setDirection(MoveDirection dir) {
        this.direction = dir;
    }

    /**
     * Depending on the {@link Snake#direction}, a position is updated in that direction
     * @param reference the start position
     * @return returns the updated position
     */
    protected Pos getNext(Pos reference) {
        switch(this.direction) {
            case UP:
                return new Pos(reference.x(), reference.y()-1);
            case DOWN:
                return new Pos(reference.x(), reference.y()+1);
            case RIGHT:
                return new Pos(reference.x()+1, reference.y());
            default:
                return new Pos(reference.x()-1, reference.y());
        }
    }

    public MoveDirection getDirection() {
        return this.direction;
    }

    /**
     * Returns the start position of the snake
     * @return position
     */
    public Pos getStart() {
        return this.start.clone();
    }

    /**
     * Returns the end position of the snake
     * @return position
     */
    public Pos getEnd() {
        return this.end.clone();
    }

}
