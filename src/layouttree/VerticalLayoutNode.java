package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class VerticalLayoutNode extends LayoutNode{

    @Override
    public Orientation getOrientation() {
        return Orientation.VERTICAL;
    }

    public VerticalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    @Override
    protected LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        ArrayList<Layout> nextChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
            nextChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        } else {
            nextChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        }
        return new HorizontalLayoutNode(children);
    }

    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        if(parent.getOrientation()==Orientation.VERTICAL){
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected VerticalLayoutNode clone() {
        return null;
    }
}