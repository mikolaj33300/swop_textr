package layouttree;

import files.FileBuffer;

import java.util.*;

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
    public void moveFocus(DIRECTION dir) throws RuntimeException {

    }

    protected void moveFocusRight() throws RuntimeException {
        this.isActive = false;
        parent.makeRightNeighbourActive(this);
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return STATUS_ROTATE.FOUND_ACTIVE;
    }

    @Override
    protected void rotateRelationshipNeighborRight() {
        // todo
    }

    protected void rotateRelationshipNeighborClockwise() {
        /*Orientation newOrientation;
        if(parent.getOrientation() == HORIZONTAL){
            newOrientation = VERTICAL;
        } else {
            newOrientation = HORIZONTAL;
        }

        LayoutLeaf newSibling = parent.getRightNeighbor(this);
        parent.deleteRightNeighbor(this);
        parent.replace(this, new LayoutNode(newOrientation, (new ArrayList<Layout>(Arrays.asList(this, newSibling)))));*/
    }

    protected boolean containsActive() {
        return isActive;
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
