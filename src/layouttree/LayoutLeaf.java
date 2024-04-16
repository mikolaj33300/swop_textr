package layouttree;

import ui.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * LayoutLeaf represents a leaf layout-structure
 * LayoutLeaf extends from Layout
 */
public class LayoutLeaf extends Layout {

    /**
     * The hashcode that links this LayoutLeaf to the ControllerFacade
     */
    private int containedHashCode;

    /**
     * Constructor for LayoutLeaf
     * Sets the containedHashCode to the given hashCode
     */
    public LayoutLeaf(int hashCode) {
        this.containedHashCode = hashCode;
    }


    /**
     * Returns the containedHashCode contained in this LayoutLeaf
     */
    public int getContainedHashCode(){
        return  this.containedHashCode;
    }

    /**
     * If the containHashCode of this LayoutLeaf is equal to the target-hash,
     * Its containedHashCode will be changed to newHashCode
     */
    @Override
    public void changeHash(int target, int newHashCode) {
        if(this.containedHashCode == target)
            this.containedHashCode = newHashCode;
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
     * Returns the containedHashCode of this LayoutLeaf
     */
    @Override
    protected int getLeftmostContainedHash() {
        return containedHashCode;
    }

    /**
     * Returns the containedHashCode of this LayoutLeaf
     */
    @Override
    protected int getRightmostContainedHash() {
        return containedHashCode;
    }

    /**
     * Returns this LayoutLeaf
     */
    @Override
    protected LayoutLeaf getLeftLeaf() {
        return this;
    }

    /**
     * Returns HashMap with an entry of this LayoutLeafs containedHashCode, linked to the given uiCoordsScaled
     * This represents the part of the terminal that this LayoutLeaf will fill up
     */
    @Override
    public HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled) {
        HashMap<Integer, Rectangle> mapToReturn = new HashMap<Integer, Rectangle>();
        mapToReturn.put(this.containedHashCode, uiCoordsScaled);
        return mapToReturn;
    }


    /**
     * If this LayoutLeafs containedHashCode equals hashToAdd, this function returns the parent of this LayoutLeaf, with
     * a new LayoutLeaf, with containedHashCode equal to hashToAdd, inserted right next to this LayoutLeaf,
     */
    @Override
    public Layout insertRightOfSpecified(int hashSpecified, int hashToAdd) {
        //TODO: Check if hashToAdd already exists
        if(this.containedHashCode != hashSpecified){
            throw new HashNotMatchingException();
        } else {
            if(parent != null){
                LayoutLeaf toAdd = new LayoutLeaf(hashToAdd);
                parent.children.add(parent.children.indexOf(this)+1, toAdd);
                toAdd.setParent(parent);
                return this.getRootLayoutUncloned();
            } else {
                ArrayList<Layout> newChildrenArrList = new ArrayList<>();
                newChildrenArrList.add(this);
                newChildrenArrList.add(new LayoutLeaf(hashToAdd));
                return new VerticalLayoutNode(newChildrenArrList);
            }
        }
    }

    /**
     * Rotates this LayoutLeaf if its containedHashCode equal to the given targetHashcode in the layouttree of its parent
     * (if it has one) with its right neighbor if it exists; if it doesn't, a bell-sound will be sounded
     * The rotation happens clockwise or counterclockwise according to rotdir.
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


    /**
     * Returns true
     * This is the case because a LayoutLeaf is always allowed to be a child of a LayoutNode
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode futureParent) {
        return true;
    }

    /**
     * If this LayoutLeafs containedHashCode equals to hashDeletes, this function returns
     * the parent of this LayoutLeaf with this LayoutLeaf deleted from it
     */
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
     * Deletes this LayoutLeaf from its parent list of children, if it has a parent
     */
    @Override
    protected void deleteLeftLeaf() {
        if (super.parent != null) {
            super.parent.delete(this);
        }
    }

    /**
     * Returns a copy of LayoutLeaf
     * The references to this object will be removed
     * The parent of this LayoutLeaf will not be brought over to the clone
     */
    @Override
    public LayoutLeaf clone() {
        return new LayoutLeaf(containedHashCode);
    }


    /**
     * Returns true if this LayoutLeaf and the given Object are equal content-wise, false otherwise
     * They are equal content-wise when the object is a LayoutLeaf and contains the same hashCode
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LayoutLeaf leaf) {
            return (this.containedHashCode == leaf.containedHashCode);
        } else {
            return false;
        }
    }

}

