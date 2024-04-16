package layouttree;

import files.FileBuffer;
import ui.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * LayoutLeaf represents a leaf layout-structure
 * LayoutLeaf extends from Layout
 */
public class LayoutLeaf extends Layout {

    /**
     * Then hashCode that links this LayoutLeaf to the ControllerFacade
     */
    private int containedHashCode;

    /**
     * Constructor for LayoutLeaf
     * Takes an integer hashCode as input
     */
    public LayoutLeaf(int hashCode) {
	    this.containedHashCode = hashCode;
    }


    /**
     * Returns the hashCode contained in this LayoutLeaf
     */
    public int getContainedHashCode(){
        return  this.containedHashCode;
    }

    /**
     * Deletes this LayoutLeaf from its parent list of children if it has a parent
     */
    @Override
    protected void deleteLeftLeaf() {
        if (super.parent != null) {
            super.parent.delete(this);
        }
    }

    /**
     * Returns the hashCode of the neighbor of this LayoutLeaf in the layouttree
     * Which of the neighbors will be chosen, is based of the MOVE_DIRECTION argument
     */
    public int getNeighborsContainedHash(MOVE_DIRECTION dir, int hashCode) {
        if(dir==MOVE_DIRECTION.RIGHT){
            return this.getRightNeighborsContainedHash(hashCode);
        } else {
            return this.getLeftNeighborsContainedHash(hashCode);
        }
    }

    /**
     * Returns the hashCode of the neighbor to the right of this LayoutLeaf in the layouttree
     */
    private int getRightNeighborsContainedHash(int hash) throws HashNotMatchingException{
        if(this.containedHashCode == hash){
            if(parent != null){
                return parent.getRightNeighbourContainedHash(this);
            } else {
                return this.containedHashCode;
            }
        } else {
            throw new HashNotMatchingException();
        }
    }

    /**
     * Returns the hashCode of the neighbor to the left of this LayoutLeaf in the layouttree
     */
    private int getLeftNeighborsContainedHash(int hash) {
        if(this.containedHashCode == hash){
            if(parent != null){
                return parent.getLeftNeighbourContainedHash(this);
            } else {
                return this.containedHashCode;
            }
        } else {
            throw new HashNotMatchingException();
        }
    }

    /**
     * Rotates the layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir, int hash) {
        if (parent != null && hash == this.containedHashCode) {
            return parent.rotateWithRightSibling(rot_dir, this);
        } else if (hash == this.containedHashCode) {
            System.out.print((char) 7); //sounds terminal bell
            return this;
        } else {
            throw new HashNotMatchingException();
        }
    }

    @Override
    protected LayoutLeaf getLeftLeaf() {
        return this;
    }

    /**
     * Determines if the given LayoutLeaf and Object are equal content-wise (not reference-wise)
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LayoutLeaf leaf) {
            return (this.containedHashCode == leaf.containedHashCode);
        } else {
            return false;
        }
    }

    /**
     * Checks if a this LayoutLeaf is allowed to be a child of the given LayoutNode
     * Two children of a LayoutNode are not allowed to be active at the same time
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode futureParent) {
        return true;
    }

    @Override
    public Layout insertRightOfSpecified(int hashSpecified, int hashToAdd) {
        //TODO: Check if hashToAdd already exists
        if(this.containedHashCode != hashSpecified){
            throw new HashNotMatchingException();
        } else {
            if(parent != null){
                parent.children.add(parent.children.indexOf(this)+1, new LayoutLeaf(hashToAdd));
                return this.getRootLayoutUncloned();
            } else {
                ArrayList<Layout> newChildrenArrList = new ArrayList<>();
                newChildrenArrList.add(this);
                newChildrenArrList.add(new LayoutLeaf(hashToAdd));
                return new VerticalLayoutNode(newChildrenArrList);
            }
        }
    }

    @Override
    public Layout delete(int hashToDelete) {
        if(containedHashCode == hashToDelete){
            if(parent != null){
                return parent.delete(this);
            } else {
                return null;
            }
        }
        throw new HashNotMatchingException();
    }


    /**
     * Makes a deepcopy of LayoutLeaf
     * The references to this object and its contents will be removed
     */
    @Override
    public LayoutLeaf clone() {
            return new LayoutLeaf(containedHashCode);
    }

    @Override
    public HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled) {
        HashMap<Integer, Rectangle> mapToReturn = new HashMap<Integer, Rectangle>();
        mapToReturn.put(this.containedHashCode, uiCoordsScaled);
        return mapToReturn;
    }

    /**
     * Makes the leftmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected int getLeftmostContainedHash() {
        return containedHashCode;
    }

    /**
     * Makes the rightmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected int getRightmostContainedHash() {
        return containedHashCode;
    }

    /**
     * Sets a new HashCode for the LayoutLeaf connected to the target HashCode
     */
    @Override
    public void changeHash(int target, int newHashCode) {
        if(this.containedHashCode == target)
            this.containedHashCode = newHashCode;

    }
}

