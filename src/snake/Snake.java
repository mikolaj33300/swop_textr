package snake;

import util.MoveDirection;
import util.Pos;

public abstract class Snake {

    /**
     * The direction the snake is rotated towards
     */
    private MoveDirection direction;

    /**
     * The start position represents where the snake moves FROM
     * The end position where the snake goes to
     */
    private Pos start, end;

    /**
     * @param direction the direction to start in
     * @param start the position of the tail
     * @param end the position of the head
     */
    public Snake(MoveDirection direction, Pos start, Pos end) {
        this.direction = direction;        this.start = start;
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
        this.start = pos.clone();
    }

    /**
     * Updates the end position of snake. Used for moving the snake.
     * @param pos the new end position
     */
    protected void updateEnd(Pos pos) {
        this.end = pos.clone();
    }

    /**
     * set a new direction for the snake
     * @param dir the new direction for the snake to go to
     */
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

    /**
     * @return the direction the snake is going in
     */
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

    /**
     * Scales the snake respectively to the new scale variables.
     * Use this method with great care!
     * @param scaleX how much the snake X position should change
     * @param scaleY how much the snake Y position should change
     */
    public void scale(float scaleX, float scaleY) {
        this.start = new Pos((int) (start.x()*scaleX), (int) (start.y()*scaleY));
        this.end = new Pos((int) (end.x()*scaleX), (int) (end.y()*scaleY));
    }
}
