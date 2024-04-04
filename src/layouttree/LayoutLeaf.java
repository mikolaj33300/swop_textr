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
     * Set containsactiveview to the given value.
     */
    @Override
    protected int setContainsActiveView(boolean active){
        this.containsActiveView = active;
        return this.hashCode;
    }
    /**
     * Deletes the mostleft this leaf's parent
     * Since the parent function found this leaf as leftmost, it will be removed
     */
    protected void deleteLeftLeaf() {
        if (super.parent != null) {
            super.parent.delete(this);
        }
    }

    @Override
    public int closeActive(int hashCode){
      if (this.hashCode == hashCode)
        parent.delete(this);
      return 0;
    }

    /**
     * Rotates the active layoutLeaf under this structure if there is one with its right neighbor if there is one,
     * clockwise or counterclockwise according to rotdir. If the right neighbor is a direct sibling it rotates to stand
     * perpendicular to the current orientation of the parent. If there is no direct right sibling it is added to the parent of the
     * active leaf and rotated then.
     */
    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir, int hashCode) {
        if (parent != null) {
            return parent.rotateWithRightSibling(rot_dir, this);
        } else {
            System.out.print((char) 7); //sounds terminal bell
            return this;
        }
    }

    @Override
    public Layout clone(){
      return this.clone();
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
    public boolean equals(Object obj) {
      if (obj instanceof LayoutLeaf leaf)
        return this.hashCode == leaf.hashCode ;
      return false;
    }

    public int getHashActiveNeighbour(DIRECTION dir, int activeHash){
      return 0;
    }

    /**
     * Returns the leftmost leaf of the current layout-level
     * As this a leaf of the layout-structure,
     * it does not have any children and will return a copy of itself
     */
    protected LayoutLeaf getLeftLeaf() {
        return this;
    }

}

