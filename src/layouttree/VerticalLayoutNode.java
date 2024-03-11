package layouttree;

import java.util.ArrayList;
import java.util.Arrays;

public class VerticalLayoutNode extends LayoutNode{

    /**
     * Constructor for VerticalLayoutNode, clones its arguments to prevent representation exposure
     */
    public VerticalLayoutNode(ArrayList<Layout> newChildren) {
        super(newChildren);
    }

    /**
     * Returns the orientation of the VerticalLayoutNode
     */
    @Override
    public Orientation getOrientation() {
        return Orientation.VERTICAL;
    }

    /**
     * Rotates the VerticalLayoutNode's child and its neighbour on the right
     */
    @Override
    protected LayoutNode getNewMergedRotatedChild(ROT_DIRECTION rotdir, Layout child) {
        LayoutLeaf newSibling = this.getRightNeighbor(child);
        ArrayList<Layout> newChildren;
        if(rotdir == ROT_DIRECTION.CLOCKWISE){
            newChildren = new ArrayList<>(Arrays.asList(newSibling, child));
        } else {
            newChildren = new ArrayList<>(Arrays.asList(child, newSibling));
        }
        return new HorizontalLayoutNode(newChildren);
    }

    /**
     * Checks a given LayoutNode can be a child of this VerticalLayoutNode
     * A child should have a different orientation than its parent
     */
    @Override
    protected boolean isAllowedToBeChildOf(LayoutNode layoutNode) {
        if(parent.getOrientation()==Orientation.VERTICAL){
            return false;
        } else {
            return true;
        }
    }

    /**
     Makes a deepcopy of VerticalLayoutNode
     The references to this object and its contents will be removed
     */
    @Override
    protected VerticalLayoutNode clone() {
        ArrayList<Layout> deepCopyList = new ArrayList<>();
        for(Layout l : super.children){
            deepCopyList.add(l.clone());
        }
        return new VerticalLayoutNode(deepCopyList);
    }
}