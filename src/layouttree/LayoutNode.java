package layouttree;

import exception.HashNotMatchingException;
import util.MoveDirection;
import util.RotationDirection;

import java.util.ArrayList;

public abstract class LayoutNode extends Layout {

    /**
     * The Layout-children of this LayoutNode in the layouttree
     */
    protected ArrayList<Layout> children;

    /**
     * Searches for the LayoutLeaf connected to the target-hashcode in the underlying layouttree
     * If found, the containHashCode of LayoutLeaf will be set to newHash
     */
    @Override
    public void changeHash(int target, int newHash) {
        for(Layout child : this.children)
            child.changeHash(target, newHash);
    }

    /**
     * Constructor for LayoutNode
     * The given newChildren will be cloned before setting the children of this HorizontalLayoutNode
     * There must be atleast 2 Layouts in newChildren for the LayoutNode to be instantiated correctly
     */
    public LayoutNode(ArrayList<Layout> newChildren) {
        if (newChildren.size() < 2)
            throw new RuntimeException("newChildren too short");

        this.children = new ArrayList<>();
        for (Layout l : newChildren)
            this.insertDirectChild(l.clone());
    }

    public LayoutNode(int[] hashes) {
      this.children = new ArrayList<>(hashes.length);
      for (int i : hashes) {
        this.children.add(new LayoutLeaf(i));
      }
    }


    /**
     * Returns the orientation of the LayoutNode
     */
    public abstract Orientation getOrientation();

