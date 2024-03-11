package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class HorizontalLayoutNode extends LayoutNode {

    public HorizontalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    @Override
    public Orientation getOrientation() {
        return Orientation.HORIZONTAL;
    }

    @Override
    protected void delete(Layout childToDelete) {

    }

    @Override
    protected LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        ArrayList<Layout> nextChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
            nextChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        } else {
            nextChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        }
        return new VerticalLayoutNode(children);
    }

    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        if(parent.getOrientation()==Orientation.HORIZONTAL){
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected HorizontalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : super.children){
            deepCopyList.add(l.clone());
        }
        return new HorizontalLayoutNode(deepCopyList);
    }
}
