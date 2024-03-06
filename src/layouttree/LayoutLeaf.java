package layouttree;

import files.FileBuffer;

public class LayoutLeaf extends Layout {

    private LayoutNode parent;
    private boolean isActive;
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.isActive = active;
    }

    protected void deleteLeftLeaf() {
        parent.delete(this);
    }

    protected void mergeActiveWith(Layout toMergeLayout) {

    }

    // Hier nog ergens een enum zetten van wanneer we de actieve hebben gevonden???
    public STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException {
        return STATUS_MOVE.FOUND_ACTIVE;
    }

    protected STATUS_MOVE moveFocusRight() throws RuntimeException {
        return null;
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return STATUS_ROTATE.FOUND_ACTIVE;
    }

    protected STATUS_ROTATE rotateRelationshipNeighborRight() {
        return null;
    }

    public void render() {
        return;
    }

    @Override
    protected void makeLeftmostLeafActive() {
        isActive = true;
    }

    @Override
    protected void makeRightmostLeafActive() {
        isActive = true;
    }

    @Override
    protected void setInactive() {
        isActive = false;
    }

    @Override
    protected void mergeActiveAndRotate(DIRECTION dir) {

    }

    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

    @Override
    protected LayoutLeaf clone() {
        return new LayoutLeaf(containedFileBuffer.clone(), true);
    }
}
