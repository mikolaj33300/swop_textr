package layouttree;

import files.FileBuffer;

public class LayoutLeaf extends Layout {
    private boolean isActive;
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.isActive = active;
    }

    // Hier nog ergens een enum zetten van wanneer we de actieve hebben gevonden???
    public STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException {
        return STATUS_MOVE.FOUND;
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return STATUS_ROTATE.FOUND;
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

    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

    @Override
    protected LayoutLeaf clone() throws CloneNotSupportedException{
        return new LayoutLeaf(containedFileBuffer.clone(), true);
    }
}
