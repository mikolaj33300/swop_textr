package layouttree;

import snake.SnakeHead;

import java.util.ArrayList;

public abstract class LayoutNode extends Layout {

    @Override
    public Layout insertRightOfSpecified(int hashSpecified, int hashToAdd){
        for(Layout l:children){
            try{
                return l.insertRightOfSpecified(hashSpecified, hashToAdd);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    @Override
    public Layout delete(int hashToDelete){
        for(Layout l:children){
            try{
                return l.delete(hashToDelete);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    /**
     * The children of this LayoutNode in the layout-tree
     */
    protected ArrayList<Layout> children;

    /**
     * Clones its arguments to prevent representation exposure
     * LayoutNodes need at least 2 children in its children-array
     */
    public LayoutNode(ArrayList<Layout> newChildren) {
        if (newChildren.size() < 2)
            throw new RuntimeException("newChildren too short");

        this.children = new ArrayList<>();
        for (Layout l : newChildren)
            this.insertDirectChild(l.clone());
    }


    /**
     * Returns the orientation of the LayoutNode
     */
    public abstract Orientation getOrientation();

    /**
     * Returns a deep-copy of the children of this LayoutNode
     */
    public ArrayList<Layout> getDirectChildren() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for (Layout l : children) {
            deepCopyList.add(l.clone());
        }
        return deepCopyList;
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    @Override
    public int getNeighborsContainedHash(MOVE_DIRECTION dir, int hash) throws RuntimeException {
        for (Layout l : children) {
            try{
                return l.getNeighborsContainedHash(dir, hash);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    /**
     * Checks whether this layout is allowed to be added as a child of the given {@link LayoutNode}
     */
    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);


    /**
     * Inserts a Layout as a child to this LayoutNode
     * The reference to the given Layout will be lost
     */
    public void insertDirectChild(Layout toInsert){
        if(toInsert.isAllowedToBeChildOf(this)){
            Layout cloneOfInsert = toInsert.clone();
            cloneOfInsert.setParent(this);
            this.children.add(cloneOfInsert);
            }
    }

    /**
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir, int hash) throws RuntimeException {
        for (Layout l : children) {
            try{
                return l.rotateRelationshipNeighbor(rot_dir, hash);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    /**
     * Returns the right neighbor
     */
    protected LayoutLeaf getRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            return children.get(index + 1).getLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                return parent.getRightNeighbor(this);
            } else {
                return null;
            }
            //called when we need to backtrack one level up
        }
    }

    /**
     * Deletes the right neighbour of this tree
     */
    protected void deleteRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1)
            children.get(index + 1).deleteLeftLeaf(); // Called when we can make child of current node the active one.
        else
            parent.deleteRightNeighbor(this); //called when we need to backtrack one level up
    }

    /**
     * Checks if parameter layout is of type {@link LayoutNode}, and if the children match
     * the layout of this node. All leaves must have a FileBuffer containing the same path.
     * ! Assumes the children are always put in the same sequence upon creation of layout
     */

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    /**
     * Deletes a child of this LayoutNode, returns new rootlayout
     */
    protected Layout delete(Layout l) {
        children.remove(l);
        if (this.children.size() == 1) {
            if (this.parent != null) {
                this.parent.children.set(this.parent.children.indexOf(this), this.children.get(0));
                this.children.get(0).setParent(this.parent);
                return parent.getRootLayoutUncloned();
            } else {
                return this.children.get(0);
            }
        }
        return this.getRootLayoutUncloned();
    }

    /**
     * Sets containsActive of the left leaf of the subtree with this as root and all the nodes inbetween to true.
     */
    protected int getLeftmostContainedHash() {
        return children.get(0).getLeftmostContainedHash();
    }

    /**
     * Sets containsActive of the right leaf of the subtree with this as root and all the nodes inbetween to true.
     */
    protected int getRightmostContainedHash() {
        return children.get(children.size() - 1).getRightmostContainedHash();
    }

    /**
     * Returns a direct reference to the leftmost {@link LayoutLeaf} under this tree.
     */
    protected LayoutLeaf getLeftLeaf() {
        return children.get(0).getLeftLeaf();
    }

    /**
     * Makes the leftmost leaf that is to the right of the given subtree active
     * Requires passed layout to be a child of this.
     *
     * @return
     */
    protected int getRightNeighbourContainedHash(Layout layout) {
        int index = children.indexOf(layout);
        if (index < children.size() - 1) {
            return children.get(index + 1).getLeftmostContainedHash(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                return parent.getRightNeighbourContainedHash(this); //called when we need to backtrack one level up
            } else {
                return getRightmostContainedHash();
            }
        }
    }

    /**
     * Makes the rightmost leaf that is to the left of the given subtree active
     * Requires passed layout to be a child of this.
     *
     * @return
     */
    protected int getLeftNeighbourContainedHash(Layout layout) {
        int index = children.indexOf(layout);
        if (index > 0) {
            return children.get(index - 1).getRightmostContainedHash(); // Called when we can make child of current node the active one.
        } else {
            if (parent != null) {
                return parent.getLeftNeighbourContainedHash(this); //called when we need to backtrack one level up
            } else {
                return getLeftmostContainedHash();
            }
        }
    }

    /**
     * Replaces the given child with a new layoutnode with the child and its sibling rotated
     * Returns the new root layout of the rotated node
     */
    protected Layout rotateWithRightSibling(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        LayoutNode newChild = null;

        if (newSibling == null) {
            System.out.print((char) 7);
        } else if (newSibling.parent != this) {
            this.deleteRightNeighbor(child);
            if (rotdir == ROT_DIRECTION.COUNTERCLOCKWISE) {
                children.add(children.indexOf(child), newSibling);
                newSibling.setParent(this);
            } else {
                children.add(children.indexOf(child) + 1, newSibling);
                newSibling.setParent(this);
            }
            this.fixChangedTreeFromNewNode();
        } else {
            newChild = getNewMergedRotatedChild(rotdir, child, newSibling);
            this.replaceAndSetParent(child, newChild);
            this.deleteRightNeighbor(newChild);
            newChild.fixChangedTreeFromNewNode();
        }

        if (newChild == null) {
            return child.getRootLayoutUncloned();
        } else {
            return newChild.getRootLayoutUncloned();
        }
    }

    /**
     *  Returns a layoutnode containing a child and a new specified sibling after rotating the active Layout
     */
    protected abstract LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child, LayoutLeaf newSibling);

    /**
     * Replaces the parent of a Layout with a new Layout
     */
    private void replaceAndSetParent(Layout toReplace, Layout newLayout) {
        int index = children.indexOf(toReplace);
        children.set(index, newLayout);
        newLayout.setParent(this);
    }

    /**
     * Fixes the tree structure to make if valid:
     * Deeper explanation-->
     * Bottom up fixing of tree structure with unary and binary constraints.
     * Given that the children Layouts are in a consistent state with this
     * (= could exist as root on its own and are oriented differently)
     * make the current node and its parent (may be null) consistent with each other.
     * Rules: A parent's child LayoutNodes aren't empty, don't contain just 1 element
     * and are oriented differently.
     */
    private void fixChangedTreeFromNewNode() {
        if (parent != null) {
            //absorb this and siblings if orientations of parent
            if (this.getOrientation() == parent.getOrientation()) {
                for (Layout l : this.children) {
                    l.setParent(parent);
                }
                parent.children.addAll(parent.children.indexOf(this), this.children);
                parent.children.remove(this);
            }
            if (parent.children.size() == 1) {
                if (parent.parent != null) {
                    //As of 13/03 there is no coverage on this part. This is because due to the way delete is called, it takes care of this already. However, if the assignment and order of operations were to change, this would no longer be the case.
                    this.parent.parent.children.set(this.parent.parent.children.indexOf(parent), this);
                    this.parent = parent.parent;
                    this.fixChangedTreeFromNewNode();
                } else {
                    this.parent = null;
                }
            }
        }
    }

    /**
     * Returns a clone of this LayoutNode, without any references to it
     */
    @Override
    public abstract LayoutNode clone();

    @Override
    public void changeHash(int target, int newHash) {
        for(Layout child : this.children)
            child.changeHash(target, newHash);
    }

    /**
     * Enumerator that represents whether the Layouts inside this Layout
     * will be stacked (VERTICAL) or side-by-side (HORIZONTAL)
     */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
