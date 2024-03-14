package layouttree;

import files.FileBuffer;
import ui.FileBufferView;
import ui.View;

import java.io.IOException;


/**
 * LayoutLeaf represents the leaf layout-structure
 * LayoutLeaf inherets from Layout
 */
public class LayoutLeaf extends Layout {
    private FileBufferView containedFileBufferView;

    public int getStartX(int terminalWidth, int terminalHeight){
        return parent.getStartX(this, terminalWidth, terminalHeight);
    }

    public void clearContent() throws IOException {
        containedFileBufferView.clearContent();
    }
    public int getStartY(int terminalWidth, int terminalHeight){
        return parent.getStartY(this, terminalWidth, terminalHeight);
    }

    public int getHeight(int terminalWidth, int terminalHeight){
        return parent.getHeight(this, terminalWidth, terminalHeight);
    }

    public int getWidth(int terminalWidth, int terminalHeight){
        return parent.getWidth(this, terminalWidth, terminalHeight);
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

    @Override
    public void moveCursor(char c) {
        containedFileBufferView.moveCursor(c);
    }

    @Override
    public void enterText(byte b) {
        containedFileBufferView.write(b);
    }

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
     * If no neighbours left, the active Layout stays active
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
    public void closeActive() {
        if (containsActiveView) {
            containedFileBufferView.close();
        }
    }

    @Override
    public void renderContent() throws IOException {
        containedFileBufferView.render();
    }

    @Override
    public void forcedCloseActive() {
        if (containsActiveView) {
            parent.makeRightNeighbourActive(this);
            parent.delete(this);
        }
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

