package LayoutTree;

import java.util.ArrayList;

import static LayoutTree.Layout.STATUS_MOVE.*;

public class LayoutNode extends Layout{
    private ArrayList<Layout> children;

    public LayoutNode(ArrayList<Layout> listLayouts){
        children = new ArrayList<>();
        for(Layout l : listLayouts){
            children.add(l.clone());
        }
    }

    public STATUS_MOVE moveFocus( DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            STATUS_MOVE statusMove = l.moveFocus(dir);
            if (statusMove == FOUND) {
                int index = children.indexOf(l);
                if (index < children.size()-1) {
                    children.get(index+1).makeLeftmostLeafActive();
                    return SUCCESS;
                } else {
                    return FOUND;
                }
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
        throw new RuntimeException("Children empty!");
    }

    //direction needs to be analysed and further improved
    public STATUS_ROTATE rotateRelationshipNeighbor(DIRECTION dir) throws RuntimeException {
        for (Layout l : children) {
            STATUS_ROTATE statusMove = l.rotateRelationshipNeighbor(dir);
            if(statusMove == STATUS_ROTATE.FOUND){
                int index = children.indexOf(l);
                if (index < children.size()-1) {
                    this.mergeRotateActive(children.get(index+1).getLeftLeaf(), dir);
                    return STATUS_ROTATE.SUCCESS;
            }
        }


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
