package layouttree;

public abstract class Layout implements Cloneable {

    protected abstract void deleteLeftLeaf();

    public enum STATUS_MOVE {

        FOUND_ACTIVE,
        SUCCESS
    }

    public enum STATUS_ROTATE {
        FOUND_ACTIVE,
        SUCCESS
    }

    public enum DIRECTION {
        LEFT,
        RIGHT
    }

    public enum ROT_DIRECTION {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private Layout parent;
    private int height;
    private int width;

    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract void moveFocusRight() throws RuntimeException;

    protected abstract boolean containsActive();

    public abstract void render();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    protected abstract void setInactive();

    public abstract void rotateRelationshipNeighbor(ROT_DIRECTION rotdir);

    protected abstract LayoutLeaf getLeftLeaf();

    @Override
    protected abstract Layout clone();

    public abstract boolean equals(Layout layout);
}
