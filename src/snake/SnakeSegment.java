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
     * each tick we need to update
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
