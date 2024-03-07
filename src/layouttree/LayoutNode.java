package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class LayoutNode extends Layout{
    private LayoutNode parent;

    private Orientation orientation;
    private ArrayList<Layout> children;
    private boolean containsActive;

    public LayoutNode(Orientation newOrientation, ArrayList<Layout> listLayouts){
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
    public void rotateRelationshipNeighborClockwise() throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){
                l.rotateRelationshipNeighborClockwise();
            }
        }
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

    @Override
    protected void mergeActiveAndRotate(DIRECTION dir) {

    }

    protected LayoutLeaf getLeftLeaf(){
        return children.get(0).getLeftLeaf();
    }

    protected Layout clone(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new LayoutNode(newOrientation, deepCopyList);
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

    protected void replace(Layout toReplace, LayoutNode replacedWith) {
        if(replacedWith.)
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    public LayoutLeaf getRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            return children.get(index + 1).getLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            return parent.getRightNeighbor(this); //called when we need to backtrack one level up
        }
    }

    public void deleteRightNeighbor(Layout subtree) {
        int index = children.indexOf(subtree);
        if (index < children.size() - 1) {
            children.get(index + 1).deleteLeftLeaf(); // Called when we can make child of current node the active one.
        } else {
            parent.deleteRightNeighbor(this); //called when we need to backtrack one level up
        }
    }

    protected void mergeAndRotateClockwise(Layout child, Layout newSibling) {
        int index = children.indexOf(child);
        Orientation nextOrientation;
        ArrayList<Layout> nextChildren;
        if(this.orientation == Orientation.HORIZONTAL){
            nextOrientation = Orientation.VERTICAL;
            nextChildren = new ArrayList<Layout>(Arrays.asList(child, newSibling));
        } else {
            nextOrientation = Orientation.HORIZONTAL;
            nextChildren = new ArrayList<Layout>(Arrays.asList(newSibling, child));
        }
        Layout newChild = new LayoutNode(nextOrientation, nextChildren);
        children.set(index, newChild);
    }
}
