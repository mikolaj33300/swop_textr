package layouttree;

import java.io.IOException;

public abstract class Layout implements Cloneable {

    protected Layout getRootLayoutUncloned() {
        if(this.parent != null){
            return parent.getRootLayoutUncloned();
        } else {
            return this;
        }
    }

    public Layout getRootLayout() {
        if(this.parent != null){
            return parent.getRootLayout();
        } else {
            return this.clone();
        }
    }

    public LayoutNode getParent(){
        return parent.clone();
    }

    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);

    public abstract void deleteCharacter();

    public abstract void closeActive();

    public abstract void clearContent() throws IOException;

    protected LayoutNode parent = null;

    protected boolean containsActiveView;

    public abstract void renderContent() throws IOException;

/*    public abstract void closeActiveFile();
    public abstract void writeActiveFile();
    public abstract void deleteActiveFile();*/

    protected abstract void deleteLeftLeaf();
    protected void setParent(LayoutNode layoutNode){
        this.parent = layoutNode;
    }
    public boolean getContainsActiveView(){
        return containsActiveView;
    }
    public abstract void moveFocus(DIRECTION dir) throws RuntimeException;

    public abstract void moveCursor(char c);
    public abstract void enterText(byte b);
    public abstract void saveActiveBuffer();
    protected abstract void makeLeftmostLeafActive();
    protected abstract void makeRightmostLeafActive();

    public abstract void renderCursor() throws IOException;

    public abstract void enterInsertionPoint();

    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir);
    protected abstract LayoutLeaf getLeftLeaf();
    protected void setContainsActiveView(boolean active){
        this.containsActiveView = active;
    }

    public abstract void forcedCloseActive();

    public abstract boolean equals(Object obj);
    public abstract Layout clone();
}
