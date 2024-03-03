package LayoutTree;

public abstract class Layout implements Cloneable {
    public enum STATUS_MOVE{
        SUCCESS,
        CANNOT_FIND,
        FOUND
    }

    public enum STATUS_ROTATE{
        SUCCESS,
        CANNOT_FIND,
        FOUND
    }

    public enum DIRECTION{
        LEFT,
        RIGHT
    }
    private Layout parent;
    private int height;
    private int width;

    public abstract STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException;
    public abstract STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException;

    public abstract void render();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();

    protected void mergeActiveAndRotate(LayoutLeaf leftLeaf, DIRECTION dir){
        applyToFocused(...);
    };

    protected abstract LayoutLeaf getLeftLeaf();

    @Override
    protected abstract Layout clone();
}
