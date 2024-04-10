package snake;

public class Segment {

    private final Pos pos1, pos2;

    public Segment(Pos pos1, Pos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Pos getStart() {
        return this.pos1;
    }

    public Pos getEnd() {
        return this.pos2;
    }

}
