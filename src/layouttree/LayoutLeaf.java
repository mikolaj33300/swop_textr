package layouttree;

import files.FileBuffer;

public class LayoutLeaf extends Layout {
    private boolean isActive;
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.isActive = active;
    }
    protected void deleteLeftLeaf() {
        super.parent.delete(this);
    }
    public void moveFocus(DIRECTION dir) throws RuntimeException {

    }

    protected void moveFocusRight() throws RuntimeException {
        if(parent != null){
            this.isActive = false;
            parent.makeRightNeighbourActive(this);
        } else {
            throw new RuntimeException("Not implemented yet: child has no more neighbors");
        }

    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) {
        if(parent != null){
            LayoutLeaf newSibling = parent.getRightNeighbor(this);
            parent.deleteRightNeighbor(this);
            parent.mergeAndRotate(rot_dir, this, newSibling);
            return super.getRootLayoutUncloned();
        } else {
            throw new RuntimeException("Not implemented yet: child has no more neighbors");
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

    protected boolean containsActive() {
        return isActive;
    }

    public void render() {
        return;
    }

    @Override
    protected LayoutLeaf clone() {
        return new LayoutLeaf(containedFileBuffer.clone(), isActive);
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

    @Override
    protected void sanitizeInputChild(LayoutNode futureParent) {
        if(containsActive() && futureParent.containsActive()){
            throw new RuntimeException("Invalid child: more than two active");
        }
    }

    protected void setParent(LayoutNode layoutNode) {
        this.parent = layoutNode;
    }
}
