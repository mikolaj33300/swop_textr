package snake;

import util.MoveDirection;
import util.Pos;

class SnakeSegment extends Snake {

  /**
   * @param dir movedirection
   * @param start the tail
   * @param end the head
   */
    public SnakeSegment(MoveDirection dir, Pos start, Pos end) {
        super(dir, start, end);
    }

    /**
     * Function that implements the behaviour that the SnakeSegment should execute every 'tick'
     * This changes the location of this segment, to the next segment in the direction of the head (aka moving forward)
     */
    @Override
    protected void tick() {
        this.updateStart(getNext(getStart()));
    }

    /**
     * Determines if this segment can be deleted
     * @return a boolean determining if this segment has completed
     */
    public boolean canDelete() {
        return this.getStart().equals(getEnd());
    }


}
