package layouttree;

import java.util.ArrayList;

import static layouttree.Layout.STATUS_MOVE.*;

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
    public STATUS_MOVE moveFocus(DIRECTION dir) throws RuntimeException {
        // Loop over all children --
        for (Layout l : children) {
            // Called method again
            STATUS_MOVE statusMove = l.moveFocus(dir);
            // FOUND is only when l was a leaf.
            if (statusMove == FOUND) {
                int index = children.indexOf(l);
                if (index < children.size()-1) {
                    children.get(index+1).makeLeftmostLeafActive();
                    return SUCCESS; // Called when this l is not the last child.
                } else {
                    return FOUND; // If this is the last child, we move to another child.
                }
                // Upon success, we stop the calls.
            } else if (statusMove == SUCCESS){
                return SUCCESS;
            } else if (statusMove == CANNOT_FIND){
                int indexChild = children.indexOf(l);
                if(indexChild == children.size()-1){
                    throw new RuntimeException("Layout contains no active leaf");
                }
                return CANNOT_FIND;
            }
        }
        // Wanneer we verderzoeken in node
        throw new RuntimeException("Children empty!");
    }

    //direction needs to be analysed and further improved
    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            STATUS_ROTATE statusMove = l.rotateRelationshipNeighbor(dir);
            if(statusMove == STATUS_ROTATE.FOUND){
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

    @Override
    protected void makeLeftmostLeafActive() {
        children.get(0).makeLeftmostLeafActive();
    }

    @Override
    protected void makeRightmostLeafActive() {
        children.get(children.size()-1).makeRightmostLeafActive();
    }

    @Override
    protected void setInactive() {
    }

    protected LayoutLeaf getLeftLeaf(){
        return children.get(0).getLeftLeaf();
    }

    @Override
    protected Layout clone(){
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : children){
            deepCopyList.add(l.clone());
        }
        return new LayoutNode(deepCopyList);
    }
}
