package layouttree;

import files.FileBuffer;
import ui.FileBufferView;

import java.io.IOException;


/**
 * LayoutLeaf represents the leaf layout-structure
 * LayoutLeaf inherets from Layout
 */
public class LayoutLeaf extends Layout {
    private final int hashCode;

    /**
     * Constructor for {@link LayoutLeaf}, clones its arguments to prevent representation exposure
     */
    public LayoutLeaf(int hash) throws IOException {
      this.hashCode = hash;
    }

    /**
     * Returns the x coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartX(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hashCode){
            return parent.getStartX(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the y coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartY(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hashCode){
            return parent.getStartY(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the height of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getHeight(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hashCode){
            return parent.getHeight(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the width of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getWidth(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hashCode){
            return parent.getWidth(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Deletes the mostleft this leaf's parent
     * Since the parent function found this leaf as leftmost, it will be removed
     */
    protected void deleteLeftLeaf(int hash) {
        if (super.parent != null && hash == this.hashCode) {
            super.parent.delete(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public void moveFocus(DIRECTION dir, int hash) {
        if (dir == DIRECTION.RIGHT && hash == this.hashCode) {
            moveFocusRight(hash);
        } else if (dir == DIRECTION.LEFT && hash == this.hashCode) {
            moveFocusLeft(hash);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the rightneigbouring layout
     * If no neighbours right, the active Layout stays active
     */
    private void moveFocusRight(int hash) {
        if (parent != null && hash == this.hashCode) {
            this.setContainsActiveView(false);
            parent.makeRightNeighbourActive(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the leftneigbouring layout
     * If no neighbours left, the active Layout stays active
     */
    private void moveFocusLeft(int hash) {
        if (parent != null && hash == this.hashCode) {
            this.setContainsActiveView(false);
            parent.makeLeftNeighbourActive(this);
        }
    }

    /**
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir, int hash) {
        if (parent != null && hash == this.hashCode) {
            return parent.rotateWithRightSibling(rot_dir, this);
        } else if (hash == this.hashCode) {
            System.out.print((char) 7); //sounds terminal bell
            return this;
        }
    }
    /**
     * Calls forcedCloseActive() on the contained {@link ui.FileBufferView} if it's active.
     * Returns 0 if close was succesful and 2 if close was succesful but there are no more other children.
     */
    @Override
    public int forcedCloseActive(int hash) {
	if (hash != this.hashCode)
	    return -1;
        if (containsActiveView) {
            if(parent != null){
                parent.makeRightNeighbourActive(this);
                parent.delete(this);
                return 0;
            }

        }
        return 0;
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

    /**
     * Makes the leftmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    protected void makeLeftmostLeafActive(int hash) {
	if (hash != this.hashCode)
	    return;
        setContainsActiveView(true);
    }

    /**
     * Makes the rightmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    protected void makeRightmostLeafActive(int hash) {
	if (hash != this.hashCode)
	    return;
        setContainsActiveView(true);
    }

    /**
     * Returns the leftmost leaf of the current layout-level
     * As this a leaf of the layout-structure,
     * it does not have any children and will return a copy of itself
     */
    protected LayoutLeaf getLeftLeaf(int hash) {
	if (hash != this.hashCode)
	    return null;
        return this;
    }

}

