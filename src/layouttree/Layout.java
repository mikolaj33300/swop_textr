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

    protected boolean containsActive;
    public abstract STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract STATUS_MOVE moveFocusRight() throws RuntimeException;
    public abstract STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException;
    protected abstract STATUS_ROTATE rotateRelationshipNeighborRight();


    protected boolean containsActive() {
        return containsActive;
    }

    public abstract void render();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    protected abstract void setInactive();

    protected abstract void mergeActiveAndRotate(DIRECTION dir);

    protected abstract LayoutLeaf getLeftLeaf();

    @Override
    protected abstract Layout clone();
}
