package layouttree;

import files.FileBuffer;

public class LayoutLeaf extends Layout {
    private boolean isActive;
    private FileBuffer containedFileBuffer;

    public LayoutLeaf(FileBuffer fb, boolean active){
        this.containedFileBuffer = fb.clone();
        this.isActive = active;
    }

    public STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException {
        return STATUS_MOVE.FOUND;
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return STATUS_ROTATE.FOUND;
    }

    public void render() {
        return;
    }

    protected void makeLeftmostLeafActive() {
        isActive = true;
    }

    protected void makeRightmostLeafActive() {
        isActive = true;
    }

    protected LayoutLeaf getLeftLeaf() {
        return this.clone();
    }

    @Override
    protected LayoutLeaf clone() throws CloneNotSupportedException{
        return new LayoutLeaf(containedFileBuffer.clone(), true);
    }
}
