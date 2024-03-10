package layouttree;

public abstract class Layout implements Cloneable {
    protected Layout getRootLayoutUncloned() {
        if(this.parent != null){
            return parent.getRootLayoutUncloned();
        } else {
            return this;
        }
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

    protected boolean containsActive;

    public abstract void render();

/*    public abstract void closeActiveFile();
    public abstract void writeActiveFile();
    public abstract void deleteActiveFile();*/

    protected abstract void deleteLeftLeaf();
    protected void setParent(LayoutNode layoutNode){
        this.parent = layoutNode;
    };
    protected boolean getContainsActive(){
        return containsActive;
    };
    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir);
    protected abstract LayoutLeaf getLeftLeaf();
    protected abstract void sanitizeAsChildOfParent(LayoutNode futureParent);
    protected void setContainsActive(boolean active){
        this.containsActive = active;
    };

    protected abstract Layout clone();
    public abstract boolean equals(Object obj);
}
