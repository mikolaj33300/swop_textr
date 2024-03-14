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

    public abstract void deleteCharacter();

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

    public abstract void renderTextContent(int startX, int startY, int width, int height);

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

    public abstract void moveCursor(char c);
    public abstract void enterText(byte b);
    public abstract void saveActiveBuffer();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();

    public abstract void renderCursor(int startX, int startY, int width, int height);

    public abstract void enterInsertionPoint();

    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir);
    protected abstract LayoutLeaf getLeftLeaf();
    protected void setContainsActive(boolean active){
        this.containsActive = active;
    }
    public abstract boolean equals(Object obj);
    public abstract Layout clone();
}
