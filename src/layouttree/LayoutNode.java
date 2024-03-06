package layouttree;

import java.util.ArrayList;

import static layouttree.Layout.STATUS_MOVE.*;
import static layouttree.Layout.STATUS_ROTATE.FOUND;

public class LayoutNode extends Layout{
    private ArrayList<Layout> children;

    public LayoutNode(ArrayList<Layout> listLayouts){
        children = new ArrayList<>();
        for(Layout l : listLayouts){
            children.add(l.clone());
        }
    }

    /**
     * Moves focus of leaf to another.
     * Called on root layout
     *
     * Wanneer zien we dat we de actieve leaf hebben gevonden?
     */
    public STATUS_MOVE moveFocusRight() throws RuntimeException {
        // Loop over all children until active found
        for (Layout l : children) {
            if(l.containsActive()){
                STATUS_MOVE statusMove = l.moveFocusRight();
                if (statusMove == FOUND_ACTIVE) {
                    int index = children.indexOf(l);
                    if (index < children.size() - 1) {
                        children.get(index + 1).makeLeftmostLeafActive(); // Called when we can make child of current node the active one.
                        return SUCCESS;
                    } else {
                        this.containsActive = false; //called when we need to backtrack one level up
                        return FOUND_ACTIVE;
                    }
                }
                // Upon success, we stop the calls.
                else if (statusMove == SUCCESS){
                    return STATUS_MOVE.SUCCESS;
                }
            }
        }
        throw new RuntimeException("No node contains active!");
    }

    //direction needs to be analysed and further improved
    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            if(l.containsActive()){

                int index = children.indexOf(l);
                if (index < children.size()-1) {
                    this.mergeRotateActive(children.get(index + 1).getLeftLeaf(), dir);
                    return STATUS_ROTATE.SUCCESS;
                }
            }
        }
        // This method was wrong by syntax so this PLACEHOLDER was put here
        return STATUS_ROTATE.CANNOT_FIND;
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
}
