package snake;

import java.io.FileWriter;
import java.io.IOException;

import static snake.MoveDirection.DOWN;
import static snake.MoveDirection.UP;

/**
 * Information expert:
 * Cohesion: this class holds information about snake creation & logic behind controlling the snake.
 *           class makes sure that the creator does not have to worry about specifics, only about the location
 *           of the head of the snake
 * Creator: creates SnakeSegment
 * Low coupling: no coupling except superclasses
 * Indirection:
 * Polymorphism: yes, this is an implementation of an abstract class
 */
public class SnakeHead extends Snake {

    /**
     * Segments are locations where the snake has moved in another direction, splitting the snake's movement
     * direction. They are created and removed constantly.
     */
    private SnakeSegment[] segments;
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
     * Moves the snake in a different direction.
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
     */
    public void grow(int amount) {
        growState += amount;
    }

    /**
     * Ticks the snake. Makes sure that the snake progresses each tick & handles the
     * {@link SnakeHead#segments} array.
     */
    public void tick() {

        progress();

        // We delete a segment from the list if its start has reached its end
        if(this.segments.length > 0 && this.segments[0].canDelete())
            popSegment();

    }

    /**
     * Returns the segments of this snake. Used for rendering
     * @return an array of {@link Snake} objects
     */
    public SnakeSegment[] getSegments() {
        return this.segments.clone();
    }

    @Override
    public void progress() {
        // We move the snake head.
        this.updateEnd(getNext(getEnd()));

        // If the snake has eaten recently, we don't update the start position so the snake becomes longer
        if(growState != 0)
            growState--;
        // If the snake hasn't eaten we just update the head & its 'first' segment
        else {
            // Update the end of the snake (if there is no segment)
            if (this.segments.length == 0)
                this.updateStart(getNext(getStart()));
            else
            // If there is a segment, we can just call progress() on that Snake object
                this.segments[0].progress();
        }
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

    // Internal usage:

    /**
     * Inserts a segment at the end of the array
     * @param segment
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

    // Debug
    public static void log(String log) {

        try {
            FileWriter writer = new FileWriter("test/test.txt", true);
            writer.write("\n" + log);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Returns the length of the snake.
     * @return length integer
     */
    int getLength() {
        if(this.segments.length == 0) return Math.abs(getStart().distanceX(getEnd()));
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

}
