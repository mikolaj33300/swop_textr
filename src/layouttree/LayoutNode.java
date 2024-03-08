package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class LayoutNode extends Layout {
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

    protected void delete(Layout l){
        children.remove(l);
        this.clean();
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

    //direction needs to be analysed and further improved
    public void rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){
                l.rotateRelationshipNeighbor(rot_dir);
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

    protected LayoutLeaf getLeftLeaf(){
        return children.get(0).getLeftLeaf();
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
        parent.replace(this, newChild);
    }

    protected void replace(Layout toReplace, Layout newLayout){
        int index = children.indexOf(toReplace);
        children.set(index, newLayout);
        this.clean();
    }
    protected void absorbChildren(LayoutNode toAbsorb){
        int index = children.indexOf(toAbsorb);
        this.children.addAll(index, toAbsorb.children);
    }
    protected void clean(){
        if(children.size()==1){
            parent.replace(this, this.children.get(0));
        } else {
            parent.delete(this);
        }
        if(this.orientation == parent.getOrientation()){
            parent.absorbChildren(this);
        }
        this.parent.clean();
    }

    public void mergeAndRotateCounterclockwise(Layout child, LayoutLeaf newSibling) {
        int index = children.indexOf(child);
        Orientation nextOrientation;
        ArrayList<Layout> nextChildren;
        if(this.orientation == Orientation.HORIZONTAL){
            nextOrientation = Orientation.VERTICAL;
            nextChildren = new ArrayList<Layout>(Arrays.asList(newSibling, child));
        } else {
            nextOrientation = Orientation.HORIZONTAL;
            nextChildren = new ArrayList<Layout>(Arrays.asList(child, newSibling));
        }
        Layout newChild = new LayoutNode(nextOrientation, nextChildren);
        children.set(index, newChild);
    }

    @Override
    protected Layout clone(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new LayoutNode(this.orientation, deepCopyList);
    }

    /**
     * Checks if parameter layout is of type {@link LayoutNode}, and if the children match
     * the layout of this node. All leaves must have a FileBuffer containing the same path.
     * ! Assumes the children are always put in the same sequence upon creation of layout
     */
    @Override
    public boolean equals(Layout node) {

        if(node instanceof LayoutNode layoutNode) {

            // Return early when the amount of children don't match.
            if(layoutNode.children.size() != this.children.size()) return false;

            // Loop over the children of both
            for(int i = 0; i < layoutNode.children.size(); i++) {

                // We keep checking if they are equal, if not we return early.
                if(!layoutNode.children.get(i).equals(this.children.get(i))) return false;

            }

            // If the process didn't find disparities, the layouts are equal.
            return true;

        }

        return false;

    }

}
