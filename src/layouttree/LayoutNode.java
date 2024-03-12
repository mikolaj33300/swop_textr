package layouttree;

import java.util.ArrayList;

public abstract class LayoutNode extends Layout {
    /**
     * The children of this LayoutNode in the layout-tree
     */
    protected ArrayList<Layout> children;

    /**
     * Constructor for LayoutNode, clones its arguments to prevent representation exposure
     * LayoutNodes need atleast 2 children in its children-array
     */
    public LayoutNode(ArrayList<Layout> newChildren){
        if(newChildren.size()<2){
            throw new RuntimeException("newChildren too short");
        }
        this.children = new ArrayList<>();
        for(Layout l : newChildren){
            if(l.isAllowedToBeChildOf(this))
                this.insertDirectChild(l.clone());
        }
    }

    /**
     * Returns the orientation of the LayoutNode
     */
    public abstract Orientation getOrientation();

    /**
     * Returns a deepcopy of the children of this LayoutNode
     */
    public ArrayList<Layout> getDirectChildren(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return deepCopyList;
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public void moveFocus(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.getContainsActive()){
                l.moveFocus(dir);
                return;
            }
        }
    }

    public void insertDirectChild(Layout toInsert){
        if(toInsert.isAllowedToBeChildOf(this)){
            Layout cloneOfInsert = toInsert.clone();
            cloneOfInsert.setParent(this);
            this.children.add(cloneOfInsert);
            if(cloneOfInsert.getContainsActive()){
                this.containsActive = true;
            }
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.getContainsActive()){
                return l.rotateRelationshipNeighbor(rot_dir);
            }
        }
        throw new RuntimeException("Contains no active leaf!");
    }

    protected LayoutLeaf getRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            return children.get(index + 1).getLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            return parent.getRightNeighbor(this); //called when we need to backtrack one level up
        }
    }

    protected void deleteRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            children.get(index + 1).deleteLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            parent.deleteRightNeighbor(this); //called when we need to backtrack one level up
        }
    }

    /**
     * Checks if parameter layout is of type {@link LayoutNode}, and if the children match
     * the layout of this node. All leaves must have a FileBuffer containing the same path.
     * ! Assumes the children are always put in the same sequence upon creation of layout
     */
    @Override
    public boolean equals(Object node) {
        if(node instanceof LayoutNode layoutNode) {
            //Check objects for same activity-status
            if(this.getContainsActive() != layoutNode.getContainsActive()){
                return false;
            }
            // Return early when the amount of children don't match.
            if(layoutNode.children.size() != this.children.size()) return false;
            // Loop over the children of both
            for(int i = 0; i < layoutNode.children.size(); i++) {
                // We keep checking if they are equal, if not we return early.
                if(!layoutNode.children.get(i).equals(this.children.get(i))) return false;
            }
            // If the process didn't find disparities, the layouts are equal.
            return true;
        }
        return false;
    }

    protected boolean containsActive() {
        return containsActive;
    }

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    protected void delete(Layout l){
        children.remove(l);
        this.fixInvarsOfChangedTree();
    }

    protected void makeLeftmostLeafActive() {
        this.containsActive=true;
        children.get(0).makeLeftmostLeafActive();
    }

    protected void makeRightmostLeafActive() {
        this.containsActive=true;
        children.get(children.size()-1).makeRightmostLeafActive();
    }

    protected LayoutLeaf getLeftLeaf(){
        return children.get(0).getLeftLeaf();
    }

    //makes the leftmost leaf that is to the right of the given subtree active
    //Requires passed layout to be a child of this.
    protected void makeRightNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index < children.size() - 1) {
            children.get(index + 1).makeLeftmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            if(parent != null){
                containsActive = false;
                parent.makeRightNeighbourActive(this); //called when we need to backtrack one level up
            } else {
                makeRightmostLeafActive();
            }
        }
    }

    //see makeRightNeighbourActive
    protected void makeLeftNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index > 0) {
            children.get(index - 1).makeRightmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            if(parent != null){
                containsActive = false;
                parent.makeLeftNeighbourActive(this); //called when we need to backtrack one level up
            } else {
                makeLeftmostLeafActive();
            }
       }
    }

    //replaces the given child with a new layoutnode with the child and its sibling rotated
    protected void mergeWithSiblingAndRotate(ROT_DIRECTION rotdir, Layout child) {
        LayoutNode newChild = getNewMergedRotatedChild(rotdir, child);
        this.replaceWithNewLayout(child, newChild);
    }

    //Returns a layoutnode containing a child and a new specified sibling
    protected abstract LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child);

    private void replaceWithNewLayout(Layout toReplace, Layout newLayout) {
        int index = children.indexOf(toReplace);
        newLayout.setParent(this);
        children.set(index, newLayout);
        newLayout.setParent(this);
        this.fixInvarsOfChangedTree();
    }

    //Bottom up fixing of tree structure with unary and binary constraints.
    //Given that the children Layouts are in a consistent state with this
    //(= could exist as root on its own and are oriented differently)
    //make the current node and its parent (may be null) consistent with each other.
    //Rules: A parent's child LayoutNodes aren't empty, don't contain just 1 element
    // and are oriented differently.
    private void fixInvarsOfChangedTree(){
        if(parent != null){
            if(this.getOrientation() == parent.getOrientation()){
                parent.absorbChildrenAndReplace(this);
                parent.fixInvarsOfChangedTree();
                return;
            }
        }
        if(children.size()==1){
            if(parent != null){
                parent.absorbChildrenAndReplace(this);
                parent.fixInvarsOfChangedTree();
            } else {
                this.children.get(0).setParent(null);
            }
        } else if (children.isEmpty()){
            if(parent != null){
                parent.delete(this);
                parent.fixInvarsOfChangedTree();
            }
        }
    }

    protected void absorbChildrenAndReplace(LayoutNode toAbsorb){
        int index = children.indexOf(toAbsorb);
        for(Layout child : toAbsorb.children){
            child.setParent(this);
        }
        this.children.addAll(index, toAbsorb.children);
    }

    @Override
    protected abstract LayoutNode clone();

    /**
     * Enumerator that represents whether the Layouts inside this Layout
     * will be stacked (VERTICAL) or side-by-side (HORIZONTAL)
     */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
