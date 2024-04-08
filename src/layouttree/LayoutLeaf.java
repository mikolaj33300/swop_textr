package layouttree;

import files.FileBuffer;
import ui.Rectangle;

import java.io.IOException;
import java.util.HashMap;


/**
 * LayoutLeaf represents the leaf layout-structure
 * LayoutLeaf inherets from Layout
 */
public class LayoutLeaf extends Layout {
    private final int containedHashCode;

    /**
     * Constructor for {@link LayoutLeaf}, clones its arguments to prevent representation exposure
     */
    public LayoutLeaf(int hash) throws IOException {
	    this.containedHashCode = hash;
    }


    /**
     * Deletes the mostleft this leaf's parent
     * Since the parent function found this leaf as leftmost, it will be removed
     */
    @Override
    protected void deleteLeftLeaf() {
        if (super.parent != null) {
            super.parent.delete(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public int getNeighborsContainedHash(DIRECTION dir, int hash) {
        if(dir==DIRECTION.RIGHT){
            return this.getRightNeighborsContainedHash(hash);
        } else {
            return this.getLeftNeighborsContainedHash(hash);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the rightneigbouring layout
     * If no neighbours right, the active Layout stays active
     *
     * @return
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
     * Moves focus from the currently active Layout, to the leftneigbouring layout
     * If no neighbours left, the active Layout stays active
     *
     * @return
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
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
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
        return null;
    }

    /**
     * Determines if the given LayoutLeaf and Object are equal content-wise (not reference-wise)
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LayoutLeaf leaf) {
            return (this.getContainsActiveView() == leaf.getContainsActiveView());
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
        if (getContainsActiveView() && futureParent.getContainsActiveView()) {
            throw new RuntimeException("Invalid child: more than two active");
        } else {
            return true;
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
        try {
            return new LayoutLeaf(containedHashCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<int, Rectangle> getCoordsList(Rectangle uiCoordsScaled) {
        HashMap<int, Rectangle> mapToReturn = new HashMap<int, Rectangle>();
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
     * Returns the leftmost leaf of the current layout-level
     * As this a leaf of the layout-structure,
     * it does not have any children and will return a copy of itself
     */
    protected LayoutLeaf getLeftLeaf(int hash) {
        return this;
    }

}

