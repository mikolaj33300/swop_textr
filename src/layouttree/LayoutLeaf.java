package layouttree;

import files.FileBuffer;

/*
    LayoutLeaf represents the leaf layout-structure
    LayoutLeaf inherets from Layout
 */
public class LayoutLeaf extends Layout {
    private FileBuffer containedFileBuffer;

    /**
       Constructor for LayoutLeaf, clones its arguments to prevent representation  exposure
     */
    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.setContainsActive(active);
    }

    /**
     * Deletes the mostleft this leaf's parent
     * Since the parent function found this leaf as leftmost, it will be removed
     */
    protected void deleteLeftLeaf() {
        if(super.parent != null) {
            super.parent.delete(this);
        }
    }
    /**
     Calls the correct moveFocus variant moveFocusRight() or moveFocusLeft(),
      based on the DIRECTION argument
     */
    public void moveFocus(DIRECTION dir){
        if(dir == DIRECTION.RIGHT){
            moveFocusRight();
        } else {
            moveFocusLeft();
        }
    }

    /**
     Moves the focus from the current Layout to the one right of it in the layout-tree
     I there is only one Layout in the layout-tree, that Layout stays active
     */
    private void moveFocusRight(){
        if(parent != null){
            this.setContainsActive(false);
            parent.makeRightNeighbourActive(this);
        }
    }
    /**
     Moves the focus from the current Layout to the one left f    it in the layout-tree
     I there is only one Layout in the layout-tree, that Layout stays active
     */
    private void moveFocusLeft(){
        if(parent != null){
            this.setContainsActive(false);
            parent.makeLeftNeighbourActive(this);
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) {
        if(parent != null) {
            LayoutLeaf newSibling = parent.getRightNeighbor(this);
            parent.deleteRightNeighbor(this);
            parent.mergeAndRotate(rot_dir, this, newSibling);
            return super.getRootLayoutUncloned();
        }
        else{
            return super.getRootLayoutUncloned();
        }
    }

    /**
     * Determines if the given LayoutLeaf and Object are equal content-wise (not reference-wise)
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LayoutLeaf leaf) {
            return leaf.containedFileBuffer.equals(this.containedFileBuffer) && (this.getContainsActive() == leaf.getContainsActive());
        } else {
            return false;
        }
    }

    /**
     Renders the current layout-tree
     Renders:
      All the instances of LayoutLeaf
      A statusbar per file
      Two scrollbars per file (one horizontal and one vertical)
     The insertionPoint in the current active LayoutLeaf
     */
    public void render() {
    }

    /**
     Makes a deepcopy of LayoutLeaf
     The references to this object and its contents will be removed
      */
    @Override
    protected LayoutLeaf clone() {
        return new LayoutLeaf(containedFileBuffer.clone(), getContainsActive());
    }

    /**
      Makes the leftmost leaf active
      This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
      */
    @Override
    protected void makeLeftmostLeafActive() {
        setContainsActive(true);
    }

    /**
     Makes the rightmost leaf active
     This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected void makeRightmostLeafActive() {
        setContainsActive(true);
    }

    /**
     Returns the leftmost leaf of the current layout-level
     As this a leaf of the layout-structure,
     it does not have any children and will return a copy of itself
     */
    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

    /**
     Checks whether the current layout-structure (parent and children) is valid
     Checks if there are multiple active children in the closeby layout-structure
     */
    @Override
    protected void sanitizeAsChildOfParent(LayoutNode futureParent) {
        if(getContainsActive() && futureParent.containsActive()){
            throw new RuntimeException("Invalid child: more than two active");
        }
    }
}

