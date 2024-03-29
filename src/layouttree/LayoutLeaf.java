package layouttree;

import files.FileBuffer;
import ui.FileBufferView;

import java.io.IOException;


/**
 * LayoutLeaf represents the leaf layout-structure
 * LayoutLeaf inherets from Layout
 */
public class LayoutLeaf extends Layout {
    private FileBufferView containedFileBufferView;
    private final int hashCode;

    /**
     * Constructor for {@link LayoutLeaf}, clones its arguments to prevent representation exposure
     */
    public LayoutLeaf(String path, boolean active, int hash) throws IOException {
	this.containedFileBufferView = new FileBufferView(path, this);
        this.setContainsActiveView(active);
	this.hashCode = hash;
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException {
        containedFileBufferView.clearContent();
    }

    /**
     * Returns the x coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartX(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hash){
            return parent.getStartX(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the y coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartY(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hash){
            return parent.getStartY(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the height of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getHeight(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash = this.hash){
            return parent.getHeight(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Returns the width of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getWidth(int terminalWidth, int terminalHeight, int hash){
        if(parent != null && hash == this.hash){
            return parent.getWidth(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     * Deletes the mostleft this leaf's parent
     * Since the parent function found this leaf as leftmost, it will be removed
     */
    protected void deleteLeftLeaf(int hash) {
        if (super.parent != null && hash == this.hash) {
            super.parent.delete(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public void moveFocus(DIRECTION dir, int hash) {
        if (dir == DIRECTION.RIGHT && hash == this.hash) {
            moveFocusRight();
        } else if (dir == DIRECTION.LEFT && hash == this.hash) {
            moveFocusLeft();
        }
    }

    /**
     * Calls moveCursor() on the contained active {@link ui.FileBufferView}(s).
     */
    @Override
    public void moveCursor(char c, int hash) {
	if (hash == this.hash)
	    containedFileBufferView.moveCursor(c);
    }

    /**
     * Calls enterText() on the contained {@link ui.FileBufferView}.
     */
    @Override
    public void enterText(byte b, int hash) {
	if (hash == this.hash)
	    containedFileBufferView.write(b);
    }

    /**
     * Calls save() on the contained {@link ui.FileBufferView}.
     *
     * @return
     */
    @Override
    public int saveActiveBuffer(int hash) {
	if (hash == this.hash)
	    return containedFileBufferView.save();
	return 0;
    }

    @Override
    public void enterInsertionPoint(int hash) {
	if (hash == this.hash)
	    containedFileBufferView.enterInsertionPoint();
    }

    /**
     * Moves focus from the currently active Layout, to the rightneigbouring layout
     * If no neighbours right, the active Layout stays active
     */
    private void moveFocusRight(int hash) {
        if (parent != null && hash == this.hash) {
            this.setContainsActiveView(false);
            parent.makeRightNeighbourActive(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the leftneigbouring layout
     * If no neighbours left, the active Layout stays active
     */
    private void moveFocusLeft(int hash) {
        if (parent != null && hash == this.hash) {
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
        if (parent != null && hash == this.hash) {
            return parent.rotateWithRightSibling(rot_dir, this);
        } else if (hash == this.hash) {
            System.out.print((char) 7); //sounds terminal bell
            return this;
        }
    }

    /**
      * Calls forcedCloseActive() on the contained {@link ui.FileBufferView} if it's active.
      * Returns 0 if close was succesful and 2 if close was succesful but there are no more other children.
     */
    @Override
    public int closeActive(int hash) {
	if (hash != this.hash)
	    return -1;
        if (containsActiveView) {
            if(containedFileBufferView.close()==0){
                if(parent != null){
                    //should be replaced by hasrightneighbour
                    if(parent.children.indexOf(this)<parent.children.size()-1){
                        parent.makeRightNeighbourActive(this);
                    } else {
                        parent.makeLeftNeighbourActive(this);
                    }
                    parent.delete(this);
                    return 0;// close successful, parent still exists
                }
                else return 2;// final window
            } else {
                return 1;// filebuffer won't close
            }
        } else {
            return 0;// isn't active
        }
    }

    /**
     * Calls forcedCloseActive() on the contained {@link ui.FileBufferView} if it's active.
     * Returns 0 if close was succesful and 2 if close was succesful but there are no more other children.
     */
    @Override
    public int forcedCloseActive(int hash) {
	if (hash != this.hash)
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
     * Determines if the given LayoutLeaf and Object are equal content-wise (not reference-wise)
     * Determines if the given layout is a {@link LayoutLeaf} and their {@link FileBuffer}'s are also equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LayoutLeaf leaf) {
            return leaf.containedFileBufferView.equals(this.containedFileBufferView) && (this.getContainsActiveView() == leaf.getContainsActiveView());
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

    /**
     * Calls deleteCharacter on the {@link ui.FileBufferView} contained in the active {@link LayoutLeaf} under this node, if there is one.
     */
    @Override
    public void deleteCharacter() {
        containedFileBufferView.deleteCharacter();
    }

    /**
     * Makes a deepcopy of LayoutLeaf
     * The references to this object and its contents will be removed
     */
    @Override
    public LayoutLeaf clone() {
        try {
            return new LayoutLeaf(this.containedFileBufferView.getContainedFileBuffer().getFileHolder().getPath(), getContainsActiveView());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Makes the leftmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected void makeLeftmostLeafActive(int hash) {
	if (hash != this.hash)
	    return -1;
        setContainsActiveView(true);
    }

    /**
     * Makes the rightmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected void makeRightmostLeafActive(int hash) {
	if (hash != this.hash)
	    return -1;
        setContainsActiveView(true);
    }

    /**
     * Returns the leftmost leaf of the current layout-level
     * As this a leaf of the layout-structure,
     * it does not have any children and will return a copy of itself
     */
    protected LayoutLeaf getLeftLeaf(int hash) {
	if (hash != this.hash)
	    return -1;
        return this;
    }

}

