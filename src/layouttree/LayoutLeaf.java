package layouttree;

import files.FileBuffer;

public class LayoutLeaf extends Layout {
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.setContainsActive(active);
    }
    protected void deleteLeftLeaf() {
        super.parent.delete(this);
    }
    public void moveFocus(DIRECTION dir) throws RuntimeException {
        if(dir == DIRECTION.RIGHT){
            moveFocusRight();
        } else {
            moveFocusLeft();
        }
    }

    private void moveFocusRight() throws RuntimeException {
        if(parent != null){
            this.setContainsActive(false);
            parent.makeRightNeighbourActive(this);
        } else {
            throw new RuntimeException("Not implemented yet: child has no more neighbors");
        }
    }

    private void moveFocusLeft() throws RuntimeException {
        if(parent != null){
            this.setContainsActive(false);
            parent.makeLeftNeighbourActive(this);
        } else {
            throw new RuntimeException("Not implemented yet: child has no more neighbors");
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) {
        if(parent != null){
            parent.mergeWithSiblingAndRotate(rot_dir, this);
            parent.deleteRightNeighbor(this);
            return super.getRootLayoutUncloned();
        } else {
            throw new RuntimeException("Not implemented yet: child has no more neighbors");
        }

    }

    /**
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LayoutLeaf leaf)
            return leaf.containedFileBuffer.equals(this.containedFileBuffer) && (this.getContainsActive() == leaf.getContainsActive());
        else return false;
    }

    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode futureParent) {
        if(getContainsActive() && futureParent.containsActive()){
            throw new RuntimeException("Invalid child: more than two active");
        } else {
            return true;
        }
    }

    public void render() {
    }

    @Override
    protected LayoutLeaf clone() {
        return new LayoutLeaf(containedFileBuffer.clone(), getContainsActive());
    }

    @Override
    protected void makeLeftmostLeafActive() {
        setContainsActive(true);
    }

    @Override
    protected void makeRightmostLeafActive() {
        setContainsActive(true);
    }

    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

/*    @Override
    protected void sanitizeAsChildOfParent(LayoutNode futureParent) {
        if(getContainsActive() && futureParent.containsActive()){
            throw new RuntimeException("Invalid child: more than two active");
        }
    }*/
}

