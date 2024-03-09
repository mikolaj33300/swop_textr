package layouttree;

import files.FileBuffer;

import java.util.*;

import static layouttree.Layout.Orientation.HORIZONTAL;
import static layouttree.Layout.Orientation.VERTICAL;

public class LayoutLeaf extends Layout {

    private LayoutNode parent;
    private boolean isActive;
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.isActive = active;
    }
    protected void deleteLeftLeaf() {
        parent.delete(this);
    }
    public void moveFocus(DIRECTION dir) throws RuntimeException {

    }

    protected void moveFocusRight() throws RuntimeException {
        this.isActive = false;
        parent.makeRightNeighbourActive(this);
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return STATUS_ROTATE.FOUND_ACTIVE;
    }


    public void rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) {
        LayoutLeaf newSibling = parent.getRightNeighbor(this);
        parent.deleteRightNeighbor(this);
        if(rot_dir==ROT_DIRECTION.CLOCKWISE){
            parent.mergeAndRotateClockwise(this, newSibling);
        } else {
            parent.mergeAndRotateCounterclockwise(this, newSibling);
        }

    }

    /**
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Layout layout) {
        if(layout instanceof LayoutLeaf leaf)
            return leaf.containedFileBuffer.equals(this.containedFileBuffer);
        else return false;
    }

    public LayoutLeaf clone() {
        return new LayoutLeaf(containedFileBuffer.clone(), isActive);
    }

    protected boolean containsActive() {
        return isActive;
    }

    public void render() {
        return;
    }

    @Override
    protected void makeLeftmostLeafActive() {
        isActive = true;
    }

    @Override
    protected void makeRightmostLeafActive() {
        isActive = true;
    }

    @Override
    protected void setInactive() {
        isActive = false;
    }

    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

    protected void setParent(LayoutNode layoutNode) {
        this.parent = layoutNode;
    }
}
