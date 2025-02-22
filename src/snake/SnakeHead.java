package snake;

import util.MoveDirection;
import util.Pos;

import static util.MoveDirection.DOWN;
import static util.MoveDirection.UP;

public class SnakeHead extends Snake {

    /**
     * Segments are locations where the snake has moved in another direction, splitting the snake's movement
     * direction. They are created and removed constantly.
     */
    private SnakeSegment[] segments;

    /**
     * Determines the amount the snake still has to grow
     */
    private int growState = 0;

    /**
     * Creates the Snake objects. Initially, we work with integers for positions.
     * @param length the start length of the snake
     * @param startX the x position where the snake should spawn
     * @param startY the y position where the snake should spawn
     */
    public SnakeHead(int length, int startX, int startY) {
        super(MoveDirection.RIGHT, new Pos(startX - length, startY), new Pos(startX, startY));
        this.segments = new SnakeSegment[] {};
    }

    /**
     * Moves the snake to a different direction.
     * @param dir a {@link MoveDirection} object.
     */
    public void move(MoveDirection dir) {
        // Ignore same direction & refuse direction opposite to current
        if(dir.equals(this.getDirection()) || dir.getOpposite().equals(this.getDirection()))
            return;
        insertSegment(
                new SnakeSegment(getDirection(), getStart(), getEnd())
        );
        setDirection(dir);
        this.updateStart(this.getEnd());
    }

    /**
     * Called when the head has consumed a fruit. This behaviour is controlled by {@link SnakeGame}
     * @param amount the number of segments to add
     */
    public void grow(int amount) {
        growState += amount;
    }

    /**
     * Returns the segments of this snake. Used for rendering
     * @return an array of {@link Snake} objects
     */
    public SnakeSegment[] getSegments() {
        return this.segments.clone();
    }

    /**
     * Function that implements the behaviour that the snake should execute every 'tick'
     * This includes gowing the snake, moving the snake, and deleting segments
     */
    @Override
    public void tick() {
        // We move the snake head.
        this.updateEnd(getNext(getEnd()));

        // If the snake has eaten recently, we don't update the last segment
        if(growState > 0)
            growState--;
        // Starve mechanic
        else if(growState < 0) {
            updateTail();
            checkSegments();
            updateTail();
            growState++;
        }
        // If the snake hasn't eaten we just update the head & its last segment
        else {
            updateTail();
        }

        checkSegments();
    }

    /**
     * Returns the string that represents the head of the snake. Used for rendering.
     * @return the head of the snake character
     */
    public String getHeadString() {
        return switch (getDirection()) {
            case UP -> "^";
            case DOWN -> "v";
            case RIGHT -> ">";
            case LEFT -> "<";
        };
    }

    /**
     * Checks the segments progress and deletes finished tails
     */
    void checkSegments() {
        if(this.segments.length > 0 && this.segments[0].canDelete())
            popSegment();
    }

    /**
     * Updates the tail of the snake. This can be a {@link SnakeSegment} or the {@link SnakeHead} itself.
     */
    void updateTail() {
        // Update the end of the snake (if there is no segment)
        if (this.segments.length == 0)
            this.updateStart(getNext(getStart()));
        else
            // If there is a segment, we can just call progress() on that Snake object
            this.segments[0].tick();
    }

    // Internal usage:

    /**
     * Inserts a segment at the end of the array
     * @param segment the snake segment to be inserted
     */
    private void insertSegment(SnakeSegment segment) {
        SnakeSegment[] segments = new SnakeSegment[this.segments.length+1];
        for(int i = 0; i < this.segments.length; i++)
            segments[i] = this.segments[i];
        segments[this.segments.length] = segment;
        this.segments = segments;
    }

    /**
     * Removes the segment at the start of the array
     */
    private void popSegment() {
        SnakeSegment[] segments = new SnakeSegment[this.segments.length-1];
        for(int i = 1; i < this.segments.length; i++)
            segments[i-1] = this.segments[i];
        this.segments = segments;
    }

    /**
     * Returns the length of the snake.
     * @return length integer
     */
    int getLength() {
        if(this.segments.length == 0) return getDirection() == UP || getDirection() == DOWN ? Math.abs(getStart().distanceY(getEnd())) : Math.abs(getStart().distanceX(getEnd()));
        else {
            int length = getDirection() == UP || getDirection() == DOWN ? Math.abs(getStart().distanceY(getEnd())) : Math.abs(getStart().distanceX(getEnd()));
            for(int i = segments.length-1; i >= 0; i--) {
                SnakeSegment s = segments[i];
                if (s.getDirection() == UP || s.getDirection() == DOWN) {
                    length += Math.abs(s.getStart().distanceY(s.getEnd()));
                } else {
                    length += Math.abs(s.getStart().distanceX(s.getEnd()));
                }
            }
                return length;
        }
    }

    /**
     * Scales the snake by a scaleX and a scaleY factor
     * @param scaleX the factor to scale the snake in the x direction
     * @param scaleY the factor to scale the snake in the y direction
     */
    @Override
    public void scale(float scaleX, float scaleY) {
        for (SnakeSegment segment : segments) segment.scale(scaleX, scaleY);
        super.scale(scaleX, scaleY);
    }

}
