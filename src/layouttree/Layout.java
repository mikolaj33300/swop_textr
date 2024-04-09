package layouttree;

import ui.Rectangle;

import java.util.HashMap;

public abstract class Layout implements Cloneable {

    /**
     * Gets an uncloned reference to the root of this, this makes some protected class methods faster.
     */
    protected Layout getRootLayoutUncloned() {
        if(this.parent != null){
            return parent.getRootLayoutUncloned();
        } else {
            return this;
        }
    }

    /**
     * Returns a clone of the root layout by following the parent field from this layout.
     */
    public Layout getRootLayout() {
        if(this.parent != null){
            return parent.getRootLayout();
        } else {
            return this.clone();
        }
    }

    /**
     * Returns a clone of this parent.
     */
    public LayoutNode getParent(){
        return parent.clone();
    }

    /**
     * Checks whether this layout is allowed to be added as a child of the given {@link LayoutNode}
     */
    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);

    public abstract Layout delete(int hashToDelete);

    protected LayoutNode parent = null;

    protected boolean containsActiveView;

    /**
     * Deletes the leftmost {@link LayoutLeaf}from the underlying structure. In composite Layouts it is relayed to the leftmost child. If this is a leaf, it itself deletes itself from the parent.
     */
    protected abstract void deleteLeftLeaf();
    /**
     * Sets parent field of this to provided node.
     */
    protected void setParent(LayoutNode layoutNode){
        this.parent = layoutNode;
    }

    /**
     * Returns whether this layout contains an active view somewhere under it, either directly or as a child deeper.
     */
    public boolean getContainsActiveView(){
        return containsActiveView;
    }

    /**
     * Makes next/previous view in the structure active according to dir (left/right).
     */
    public abstract int getNeighborsContainedHash(DIRECTION dir, int hash) throws RuntimeException;

    /**
     * Sets containsActive of the left leaf of the subtree with this as root and all the nodes inbetween to true.
     */
    protected abstract int getLeftmostContainedHash();

    /**
     * Sets containsActive of the right leaf of the subtree with this as root and all the nodes inbetween to true.
     */
    protected abstract int getRightmostContainedHash();


    /**
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir, int hash);

    /**
     * Returns a direct reference to the leftmost {@link LayoutLeaf} under this tree.
     */
    protected abstract LayoutLeaf getLeftLeaf();

    /**
     * Returns whether this Layout and the given object are equals in contents
     */
    public abstract boolean equals(Object obj);

    /**
     * Returns a clone of this Layout, without any references to it
     */
    public abstract Layout clone();

    public abstract HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled);
}