    /**
     * Returns a deep-copy of the children of this LayoutNode
     * Every child will thus also be cloned
     * The parent-field won't be brought over to the returned children
     */
    public ArrayList<Layout> getDirectChildren() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for (Layout l : children) {
            deepCopyList.add(l.clone());
        }
        return deepCopyList;
    }

    /**
     * Returns the first child of this LayoutNode
     */
    protected LayoutLeaf getLeftLeaf() {
        return children.get(0).getLeftLeaf();
    }

    /**
     * Returns the leftmost LayoutLeaf of the Layout right of the given subtree in this LayoutNodes children
     * ,if this subtree is a child of this LayoutNode
     * Otherwise if this LayoutNode has a parent, the leftmost LayoutLeaf of the layout right to this LayoutNode in its
     * parents list of children will be returned
     * Otherwise it returns null
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
     * Returns the containedHashCode of the leftmost LayoutLeaf in the underlying layouttree
     */
    protected int getLeftmostContainedHash() {
        return children.get(0).getLeftmostContainedHash();
    }

    /**
     * Returns the containedHashCode of the rightmost LayoutLeaf in the underlying layouttree
     */
    protected int getRightmostContainedHash() {
        return children.get(children.size() - 1).getRightmostContainedHash();
    }

    /**
     * Returns the containedHashCode of the neighbor of the LayoutLeaf with a containedHashCode equal to targetHashCode
     * Which neighbour is decided by the dir argument
     */
    @Override
    public int getNeighborsContainedHash(MoveDirection dir, int hash) throws RuntimeException {
        for (Layout l : children) {
            try{
                return l.getNeighborsContainedHash(dir, hash);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    /**
     * Returns the containedHashCode of the leftmost LayoutLeaf of the Layout right of the given layout
     * in this LayoutNodes children, if this layout is a child of this LayoutNode
     * Otherwise if this LayoutNode has a parent, the leftmost LayoutLeaf of the layout right to this LayoutNode in its
     * parents list of children will be returned
     * Otherwise it returns the containedHashCode of the last LayoutLeaf in this layouttree
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
     * Returns the containedHashCode of the rightmost LayoutLeaf of the Layout left of the given layout
     * in this LayoutNodes children, if this layout is a child of this LayoutNode
     * Otherwise if this LayoutNode has a parent, the rightmost LayoutLeaf of the layout left to this LayoutNode in its
     * parents list of children will be returned
     * Otherwise it returns the containedHashCode of the first LayoutLeaf in this layouttree
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
     * Searches for the give Layout toReplace in the children of this LayoutNode
     * If found, the Layout will be replaced by the given Layout newLayout
     */
    private void replaceAndSetParent(Layout toReplace, Layout newLayout) {
        int index = children.indexOf(toReplace);
        children.set(index, newLayout);
        newLayout.setParent(this);
    }

    /**
     * Inserts the given Layout toInsert as a child to this LayoutNode
     * The reference to the given Layout toInsert will be lost
     */
    public void insertDirectChild(Layout toInsert){
        if(toInsert.isAllowedToBeChildOf(this)){
            Layout cloneOfInsert = toInsert.clone();
            cloneOfInsert.setParent(this);
            this.children.add(cloneOfInsert);
        }
    }

    /**
     * Returns this LayoutNode, with a new LayoutLeaf inserted with containedHashCode equal to hashToAdd
     * The LayoutLeaf will be inserted directly right of a LayoutLeaf with containedHashCode equal to hashSpecified
     */
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

    /**
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public Layout rotateRelationshipNeighbor(RotationDirection rot_dir, int hash) throws RuntimeException {
        for (Layout l : children) {
            try{
                return l.rotateRelationshipNeighbor(rot_dir, hash);
            } catch(HashNotMatchingException ignored){

            }
        }
        throw new HashNotMatchingException();
    }

    /**
     * Replaces the given child with a new layoutnode with the child and its sibling rotated
     * Returns the new root layout of the rotated node
     */
    protected Layout rotateWithRightSibling(RotationDirection rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        LayoutNode newChild = null;

        if (newSibling == null) {
            System.out.print((char) 7);
        } else if (newSibling.parent != this) {
            this.deleteRightNeighbor(child);
            if (rotdir == RotationDirection.COUNTERCLOCKWISE) {
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
    protected abstract LayoutNode getNewMergedRotatedChild(RotationDirection rotdir, Layout child, LayoutLeaf newSibling);

    /**
     * Fixes this LayoutNode to change it from an illegal to a legal LayoutNode
     * The illegal states not having two or more children and having a LayoutNode as a child of a different LayoutNode,
     * with the same orientation
     * In the first case, children will be pushed upwards to the parent of this LayoutNode
     * In the second case, the upper-LayoutNode will be removed from its parent and replaced by the lower LayoutNode
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
     * Checks whether this layout is allowed to be added as a child of the given LayoutNode
     * Returns true if the orientation of this LayoutNode and the given layoutNode are different, returns false otherwise
     */
    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);

    /**
     * Checks if parameter layout is of type {@link LayoutNode}, and if the children match
     * the layout of this node. All leaves must have a FileBuffer containing the same path.
     * ! Assumes the children are always put in the same sequence upon creation of layout
     */
    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    /**
     * Deletes the Layout right of the given subtree
     * If this subtree is not a child of this LayoutNode,
     * this LayoutNode itself will be removed from its parents children
     */
    protected void deleteRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1)
            children.get(index + 1).deleteLeftLeaf(); // Called when we can make child of current node the active one.
        else
            parent.deleteRightNeighbor(this); //called when we need to backtrack one level up
    }

    /**
     * Returns the current LayoutNode, but without the Layout specified in the ajrgument
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
     * Returns the current LayoutNode, but without the LayoutLeaf with a containedHashCode equal to hashToDelete
     */
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
     * Returns a deepcopy of this LayoutNode
     * The reference to this LayoutNode will be lost and
     * Every Layout-child of this LayoutNode will also be cloned
     * The parent of this LayoutNode will not be brought over to the clone
     */
    @Override
    public abstract LayoutNode clone();

    /**
     * Enumerator that represents whether the Layouts inside this Layout
     * will be stacked (VERTICAL) or side-by-side (HORIZONTAL)
     */
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
}
