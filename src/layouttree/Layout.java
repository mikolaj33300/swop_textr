package layouttree;

public abstract class Layout implements Cloneable {
    protected Layout getRootLayoutUncloned() {
        if(this.parent != null){
            return parent.getRootLayoutUncloned();
        } else {
            return this;
        }
    }

    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);

    public enum DIRECTION {
        LEFT,
        RIGHT
    }

    public enum ROT_DIRECTION {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    protected LayoutNode parent = null;

    protected boolean containsActive;

    public abstract void render(int startX, int startY, int width, int height);

/*    public abstract void closeActiveFile();
    public abstract void writeActiveFile();
    public abstract void deleteActiveFile();*/

    protected abstract void deleteLeftLeaf();
    protected void setParent(LayoutNode layoutNode){
        this.parent = layoutNode;
    }
    public boolean getContainsActive(){
        return containsActive;
    }
    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();
    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir);
    protected abstract LayoutLeaf getLeftLeaf();
    protected void setContainsActive(boolean active){
        this.containsActive = active;
    }
    public abstract boolean equals(Object obj);
    public abstract Layout clone();
}
