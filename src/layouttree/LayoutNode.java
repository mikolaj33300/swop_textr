package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class LayoutNode extends Layout {
    private Orientation orientation;
    private ArrayList<Layout> children;
    private boolean containsActive;

    public LayoutNode(Orientation newOrientation, ArrayList<Layout> newChildren){
        if(newChildren.size()<2){
            throw new RuntimeException("newChildren too short");
        }
        this.orientation = newOrientation;
        this.children = new ArrayList<>();
        for(Layout l : newChildren){
            sanitizeInputChild(this.clone());
            this.insertDirectChild(l.clone());
        }
    }

    public ArrayList<Layout> getDirectChildren(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return deepCopyList;
    }

    public void moveFocus(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){
                l.moveFocus(dir);
            }
        }
        throw new RuntimeException("No node contains active!");
    }

    public void insertDirectChild(Layout toInsert){
        toInsert.sanitizeInputChild(this.clone());
        Layout cloneOfInsert = toInsert.clone();
        this.children.add(cloneOfInsert);
        if(cloneOfInsert.containsActive()){
            this.containsActive = true;
        }
    }

    public Layout rotateRelationshipNeighbor(ROT_DIRECTION rot_dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){
                return l.rotateRelationshipNeighbor(rot_dir);
            }
        }
        throw new RuntimeException("Contains no active leaf!");
    }
    public void render() {
        return;
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

    protected boolean containsActive() {
        return containsActive;
    }

    protected void deleteLeftLeaf() {
        children.get(0).deleteLeftLeaf();
    }

    @Override
    protected void setParent(LayoutNode layoutNode) {
        this.parent = layoutNode;
    }

    protected void delete(Layout l){
        children.remove(l);
        this.clean();
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

    protected void makeRightNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index < children.size() - 1) {
            children.get(index + 1).makeLeftmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            containsActive = false;
            parent.makeRightNeighbourActive(this); //called when we need to backtrack one level up
        }
    }

    protected void makeLeftNeighbourActive(Layout layout) {
        int index = children.indexOf(layout);
        if (index > 0) {
            children.get(index - 1).makeRightmostLeafActive(); // Called when we can make child of current node the active one.
        } else {
            containsActive = false;
            parent.makeRightNeighbourActive(this); //called when we need to backtrack one level up
        }
    }

    protected void mergeAndRotate(ROT_DIRECTION rotdir, Layout child, Layout newSibling) {
        int index = children.indexOf(child);
        Layout newChild = getNewMergedRotatedChild(rotdir, child, newSibling);
        children.set(index, newChild);
        this.clean();
    }

    protected void replace(Layout toReplace, Layout newLayout){
        Layout clonedReplacementInput = newLayout.clone();
        int index = children.indexOf(toReplace);
        children.set(index, clonedReplacementInput);
        this.clean();
    }

    protected void clean(){
        if(children.size()==1){
            parent.replace(this, this.children.get(0));
        } else if (children.isEmpty()){
            parent.delete(this);
        }
        if(this.orientation == parent.getOrientation()){
            parent.absorbChildren(this);
        }
    }

    protected void sanitizeInputChild(LayoutNode futureParent){
        if(children.size()<2){
            throw new RuntimeException("Invalid child layout, contains too little children");
        } else if((this.containsActive && futureParent.containsActive())){
            throw new RuntimeException("Parent already contains an active child");
        } else if(this.orientation == futureParent.getOrientation()){
            throw new RuntimeException("Orientation of child equals orientation of parent");
        }
    }

    private void absorbChildren(LayoutNode toAbsorb){
        int index = children.indexOf(toAbsorb);
        this.children.addAll(index, toAbsorb.children);
    }

    @Override
    protected LayoutNode clone(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new LayoutNode(this.orientation, deepCopyList);
    }

    private Layout getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child, Layout newSibling) {
        Orientation nextOrientation;
        ArrayList<Layout> nextChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
            if (this.orientation == Orientation.HORIZONTAL) {
                nextOrientation = Orientation.VERTICAL;
                nextChildren = new ArrayList<>(Arrays.asList(child, newSibling));
            } else {
                nextOrientation = Orientation.HORIZONTAL;
                nextChildren = new ArrayList<>(Arrays.asList(newSibling, child));
            }
        } else {
            if (this.orientation == Orientation.HORIZONTAL) {
                nextOrientation = Orientation.VERTICAL;
                nextChildren = new ArrayList<>(Arrays.asList(newSibling, child));
            } else {
                nextOrientation = Orientation.HORIZONTAL;
                nextChildren = new ArrayList<>(Arrays.asList(child, newSibling));
            }
        }
        return new LayoutNode(nextOrientation, nextChildren);
    }
}
