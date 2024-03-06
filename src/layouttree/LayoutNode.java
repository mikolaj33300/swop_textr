package layouttree;

import java.util.ArrayList;

public class LayoutNode extends Layout{
    private LayoutNode parent;
    private ArrayList<Layout> children;
    private boolean containsActive;

    public LayoutNode(ArrayList<Layout> listLayouts){
        children = new ArrayList<>();
        for(Layout l : listLayouts){
            children.add(l.clone());
        }
    }

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    protected boolean delete(Layout l){
        return children.remove(l);
    }

    protected void mergeActiveWith(Layout toMergeLayout) {

    }

    public void moveFocus(DIRECTION dir) throws RuntimeException {
    }

    /**
     * Moves focus of leaf to another.
     * Called on root layout
     * <p>
     * Wanneer zien we dat we de actieve leaf hebben gevonden?
     */
    public void moveFocusRight() throws RuntimeException {
        // Loop over all children until active found
        for (Layout l : children) {
            if(l.containsActive()){
                l.moveFocusRight();
            }
        }
        throw new RuntimeException("No node contains active!");
    }

    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        return null;
    }

    //direction needs to be analysed and further improved
    public void rotateRelationshipNeighborRight() throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){
                l.rotateRelationshipNeighborRight();
/*                STATUS_ROTATE statusRotate = l.rotateRelationshipNeighborRight();
                if (statusRotate == STATUS_ROTATE.FOUND_ACTIVE) {
                    int index = children.indexOf(l);
                    if (index < children.size() - 1) {
                        Layout toMergeLayout = children.get(index + 1).getLeftLeaf(); // Called when a child of current node contains the to merge leaf.
                        children.get(index + 1).deleteLeftLeaf();
                        this.mergeActiveWith(toMergeLayout);
                        return STATUS_ROTATE.SUCCESS;
                    } else {
                        return STATUS_ROTATE.FOUND_ACTIVE;
                    }
                }
                // Upon success, we stop the calls.
                else if (statusRotate == STATUS_ROTATE.SUCCESS){
                    return STATUS_ROTATE.SUCCESS;
                }*/
            }
        }
        return null;
    }


    protected boolean containsActive() {
        return containsActive;
    }

    public void render() {
        return;
    }

    protected void makeLeftmostLeafActive() {
        children.get(0).makeLeftmostLeafActive();
    }

    protected void makeRightmostLeafActive() {
        children.get(children.size()-1).makeRightmostLeafActive();
    }

    protected void setInactive() {
        this.containsActive = false;
    }

    protected LayoutLeaf getLeftLeaf(){
        return children.get(0).getLeftLeaf();
    }

    protected Layout clone(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new LayoutNode(deepCopyList);
    }

    //add check for valid subtree child
    protected void makeRightNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index < children.size() - 1) {
            children.get(index + 1).makeLeftmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            parent.makeRightNeighbourActive(this); //called when we need to backtrack one level up
        }
    }
}
