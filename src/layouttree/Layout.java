package layouttree;

public abstract class Layout implements Cloneable {

    protected abstract void deleteLeftLeaf();

    protected abstract void mergeActiveWith(Layout toMergeLayout);

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
    private Layout parent;
    private int height;
    private int width;

    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract void moveFocusRight() throws RuntimeException;
    public abstract STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException;
    protected abstract void rotateRelationshipNeighborRight();


    protected abstract boolean containsActive();

    public abstract void render();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    protected abstract void setInactive();

    protected abstract void mergeActiveAndRotate(DIRECTION dir);

    protected abstract LayoutLeaf getLeftLeaf();

    @Override
    protected abstract Layout clone();
}
