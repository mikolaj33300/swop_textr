package snake;

class SnakeSegment extends Snake {

    public SnakeSegment(MoveDirection dir, Pos start, Pos end) {
        super(dir, start, end);
    }

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
