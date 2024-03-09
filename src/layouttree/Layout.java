package layouttree;

public abstract class Layout implements Cloneable {
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

    protected LayoutNode parent = null;

    public abstract void render();
    protected abstract void deleteLeftLeaf();
    protected abstract void setParent(LayoutNode layoutNode);
    protected abstract boolean containsActive();
    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    protected abstract void setInactive();
    public abstract void rotateRelationshipNeighbor(ROT_DIRECTION rotdir);
    protected abstract LayoutLeaf getLeftLeaf();
    protected abstract Layout clone();
    public abstract boolean equals(Layout layout);
}
