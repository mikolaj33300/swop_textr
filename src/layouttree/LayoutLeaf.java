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

    /**
     *
     * @param terminalWidth
     * @param terminalHeight
     * @return
     *
     * Returns the x coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartX(int terminalWidth, int terminalHeight){
        if(parent != null){
            return parent.getStartX(this, terminalWidth, terminalHeight);
        }
        return 0;
    }


    public void clearContent() throws IOException {
        containedFileBufferView.clearContent();
    }

    /**
     *
     * @param terminalWidth
     * @param terminalHeight
     * @return
     *
     * Returns the y coordinate of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getStartY(int terminalWidth, int terminalHeight){
        if(parent != null){
            return parent.getStartY(this, terminalWidth, terminalHeight);
        }
        return 0;
    }

    /**
     *
     * @param terminalWidth
     * @param terminalHeight
     * @return
     *
     * Returns the height of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getHeight(int terminalWidth, int terminalHeight){
        if(parent != null){
            return parent.getHeight(this, terminalWidth, terminalHeight);
        }
        return terminalHeight;
    }

    /**
     *
     * @param terminalWidth
     * @param terminalHeight
     * @return
     *
     * Returns the width of this leaf by traversing to the root, providing info about terminal from below.
     */
    public int getWidth(int terminalWidth, int terminalHeight){
        if(parent != null){
            return parent.getWidth(this, terminalWidth, terminalHeight);
        }
        return terminalWidth;
    }
    /**
     * Constructor for {@link LayoutLeaf}, clones its arguments to prevent representation exposure
     */
    public LayoutLeaf(String path, boolean active) {
        this.containedFileBufferView = new FileBufferView(path, this);
        this.setContainsActiveView(active);
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

    /**
     * Moves focus from the currently active Layout, to the neigbouring layout
     * Which neighbour is decided by the dir argument
     * If no neighbours left, the active Layout stays active
     */
    public void moveFocus(DIRECTION dir) {
        if (dir == DIRECTION.RIGHT) {
            moveFocusRight();
        } else {
            moveFocusLeft();
        }
    }

    /**
     * Calls moveCursor() on the contained active {@link ui.FileBufferView}(s).
     */
    @Override
    public void moveCursor(char c) {
        containedFileBufferView.moveCursor(c);
    }

    /**
     * Calls enterText() on the contained {@link ui.FileBufferView}.
     */
    @Override
    public void enterText(byte b) {
        containedFileBufferView.write(b);
    }

    /**
     * Calls save() on the contained {@link ui.FileBufferView}.
     */
    @Override
    public void saveActiveBuffer() {
        containedFileBufferView.save();
    }

    @Override
    public void enterInsertionPoint() {
        containedFileBufferView.enterInsertionPoint();
    }

    /**
     * Moves focus from the currently active Layout, to the rightneigbouring layout
     * If no neighbours right, the active Layout stays active
     */
    private void moveFocusRight() {
        if (parent != null) {
            this.setContainsActiveView(false);
            parent.makeRightNeighbourActive(this);
        }
    }

    /**
     * Moves focus from the currently active Layout, to the leftneigbouring layout
     * If no neighbours left, the active Layout stays active
     */
    private void moveFocusLeft() {
        if (parent != null) {
            this.setContainsActiveView(false);
            parent.makeLeftNeighbourActive(this);
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) {
        if (parent != null) {
            return parent.rotateWithRightSibling(rot_dir, this);
        } else {
            System.out.print((char) 7); //sounds terminal bell
            return this;
        }
    }

    @Override
    public int closeActive() {
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
                    return 0;
                }
                else return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * Calls renderContent() on the contained {@link ui.FileBufferView}(s).
     */
    @Override
    public void renderContent() throws IOException {
        containedFileBufferView.render();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int forcedCloseActive() {
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
     * @inheritDoc
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
        return new LayoutLeaf(this.containedFileBufferView.getContainedFileBuffer().getFileHolder().getPath(), getContainsActiveView());
    }

    /**
     * Makes the leftmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected void makeLeftmostLeafActive() {
        setContainsActiveView(true);
    }

    /**
     * Makes the rightmost leaf active
     * This is a leaf of the layout-structure, so it doesn't have any children, so this leaf should be madea active
     */
    @Override
    protected void makeRightmostLeafActive() {
        setContainsActiveView(true);
    }

    /**
     *
     * @inheritDoc
     */
    @Override
    public void renderCursor() throws IOException {
        if(super.containsActiveView){
            containedFileBufferView.renderCursor();
        }
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

