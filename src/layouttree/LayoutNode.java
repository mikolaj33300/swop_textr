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
            if(parent != null){
                return parent.getRightNeighbor(this);
            } else {
                return null;
            }
             //called when we need to backtrack one level up
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

    protected boolean containsActive() {
        return containsActive;
    }

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    protected void delete(Layout l){
        children.remove(l);
        if(this.children.size() == 1){
            if(this.parent != null){
                this.parent.children.set(this.parent.children.indexOf(this), this.children.get(0));
                this.children.get(0).setParent(this.parent);
            }
        }
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
    //Returns the new root layout of the rotated node
    protected Layout mergeWithSibling(ROT_DIRECTION rotdir, Layout child) {
        LayoutNode newChild = getNewMergedRotatedChild(rotdir, child);
        if(newChild==null){
            return child.getRootLayoutUncloned();
        }
        this.replaceWithNewLayout(child, newChild);
        this.deleteRightNeighbor(newChild);
        newChild.fixChangedTreeFromNewNode();
        return newChild.getRootLayoutUncloned();
    }

    //Returns a layoutnode containing a child and a new specified sibling
    protected abstract LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child);

    private void replaceWithNewLayout(Layout toReplace, Layout newLayout) {
        int index = children.indexOf(toReplace);
        children.set(index, newLayout);
        newLayout.setParent(this);
    }

    //Bottom up fixing of tree structure with unary and binary constraints.
    //Given that the children Layouts are in a consistent state with this
    //(= could exist as root on its own and are oriented differently)
    //make the current node and its parent (may be null) consistent with each other.
    //Rules: A parent's child LayoutNodes aren't empty, don't contain just 1 element
    // and are oriented differently.
    private void fixChangedTreeFromNewNode(){
        if(parent != null) {
            //absorb this and siblings if orientations of parent
            if (this.getOrientation() == parent.getOrientation()) {
                for (Layout l : this.children) {
                    l.setParent(parent);
                }
                parent.children.addAll(parent.children.indexOf(this), this.children);
                parent.children.remove(this);
            }
            if(parent.children.size()==1){
                if(parent.parent != null) {
                    this.parent.parent.children.set(this.parent.parent.children.indexOf(parent), this);
                    this.parent = parent.parent;
                    this.fixChangedTreeFromNewNode();
                } else {
                    this.parent = null;
                }
            }
        }
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
