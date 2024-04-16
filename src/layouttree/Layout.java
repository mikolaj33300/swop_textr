package layouttree;

import ui.Rectangle;

import java.util.HashMap;

public abstract class Layout implements Cloneable {

    /**
     * The parent LayoutNode of this parent
     * Its initial value is null
     */
    protected LayoutNode parent = null;

    /**
     * Returns a clone of this Layouts parent.
     */
    public LayoutNode getParent(){
        if(this.parent != null)
            return parent.clone();
        else
            return null;
    }

    /**
     * Sets parent field of this to provided node.
     */
    protected void setParent(LayoutNode layoutNode){
        this.parent = layoutNode;
    }

    /**
     * Sets a new HashCode for the LayoutLeaf connected to the target HashCode in the underlying layouttree
     */
    public abstract void changeHash(int target, int newHash);

    /**
     * Returns a clone of the root layout
     * The root is the highest parent in the layouttree that doesn't have a parent itself
     */
    public Layout getRootLayout() {
        if(this.parent != null){
            return parent.getRootLayout();
        } else {
            return this.clone();
        }
    }

    /**
     * Returns an uncloned reference to the root of this Layout
     * The root is the highest parent in the layouttree that doesn't have a parent itself
     */
    protected Layout getRootLayoutUncloned() {
        if(this.parent != null){
            return parent.getRootLayoutUncloned();
        } else {
            return this;
        }
    }

    /**
     * Returns the containedHashCode of the neighbor of the LayoutLeaf with a containedHashCode equal to targetHashCode
     * Which neighbour is decided by the dir argument
     */
    public abstract int getNeighborsContainedHash(MOVE_DIRECTION dir, int targetHashCode) throws RuntimeException;

    /**
     * Returns the containedHashCode of the leftmost LayoutLeaf in the underlying layouttree
     */
    protected abstract int getLeftmostContainedHash();

    /**
     * Returns the containedHashCode of the rightmost LayoutLeaf in the underlying layouttree
     */
    protected abstract int getRightmostContainedHash();

    /**
     * Returns a direct reference to the leftmost LayoutLeaf in this part of the layouttree
     */
    protected abstract LayoutLeaf getLeftLeaf();

    /**
     * Returns HashMap of coords connected to the containedHashCodes of the LayoutLeafs
     * The rectangle defines which part of the terminal, this LayoutLeaf will fill up
     * The result is scaled by the given rectangle uiCoordsScaled
     */
    public abstract HashMap<Integer, Rectangle> getCoordsList(Rectangle uiCoordsScaled);

    /**
     * Returns this Layout, with a new LayoutLeaf inserted with containedHashCode equal to hashToAdd
     * The LayoutLeaf will be inserted directly right of a LayoutLeaf with containedHashCode equal to hashSpecified
     */
    public abstract Layout insertRightOfSpecified(int hashSpecified, int hashToAdd);

    /**
     * Rotates a LayoutLeaf with containedHashCode equal to the given targetHashcode int the layouttree under this Layout
     * with its right neighbor if it exists; if it doesn't, a bell-sound will be sounded
     * The rotation happens clockwise or counterclockwise according to rotdir.
     */
    public abstract Layout rotateRelationshipNeighbor(ROT_DIRECTION rotdir, int targetHashCode);

    /**
     * Returns true if the given LayoutNode is allowed to be a child of this Layout
     */
    protected abstract boolean isAllowedToBeChildOf(LayoutNode layoutNode);

    /**
     * Deletes the leftmost LayoutLeaf of this part of the layouttree
     */
    protected abstract void deleteLeftLeaf();

    /**
     * Returns the current layout, but without the LayoutLeaf with a containedHashCode equal to hashToDelete
     */
    public abstract Layout delete(int hashToDelete);

    /**
     * Returns a deepcopy of this Layout
     * The references to this object and its contents will be removed
     * The parent of this Layout will not be brought over to the clone
     */
    public abstract Layout clone();

    /**
     * Returns true if this Layout and the given Object are equal content-wise, otherwise it returns false
     */
    public abstract boolean equals(Object obj);
}
